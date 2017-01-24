package fr.tse.info4.project.model.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Answer;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.Tag;

import fr.tse.info4.project.model.datarecovery.AliceApiManager;
import fr.tse.info4.project.model.datarecovery.ApiManager;

/**
 * Functions and demo main method for user story Alice
 */
public class Alice extends Personae {

	/**
	 * Number of the best user's tags. <br>
	 * Default value : 3.
	 */
	protected int nbTags = 3;

	/**
	 * Number of the newest displayed questions for each tag. <br>
	 * Limited to 100 questions <br>
	 * Default value : 3
	 */
	private int nbQuestionsPerTag = 3;

	/*
	 * Number of the best user's answers. <br> Default value : 10.
	 */
	private int nbAnswers = 10;

	// -------- Constructors -----------
	/**
	 * Default Constructor
	 */
	public Alice() {
		apiManager = new AliceApiManager();
	}

	/**
	 * Constructor.
	 * 
	 * @param accessToken
	 */
	public Alice(String accessToken) {
		apiManager = new AliceApiManager();
		this.accessToken = accessToken;
	}

	// --------- Getter and setters -------------

	/**
	 * Get number of questions showing by tag.
	 * @return nbQuestionsPerTag
	 */
	public int getNbQuestionsPerTag() {
		return nbQuestionsPerTag;
	}

	/**
	 * Set number of questions showing by tag.
	 * @param nbQuestionsPerTag
	 */
	public void setNbQuestionsPerTag(int nbQuestionsPerTag) {
		if (nbQuestionsPerTag <= 100) {
			this.nbQuestionsPerTag = nbQuestionsPerTag;
		}
	}

	/**
	 * Get number of tags in which new questions are searched.
	 * @return nbTags
	 */
	public int getNbTags() {
		return nbTags;
	}

	/**
	 * Set number of tags in which new questions are searched.
	 * @param nbTags
	 */
	public void setNbTags(int nbTags) {
		this.nbTags = nbTags;
	}

	/**
	 * Get number of answers showing.
	 * @return nbAnswers
	 */
	public int getNbAnswers() {
		return nbAnswers;
	}

	/**
	 * Set number of answers showing.
	 * @param nbAnswers
	 */
	public void setNbAnswers(int nbAnswers) {
		if (nbAnswers > 0)
			this.nbAnswers = nbAnswers;
	}

	// ----------------------- Alice 1 : New Questions ------------------------
	/**
	 * Create all combination of elements in the Set parameter.
	 * @param originalSet
	 * @return
	 */
	public static <T> Set<Set<T>> powerSet(Set<T> originalSet) {
	    Set<Set<T>> sets = new HashSet<Set<T>>();
	    if (originalSet.isEmpty()) {
	        sets.add(new HashSet<T>());
	        return sets;
	    }
	    List<T> list = new ArrayList<T>(originalSet);
	    T head = list.get(0);
	    Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
	    for (Set<T> set : powerSet(rest)) {
	        Set<T> newSet = new HashSet<T>();
	        newSet.add(head);
	        newSet.addAll(set);
	        sets.add(newSet);
	        sets.add(set);
	    }    
	    return sets;
	}  
	/**
	 * For each tag in top tag List, get the the newest unanswered posts.
	 * 
	 * @param idUser
	 * @return the new questions in the best user's tags.
	 * 
	 */
	private Map<String[], PagedList<Question>> getNewQuestions(int idUser) {
		PagedList<Tag> tags = ApiManager.getBestTags(nbTags, idUser);
		
		if (tags.size() == 0) {
			System.err.println("No tags for this user");
			return null;
		}
		
		Set<String> tagsSet = new HashSet<>(tags.size());
		for (Tag tag : tags){
			tagsSet.add(tag.getName());
		}
		Set<Set<String>> powerSet = powerSet(tagsSet);
		
		Map<String[], PagedList<Question>> questionsForTags = new HashMap<String[], PagedList<Question>>();

		for (Set<String> set : powerSet ) {
			ArrayList<String> tagsList= new ArrayList<>();
			for (String tag : set){
				tagsList.add(tag);
			}
			String[] tagsArray = new String[tagsList.size()];
			tagsList.toArray(tagsArray); 
			
			PagedList<Question> questionForTag = ((AliceApiManager) apiManager).getNewQuestionsByTag(tagsArray,
					nbQuestionsPerTag);
			questionsForTags.put(tagsArray, questionForTag);
		}

		return questionsForTags;
	}

	/**
	 * For each tag in top tag List, get the the newest unanswered posts.
	 * 
	 * @return the new questions in the best user's tags.
	 */
	public Map<String[], PagedList<Question>> getNewQuestions() {
		if (accessToken == null) {
			return getNewQuestions(idUser);
		}
		return getNewQuestions((int) ApiManager.getIdUser(accessToken));
	}

	
	// ------------- Alice 2 : Badges ------------------
	public ArrayList<Integer> compareBadge() {

		// On récupère les badges d'alice et leurs nombres
		// TreeMap<Integer, Integer> badgesAlice =
		// StackExchangeApiManager.getUserBadgeAwardCounts(userId);
		return null;
	}

	
	// ---------- Alice 3 : Sort Answers -------------
	/**
	 * Sort (by score) the answers an user have posted.
	 * 
	 * @param idUser
	 * @return the best user's answers.
	 */
	private List<Answer> getSortedAnswers(int idUser) {
		return ((AliceApiManager) apiManager).getAnswers(idUser, nbAnswers);
	}

	/**
	 * Sort (by score) the answers an user have posted.
	 * 
	 * @return the best user's answers.
	 */
	public List<Answer> getSortedAnswers() {
		if (accessToken == null) {
			return getSortedAnswers(idUser);
		}
		return getSortedAnswers((int) ApiManager.getIdUser(accessToken));
	}

}
