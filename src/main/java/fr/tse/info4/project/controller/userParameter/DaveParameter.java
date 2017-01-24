package fr.tse.info4.project.controller.userParameter;

import fr.tse.info4.project.model.user.Dave;

/**
 * 
 * Handles the Dave User Story parameters
 * <br> To be used with controller.UserFactory
 */
public class DaveParameter {
	/**
	 * Dave object.
	 */
	private Dave dave;

	/**
	 * Default constructor. <br>
	 * Default values : 
	 * <ul>
	 * <li>nbUsers = 10</li>
	 * <li>refreshRateTopAnswerers = 24 hours</li>
	 * <li>forceUpdtate = false</li>
	 * </ul>
	 */
	public DaveParameter() {
		dave = new Dave();
	}

	/**
	 * Constructor with accessToken.
	 * <ul>
	 * <li>nbUsers = 10</li>
	 * <li>refreshRateTopAnswerers = 24 hours</li>
	 * <li>forceUpdtate = false</li>
	 * </ul>
	 * @param accessToken
	 */
	public DaveParameter(String accessToken) {
		dave = new Dave(accessToken);
	}

	/**
	 * Number of top answerers searched for in a given tag.
	 * @param nbUsers
	 * @return the updated DaveParameter object.
	 */
	public DaveParameter withNbUsers(int nbUsers) {
		dave.setNbUsers(nbUsers);
		return this;
	}

	/**
	 * Force the database update
	 * @return the updated DaveParameter object.
	 */
	public DaveParameter withForceUpdate() {
		dave.setForceUpdateTopAnswerers(true);
		return this;
	}

	/**
	 * Don't force the database update
	 * @return the updated DaveParameter object.
	 */
	public DaveParameter withoutForceUpdate() {
		dave.setForceUpdateTopAnswerers(false);
		return this;
	}

	/**
	 * Time between two data update (in hours).
	 * @param refreshRate
	 * @return the updated DaveParameter object.
	 */
	public DaveParameter withRefreshRate(int refreshRate) {
		dave.setRefreshRateTopAnswerers(refreshRate);
		return this;
	}

	/**
	 * 
	 * @return the Dave object.
	 */
	public Dave get() {
		return dave;
	}

}
