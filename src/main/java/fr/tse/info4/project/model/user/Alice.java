package fr.tse.info4.project.model.user;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Answer;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.Tag;

import fr.tse.info4.project.model.datarecovery.AliceApiManager;
import fr.tse.info4.project.model.datarecovery.ApiManager;

/**
 * Functions and demo main method for user story Alice
 */
public class Alice extends Personae{

	

	// ------------- Alice 1 ----------------

	protected int nbTags = 3;

	/*
	 * Number of newest questions displayed for each tag. 
	 * <br> Limited to 100 questions 
	 * <br> Default value : 3
	 */
	private int nbQuestionsPerTag = 3;

	
	// ----------------- Alice 3 ------------------
	
	private int nbAnswers = 100;
	
	
	public Alice(){
		apiManager = new AliceApiManager();
	}
	
	public Alice(String accessToken){
		apiManager = new AliceApiManager();
		this.accessToken = accessToken;
	}
	
	public int getNbQuestionsPerTag() {
		return nbQuestionsPerTag;
	}

	public void setNbQuestionsPerTag(int nbQuestionsPerTag) {
		if (nbQuestionsPerTag <= 100) {
			this.nbQuestionsPerTag = nbQuestionsPerTag;
		}
	}
	

	

	public int getNbTags() {
		return nbTags;
	}

	public void setNbTags(int nbTags) {
		this.nbTags = nbTags;
	}

	public int getNbAnswers() {
		return nbAnswers;
	}

	public void setNbAnswers(int nbAnswers) {
		this.nbAnswers = nbAnswers;
	}
	

	// ----------------------- Alice 1 : New Questions ------------------------
	/**
	 * For each tag in top 100 tag List, get the nbQuestionsPerTag newest
	 * unanswered posts on stackoverflow and add them to a treemap for display
	 * 
	 * Overloaded to use AccessToken authentication or enter a given userId
	 * 
	 * @param idUser
	 * @return
	 * 
	 */
	private Map<String, PagedList<Question>> getNewQuestions(int idUser) {
		PagedList<Tag> tags = ApiManager.getTags(nbTags, idUser);
		if (tags.size() == 0) {
			System.err.println("No tags for this user");
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
	 * Overloads getNewQuestions to allow for accesToken authentication
	 * 
	 * 
	 * @return
	 */
	public Map<String, PagedList<Question>> getNewQuestions() {
		if (accessToken == null) {
			return getNewQuestions(idUser);			
		}
		return getNewQuestions((int) ApiManager.getIdUser(accessToken));
	}

	/**
	 * 
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public ArrayList<Integer> compareBadge() throws IOException {

		// On récupère les badges d'alice et leurs nombres
		// TreeMap<Integer, Integer> badgesAlice =
		// StackExchangeApiManager.getUserBadgeAwardCounts(userId);
		return null;
	}
	
	/**
	 * Sort the questions to which a user has given answers by popularity
	 * Overloaded to use AccessToken authentication or enter a given userId
	 * @param idUser
	 * @return
	 */

	private List<Question> getSortedAnsweredQuestions(int idUser) {
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
	
	/**
	 * Overloads getSortedAnsweredQuestions to allow for accessToken authentication
	 * @return
	 */
	public List<Question> getSortedAnsweredQuestions(){
		if (accessToken == null) {
			return getSortedAnsweredQuestions(idUser);
		}
		return getSortedAnsweredQuestions((int) ApiManager.getIdUser(accessToken));
	}

	


}
