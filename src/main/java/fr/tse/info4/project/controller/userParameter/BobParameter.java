package fr.tse.info4.project.controller.userParameter;

import fr.tse.info4.project.model.user.Bob;

public class BobParameter {

	private Bob bob;
	
	/**
	 * Default Constructor.
	 */
	public BobParameter(){
		bob = new Bob();
	}
	/**
	 * Constructor.
	 * @param accesToken
	 */
	public BobParameter(String accesToken){
		bob  = new Bob(accesToken);
	}
	
	/**
	 * Number of question displayed for each tag.
	 * @param nbQuestions
	 * @return the bob parameter
	 */
	public BobParameter withNbQuestions(int nbQuestions){
		bob.setNbQuestionsPerTag(nbQuestions);
		return this;
	}
	
	/**
	 * Number of questions to display for a given question title.
	 * @param nbSimilarQuestions
	 * @return the bob parameter
	 */
	public BobParameter withNbSimilarQuestions(int nbSimilarQuestions){
		bob.setNbSimilarQuestions(nbSimilarQuestions);
		return this;
	}
	
	/**
	 * Number of the Bob's most popular tags.
	 * @param nbTags
	 * @return the bob parameter
	 */
	public BobParameter withNbBestTags(int nbTags){
		bob.setNbTags(nbTags);
		return this;
	}
	
	/**
	 * Id of the user (on stackoverflow site).
	 * @param id
	 * @return the bob parameter.
	 */
	public BobParameter withId(int id){
		bob.setIdUser(id);
		return this;
	}
	
	/**
	 * Number of experts to follow in each tag.
	 * @param nbExperts
	 * @return the bob parameter
	 */
	public BobParameter withNbExperts(int nbExperts){
		bob.setNbExpertsPerTag(nbExperts);
		return this;
	}
	
	/**
	 * 
	 * @return the bob object.
	 */
	public Bob get(){
		return bob;
	}
	
}
