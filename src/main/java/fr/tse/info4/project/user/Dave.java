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

	// ---- First method : getTopAnswerers & Seconde Method : getTopTag
	// ------------------------

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

	/*
	 * Participation minimal for a tag post count 
	 */
	private int  minParticipation = 100;

	/*
	 * Time between 2 upddtate of data concerning the best tags of an user
	 */
	private int refreshRateTagUser = 24;
	/*
	 * Variable allowing to foce the update of the data concerning the best tags
	 * of an user.
	 */
	private boolean forceUpdateTagUser = false;

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

	public float getMinParticipation() {
		return minParticipation;
	}

	public void setMinParticipation(float minParticipation) {
		this.minParticipation = minParticipation;
	}

	public int getRefreshRateTagUser() {
		return refreshRateTagUser;
	}

	public void setRefreshRateTagUser(int refreshRateTagUser) {
		this.refreshRateTagUser = refreshRateTagUser;
	}

	public boolean getForceUpdateTagUser() {
		return forceUpdateTagUser;
	}

	public void setForceUpdateTagUser(boolean forceUpdateTagUser) {
		this.forceUpdateTagUser = forceUpdateTagUser;
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
	 * @param idUser
	 * @param postCountMax
	 */
	private void updateTagDataForUser(int idUser, int postCountMax) {
		boolean nextPage = true;
		int pageNumber = 1;
		// Tant que l'on n'a pas trouvé tous les tags où l'utilisateur à un
		// score supérieux au score mininum attendu
		//
		while (nextPage) {
			Paging page = new Paging(pageNumber, 100);
			DaveApiManager manager = new DaveApiManager(ApiManager.APP_KEY, ApiManager.SITE);
			System.out.println(idUser + " : " + pageNumber);
			PagedList<Tag> list = manager.getTagsForUsers(page, idUser);

			DaveDatabaseManager.insertListTag(list, idUser);
			// Si le dernider élément de la liste a un post count plus petit que
			// celui
			// attendu, on s'arrête
			if (list.get(list.size() - 1).getCount() < postCountMax * minParticipation) {
				nextPage = false;
			}
			pageNumber++;

		}
	}

	public TagScore getTopUserMultipleTags(ArrayList<String> tagNameList, int topPosition) throws SQLException{
		ArrayList<Long> ids = new ArrayList<Long>();

		int minPostCount = 0;
		int nbUsersTemp = nbUsers;
		nbUsers = 20;
		for (int i = 0; i < tagNameList.size(); i++) {

			ArrayList<TagScore> tagTopAnswerers = (ArrayList<TagScore>) getTopAnswerers(tagNameList.get(i));
			
			if(i ==0){
				minPostCount = tagTopAnswerers.get(0).getPostCount();
			}

			for (int j = 0; j < tagTopAnswerers.size(); j++) {
				long idUser = tagTopAnswerers.get(j).getUser().getUserId();
				long time = DaveDatabaseManager.getTimeUpdateUserTags((int)idUser);
				
				if (time == -1 || time > refreshRateTagUser * 3600 || forceUpdateTagUser){
					ids.add(idUser);
					DaveDatabaseManager.updateDateUser((int)idUser);
				}
				
				int postCount = tagTopAnswerers.get(j).getPostCount();
				if (minPostCount > postCount && postCount >=minParticipation){
					minPostCount = postCount;
				}
			}

		}
		nbUsers = nbUsersTemp;
		System.out.println(ids);
		System.out.println(minPostCount);
		
		DaveApiManager dave = new DaveApiManager(ApiManager.APP_KEY, ApiManager.SITE);
		
		
		// Si au moins 1 utilisateur a besoin d'une mise à jour sur ses données, on l'effectue
		if (ids.size() > 0){
			ArrayList<Tag> tagList = dave.getTagsOnUsers(ids, minPostCount);
		
			DaveDatabaseManager.insertListTag(tagList);
		}
		
		return DaveDatabaseManager.getTopAnswererMultipleTag(tagNameList, topPosition);
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
