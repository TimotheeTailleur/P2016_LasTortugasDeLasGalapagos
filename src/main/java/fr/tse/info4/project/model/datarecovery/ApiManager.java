package fr.tse.info4.project.model.datarecovery;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.code.stackexchange.client.constant.ApplicationConstants;
import com.google.code.stackexchange.client.impl.StackExchangeApiJsonClient;
import com.google.code.stackexchange.client.provider.url.ApiUrlBuilder;
import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;
import com.google.code.stackexchange.client.query.impl.TagApiQueryImpl;
import com.google.code.stackexchange.client.query.impl.UserApiQueryImpl;
import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Paging;
import com.google.code.stackexchange.schema.StackExchangeSite;
import com.google.code.stackexchange.schema.Tag;
import com.google.code.stackexchange.schema.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.tse.info4.project.model.schema.TagScore;

public class ApiManager extends StackExchangeApiJsonClient  {
	
	

	/**  Application key */
	public final static String APP_KEY = "6WGYkZgsp2UiQfMqTj3LCQ((";
	/** Site (stack overflow) */
	public final static StackExchangeSite SITE = StackExchangeSite.STACK_OVERFLOW;

	private static final String TOP_ANSWERERS_FILTER = "!*Jxe6D.tT0bNxx(Z";
	
	public static final String GET_TOP_ANSWERERS = "com.google.code.stackexchange.client.getTagTopAnswerers";
	/**
	 * Factory item used to build the request.
	 */
	protected StackExchangeApiQueryFactory factory;
	
	/**
	 * Constructor to use application key with every api url execution
	 * and specify StackExchange website on which execute querries
	 * @param applicationKey
	 * @param site
	 */
	public ApiManager(String applicationKey, StackExchangeSite site) {
		super(applicationKey, site);

	}

	/**
	 * Default constructor.
	 */
	public ApiManager() {
		super(APP_KEY,SITE);
	}

	/**
	 * 
	 * Retrieves the id of user identified by his accessToken
	 * 
	 * @param accessToken
	 * @return the user id
	 */
	public static long getIdUser(String accessToken){
		String filter = "!6Xcz2t9HFnLLg";
		UserApiQueryImpl query = new UserApiQueryImpl(APP_KEY, SITE, accessToken);
		query.withFilter(filter);
		PagedList<User> list = query.listMe();
		
		
		return list.get(0).getUserId();
	}
	
	/**
	 * 
	 * Retrieves the best tags of the user (sorted by popularity)
	 * 
	 * @param nbTags
	 * @param idUser
	 * @return the best tags of a user.
	 */
	public static PagedList<Tag> getBestTags(int nbTags, int idUser) {
		final String filter = "!-.G.68grSaJo";
		final TagApiQueryImpl tags = new TagApiQueryImpl(ApiManager.APP_KEY, ApiManager.SITE);
		Paging page = new Paging(1, nbTags);

		tags.withUserIds(idUser);
		tags.withPaging(page);
		tags.withFilter(filter);
		tags.withSort(Tag.SortOrder.MOST_POPULAR);

		return tags.listTagsOnUser();
	}

	/**
	 * 
	 * Retrieves the best tags of the user (sorted by popularity)
	 * 
	 * @param nbTags
	 * @param accessToken
	 * @return the best tags of the user.
	 */
	public static PagedList<Tag> getBestTags(int nbTags, String accessToken) {
		return getBestTags(nbTags, (int) ApiManager.getIdUser(accessToken));
	}
	
	/**
	 * Returns the top 30 answerers active in a single tag in the last 30 days
	 * 
	 * @param String
	 *            tag
	 * @return List TagScore
	 */
	public List<TagScore> getTopAnswerers(String tag) {
		ApiUrlBuilder builder = createStackOverflowApiUrlBuilder(GET_TOP_ANSWERERS).withField("tags", tag)
				.withField("period", TagScore.Period.ALL_TIME.toString()).withFilter(TOP_ANSWERERS_FILTER);
		String apiUrl = builder.buildUrl();

		Charset UTF_8_CHAR_SET = Charset.forName(ApplicationConstants.CONTENT_ENCODING);
		JsonParser parser = new JsonParser();
		InputStream jsonContent = callApiMethod(apiUrl);
		JsonElement response = parser.parse(new InputStreamReader(jsonContent, UTF_8_CHAR_SET));
		JsonObject adaptee = response.getAsJsonObject();
		JsonArray elements = adaptee.get("items").getAsJsonArray();

		ArrayList<TagScore> topAnswerers = new ArrayList<TagScore>(30);
		for (JsonElement o : elements) {
			JsonObject jsonObject = o.getAsJsonObject();
			int postCount = Integer.parseInt(jsonObject.get("post_count").toString());
			int score = Integer.parseInt(jsonObject.get("score").toString());
			JsonObject userJsonObject = jsonObject.get("user").getAsJsonObject();
			long id = Long.parseLong(userJsonObject.get("user_id").toString());
			String name = userJsonObject.get("display_name").toString();

			User user = new User();
			user.setDisplayName(name);
			user.setUserId(id);

			TagScore tagScore = new TagScore(postCount, score, user);
			topAnswerers.add(tagScore);
		}
		

		return topAnswerers;

	}
	
	/**
	 * Returns true if the user exists, false otherwise.
	 * @param userId the user id.
	 * @return the boolean checking if a user exists.
	 */
	public boolean userExists(int userId){
		factory = StackExchangeApiQueryFactory.newInstance(APP_KEY, SITE);
		List<User> users = factory.newUserApiQuery().withUserIds(userId).listUserByIds();
		return users.size() !=0;		
	}
	
	
	/**
	 * Returns the user name identified by an id.
	 * @param userId the user id
	 * @return the user name if the user exists, void string otherwise.
	 */
	public String getUserNAme(int userId){
		factory = StackExchangeApiQueryFactory.newInstance(APP_KEY, SITE);
		List<User> users = factory.newUserApiQuery().withUserIds(userId).listUserByIds();
		if (users.size() != 0){
			return users.get(0).getDisplayName();
		}
		return "";
	}

}
