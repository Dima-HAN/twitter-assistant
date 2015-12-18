package com.twitterassistant.service.social;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.entity.model.social.SocialPost;
import com.twitterassistant.entity.model.social.SocialProvider;
import com.twitterassistant.util.JsonUtils;
import com.twitterassistant.util.ObjectUtils;

/**
 * Facebook.java
 * 
 * Error codes: http://developers.facebook.com/docs/reference/api/errors/
 * 
 */
@Component("Facebook")
public class Facebook extends BaseSocial {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final SocialProvider provider = SocialProvider.FACEBOOK;
	// url to get profile info
	private static final String profileUrl = "https://graph.facebook.com/me";
	// url to get profile info
	private static final String postUrl = "https://graph.facebook.com/%s/feed";
	// url to delete comment
	private static final String deleteUrl = "https://graph.facebook.com/%s";
	//get a list of pages user is admin of
	private static final String pagesUrl = "https://graph.facebook.com/me/accounts";
	// url to extend time of the token
	private static final String longTokenUrl = "https://graph.facebook.com/oauth/access_token?grant_type=fb_exchange_token";
	
	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// security
	@Value("#{twitProperties['facebook.clientId']}")
	private String clientId;

	@Value("#{twitProperties['facebook.clientSecret']}")
	private String clientSecret;


	// Constructors ---------------------------------------------------------------------------------------- Constructors
	public Facebook() {
		// do nothing
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	
	/* 
	 *	Facebook we cannot pass state in the callback url as callback url has to be the same requested and later called
	 */
	
	public OAuthService service(String callback, String scope) {
		//@formatter:off
		return new ServiceBuilder()
							.provider(FacebookApi.class)
							.apiKey(clientId)
							.apiSecret(clientSecret)
							.callback(callback == null ? host : (host + callback + "/" + provider.toString().toLowerCase()))
							.scope(scope == null || scope.equals("") ? "email" : scope) // set email as a default scope
							.build();
		//@formatter:on
	}

	@Override
	public String getAuthorizationUrl(String callback, String scope) { 
		return service(callback, scope).getAuthorizationUrl(EMPTY_TOKEN);
	} 
	
	@Override
	public SocialConnection connect(String code, String callback) {
		OAuthService service = service(callback, null);
		SocialConnection socialConnection = null;

		Verifier verifier = new Verifier(code);
		Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);

		LOG.debug("Getting access token: " + accessToken);

		OAuthRequest request = new OAuthRequest(Verb.GET, longTokenUrl);
		request.addQuerystringParameter("client_id", clientId);
		request.addQuerystringParameter("client_secret", clientSecret);
		request.addQuerystringParameter("fb_exchange_token", accessToken.getToken());
		service.signRequest(accessToken, request);
		Response response = request.send();

		LOG.debug("Exchange it to long time token: Code: " + response.getCode() + ", Body: " + response.getBody());

		// getting long expire date token
		if (response.getBody() != null) {

			HashMap<String, String> map = ObjectUtils.QueryToMap(response.getBody());

			accessToken = new Token(map.get("access_token"), clientSecret);

			// lets make a call to get profile data
			LOG.debug("Making a call to get profile info: " + profileUrl);
			request = new OAuthRequest(Verb.GET, profileUrl);
			service.signRequest(accessToken, request);
			response = request.send();
			LOG.debug("Got it: Code: " + response.getCode() + ", Body: " + response.getBody());

			HashMap<String, String> profile = JsonUtils.toObject(response.getBody(), HashMap.class);

			socialConnection = new SocialConnection();
			socialConnection.setAccessToken(accessToken.getToken());
			socialConnection.setProvider(provider);
			socialConnection.setSecret(accessToken.getSecret());
			socialConnection.setProviderUserId(profile.get("id"));
			socialConnection.setDisplayName(profile.get("username"));
			socialConnection.setProfileUrl(profile.get("link"));
			socialConnection.setFirstName(profile.get("first_name"));
			socialConnection.setLastName(profile.get("last_name"));
			socialConnection.setEmail(profile.get("email"));
			socialConnection.setPhotoUrl("https://graph.facebook.com/" + profile.get("id") + "/picture?width=215&height=215");
		}

		return socialConnection;
	}

