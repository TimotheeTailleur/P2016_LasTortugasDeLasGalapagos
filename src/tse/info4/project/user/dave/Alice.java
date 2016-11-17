package tse.info4.project.user.dave;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;

import org.json.JSONException;

import main.Main;
import tse.info4.project.database.AliceDatabaseManager;
import tse.info4.project.database.DatabaseManager;
import tse.info4.project.datarecovery.StackExchangeApiManager;

/**
 * Functions and demo main method for user story Alice
 */
public class Alice {
	
	protected  int userId;
	protected  ArrayList<String> tagList;
	protected int nbTags=3;
	
	public int getUserId() {
		return userId;
	}

	private void setnbTags(int nbTags) {
		this.nbTags = nbTags;
	}
	
	/**
	 * Constructor that inits userId from input and creates the list of top 100 tags to which the user has contributed
	 * @param userId
	 */
	public Alice(int userId){
		super();
		this.userId = userId;
		try {
			tagList=StackExchangeApiManager.getTopTags(userId, nbTags);
		} catch (IOException | JSONException e) {
			System.out.println("Alice - (Alice) Accès à l'api impossible");
		}
	}
	
	/**
	 * For each tag in top 100 tag List, get 3 the  newest unanswered posts on stackoverflow and add themp to a treemap for display
	 * 
	 * @return 
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public TreeMap<String,TreeMap<Integer,String>> getNewQuestions() throws JSONException, IOException{
		TreeMap<String,TreeMap<Integer,String>> resultMap = new TreeMap<String,TreeMap<Integer,String>>();
		for (String s : tagList)
		{
			TreeMap<Integer,String> currentNewPostsMap = StackExchangeApiManager.getNewPosts(s,3);
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
	 * Return a list of questions the user answered ranked by their score
	 * .
	 * 
	 * @param nbQuestions
	 * @param nbHours
	 * @param forceUpdate
	 * @return ArrayList<TreeMap<String, Integer>> : list of map where the keys are idQuestion and score
	 */
	public ArrayList<TreeMap<String, Integer>> getSortedAnsweredQuestions(int nbQuestions, int nbHours, boolean forceUpdate){
		// Duration between the last update and the current time
		long time = AliceDatabaseManager.getTime(this);
		
		ArrayList<TreeMap<String, Integer>> questionsSorted = new ArrayList<TreeMap<String, Integer>>();
		
		if (time == -1 || time > nbHours * 3600 || forceUpdate){
			AliceDatabaseManager.fillTableQuestion(this);
		}
		
		DatabaseManager.setup();
		String sql = "SELECT ID_QUESTION, SCORE FROM " + DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_QUESTIONS_TABLE) + "ORDER BY SCORE DESC";	
		
		try {
			Statement stmt = DatabaseManager.databaseConnection.createStatement();
			ResultSet res = stmt.executeQuery(sql);
			
			int cpt = 0;
			while(res.next() && cpt <nbQuestions){
				int idQuestion = res.getInt("ID_QUESTION");
				int score = res.getInt("SCORE");
				
				TreeMap<String, Integer> questionData = new TreeMap<String, Integer>();
				questionData.put("idQuestion", idQuestion);
				questionData.put("score", score);
				questionsSorted.add(questionData);
				cpt++;
				
			}
			
			stmt.close();
			res.close();
			
		} catch (SQLException e) {
			System.out.println("sortQuestion (Alice) - Erreur lors de la requête sql");
			e.printStackTrace();
		}
		
		
		
		return questionsSorted;
	}
	
	/**
	 * Returns Stack Overflow URL of question (id)
	 * 
	 * @param id
	 * @return Stack Overflow question URL
	 * @throws IOException
	 * @throws JSONException
	 */

	public static String getLinkQuestion(int id) {
		return ("stackoverflow.com/q/" + id);
	}
	
	public static void main(String[] args) throws JSONException, IOException {
		Alice user = new Alice(1200);
		System.out.println(user.getSortedAnsweredQuestions(3, 48, false));
	}
	
	

}
