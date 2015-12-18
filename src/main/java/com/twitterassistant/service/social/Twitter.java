package com.twitterassistant.service.social;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.jsoup.Jsoup;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.twitterassistant.dto.TwitterUsersDto;
import com.twitterassistant.entity.model.social.SocialConnection;
import com.twitterassistant.entity.model.social.SocialPost;
import com.twitterassistant.entity.model.social.SocialProvider;
import com.twitterassistant.entity.model.twitter.TwitterUserType;
import com.twitterassistant.exception.RateLimitException;
import com.twitterassistant.util.JsonUtils;

/**
 * Twitter.java
 * 
 * Errors: https://dev.twitter.com/docs/error-codes-responses
 * 
 */
@Component("Twitter")
public class Twitter extends BaseSocial {

	// Constants ---------------------------------------------------------------------------------------------- Constants

	private static final SocialProvider provider = SocialProvider.TWITTER;
	// url to get profile info
	private static final String profileUrl = "https://api.twitter.com/1.1/account/verify_credentials.json";
	// get user info
	private static final String userInfoUrl = "https://api.twitter.com/1.1/users/show.json?user_id=%s";
	// list of followers
	private static final String followersIdUrl = "https://api.twitter.com/1.1/followers/ids.json?user_id=%s&cursor=%s&count=%s";
	private static final String followersUrl = "https://api.twitter.com/1.1/followers/list.json?user_id=%s&cursor=%s&count=%s";

	// url to post
	private static final String postUrl = "https://api.twitter.com/1.1/statuses/update_with_media.json";
	private static final String postUrlNoMedia = "https://api.twitter.com/1.1/statuses/update.json";
	private static final String deleteUrl = "https://api.twitter.com/1.1/statuses/destroy/%s.json";

	// length of the message allowed by twitter
	private static final int messLen = 140; // if we post a media we have to reduce tweet by the lenght of the media url apx 30
	private static final int messMediaLen = 110;

	// Instance Variables ---------------------------------------------------------------------------- Instance Variables

	// security
	@Value("#{twitProperties['twitter.clientId']}")
	private String clientId;

	@Value("#{twitProperties['twitter.clientSecret']}")
	private String clientSecret;

	// Constructors ---------------------------------------------------------------------------------------- Constructors
	public Twitter() {
		// do nothing
	}

