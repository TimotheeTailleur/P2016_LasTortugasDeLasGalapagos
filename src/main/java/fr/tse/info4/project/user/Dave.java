package fr.tse.info4.project.user;

import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

import org.json.JSONException;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Paging;
import com.google.code.stackexchange.schema.StackExchangeSite;
import com.google.code.stackexchange.schema.Tag;

import fr.tse.info4.project.database.DatabaseManager;
import fr.tse.info4.project.database.DaveDatabaseManager;
import fr.tse.info4.project.datarecovery.ApiManager;
import fr.tse.info4.project.datarecovery.DaveApiManager;
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

	// ----------------- Third method : --------------------------------------


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

	/**(
	 * Return a list of TagScore, sorted by post count(desc) of the top
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
	 * Return true if the array tagsScore contains the user identified by idUser. <br>
	 * False otherwise
	 * 
	 * @param tagsScores
	 * @param idUser
	 * @return
	 */
	public static boolean contains(ArrayList<TagScore> tagsScores, int idUser){
		for (int i =0 ; i<tagsScores.size(); i++){
			if ( (int) tagsScores.get(i).getUser().getUserId() == idUser){
				return true;
			}
		}
		return false;
	}

	
	public TagScore getTopUserMultipleTags(ArrayList<String> tagNameList){
		int nbUsersTemp = nbUsers;
		nbUsers = 20;
		
		int totalPostCount = 0;
		ArrayList<TagScore> potentialUser =  (ArrayList<TagScore>) getTopAnswerers(tagNameList.get(0));
		for (int i = 1 ; i<tagNameList.size(); i++){
			ArrayList<TagScore> tagTopAnswerers= (ArrayList<TagScore>) getTopAnswerers(tagNameList.get(i));
			for (int j = 0; j<tagTopAnswerers.size(); j++){
				if (contains(potentialUser, (int) tagTopAnswerers.get(i).getUser().getUserId())){
					totalPostCount += tagTopAnswerers.get(i).getPostCount();
				}
			}
		}
		
		System.out.println(totalPostCount);
		
		nbUsers = nbUsersTemp;
	}
	
	

	// Méthode main pour démo
	public static void main(String[] args) throws SQLException {
		DatabaseManager.setup();
		DatabaseManager.truncateTable(DatabaseManager.TITLE_TAG_POST_TABLE);

		Dave user = new Dave();
		user.setForceUpdateTopAnswerers(true);
		ArrayList<String> tagNameList = new ArrayList<String>();
		tagNameList.add("ios");
		tagNameList.add("android+");
		TagScore tag = user.getTopUserMultipleTags(tagNameList, 1);
		System.out.println(tag.getUser().getUserId());
		System.out.println(tag.getPostCount());
	}

}
