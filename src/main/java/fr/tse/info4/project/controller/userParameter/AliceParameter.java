package fr.tse.info4.project.controller.userParameter;

import fr.tse.info4.project.model.user.Alice;

/**
 * Handles the Alice parameters.
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
	 * Constructor. <br>
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
	 * Number of the best user's answers. <br>
	 * 
	 * @param nbAnswers
	 * @return the AliceParameter object.
	 */
	public AliceParameter withNbAnwsers(int nbAnswers) {
		alice.setNbAnswers(nbAnswers);
		return this;
	}

	/**
	 * Number of the newest displayed questions for each tag.
	 * @param nbQuestions
	 * @return the AliceParameter object.
	 */
	public AliceParameter withNbQuestions(int nbQuestions) {
		alice.setNbQuestionsPerTag(nbQuestions);
		return this;
	}

	/**
	 * Number of the best user's tags.
	 * @param nbTags
	 * @return the AliceParameter object.
	 */
	public AliceParameter withNbTags(int nbTags) {
		alice.setNbTags(nbTags);
		return this;
	}

	/**
	 * User id.
	 * @param id
	 * @return the AliceParameter object.
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
