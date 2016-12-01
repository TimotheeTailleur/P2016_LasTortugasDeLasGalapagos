package fr.tse.info4.project.datarecovery;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.code.stackexchange.client.constant.ApplicationConstants;
import com.google.code.stackexchange.client.provider.url.ApiUrlBuilder;
import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Paging;
import com.google.code.stackexchange.schema.StackExchangeSite;
import com.google.code.stackexchange.schema.Tag;
import com.google.code.stackexchange.schema.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fr.tse.info4.project.schema.TagScore;

public class DaveApiManager extends ApiManager{
	
	
	public static final String GET_TOP_ANSWERERS = "com.google.code.stackexchange.client.getTagTopAnswerers";
	
	/* FILTERS */
	private static final String TOP_ANSWERERS_FILTER = "!*Jxe6D.tT0bNxx(Z";
	
	
	public DaveApiManager(String applicationKey, StackExchangeSite site) {
		super(applicationKey, site);
	}

	
	/**
	 * Returns the top 30 answerers active un a single taf of the last 30 days
	 * the the stack overflow api returns
	 * 
	 * @param String tag
	 * @return List<TagScore>
	 */
	public List<TagScore> getTopAnswerers(String tag){
		ApiUrlBuilder builder = createStackOverflowApiUrlBuilder(GET_TOP_ANSWERERS)
								.withField("tags", tag)
								.withField("period", TagScore.Period.ALL_TIME.toString())
								.withFilter(TOP_ANSWERERS_FILTER);
		String apiUrl = builder.buildUrl();
		
		Charset UTF_8_CHAR_SET = Charset
				.forName(ApplicationConstants.CONTENT_ENCODING);
		JsonParser parser = new JsonParser();
		InputStream jsonContent = callApiMethod(apiUrl);
		JsonElement response = parser.parse(new InputStreamReader(
				jsonContent, UTF_8_CHAR_SET));
		JsonObject adaptee = response.getAsJsonObject();
		JsonArray elements = adaptee.get("items")
				.getAsJsonArray();
		
		Gson gson = new Gson();
		ArrayList<TagScore> topAnswerers = new ArrayList<TagScore>(30);
		for (JsonElement o : elements){
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
	 * @return True if the pagedList contains the tag named tagName
	 */
	public static boolean contains(String tagName, PagedList<Tag> list){
		for (int i =0 ; i<list.size(); i++){
			if (list.get(i).getName().equals(tagName)){
				return true;
			}
		}
		return false;
	}
	
	

	public static void main(String[] args) {
		DaveApiManager manager = new DaveApiManager(APP_KEY, SITE);
		Paging page = new Paging(5,100);
		PagedList<Tag> list = manager.getTagsForUsers(page, 1200);
		for (int i =0 ; i<list.size() ;i++){
			System.out.println(list.get(i).getName() + " : " + list.get(i).getCount());
		
		}
	}
	
}
