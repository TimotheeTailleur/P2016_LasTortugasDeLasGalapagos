package fr.tse.info4.project.user;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.Tag;

import fr.tse.info4.project.datarecovery.ApiManager;
import fr.tse.info4.project.datarecovery.BobApiManager;

public class Bob {

	/*
	 * Access token of the user for the application. <br> Default value : null
	 * 
	 */
	private String accessToken = null;

	// ------------------ BOB 4 : new Questions by user's tags -----------------
	
	/*
	 * Number of the most popular tags of Bob. <br> Number limited to 100 tags
	 * Default value : 5.
	 */
	private int nbTags = 5;

	/*
	 * Number of questions displayed for each tag. <br> Default value : 3
	 * <br> Number limited to 30 questions
	 */
	private int nbQuestionsPerTag = 3;

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

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
	 * Return the new questions (with at least one answer) amon user's best
	 * popular tags <br>
	 * User id identified by his acces token.
	 * 
	 * @return
	 */
	public TreeMap<String, ArrayList<Question>> getNewQuestionsAnswered() {
		if (accessToken == null) {
			System.err.println("Access token doesn't specified");
			return null;
		}
		return getNewQuestionsAnswered((int) ApiManager.getIdUser(accessToken));
	}

	/**
	 * Return the new questions (with at least one answer) amon user's best
	 * popular tags <br>
	 * User id identified by his id.
	 * 
	 * @return
	 */
	public TreeMap<String, ArrayList<Question>> getNewQuestionsAnswered(int idUser) {
		PagedList<Tag> tags = ApiManager.getTags(nbTags, idUser);
		if (tags.size() == 0) {
			System.err.println("No tag for this user");
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
