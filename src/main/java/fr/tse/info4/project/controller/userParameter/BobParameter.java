package fr.tse.info4.project.controller.userParameter;

import fr.tse.info4.project.model.user.Bob;

/**
 * Handles the Bob parameters.
 */
public class BobParameter {

	/**
	 * Bob object.
	 */
	private Bob bob;
	
	/**
	 * Default Constructor. <br>
	 * Default values : 
	 * <ul>
	 * <li>nbBestTags = 5</li>
	 * <li>nbQuesionsPerTag = 3</li>
	 * <li>nbSimilarQuestions = 10 </li>
	 * <li>nbExpertsPerTag = 3 </li>
	 * <li>idUser = 1<li>
	 * </ul>
	 */
	public BobParameter(){
		bob = new Bob();
	}
	/**
	 * Constructor.
	 *  Default values : 
	 * <ul>
	 * <li>nbBestTags = 5</li>
	 * <li>nbQuesionsPerTag = 3</li>
	 * <li>nbSimilarQuestions = 10 </li>
	 * <li>nbExpertsPerTag = 3 </li>
	 * <li>idUser = 1<li>
	 * </ul>
	 * @param accesToken
	 */
	public BobParameter(String accesToken){
		bob  = new Bob(accesToken);
	}
	
	/**
	 * Number of question displayed for each tag.
	 * @param nbQuestions
	 * @return the BobParameter object.
	 */
	public BobParameter withNbQuestions(int nbQuestions){
		bob.setNbQuestionsPerTag(nbQuestions);
		return this;
	}
	
	/**
	 * Number of questions to display for a given question title.
	 * @param nbSimilarQuestions
	 * @return the BobParameter object.
	 */
	public BobParameter withNbSimilarQuestions(int nbSimilarQuestions){
		bob.setNbSimilarQuestions(nbSimilarQuestions);
		return this;
	}
	
	/**
	 * Number of the Bob's most popular tags.
	 * @param nbTags
	 * @return the BobParameter object.
	 */
	public BobParameter withNbBestTags(int nbTags){
		bob.setNbTags(nbTags);
		return this;
	}
	
	/**
	 * Id of the user (on stackoverflow site).
	 * @param id
	 * @return the BobParameter object.
	 */
	public BobParameter withId(int id){
		bob.setIdUser(id);
		return this;
	}
	
	/**
	 * Number of experts to follow in each tag.
	 * @param nbExperts
	 * @return the BobParameter object.
	 */
	public BobParameter withNbExperts(int nbExperts){
		bob.setNbExpertsPerTag(nbExperts);
		return this;
	}
	
	/**
	 * @return the Bob object.
	 */
	public Bob get(){
		return bob;
	}
	
}
