package com.twitterassistant.service.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
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

/**
 * LinkedIn.java
 * 
 * ERROR CODES: https://developer.linkedin.com/documents/handling-errors-invalid-tokens
 * 
 */
@Component("Linkedin")
public class LinkedIn extends BaseSocial {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final SocialProvider provider = SocialProvider.LINKEDIN;
	// url to get profile info
	private static final String profileUrl = "http://api.linkedin.com/v1/people/~:(id,first-name,last-name,formatted-name,headline,picture-url,email-address,public-profile-url)?format=json";
	// url to post
	private static final String postUrl = "http://api.linkedin.com/v1/people/~/shares";
	// url to get picture
	private static final String pictureUrl = "http://api.linkedin.com/v1/people/%s/picture-urls::(original)?format=json";

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables
	
	// security
	@Value("#{twitProperties['linkedin.clientId']}")
	private String clientId;

	@Value("#{twitProperties['linkedin.clientSecret']}")
	private String clientSecret;

	// Constructors ---------------------------------------------------------------------------------------- Constructors
	public LinkedIn() {
		// do nothing
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public OAuthService service(String callback, String scope) {
		//@formatter:off
		return new ServiceBuilder()
							.provider(LinkedInApi.class)
							.apiKey(clientId)
							.apiSecret(clientSecret)
							.callback(callback == null ? host : (host + callback + "/" + provider.toString().toLowerCase()))
							.scope(scope == null || scope.equals("") ? "r_emailaddress" : scope) // r_fullprofile,r_contactinfo,r_emailaddress
							//.debug()
							.build();
		//@formatter:on
	}

	@Override
	public String getAuthorizationUrl(String callback, String scope) {
		OAuthService service = service(callback, scope);
		Token requestToken = service.getRequestToken();
		// lets save request token in session
		setRequestToken(requestToken);
		return service.getAuthorizationUrl(requestToken);
	} 
	
	@Override
	public SocialConnection connect(String oAuthVerifier, String callback) {
		OAuthService service = service(callback);
		SocialConnection socialConnection = null;
		Verifier verifier = new Verifier(oAuthVerifier);
		Token accessToken = service.getAccessToken(getRequestToken(), verifier);

		LOG.debug("Getting access token: " + accessToken);

		OAuthRequest request = new OAuthRequest(Verb.GET, profileUrl);
		service.signRequest(accessToken, request);
		Response response = request.send();
		LOG.debug("Got it: Code: " + response.getCode() + ", Body: " + response.getBody());

		if (response.getBody() != null) {
			HashMap<String, String> profile = JsonUtils.toObject(response.getBody(), HashMap.class);

			socialConnection = new SocialConnection();
			socialConnection.setAccessToken(accessToken.getToken());
			socialConnection.setProvider(provider);
			socialConnection.setSecret(accessToken.getSecret());
			socialConnection.setProviderUserId(String.valueOf(profile.get("id")));
			socialConnection.setDisplayName(profile.get("formattedName"));
			socialConnection.setProfileUrl(profile.get("publicProfileUrl"));
			socialConnection.setFirstName(profile.get("firstName"));
			socialConnection.setLastName(profile.get("lastName"));
			socialConnection.setEmail(profile.get("emailAddress"));
			socialConnection.setPhotoUrl(profile.get("pictureUrl"));

			// lets make another call to get a quality picture size
			request = new OAuthRequest(Verb.GET, String.format(pictureUrl, profile.get("id")));
			service.signRequest(accessToken, request);
			response = request.send();
			LOG.debug("Got it: Code: " + response.getCode() + ", Body: " + response.getBody());

			if (response.getBody() != null) {
				HashMap<String, List<String>> picture = JsonUtils.toObject(response.getBody(), HashMap.class);
				if (picture.get("values") != null && picture.get("values").size() > 0)
					socialConnection.setPhotoUrl(picture.get("values").get(0));
			}

		}
		return socialConnection;
	}	

	@Override
	public HashMap<String, String> getRequestMap(HashMap<String, String> map) {
		
		String title = map.get("title") != null ? Jsoup.parse(map.get("title")).text() : null;
		String message = map.get("message") != null ? Jsoup.parse(map.get("message")).text() : null;
		String description = map.get("description") != null ? Jsoup.parse(map.get("description")).text() : null;

		title = title != null && title.length() > 197 ? (title.substring(0, 197) + "...") : title;
		message = message != null && message.length() > 647 ? (message.substring(0, 647) + "...") : message;
		description = description != null && description.length() > 243 ? (description.substring(0, 243) + "...") : description;

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
			OAuthRequest request = new OAuthRequest(Verb.POST, postUrl);
			Token accessToken = new Token(sc.getAccessToken(), sc.getSecret());

			HashMap<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("comment", properties.get("message"));

			if(properties.get("title") != null || properties.get("description") != null || properties.get("link") != null) {
				HashMap<String, String> contentObject = new HashMap<String, String>();
				if(properties.get("title") != null)
					contentObject.put("title", properties.get("title"));
				if(properties.get("description") != null)
					contentObject.put("description", properties.get("description"));
				if(properties.get("link") != null)
					contentObject.put("submitted-url", properties.get("link"));
				if(properties.get("picture") != null)
					contentObject.put("submitted-image-url", properties.get("picture"));
				jsonMap.put("content", contentObject);			
			}

			HashMap<String, String> visibilityObject = new HashMap<String, String>();
			visibilityObject.put("code", "anyone");
			jsonMap.put("visibility", visibilityObject);

			String payload = JsonUtils.toJson(jsonMap);

			service.signRequest(accessToken, request);
			request.addHeader("Content-Type", "application/json");
			request.addHeader("x-li-format", "json");
			request.addHeader("Content-Length", Integer.toString(payload.length()));
			request.addPayload(payload);

			Response response = request.send();
			LOG.debug("Got it: Code: " + response.getCode() + ", Body: " + response.getHeaders().toString());			

			if (response.getBody() != null) {
				
				String message = null;				
				int code = response.getCode() == 201 ? 200 : response.getCode();
				String responseStr = response.getBody();
				HashMap<String, Object> map = JsonUtils.toObject(responseStr, HashMap.class);
				
				message = (String) map.get("message");
				
				LOG.debug("LinkedIn Body Response: " + responseStr);
				
				if (code == 200) {
					res.put("providerPostId", response.getHeader("Location"));
				}	else	if (code == 401) { //unauthorized
					code = 2;					
				}	else	if (code == 403 || code == 500) { //Forbidden, rescheduled			
					code = 3;							
				} else {
					code = 1;	//failed post				
				}				

				// linked in gives 201 on success
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
		// DELETE IS NOT SUPPORTED YET
	}

	@Override
	public ArrayList<HashMap<String, String>> getPages(SocialConnection sc) {
		return null;
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