	@Override
	public HashMap<String, String> getRequestMap(HashMap<String, String> map) {
		
		String title = map.get("title") != null ? Jsoup.parse(map.get("title")).text() : null;
		String message = map.get("message") != null ? Jsoup.parse(map.get("message")).text() : null;
		String description = map.get("description") != null ? Jsoup.parse(map.get("description")).text() : null;

		title = title != null && title.length() > 197 ? (title.substring(0, 197) + "...") : title;
		message = message != null && message.length() > 697 ? (message.substring(0, 697) + "...") : message;
		description = description != null && description.length() > 253 ? (description.substring(0, 253) + "...") : description;

		HashMap<String, String> request = new HashMap<String, String>();		
		request.put("title", title);
		request.put("message", message);
		request.put("description", description);
		request.put("link", map.get("link"));
		request.put("picture", map.get("picture"));
		request.put("stream_uuid", map.get("stream_uuid"));			

		return request;
	}

	@Override
	public HashMap<String, String> post(SocialConnection sc, HashMap<String, String> properties) {
		HashMap<String, String> res = new HashMap<String, String>();

		try {

			// prepare properties
			properties = getRequestMap(properties);

			OAuthService service = service();
			OAuthRequest request = new OAuthRequest(Verb.POST, String.format(postUrl, sc.getPageId() != null ? sc.getPageId() : "me"));
			Token accessToken = new Token(sc.getPageId() != null && sc.getPageAccessToken() != null ? sc.getPageAccessToken() : sc.getAccessToken(), sc.getSecret());
			service.signRequest(accessToken, request);
			request.addHeader("Content-Type", "text/html");
			if (properties.get("title") != null)
				request.addBodyParameter("name", properties.get("title"));
			if (properties.get("message") != null)
				request.addBodyParameter("message", properties.get("message"));
			if (properties.get("description") != null)
				request.addBodyParameter("description", properties.get("description"));
			if (properties.get("link") != null) {
				request.addBodyParameter("link", properties.get("link"));
				request.addBodyParameter("caption", properties.get("link"));
			}
			if (properties.get("picture") != null)
				request.addBodyParameter("picture", properties.get("picture"));

			Response response = request.send();
			LOG.debug("Got it: Code: " + response.getCode() + ", Body: " + response.getBody());

			if (response.getBody() != null) {
				String responseStr = response.getBody();
				HashMap<String, Object> map = JsonUtils.toObject(responseStr, HashMap.class);
				int code = response.getCode();
				String message = null;

				if (code == 200) {
					res.put("providerPostId", (String) map.get("id"));
				} else {
					if (map.get("error") != null) {
						HashMap<String, Object> error = (HashMap<String, Object>) map.get("error");
						message = (String) error.get("message");
						int subcode = (Integer) error.get("code");

						// 10, 102, 190, 200-299 User either has not granted a permission or removed a permission
						if ((subcode >= 200 && subcode <= 299) || subcode == 10 || subcode == 190 || subcode == 102)
							code = 2;
						else if (subcode == 1 || subcode == 2 || subcode == 4 || subcode == 17) // limit reached or server busy retry
							code = 3;
						else
							code = 1;
					} else {
						code = 1;
					}
				}

				res.put("code", String.valueOf(code));
				res.put("message", message);
				res.put("response", responseStr);
				res.put("request", JsonUtils.toJson(properties));

				return res;
			}

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			res.put("code", "4");
			res.put("response", e.getMessage());
			res.put("request", JsonUtils.toJson(properties));
		}

		return res;
	}

	@Override
	public void delete(SocialConnection sc, SocialPost post) {
		try {

			OAuthService service = service();
			Token accessToken = new Token(sc.getAccessToken(), sc.getSecret());
			OAuthRequest request;

			// init request
			request = new OAuthRequest(Verb.DELETE, String.format(deleteUrl, post.getProviderPostId()));
			service.signRequest(accessToken, request);

			Response response = request.send();
			String responseStr = response.getBody();
			LOG.debug("Got it: Code: " + response.getCode() + ", Body: " + responseStr);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public ArrayList<HashMap<String, String>> getPages(SocialConnection sc) {
		
		OAuthService service = service();
		OAuthRequest request = new OAuthRequest(Verb.GET, pagesUrl);
		Token accessToken = new Token(sc.getAccessToken(), sc.getSecret());
		service.signRequest(accessToken, request);
		
		Response response = request.send();
		LOG.debug("Got it: Code: " + response.getCode() + ", Body: " + response.getBody());
		
		ArrayList<HashMap<String, String>> pages = new ArrayList<HashMap<String, String>>();
		
		if (response.getBody() != null) {
			HashMap<String, Object> data = JsonUtils.toObject(response.getBody(), HashMap.class);
			if(data != null) {				
				pages = (ArrayList<HashMap<String, String>>) data.get("data");
			}			
		}
		return pages;
	}

	@Override
	public ArrayList<HashMap<String, String>> getUserInfo(SocialConnection sc, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters
}
