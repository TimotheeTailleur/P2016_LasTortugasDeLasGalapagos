package fr.tse.info4.project.model.user;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.Tag;
import com.google.code.stackexchange.schema.User;

import fr.tse.info4.project.model.datarecovery.ApiManager;
import fr.tse.info4.project.model.datarecovery.BobApiManager;
import fr.tse.info4.project.model.schema.TagScore;

public class Bob extends Personae {

	/**
	 * Number of Bob's most popular tags. <br>
	 * Number limited to 100 tags <br>
	 * Default value : 5.
	 */
	private int nbBestTags = 5;

	/**
	 * Number of questions displayed for each tag. <br>
	 * Default value : 3 <br>
	 * Number limited to 30 questions
	 */
	private int nbQuestionsPerTag = 3;


	/**
	 * Number of similar questions to display when a user's entering a
	 * questionTitle <br>
	 * Default value : 10
	 */
	private int nbSimilarQuestions = 10;

	/**
	 * Number of experts to follow in each tag.
	 */
	private int nbExpertsPerTag = 3;

	// ---------------- Constructor ----------------

	/**
	 * Constructor. Initiates BobApiManager
	 */
	public Bob() {
		apiManager = new BobApiManager();
	}

	/**
	 * Constructor.
	 * 
	 * @param accessToken
	 */
	public Bob(String accessToken) {
		apiManager = new BobApiManager();
		this.accessToken = accessToken;
	}

	// -------- Getters and Setters ----------

	public int getNbTags() {
		return nbBestTags;
	}

	public int getNbExpertsPerTag() {
		return nbExpertsPerTag;
	}

	public void setNbTags(int nbTags) {
		if (nbTags > 0 && nbTags <= 100)
			this.nbBestTags = nbTags;
	}

	public int getNbQuestionsPerTag() {
		return nbQuestionsPerTag;
	}

	public void setNbQuestionsPerTag(int nbQuestionsPerTag) {
		if (nbQuestionsPerTag > 0 && nbQuestionsPerTag <= 30)
			this.nbQuestionsPerTag = nbQuestionsPerTag;
	}

	public void setNbSimilarQuestions(int nbSimilarQuestions) {
		if (nbSimilarQuestions > 0)
			this.nbSimilarQuestions = nbSimilarQuestions;
	}

	public void setNbExpertsPerTag(int nbExperts) {
		if (nbExperts > 0)
			this.nbExpertsPerTag = nbExperts;
	}
	
	public int getNbSimilarQuestions() {
		return nbSimilarQuestions;
	}


	// -------------- Methods ----------------

	// ------------------ BOB 1 : Find similar questions to the one the user
	// wishes to submit -----------------

	/**
	 * Find similar questions to that which the user wishes to submit
	 * 
	 * @param questionTitle
	 * @return
	 */
	public List<Question> findSimilarQuestions(String questionTitle) {
		return ((BobApiManager) apiManager).findSimilarQuestions(questionTitle, nbSimilarQuestions);
	}

	// ------------------ BOB 2 : Suggest keywords (tags) based on user input of
	// a Question title -----------------

	/**
	 * Suggest keywords based on title of the question the user wishes to submit
	 * 
	 * @param questionTitle
	 * @return
	 */
	public List<String> findKeyWords(String questionTitle) {

		return ((BobApiManager) apiManager).findKeyWords(questionTitle);
	}

	// ------------------ BOB 3 -------------------

	/**
	 * Retrieves the best users (sorted by score) in the top bob tags.
	 *  @return the best users that a beginner developer could follow.
	 */
	public Set<User> getExperts() {
		if (accessToken == null) {
			return getExperts(idUser);
		}
		return getExperts((int) ApiManager.getIdUser(accessToken));
	}

	/**
	 * Retrieves the best users (sorted by score) in the top bob tags.
	 * @param idUser
	 * @return the best users that a beginner developer could follow.
	 */
	private Set<User> getExperts(int idUser) {
		PagedList<Tag> tags = ApiManager.getBestTags(nbBestTags, idUser);
		if (tags.size() == 0) {
			System.err.println("No tags for this user");
			return null;
		}
		Set<User> users = new HashSet<>();
		for (Tag tag : tags) {
			List<TagScore> bestUsers = apiManager.getTopAnswerers(tag.getName());
			for (int i = 0; i < nbExpertsPerTag; i++) {
				users.add(bestUsers.get(i).getUser());
			}
		}

		return users;
	}

	// ----------- BOB 4 -----------------
	/**
	 * Retrieves recent answered questions (with at least one answer) in user's
	 * top tags <br>
	 * 
	 * @return recent answered questions.
	 */
	public Map<String, List<Question>> getNewQuestionsAnswered() {
		if (accessToken == null) {
			return getNewQuestionsAnswered(idUser);
		}
		return getNewQuestionsAnswered((int) ApiManager.getIdUser(accessToken));
	}

	/**
	 * Retrieves recent answered questions (with at least one answer) in user's
	 * top tags <br>
	 * 
	 * @param idUser
	 * @return recent answered questions.
	 */
	private Map<String, List<Question>> getNewQuestionsAnswered(int idUser) {
		PagedList<Tag> tags = ApiManager.getBestTags(nbBestTags, idUser);
		if (tags.size() == 0) {
			System.err.println("No tags for this user");
			return null;
		}

		Map<String, List<Question>> questionsForTags = new TreeMap<String, List<Question>>();

		for (int i = 0; i < tags.size(); i++) {
			String tagName = tags.get(i).getName();
			List<Question> questionsForTag = ((BobApiManager) apiManager).getAnsweredQuestionsByTag(tagName,
					nbQuestionsPerTag);
			questionsForTags.put(tagName, questionsForTag);
		}

		return questionsForTags;

	}

}
