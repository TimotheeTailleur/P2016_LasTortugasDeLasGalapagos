package fr.tse.info4.project.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.google.code.stackexchange.schema.User;

import fr.tse.info4.project.database.DatabaseManager;
import fr.tse.info4.project.database.DaveDatabaseManager;
import fr.tse.info4.project.schema.TagScore;

/**
 * 
 * Function and demo main method for user story Dave
 *
 */
public class Dave {

	/*
	 * Number of top answerers in a given tag (Dave 1). Default value of 10
	 */
	private int nbUsers = 10;

	/*
	 * Time between 2 update of data concerning the top answerers (in hours).
	 * Default value : 24h.
	 */
	private int refreshRateTopAnswerers = 24;

	/*
	 * Variable allowing to force the update of the data of the first dave story
	 * (topAnswerers), even if the refreshRate is not exceeded. Default value :
	 * false
	 */
	private boolean forceUpdateTopAnswerers = false;

	/* Constructor */
	public Dave() {

	}

	// --------- Getters & Setters -------------

	public int getNbUsers() {
		return nbUsers;
	}

	public void setNbUsers(int nbUsers) {
		if (nbUsers > 0 && nbUsers <= 20) {
			this.nbUsers = nbUsers;
		}
	}

	public int getRefreshRateTopAnswerers() {
		return refreshRateTopAnswerers;
	}

	public void setRefreshRateTopAnswerers(int refreshRateTopAnswerers) {
		if (refreshRateTopAnswerers > 0) {
			this.refreshRateTopAnswerers = refreshRateTopAnswerers;
		}
	}

	public boolean getForceUpdateTopAnswerers() {
		return forceUpdateTopAnswerers;
	}

	public void setForceUpdateTopAnswerers(boolean forceUpdateTopAnswerers) {
		this.forceUpdateTopAnswerers = forceUpdateTopAnswerers;
	}

	/**
	 * Returns Stack Overflow profile URL of user (id)
	 * 
	 * @param id
	 * @return Stack Overflow profile URL
	 * @throws IOException
	 * @throws JSONException
	 */

	public static String getLink(int id) {
		return ("stackoverflow.com/u/" + id);
	}

	// -----------------------User story 1 ---------------------------

	/**
	 * ( Return a list of TagScore, sorted by post count(desc) of the top
	 * answerers in the given tag If the refresh rate (of top answerers) is
	 * exceeded, of if the user wants to force the update (variable
	 * forceUpdateTopAnswerers), data concerning the tags are updated.
	 * 
	 * @param tag
	 * @return
	 *         <ul>
	 *         <li>List of TagScore</li>
	 *         <li>null if the tag doesn't exist</li>
	 *         </ul>
	 */
	public List<TagScore> getTopAnswerers(String tag) {
		int idTag = DatabaseManager.getTagId(tag);
		if (idTag == -1) {
			return null;
		}
		long time = DaveDatabaseManager.getTimeUpdateTopTag(idTag);

		if (time > refreshRateTopAnswerers * 3600 || time == -1 || forceUpdateTopAnswerers) {

			DaveDatabaseManager.updateDateTag(idTag);
			try {
				DaveDatabaseManager.fillDaveTablesTopAnswerers(idTag);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return DaveDatabaseManager.getTopAnswerers(nbUsers, idTag);

	}

	// --------------------------- User Story 2 ------------------------------
	/**
	 * Return the tagScore corresponding to the user who has the best score in a
	 * given tag (passed as a parameter) If the refresh rate (of top answerers)
	 * is exceeded, of if the user wants to force the update (variable
	 * forceUpdateTopAnswerers), data concerning the tags are updated.
	 * 
	 * @param tag
	 * @return
	 *         <ul>
	 *         <li>TagScore</li>
	 *         <li>null if the tag doesn't exist</li>
	 *         </ul>
	 */
	public TagScore getTopTag(String tag) {
		int idTag = DatabaseManager.getTagId(tag);
		if (idTag == -1) {
			return null;
		}
		long time = DaveDatabaseManager.getTimeUpdateTopTag(idTag);

		if (time > refreshRateTopAnswerers * 3600 || time == -1 || forceUpdateTopAnswerers) {
			DaveDatabaseManager.updateDateTag(idTag);
			try {
				DaveDatabaseManager.fillDaveTablesTopAnswerers(idTag);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return DaveDatabaseManager.getTopTag(idTag);
	}

	// ----------------------------- User Story 3 -------------------------

	/**
	 * 
	 * Return a tagScore corresponding to the user who have the best post count
	 * among the tagNameList *
	 * 
	 * @param tagNameList
	 * @return TagScore
	 * @throws SQLException
	 */
	public TagScore getTopUserMultipleTags(List<String> tagNameList) throws SQLException {
		int nbUsersTemp = nbUsers;
		nbUsers = 30;

		List<TagScore> topAnswerers = new ArrayList<TagScore>();
		for (int i = 0; i < tagNameList.size(); i++) {
			String tagName = tagNameList.get(i);
			int nbUsersDatabase = DaveDatabaseManager.getNbUsers(tagName);
			boolean forceUpdateTemp = forceUpdateTopAnswerers;
			if (nbUsersDatabase < nbUsers) {
				forceUpdateTopAnswerers = true;
			}
			List<TagScore> topAnswerersPerTag = getTopAnswerers(tagName);
			for (int j = 0; j < topAnswerersPerTag.size(); j++) {
				topAnswerers.add(topAnswerersPerTag.get(j));
			}
			forceUpdateTopAnswerers = forceUpdateTemp;
		}

		Map<Long, Integer> users = new HashMap<Long, Integer>();
		for (int i = 0; i < topAnswerers.size(); i++) {
			long userId = topAnswerers.get(i).getUser().getUserId();
			int postCount = topAnswerers.get(i).getPostCount();
			if (!users.containsKey(userId)) {
				users.put(userId, postCount);
			} else {
				users.put(userId, users.get(userId) + postCount);
			}
		}

		nbUsers = nbUsersTemp;

		int maxPostCount = 0;
		long id = 0;
		for (Map.Entry<Long, Integer> entry : users.entrySet()) {
			int postCount = entry.getValue();
			Long idUser = entry.getKey();
			if (postCount > maxPostCount) {
				maxPostCount = postCount;
				id = idUser;

			}
		}

		User user = new User();
		user.setUserId(id);
		return new TagScore(maxPostCount, 0, user);

	}

	// Méthode main pour démo
	public static void main(String[] args) {

	}

}
