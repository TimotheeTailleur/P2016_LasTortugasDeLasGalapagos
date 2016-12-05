package fr.tse.info4.project.user;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;

import org.json.JSONException;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.Tag;

import fr.tse.info4.project.database.AliceDatabaseManager;
import fr.tse.info4.project.database.DatabaseManager;
import fr.tse.info4.project.datarecovery.AliceApiManager;
import fr.tse.info4.project.datarecovery.ApiManager;
import fr.tse.info4.project.datarecovery.StackExchangeApiManager;

/**
 * Functions and demo main method for user story Alice
 */
public class Alice {

	private String accessToken = null;

	protected int nbTags = 3;

	/*
	 * Number of question for each tag. Limited tp 100 questions <br> Default
	 * value : 3
	 */
	private int nbQuestionsPerTag = 3;

	public int getNbQuestionsPerTag() {
		return nbQuestionsPerTag;
	}

	public void setNbQuestionsPerTag(int nbQuestionsPerTag) {
		if (nbQuestionsPerTag <= 100) {
			this.nbQuestionsPerTag = nbQuestionsPerTag;
		}
	}

	public String getAccesToken() {
		return accessToken;
	}

	public void setAccesToken(String accessToken) {
		this.accessToken = accessToken;
	}

	
	
	// ----------------------- Alice 1 : New Questions ------------------------
	/**
	 * For each tag in top 100 tag List, get nbQuestionsPerTag the newest unanswered posts on
	 * stackoverflow and add themp to a treemap for display
	 *  User id identified by its id.
	 * 
	 * @param idUser
	 * @return

	 */
	public TreeMap<String, PagedList<Question>> getNewQuestions(int idUser) {
		PagedList<Tag> tags = ApiManager.getTags(nbTags, idUser);
		if (tags.size() == 0) {
			System.err.println("No tag for this user");
			return null;
		}
		TreeMap<String, PagedList<Question>> questionsForTags = new TreeMap<String, PagedList<Question>>();

		for (int i = 0; i < tags.size(); i++) {
			String tagName = tags.get(i).getName();
			PagedList<Question> questionForTag = AliceApiManager.getNewQuestionsByTag(tagName, nbQuestionsPerTag);
			questionsForTags.put(tagName, questionForTag);
		}

		return questionsForTags;
	}
	
	/**
	 * For each tag in top 100 tag List, get nbQuestionsPerTag the newest unanswered posts on
	 * stackoverflow and add themp to a treemap for display.
	 * User id identified by its access token.
	 * @return
	 */
	public TreeMap<String, PagedList<Question>> getNewQuestions(){
		if (accessToken == null) {
			System.err.println("Access token doesn't specified");
			return null;
		}
		return getNewQuestions((int)ApiManager.getIdUser(accessToken));
	}

	/**
	 * 
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public ArrayList<Integer> compareBadge() throws IOException, JSONException {

		// On récupère les badges d'alice et leurs nombres
		TreeMap<Integer, Integer> badgesAlice = StackExchangeApiManager.getUserBadgeAwardCounts(userId);
		return null;
	}

	/**
	 * 
	 * Return a list of questions the user answered ranked by their score .
	 * 
	 * @param nbQuestions
	 * @param nbHours
	 * @param forceUpdate
	 * @return ArrayList<TreeMap<String, Integer>> : list of map where the keys
	 *         are idQuestion and score
	 */
	public ArrayList<TreeMap<String, Integer>> getSortedAnsweredQuestions(int nbQuestions, int nbHours,
			boolean forceUpdate) {
		// Duration between the last update and the current time
		long time = AliceDatabaseManager.getTime(this);

		ArrayList<TreeMap<String, Integer>> questionsSorted = new ArrayList<TreeMap<String, Integer>>();

		if (time == -1 || time > nbHours * 3600 || forceUpdate) {
			AliceDatabaseManager.fillTableQuestion(this);
		}

		DatabaseManager.setup();
		String sql = "SELECT ID_QUESTION, SCORE FROM "
				+ DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_QUESTIONS_TABLE) + "ORDER BY SCORE DESC";

		try {
			Statement stmt = DatabaseManager.databaseConnection.createStatement();
			ResultSet res = stmt.executeQuery(sql);

			int cpt = 0;
			while (res.next() && cpt < nbQuestions) {
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
		Alice user = new Alice();
		
		System.out.println(user.getNewQuestions(7194334));
	}

}
