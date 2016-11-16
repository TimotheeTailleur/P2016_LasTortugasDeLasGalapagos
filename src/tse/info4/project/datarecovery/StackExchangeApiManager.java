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
	

// General use methods
	
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
	
	

 // URL builders
	
	/**
	 * Build URL to get page (pageNumber) of Tag list (overloaded to give pageNumber default value of 1)
	 * @param pageNumber Number of the page you wish to get access to
	 * @return API URL for page (pageNumber) of Tag list
	 */
	private static String buildTagUrl(Integer pageNumber){		
		return DOMAIN_NAME + '/' + VERSION + '/' + "tags?"+"page="+pageNumber+"&pagesize="+PAGE_SIZE+"&order=desc&sort=popular&site=stackoverflow";
	}
	
	@SuppressWarnings({ "unused", "javadoc" })
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
	 * Build URL to get User Answers
	 * The filter here is used to only get answers ids and questions ids for the given user
	 * @param userId
	 * @param pageNumber 
	 * @return
	 */
	public static String buildAnswersUrl (Integer userId,Integer pageNumber) {
		String filter = "!)Q2B_A19OPkd2j.3elVGjGq(";
		return DOMAIN_NAME + '/' + VERSION+"/users/"+userId
					+"/answers?page="+pageNumber+"&pagesize=100&order=desc&sort=activity&site=stackoverflow&filter=" + filter;

		
	}
	
	/**
	 * Build URL to get set of question ids and scores
	 * The filter here is used to only get question ids and question scores
	 * @param questionsList
	 * @param pageNumber
	 * @return
	 */
	public static ArrayList<String> buildQuestionsScoreUrl (ArrayList<Integer> questionsList) {
		String questionIDs = "";
		int n = questionsList.size();
		String filter = "!GeEeiQUx0cS46";
		ArrayList<String> listURL = new ArrayList<String> ();
		if (n<=100)
		{
			for (int i =0;i< n-1;i++)
			{
				questionIDs=questionIDs + questionsList.get(i)+";";
			}
			questionIDs=questionIDs+questionsList.get(n-1);
			listURL.add(DOMAIN_NAME + '/' + VERSION+"/questions/"+questionIDs
					+"?pagesize=100&order=desc&sort=votes&site=stackoverflow&filter=" + filter);
			return listURL;
			
		}else
		{
			int begin=0;
			int temp=100;
			boolean has_more=true;
			int var = 0;
			for (temp=100;temp<n+1;temp=temp+var)
			{
				questionIDs="";
				for (int i=begin;i<temp-1;i++)
				{
					questionIDs=questionIDs + questionsList.get(i)+";";
				}
				questionIDs=questionIDs+questionsList.get(temp-1);
				listURL.add(DOMAIN_NAME + '/' + VERSION+"/questions/"+questionIDs+
						"?pagesize=100&order=desc&sort=votes&site=stackoverflow&filter=" + filter);
				if (java.lang.Math.floorDiv(n-temp,100) != 0)
				{
					begin=temp;
					var=100;
				}else
				{
					if (n-temp ==0 )
					{
						temp++;
					}else
					{
						begin = temp;
						var=(n-temp);
					}
				}
			}
		}
		return listURL;

	
	}
	
	/**
	 * Build URL to get Award Count on badges earned by user (userId)
	 * The filter used at the end of the URL means we'll only get userid and badge count for each rank of badges on the site
	 * @param userId
	 * @param pageNumber 
	 * @return
	 */
	public static String buildBadgeAwardCountsUrl (Integer userId,Integer pageNumber) {
		String filter = "!mSGiqtI*CF";
		return DOMAIN_NAME + '/' + VERSION+"/users/"+userId+"/badges?page="+pageNumber+"&pagesize=100&order=desc&sort=awarded&site=stackoverflow&filter=" + filter;
	}
	
	/**
	 * Build URL to get page one of new posts in (tagName)
	 * The filter here is used to only get the id and the titles of the last 3 questions  posted on the site
	 * @param tagName
	 * @param pageNumber 
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String buildNewPostsUrl (String tagName, int nbPosts) throws UnsupportedEncodingException {
		int unixTime = (int) ((System.currentTimeMillis() / 1000L) - 84600) ; //  a day ago
		String filter = "!C(o*VY)(1IkzZXIFv";
		return DOMAIN_NAME + '/' + VERSION+"/questions?pagesize=" + nbPosts + "&fromdate="
				+(unixTime)+"&order=desc&sort=activity&tagged="+URLEncoder.encode(tagName,"UTF-8")
				+"&site=stackoverflow&filter=" + filter;
	}
	
// Dave user story methods
	
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
	
	
// Alice user story methods


	
	/**
<<<<<<< HEAD
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
	

	 /** Get User badges' award counts (keys are badges IDs while values are user (userId)'s award count in each badge
	 * @param userId
	 * @return
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public static TreeMap<Integer,Integer> getUserBadgeAwardCounts(Integer userId) throws IOException, JSONException
	{
		TreeMap<Integer,Integer> map = new TreeMap<>(); //returned variable
		boolean has_more=true;	//equals false if there is no more badges for the user ; true otherwise
		Integer pageNumber=1; 
		/*
		 * Variables used to extract the data
		 */
		String str_url;
		JSONObject badgeAwardCountsJSON;
		JSONArray badgeAwardCountsJSONArray;

		int arrayLength; //will contain JSONArray length
		
		/*
		 * While all tags haven't been recovered using the API,
		 *  get all tags in the current page, fill the Map with elements (badgeID : badgeAwardCount)
		 */
		while(has_more==true)
		{
			/*
			 * Variables to be put in the tree map at the end of each iteration of the for loop
			 */
			Integer badgeId,badgeAwardCount;
			
			str_url = buildBadgeAwardCountsUrl(userId,pageNumber);  // API URL
			badgeAwardCountsJSON = toJSONObject(str_url); // Extract JSONObject from API URL
			badgeAwardCountsJSONArray = badgeAwardCountsJSON.getJSONArray("items"); // Extract array of JSONObjects
			arrayLength=badgeAwardCountsJSONArray.length();	//Get JSONArray length
			
			/*
			 * For each JSONObject in the given page, get badgeId and award count for each badge and add them to the returned Map
			 */
			for (int i =0 ; i<arrayLength;i++)
			{
				JSONObject currentObject= badgeAwardCountsJSONArray.getJSONObject(i);
				badgeId = currentObject.getInt("badge_id");
				badgeAwardCount = currentObject.getInt("award_count");
				map.put(badgeId, badgeAwardCount);
			}
			System.out.println(pageNumber+" ok");
			has_more=badgeAwardCountsJSON.getBoolean("has_more");
			pageNumber++;
		}
		return map;	
	}
	

	
	
	/**
	 * Returns a treeMap <keys : Queston id , values : title>
	 * @param tagName
	 * @return
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public static TreeMap<Integer,String> getNewPosts(String tagName, int nbPosts) throws JSONException, IOException {
		TreeMap<Integer,String> map = new TreeMap<>(); //returned variable
		/*
		 * Variables used to extract the data
		 */
		String str_url=buildNewPostsUrl(tagName, nbPosts); //API URL
		JSONObject newPostsJSON=toJSONObject(str_url); // Extract JSONObject from API URL
		JSONArray newPostsJSONArray=newPostsJSON.getJSONArray("items"); // Extract array of JSONObjects 
		JSONObject currentObject;

		for (int i=0;i<nbPosts;i++)
		{
			currentObject = newPostsJSONArray.getJSONObject(i);
			Integer questionId=currentObject.getInt("question_id");
			String title = currentObject.getString("title");
			map.put(questionId,title);
			
		}
		return map;
	}
	
	
	/**
	 * Get all answers IDs for user (userId)
	 * @param userId
	 * @return treeMap <key : Question id, value : score>
	 * @throws JSONException 
	 * @throws IOException 
	 */
	
	public static  TreeMap<Integer,Integer> getQuestionsIdsAndScores (Integer userId) throws JSONException, IOException {
		String str_url;
		JSONObject JSON;
		JSONArray JSONArray;
		boolean has_more=true;
		int pageNumber=1; int arrayLength; int questionId;
		
		ArrayList<Integer> questionIds = new ArrayList<Integer> ();
		
		TreeMap<Integer,Integer> map= new TreeMap<Integer,Integer> ();
		
		while (has_more) {
			str_url = buildAnswersUrl(userId,pageNumber);  // API URL
			JSON = toJSONObject(str_url); // Extract JSONObject from API URL
			JSONArray = JSON.getJSONArray("items"); // Extract array of JSONObjects
			arrayLength=JSONArray.length();	//Get JSONArray length
			for (int i =0 ; i<arrayLength;i++)
			{
				JSONObject currentObject= JSONArray.getJSONObject(i);
				questionId = currentObject.getInt("question_id");
				questionIds.add(questionId);
			}
			has_more=JSON.getBoolean("has_more");
			pageNumber++;
		}
		boolean has_more2 = true;
		ArrayList<Integer> questionScores = new ArrayList<Integer> ();
		ArrayList<String> listOfApiURL = new ArrayList<String> ();
		listOfApiURL=buildQuestionsScoreUrl(questionIds);
			for (String s : listOfApiURL)
			{
				has_more2=true;
				while (has_more2)
				{
				JSON = toJSONObject(s);
				JSONArray = JSON.getJSONArray("items");
				arrayLength=JSONArray.length();
				for (int i =0; i<arrayLength;i++)
				{
					JSONObject currentObject= JSONArray.getJSONObject(i);
					questionScores.add(currentObject.getInt("score"));
				 }
				has_more2=JSON.getBoolean("has_more");
				}
		}
		for (int i = 0; i < questionIds.size(); i++) {
			   map.put(questionIds.get(i), questionScores.get(i));
			}
		return map;

	}


	/**
	 * Get the toptags to which a user has contributed (ranked by answer score)
	 * @param userId
	 * @param nbTags (<100)
	 * @return
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public static ArrayList<String> getTopTags (Integer userId, int nbTags) throws IOException, JSONException{
		String filter = "!SuLWjw(qAdrLpexM8F";
		ArrayList<String> tagList = new ArrayList<String> ();
		String str_url= DOMAIN_NAME + '/' + VERSION+
				"/users/"+userId+"/top-tags?pagesize="+nbTags+"&site=stackoverflow&filter=" + filter;
		JSONObject JSON = null;
		
			JSON = toJSONObject(str_url);

		JSONArray JSONArray = null;

			JSONArray = JSON.getJSONArray("items");

		String tagName = null;
		JSONObject currentObject = null;
		for (int i =0;i<JSONArray.length();i++)
		{
			currentObject=JSONArray.getJSONObject(i);
			tagName=currentObject.getString("tag_name");

			tagList.add(tagName);
		}
		return tagList;	
	}
	
	
// Bob user story methods
	
	
// Charlie user story methods


	 //Methode main pour test classe
	   public static void main(String[] args) throws JSONException, IOException, URISyntaxException {
		

		System.out.println(getTagUserScore(1200, 10));
		

		   /*
		    * TEST DAVE
		ArrayList<String> Tags = StackExchangeApiManager.getTags(1);
		TreeMap<Integer,ArrayList<Integer>> AnswerersMap= StackExchangeApiManager.getTopAnswerers("javascript");
		
		System.out.println("Liste de tags ");
		System.out.println(Tags.toString());
		
		System.out.println("Liste des userIDs");
		System.out.println(AnswerersMap.keySet().toString());
		
		System.out.println("Liste des score & post Count");
		System.out.println(AnswerersMap.values().toString());
		*/
		   
		   Integer userId=1200;
		   /*
		   TreeMap<Integer,String> newPosts = getNewPosts("javascript", 20);
		   System.out.println("Nouveaux posts en javascript");
		   System.out.println(newPosts.toString());
		   
		   TreeMap<Integer,Integer> badgeAwardCounts = getUserBadgeAwardCounts(userId);
		   System.out.println("Award Counts pour les badges de  l user 1200");
		   System.out.println(badgeAwardCounts.toString());
		   */
		   //TreeMap<Integer,Integer> posts = getQuestionsIds_And_Scores(userId);
		   TreeMap<Integer,Integer> posts = new TreeMap<>();
		   posts.put(12576,3);
		   System.out.println(posts.get(12576));
		   /*
		   System.out.println("Questions auxquelles l user 1200 a répondu ");
		   System.out.println(posts.toString()); */

	} 
}