package tse.info4.project.datarecovery;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class will handle every interaction with the StackExchange API. 
 * Building the right URLs, extracting data and converting it into easily iterable data structures to allow for easy insertion into the database
 *
 */
public class StackExchangeApiManager {
	
	/**
	 * Domain name for the API
	 */
	private static final String DOMAIN_NAME = "https://api.stackexchange.com";
	/**
	 * Current version of the API
	 */
	private static final String VERSION = "2.2";
	/**
	 * Parameter dictating number of results displayed by the API
	 */
	private static final String PAGE_SIZE="100";
	
	
	/**
	 * Extract a JSONObject from a given StackExchange API URL
	 * @param str_url API URL
	 * @return JSONObject to manipulate API data
	 * @throws IOException 
	 * @throws JSONException 
	 */
	
	public static JSONObject toJSONObject(String str_url) throws IOException, JSONException {
		String jsonString = HttpConnect.readUrl(str_url);
		return new JSONObject(jsonString);
	}
	
	/**
	 * Build URL to get page (pageNumber) of Tag list (overloaded to give pageNumber default value of 1)
	 * @param pageNumber Number of the page you wish to get access to
	 * @return API URL for page (pageNumber) of Tag list
	 */
	private static String buildTagUrl(Integer pageNumber){		
		return DOMAIN_NAME + '/' + VERSION + '/' + "tags?"+"page="+pageNumber+"&pagesize="+PAGE_SIZE+"&order=desc&sort=popular&site=stackoverflow";
	}
	
	private static String buildTagUrl () {
		return DOMAIN_NAME + '/' + VERSION + '/' + "tags?"+"page=1&pagesize="+PAGE_SIZE+"&order=desc&sort=popular&site=stackoverflow"; 
	}
	
	/**
	 * BuildUrl to get tag list and score of a user.
	 * @param idUSer
	 * @return Api Url
	 */
	private static String buildTagUrl(int idUser, int page)
	{
		String filter = "!-.G.68phH_FJ";
		return DOMAIN_NAME + '/' + VERSION + "/users/" + idUser + "/tags?page=" + page + "&pagesize=100&order=desc&sort=popular&site=stackoverflow&filter="+filter;
	}
	
	/**
	 * Build URL to get page one of TopAnswerers list in given (tagName)
	 * @param tagName Tag in which the search is executed
	 * @return API URL for Top Answerers in tagName
	 * @throws UnsupportedEncodingException 
	 * @throws URISyntaxException 
	 */
	private static String buildTopAnswerersUrl(String tagName) throws UnsupportedEncodingException, URISyntaxException{
		return DOMAIN_NAME + '/' + VERSION + "/tags/"+URLEncoder.encode(tagName,"UTF-8") + "/top-answerers/all_time?site=stackoverflow";
	}
	
	/** 
	 * Get list of Tag names from StackOverflow
	 * @return List of StackOverflow tag names
	 * @throws JSONException 
	 * @throws IOException 
	 * 
	 */
	
	public static ArrayList<String> getTags (int pageNumber) throws JSONException, IOException {
		

		String str_url= buildTagUrl(pageNumber); //API URL
		JSONObject tagsJSON = toJSONObject(str_url); // Extract JSONObject from API URL
		
		JSONArray tagsJSONArray = tagsJSON.getJSONArray("items"); // Extract JSONArray from  JSONObject
		
		ArrayList<String> tagNamesArray= new ArrayList<>(tagsJSONArray.length()); //returned variable
		
		//For each tag, get its name and add it to the returned variable
		for (int i =0;i<tagsJSONArray.length();i++)
		{
			String s= tagsJSONArray.getJSONObject(i).getString("name");
			tagNamesArray.add(s);
		}
		return tagNamesArray;
	}
	
	/**
	 * 
	 * Get Map of topAnswerers and some of their statistics in given tag
	 * @param tagName given tag for which you want the list of topAnswerers
	 * @return Map of all users who have contributed to tagName. Keys are users'ID and values are couples of a user's score and postcount in given tag
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws URISyntaxException 
	 * 
	 */
	
	public static  TreeMap<Integer,ArrayList<Integer>> getTopAnswerers(String tagName) throws IOException, JSONException, URISyntaxException {
		String str_url = buildTopAnswerersUrl(tagName); // API URL
		
		JSONObject topAnswerersJSON = toJSONObject(str_url); // Extract JSONObject from API URL
		
		JSONArray topAnswerersJSONArray = topAnswerersJSON.getJSONArray("items"); // Extract JSONArray from JSON file using JSONObject
		TreeMap<Integer,ArrayList<Integer>> topAnswerersMap = new TreeMap<>(); //returned variable 
		
		for (int i=0;i<topAnswerersJSONArray.length();i++)
		{
			Integer uID;	//this variable will store the user's ID
			ArrayList<Integer> uAnswersStats=new ArrayList<> (2); //List of (user Score, user PostCount) for user uID
			JSONObject currentObject= topAnswerersJSONArray.getJSONObject(i);
			
			//get score & post_count for current user
			Integer uScore = currentObject.getInt("score");
			Integer uPostCount= currentObject.getInt("post_count");
			
			//get JSONObject containing user's stats (except score & postcount) and use it to get userID
			JSONObject currentUser=currentObject.getJSONObject("user");
			
			// if accept_rate is not specified, we take uAcceptRate = 0
			Integer uAcceptRate = 0;
			try{
				uAcceptRate= currentUser.getInt("accept_rate");
			}
			catch(Exception e){
				e.getMessage();
			}
			
			uID=currentUser.getInt("user_id");
			
			//add stats to ArrayList
			uAnswersStats.add(uScore);uAnswersStats.add(uPostCount);uAnswersStats.add(uAcceptRate);
			
			//add userID & user answers Stats to the returned Map
			topAnswerersMap.put(uID, uAnswersStats);
		}
		return topAnswerersMap;
	}
	

	
	/**
	 * 
	 *  Return a list of tag concerning a user where the count is bigger than minScore for each tag.
	 * 
	 * @param idUser
	 * @param minScore
	 * @return A list of map which contain the count and the name for each tag [{cout, name}, {count, name}, ...]
	 * @throws JSONException
	 * @throws IOException
	 */
	public static ArrayList<TreeMap<String, String>> getTagUserScore(int idUser, int minScore) throws JSONException, IOException{
		int pageNumber = 1;
		String url = buildTagUrl(idUser, pageNumber);
		JSONObject tagsJson = toJSONObject(url);
		JSONArray tagsJsonArray = tagsJson.getJSONArray("items");
		
		ArrayList<TreeMap<String, String>> tagNames = new ArrayList<TreeMap<String, String>>();
		
		boolean has_more = true;
		
		while (has_more){
			int i =0;
			while (i < tagsJsonArray.length()){
				String name = tagsJsonArray.getJSONObject(i).getString("name");
				String count = tagsJsonArray.getJSONObject(i).getString("count");

				if ((Integer.parseInt(count) < minScore)){
					return tagNames;
				}
				
				TreeMap<String, String> tagData = new TreeMap<String, String>();
				tagData.put("name", name);
				tagData.put("count", count);
				tagNames.add(tagData);
				i++;
			}
			has_more = tagsJson.getBoolean("has_more");
			
		}
		
		return tagNames;
		
		
	}
	
	 //Methode main pour test classe
	   public static void main(String[] args) throws JSONException, IOException, URISyntaxException {
		
		System.out.println(getTagUserScore(1200, 10));
		
	} 
}