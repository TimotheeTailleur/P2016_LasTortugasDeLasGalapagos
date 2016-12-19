package fr.tse.info4.project.model.datarecovery;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


import com.google.code.stackexchange.client.constant.ApplicationConstants;
import com.google.code.stackexchange.client.constant.StackExchangeApiMethods;
import com.google.code.stackexchange.client.provider.url.ApiUrlBuilder;

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

public class DaveApiManager extends ApiManager {

	public static final String GET_TOP_ANSWERERS = "com.google.code.stackexchange.client.getTagTopAnswerers";

	// FILTERS
	private static final String TOP_ANSWERERS_FILTER = "!*Jxe6D.tT0bNxx(Z";

	/**
	 * Constructor to use applicationKey and specify StackExchangeSite
	 * @param applicationKey
	 * @param site
	 */
	public DaveApiManager(String applicationKey, StackExchangeSite site) {
		super(applicationKey, site);
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
	 * 
	 * @param tagName
	 * @param list
	 * @return True if pagedList contains tag (tagName)
	 */
	public static boolean contains(String tagName, PagedList<Tag> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName().equals(tagName)) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * 
	 * Method use to scan the different paging in the method getTagsOnUsers(List<Long> ids, int min)
	 * 
	 * @param ids
	 * @param min
	 * @param page
	 * @return
	 */
	private PagedList<Tag> getTagsOnUsers(List<Long> ids, int min, Paging page) {
		ApiUrlBuilder builder = createStackOverflowApiUrlBuilder(StackExchangeApiMethods.GET_TAGS_ON_USER).withIds(ids)
				.withPaging(page);
		String apiUrl = builder.buildUrl() + "&min=" + min;
		return unmarshallList(Tag.class, callApiMethod(apiUrl));
	}

	
	/**
	 * Return the tags the users identified in ids have been active in. <br>
	 * The tags have to have a score > min
	 * @param ids 
	 * @param min
	 * @return
	 */
	public List<Tag> getTagsOnUsers(List<Long> ids, int min) {
		Paging firstPage = new Paging(1, 100);
		PagedList<Tag> userTagsPage = getTagsOnUsers(ids, min, firstPage);
		List<Tag> userTags = new ArrayList<Tag>();

		for (int i = 0; i < userTagsPage.size(); i++) {
			userTags.add(userTagsPage.get(i));
		}

		int pageNumber = 2;
		while (userTagsPage.hasMore()) {
			try {
				Thread.sleep(userTagsPage.getBackoff() * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(pageNumber);
			Paging page = new Paging(pageNumber, 100);
			userTagsPage = getTagsOnUsers(ids, min, page);
			
			for (int i = 0; i < userTagsPage.size(); i++) {
				userTags.add(userTagsPage.get(i));
			}
			pageNumber++;
		}

		return userTags;

	}

	public static void main(String[] args) {

		DaveApiManager manager = new DaveApiManager(APP_KEY, SITE);
		manager.getTopAnswerers("java");

	}

}