	// Public Methods ------------------------------------------------------------------------------------ Public Methods
	public OAuthService service(String callback, String scope) {
		//@formatter:off
		return new ServiceBuilder()
						.provider(TwitterApi.SSL.class)
						.apiKey(clientId)
						.apiSecret(clientSecret)
						.callback(callback == null ? host : (host + callback + "/" + provider.toString().toLowerCase()))
						//.scope(scope.equals("") ? "r_emailaddress" : scope) // TWITTER DOES NOT HAVE SCOPE
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
			socialConnection.setDisplayName(profile.get("screen_name"));
			socialConnection.setProfileUrl("https://twitter.com/" + profile.get("screen_name"));
			if (profile.get("name") != null) {
				String[] name = profile.get("name").split(" ");
				if (name.length >= 1)
					socialConnection.setFirstName(name[0]);
				if (name.length >= 2)
					socialConnection.setLastName(name[1]);
			}
			socialConnection.setEmail(getEmail());
			socialConnection.setPhotoUrl(profile.get("profile_image_url").replace("_normal", ""));
		}
		return socialConnection;
	}

	@Override
	public HashMap<String, String> getRequestMap(HashMap<String, String> map) {

		// twitter max length is 140, lets make sure our message no longer then that.
		int maxMessLength = map.get("picture_abs") != null ? messMediaLen : messLen;
		String link = map.get("link") != null ? " " + map.get("link") : "";

		// if we have a link valid, we will attache it at the end, lets make sure max length for the message modified
		if (link != null)
			maxMessLength -= link.length();

		// lets check if message is not just a link, take it otherwise take title and description.
		String message = map.get("message");
		if (message == null || message.trim().equals(link.trim())) {
			message = "";
			if (map.get("title") != null)
				message += map.get("title") + ". ";
			if (map.get("description") != null)
				message += map.get("description");
		}

		// if we have a link lets remove other links from the message if any
		if (map.get("link") != null)
			message = message.replaceAll("\\b(https?|ftp|file):\\/\\/[-a-zA-Z0-9+&@#\\/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#\\/%=~_|]", " ").replaceAll("\\s+", " ");

		if (message != null && message.length() > maxMessLength)
			message = message.substring(0, maxMessLength - 3) + "..." + (message.trim().equals(link.trim()) == false ? link : "");
		else
			message = message + (message.trim().equals(link.trim()) == false ? link : "");

		// clean message
		if (message != null)
			message = Jsoup.parse(message).text().replaceAll("\\n", "");

		HashMap<String, String> request = new HashMap<String, String>();
		request.put("message", message);
		request.put("link", map.get("link"));
		request.put("picture", map.get("picture"));
		request.put("stream_uuid", map.get("stream_uuid"));
		// lets include abs path to the media to be uploaded
		if (map.get("picture_abs") != null)
			request.put("picture_abs", map.get("picture_abs").startsWith(assetPath) ? map.get("picture_abs") : (assetPath + "/" + map.get("picture_abs")));

		return request;
	}

	@Override
	public HashMap<String, String> post(SocialConnection sc, HashMap<String, String> properties) {
		HashMap<String, String> res = new HashMap<String, String>();

		try {

			OAuthService service = service();
			Token accessToken = new Token(sc.getAccessToken(), sc.getSecret());

			File pic;
			OAuthRequest request;

			// prepare properties
			properties = getRequestMap(properties);

			// sending text with the picture
			if (properties.get("picture_abs") != null && (pic = new File(properties.get("picture_abs"))).exists()) {

				// init request
				request = new OAuthRequest(Verb.POST, postUrl);

				FileBody bin = new FileBody(pic);
				StringBody comment = null;
				comment = new StringBody(properties.get("message"));

				MultipartEntity reqEntity = new MultipartEntity();
				reqEntity.addPart("status", comment);
				reqEntity.addPart("media[]", bin);

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				reqEntity.writeTo(out);

				request.addPayload(out.toByteArray());
				request.addHeader(reqEntity.getContentType().getName(), reqEntity.getContentType().getValue());

			} else {
				// init request
				request = new OAuthRequest(Verb.POST, postUrlNoMedia);
				// send simple text
				request.addBodyParameter("status", properties.get("message"));

			}

			service.signRequest(accessToken, request);

			Response response = request.send();
			String responseStr = response.getBody();
			LOG.debug("Got it: Code: " + response.getCode() + ", Body: " + responseStr);

			if (response.getBody() != null) {
				int code = response.getCode();
				String message = null;
				HashMap<String, Object> map = JsonUtils.toObject(responseStr, HashMap.class);

				if (code == 200) {
					res.put("providerPostId", (String) map.get("id_str"));
				} else {
					if (map.get("errors") != null) {
						ArrayList<Object> errors = (ArrayList<Object>) map.get("errors");
						if (errors.size() > 0) {
							HashMap<String, Object> error = (HashMap<String, Object>) errors.get(0);
							message = (String) error.get("message");
							int subcode = (Integer) error.get("code");
							// if code is 401 Unauthorized, lets set error to 2 and kill the connection, reconnect required
							if (code == 401)
								code = 2;
							// 190 - tweet is too long
							// 187 - Status is a duplicate
							else if (subcode == 190 || subcode == 187) // failed post completely
								code = 1;
							else if (code == 403 || code == 429 || code == 502 || code == 503 || code == 504) // limit reached, reshedule it
								code = 3;
							else
								code = 1;
						}
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
			request = new OAuthRequest(Verb.POST, String.format(deleteUrl, post.getProviderPostId()));
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
		return null;
	}

	@Override
	public ArrayList<HashMap<String, String>> getUserInfo(SocialConnection sc, String userId) {
		OAuthService service = service();
		Token accessToken = new Token(sc.getAccessToken(), sc.getSecret());

		// init request
		OAuthRequest request = new OAuthRequest(Verb.GET, String.format(userInfoUrl, userId));

		service.signRequest(accessToken, request);
		Response response = request.send();
		LOG.debug("Got it: Code: " + response.getCode() + ", Body: " + response.getBody());

		if (response.getBody() != null) {

			System.out.println(response.getHeader("x-rate-limit-limit"));
			System.out.println(response.getHeader("x-rate-limit-remaining"));
			System.out.println(response.getHeader("x-rate-limit-reset"));
		}

		return null;
	}

	/*
	 * GET followers/ids Returns a cursored collection of user IDs for every user following the specified user.
	 * https://dev.twitter.com/rest/reference/get/followers/ids
	 */
	public TwitterUsersDto getUserFollowers(SocialConnection sc, String userId, String cursor, int limit) throws Exception {
		OAuthService service = service();
		Token accessToken = new Token(sc.getAccessToken(), sc.getSecret());

		// init request
		OAuthRequest request = new OAuthRequest(Verb.GET, String.format(followersUrl, userId, cursor, limit));

		service.signRequest(accessToken, request);
		Response response = request.send();
		String responseStr = response.getBody();
		int code = response.getCode();
		LOG.debug("Got it: Code: " + response.getCode() + ", Limit: " + response.getHeader("x-rate-limit-limit") + ", Remaining: " + response.getHeader("x-rate-limit-remaining") + ", Body: " + responseStr);

		if (responseStr != null) {
			if(code == 200)
				return new TwitterUsersDto(TwitterUserType.FOLLOWER, response);
			else if(code == 429)
				throw new RateLimitException(response.getHeader("x-rate-limit-limit"), response.getHeader("x-rate-limit-remaining"), response.getHeader("x-rate-limit-reset"));
			else
				throw new Exception(responseStr);
		}
		return null;
	}

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters

	// Protected Methods ------------------------------------------------------------------------------ Protected Methods

	// Private Methods ---------------------------------------------------------------------------------- Private Methods

	// Getters & Setters ------------------------------------------------------------------------------ Getters & Setters
}
