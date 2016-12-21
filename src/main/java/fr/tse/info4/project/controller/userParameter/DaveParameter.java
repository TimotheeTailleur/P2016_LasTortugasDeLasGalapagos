package fr.tse.info4.project.controller.userParameter;

import fr.tse.info4.project.model.user.Dave;

/**
 * 
 * Handles the Dave parameters
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
	 * Constructor.
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
	 * Number of top answerer users in a given tag.
	 * @param nbUsers
	 * @return the DaveParameter object.
	 */
	public DaveParameter withNbUsers(int nbUsers) {
		dave.setNbUsers(nbUsers);
		return this;
	}

	/**
	 * Force the database update
	 * @return the DaveParameter object.
	 */
	public DaveParameter withForceUpdate() {
		dave.setForceUpdateTopAnswerers(true);
		return this;
	}

	/**
	 * Don't force the database update
	 * @return the DaveParameter object.
	 */
	public DaveParameter withoutForceUpdate() {
		dave.setForceUpdateTopAnswerers(false);
		return this;
	}

	/**
	 * Time between two data update (in hours).
	 * @param refreshRate
	 * @return the DaveParameter object.
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
