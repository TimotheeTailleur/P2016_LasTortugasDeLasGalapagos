package fr.tse.info4.project.controller.userParameter;

import fr.tse.info4.project.model.user.Bob;

/**
 * Handles the Bob User Story parameters.
 * <br> To be used with controller.UserFactory
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
	 * Constructor with accessToken.
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
	 * Number of questions displayed for each tag.
	 * @param nbQuestions
	 * @return the updated BobParameter object.
	 */
	public BobParameter withNbQuestions(int nbQuestions){
		bob.setNbQuestionsPerTag(nbQuestions);
		return this;
	}
	
	/**
	 * Number of similar questions to display for a given question title.
	 * @param nbSimilarQuestions
	 * @return the updated BobParameter object.
	 */
	public BobParameter withNbSimilarQuestions(int nbSimilarQuestions){
		bob.setNbSimilarQuestions(nbSimilarQuestions);
		return this;
	}
	
	/**
	 * Number of Bob's top tags in which the experts will be searched.
	 * @param nbTags
	 * @return the updated BobParameter object.
	 */
	public BobParameter withNbBestTags(int nbTags){
		bob.setNbTags(nbTags);
		return this;
	}
	
	/**
	 * User ID
	 * @param id
	 * @return the updated BobParameter object.
	 */
	public BobParameter withId(int id){
		bob.setIdUser(id);
		return this;
	}
	
	/**
	 * Number of experts to follow in each tag.
	 * @param nbExperts
	 * @return the updated BobParameter object.
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
