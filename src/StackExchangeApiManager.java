import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class StackExchangeApiManager {
	
	
	private static final String domainName = "https://api.stackexchange.com";
	private static final String version = "2.2";
	
	
	/*
	 * Extract a JSONObject from a given StackExchange API URL
	 * Handles proxy problems
	 * @author Timothee
	 * @return JSONObject
	 * @version 1.0
	 */
	
	public static JSONObject toJSONObject(String str_url) throws IOException, JSONException {
		String jsonString = HttpConnect.readUrl(str_url);
		return new JSONObject(jsonString);
	}
	
	
	/*
	 * 
	 * @return String The built url used to get Tag list
	 * @version 1.0	 
	 */
	private static String buildTagUrl(){		
		return domainName + '/' + version + '/' + "tags?order=desc&sort=popular&site=stackoverflow";
	}
	
	private static String buildTopAnswerersUrl(String tagName){
		return domainName + '/' + version + "/tags/" + tagName + "/top-answerers/all_time?site=stackoverflow";
		
	}
	
	/*
	 * Returns  ArrayList<String> of StackOverflow tag names using stackExchangeAPI
	 * @author Timothee
	 * @return ArrayList<String>
	 * @version 1.0
	 * 
	 */
	
	public static ArrayList<String> getTags () throws JSONException, IOException {
		

		String str_url= buildTagUrl(); //API URL
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
	
	/*
	 * Returns a map of all users who have contributed to tagName. 
	 * Keys are users'ID and values are 2-Lists of a user's score and postcount in given tag
	 * 
	 * @author Timothee
	 * @return TreeMap<Integer,ArrayList<Integer>>
	 * @version 1.0
	 * 
	 */
	
	public static  TreeMap<Integer,ArrayList<Integer>> getTopAnswerers(String tagName) throws IOException, JSONException {
		
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
	

	
	public static void main(String[] args) throws JSONException, IOException {
		
		ArrayList<String> Tags = StackExchangeApiManager.getTags();
		TreeMap<Integer,ArrayList<Integer>> AnswerersMap= StackExchangeApiManager.getTopAnswerers("javascript");
		
		System.out.println("Liste de tags ");
		System.out.println(Tags.toString());
		
		System.out.println("Liste des userIDs");
		System.out.println(AnswerersMap.keySet().toString());
		
		System.out.println("Liste des score & post Count");
		System.out.println(AnswerersMap.values().toString());
		
	}
}