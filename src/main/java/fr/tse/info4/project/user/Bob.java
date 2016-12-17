package fr.tse.info4.project.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.Tag;

import fr.tse.info4.project.datarecovery.ApiManager;
import fr.tse.info4.project.datarecovery.BobApiManager;

public class Bob {

	/*
	 * Application user access token. <br> Default value : null
	 */
	private String accessToken = null;
	
	/**
	 * ApiManager for Bob user story
	 */
	private BobApiManager apiManager;
	
	/**
	 * Constructor. Initiates BobApiManager
	 */
	public Bob() {
		super();
		apiManager = new BobApiManager();
	}
	/**
	 * Accesstoken getter
	 * @param accessToken
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	// ------------------ BOB 4 : Recent answered questions in user's top tags -----------------
	
	/*
	 * Number of Bob's most popular tags. <br> Number limited to 100 tags
	 * <br> Default value : 5.
	 */
	private int nbTags = 5;

	/*
	 * Number of questions displayed for each tag. <br> Default value : 3
	 * <br> Number limited to 30 questions
	 */
	private int nbQuestionsPerTag = 3;

	public int getNbTags() {
		return nbTags;
	}

	public void setNbTags(int nbTags) {
		if (nbTags >0 && nbTags <=100)
			this.nbTags = nbTags;
	}

	public int getNbQuestionsPerTag() {
		return nbQuestionsPerTag;
	}

	public void setNbQuestionsPerTag(int nbQuestionsPerTag) {
		if (nbQuestionsPerTag >0 && nbQuestionsPerTag <=30)
			this.nbQuestionsPerTag = nbQuestionsPerTag;
	}

	/**
	 * Returns recent answered  questions (with at least one answer) in user's top tags <br>
	 * User id obtained by access token.
	 * 
	 * @return
	 */
	public TreeMap<String, ArrayList<Question>> getNewQuestionsAnswered() {
		if (accessToken == null) {
			System.err.println("Access token isn't specified");
			return null;
		}
		return getNewQuestionsAnswered((int) ApiManager.getIdUser(accessToken));
	}

	/**
	 * Returns recent answered  questions (with at least one answer) in user's top tags <br>
	 * User  identified by his id.
	 * 
	 * @return
	 */
	public TreeMap<String, ArrayList<Question>> getNewQuestionsAnswered(int idUser) {
		PagedList<Tag> tags = ApiManager.getTags(nbTags, idUser);
		if (tags.size() == 0) {
			System.err.println("No tags for this user");
			return null;
		}

		TreeMap<String, ArrayList<Question>> questionsForTags = new TreeMap<String, ArrayList<Question>>();

		for (int i = 0; i < tags.size(); i++) {
			String tagName = tags.get(i).getName();
			ArrayList<Question> questionsForTag = BobApiManager.getAnsweredQuestionsByTag(tagName, nbQuestionsPerTag);
			questionsForTags.put(tagName, questionsForTag);
		}

		return questionsForTags;

	}

	// ------------------ BOB 2 : Suggest keywords (tags) based on user input of a Question title -----------------
	
	/**
	 * Suggest keywords based on title of the question the user wishes to submit
	 * @param questionTitle
	 * @return
	 */
	public List<String> findKeyWords(String questionTitle)
	{
		
		return apiManager.findKeyWords(questionTitle);
	}
	
	// ------------------ BOB 1 : Find similar questions to the one the user wishes to submit -----------------
	
	/**
	 * Number of similar questions to display when a user's entering a questionTitle
	 * <br> Default value : 10
	 */
	private int nbSimilarQuestions = 10;
	
	/**
	 * nbSimilarQuestions setter
	 * @param nbSimilarQuestions
	 */
	public void setNbSimilarQuestions(int nbSimilarQuestions) {
		this.nbSimilarQuestions = nbSimilarQuestions;
	}
	
	/**
	 * Find similar questions to that which the user wishes to submit
	 * @param questionTitle 
	 * @return
	 */
	public List<Question> findSimilarQuestions(String questionTitle)
	{
		return apiManager.findSimilarQuestions(questionTitle, nbSimilarQuestions);
	}
	
	
	
	/**
	 * Main demo method
	 * @param args
	 */
	public static void main(String[] args) {
		Bob user = new Bob();
		// Avec id user
		TreeMap<String, ArrayList<Question>> newQuestions = user.getNewQuestionsAnswered(1200);
		if (newQuestions != null) {

			for (Entry<String, ArrayList<Question>> entry : newQuestions.entrySet()) {
				System.out.println(entry.getKey() + " : ");
				ArrayList<Question> questions = entry.getValue();
				for (int i = 0; i < questions.size(); i++) {
					System.out.println("- " + questions.get(i).getTitle());
				}
			}
		}

		/*
		 * // Avec access token
		 * user.setAccessToken(Authenticate.getAcessToken()); TreeMap<String,
		 * ArrayList<Question>> newQuestionsToken = user.getNewQuestionsAnswered(); if
		 * (newQuestionsToken != null) { for (Entry<String, ArrayList<Question>>
		 * entry : newQuestionsToken.entrySet()) {
		 * System.out.println(entry.getKey() + " : "); ArrayList<Question>
		 * questions = entry.getValue(); for (int i = 0; i < questions.size();
		 * i++) { System.out.println("- " + questions.get(i).getTitle()); } } }
		 */
	}

}
