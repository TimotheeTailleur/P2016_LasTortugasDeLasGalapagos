package fr.tse.info4.project.user;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Answer;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.Tag;

import fr.tse.info4.project.datarecovery.AliceApiManager;
import fr.tse.info4.project.datarecovery.ApiManager;

/**
 * Functions and demo main method for user story Alice
 */
public class Alice {

	private String accessToken = null;

	// ------------- Alice 1 ----------------

	protected int nbTags = 3;

	/*
	 * Number of question for each tag. Limited tp 100 questions <br> Default
	 * value : 3
	 */
	private int nbQuestionsPerTag = 3;

	
	// ----------------- Alice 3 ------------------
	
	private int nbAnswers = 100;
	
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
	 * For each tag in top 100 tag List, get nbQuestionsPerTag the newest
	 * unanswered posts on stackoverflow and add themp to a treemap for display
	 * User id identified by its id.
	 * 
	 * @param idUser
	 * @return
	 * 
	 */
	public Map<String, PagedList<Question>> getNewQuestions(int idUser) {
		PagedList<Tag> tags = ApiManager.getTags(nbTags, idUser);
		if (tags.size() == 0) {
			System.err.println("No tag for this user");
			return null;
		}
		Map<String, PagedList<Question>> questionsForTags = new HashMap<String, PagedList<Question>>();

		for (int i = 0; i < tags.size(); i++) {
			String tagName = tags.get(i).getName();
			PagedList<Question> questionForTag = AliceApiManager.getNewQuestionsByTag(tagName, nbQuestionsPerTag);
			questionsForTags.put(tagName, questionForTag);
		}

		return questionsForTags;
	}

	/**
	 * For each tag in top 100 tag List, get nbQuestionsPerTag the newest
	 * unanswered posts on stackoverflow and add themp to a treemap for display.
	 * User id identified by its access token.
	 * 
	 * @return
	 */
	public Map<String, PagedList<Question>> getNewQuestions() {
		if (accessToken == null) {
			System.err.println("Access token doesn't specified");
			return null;
		}
		return getNewQuestions((int) ApiManager.getIdUser(accessToken));
	}

	/**
	 * 
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public ArrayList<Integer> compareBadge() throws IOException, JSONException {

		// On récupère les badges d'alice et leurs nombres
		// TreeMap<Integer, Integer> badgesAlice =
		// StackExchangeApiManager.getUserBadgeAwardCounts(userId);
		return null;
	}

	public List<Question> getSortedAnsweredQuestions(int idUser) {
		List<Answer> answers = AliceApiManager.getAnswers(idUser, nbAnswers);
		int answersSize = answers.size();
		List<Long> idsQuestions = new ArrayList<Long>(answersSize);
		for (int i = 0 ; i< answersSize ; i++){
			long id =  answers.get(i).getQuestionId();
			if (!idsQuestions.contains(id)){
				idsQuestions.add(id);
			}
		}
		
		return AliceApiManager.getSortedQuestions(idsQuestions);
	}
	
	public List<Question> getSortedAnsweredQuestions(){
		if (accessToken == null) {
			System.err.println("Access token doesn't specified");
			return null;
		}
		return getSortedAnsweredQuestions((int) ApiManager.getIdUser(accessToken));
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

		List<Question> questions = user.getSortedAnsweredQuestions(1200);
		for (int i = 0 ; i<questions.size(); i++){
			System.out.println(questions.get(i).getTitle());
			System.out.println(questions.get(i).getQuestionId());
			System.out.println(questions.get(i).getScore());
		}
	}

}
