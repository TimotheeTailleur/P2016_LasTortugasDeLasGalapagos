package fr.tse.info4.project.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import fr.tse.info4.project.database.DatabaseManager;
import fr.tse.info4.project.database.DaveDatabaseManager;
import fr.tse.info4.project.schema.TagScore;
import fr.tse.info4.project.schema.TopTagUser;

/**
 * 
 * Function and demo main method for user story Dave
 *
 */
public class Dave{

	/*
	 * Number of top answerers in a given tag (Dave 1). 
	 * <br> Default value : 10
	 */
	private int nbUsers = 10;

	/*
	 * Time between 2 updates of top answerers data (in hours).
	 * <br> Default value : 24h.
	 */
	private int refreshRateTopAnswerers = 24;

	/*
	 * Boolean changed or not by the user. If true, database update will be forced. If false, time between two updates will be refreshRateTopAnswerers
	 * <br> Default value : false
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
	 * Returns a list of TagScore objects, sorted by post count(desc) of the top
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
	 * Return Top tag in given tag (aka : user who has highest score in given tag)
	 * Returns a list of TagScore objects.
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
	 * Returns top contributor in given tag list (aka : user who has posted the most in the given set of tags
	 * Returns a TagScore object
	 * 
	 * @param tagsName
	 * @return TagScore
	 * @throws SQLException
	 */
	public TagScore getTopUserMultipleTags(List<String> tagsName){
		int nbUsersTemp = nbUsers;
		nbUsers = 30;

		Map<Integer, TopTagUser> topTagUsers = new HashMap<>();
		for (int i = 0; i < tagsName.size(); i++) {
			String tagName = tagsName.get(i);
			int nbUsersDatabase = DaveDatabaseManager.getNbUsers(tagName);
			boolean forceUpdateTemp = forceUpdateTopAnswerers;
			if (nbUsersDatabase < nbUsers) {
				forceUpdateTopAnswerers = true;
			}
			List<TagScore> topAnswerersPerTag = getTopAnswerers(tagName);
			
			for (TagScore topAnswerer : topAnswerersPerTag){
				int id = (int) topAnswerer.getUser().getUserId();
				if (topTagUsers.containsKey(id)){
					
				}
				else
				{
					
				}
				
			}
			
			forceUpdateTopAnswerers = forceUpdateTemp;
		}

		
		return null;

	}

	// Méthode main pour démo
	public static void main(String[] args) {

		
	}

}
