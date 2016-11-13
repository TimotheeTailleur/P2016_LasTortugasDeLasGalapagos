package tse.info4.project.user.dave;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.json.JSONException;

import tse.info4.project.datarecovery.StackExchangeApiManager;

/**
 * Functions and demo main method for user story Alice
 */
public class Alice {
	
	protected int userId;
	protected static ArrayList<String> tagList;
	
	/**
	 * Constructor that inits userId from input and creates the list of top 100 tags to which the user has contributed
	 * @param userId
	 * @throws IOException
	 * @throws JSONException
	 */
	public Alice(int userId) throws IOException, JSONException {
		super();
		this.userId = userId;
		tagList=StackExchangeApiManager.getTopTags(userId);
	}
	
	/**
	 * For each tag in top 100 tag List, get 3 newest unanswered posts on stackoverflow and add themp to a treemap for diaplay
	 * @return 
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public TreeMap<String,TreeMap<Integer,String>> getNewQuestions() throws JSONException, IOException{
		TreeMap<String,TreeMap<Integer,String>> resultMap = new TreeMap<String,TreeMap<Integer,String>>();
		for (String s : tagList)
		{
			TreeMap<Integer,String> currentNewPostsMap = StackExchangeApiManager.getNewPosts(s);
			resultMap.put(s,currentNewPostsMap);
		}
		return resultMap;
	}
	
	/**
	 * 
	 * @return
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public ArrayList<Integer> compareBadge() throws IOException, JSONException{
		
		// On récupère les badges d'alice et leurs nombres
		TreeMap<Integer,Integer> badgesAlice = StackExchangeApiManager.getUserBadgeAwardCounts(userId);
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> sortQuestions(){
		 
		return null;
	}
	
	
	

}
