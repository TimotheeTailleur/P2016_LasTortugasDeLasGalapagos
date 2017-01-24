package fr.tse.info4.project.controller.userParameter;

import fr.tse.info4.project.model.user.Alice;

/**
 * Handles the Alice User Story parameters.
 * <br> To be used with controller.UserFactory
 */
public class AliceParameter {
	
	/** Alice object */
	private Alice alice;

	/**
	 * Default constructor. <br>
	 * Default values :
	 * <ul>
	 * <li>nbTags = 3</li>
	 * <li>nbQuestionsPerTags = 3</li>
	 * <li>nbAnswers = 10</li>	 
	 * <li>idUser = 1</li> 
	 * <li>accessToken = null</li>
	 * </ul>
	 */
	public AliceParameter() {
		alice = new Alice();
	}

	/**
	 * Constructor with accessToken. <br>
	 * Default values :
	 * <ul>
	 * <li>nbTags = 3</li>
	 * <li>nbQuestionsPerTags = 3</li>
	 * <li>nbAnswers = 10</li>
	 * <li>idUser = 1</li>
	 * <li>accessToken = null</li>
	 * </ul>
	 * 
	 * @param accessToken
	 */
	public AliceParameter(String accessToken) {
		alice = new Alice(accessToken);
	}

	/**
	 * Number of the user's best answers to be displayed<br>
	 * 
	 * @param nbAnswers
	 * @return the updated AliceParameter object.
	 */
	public AliceParameter withNbAnwsers(int nbAnswers) {
		alice.setNbAnswers(nbAnswers);
		return this;
	}

	/**
	 * Number of newest displayed questions for each tag.
	 * @param nbQuestions
	 * @return the updated AliceParameter object.
	 */
	public AliceParameter withNbQuestions(int nbQuestions) {
		alice.setNbQuestionsPerTag(nbQuestions);
		return this;
	}

	/**
	 * Number of the user's top tags in which newest questions will be searched
	 * @param nbTags
	 * @return the updated AliceParameter object.
	 */
	public AliceParameter withNbTags(int nbTags) {
		alice.setNbTags(nbTags);
		return this;
	}

	/**
	 * User id.
	 * @param id
	 * @return the updated AliceParameter object.
	 */
	public AliceParameter withId(int id) {
		alice.setIdUser(id);
		return this;
	}

	/**
	 * 
	 * @return the Alice object.
	 */
	public Alice get() {
		return alice;
	}
}
