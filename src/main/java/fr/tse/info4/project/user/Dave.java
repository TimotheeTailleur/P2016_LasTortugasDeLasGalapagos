package fr.tse.info4.project.user;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.json.JSONException;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Paging;
import com.google.code.stackexchange.schema.Tag;
import com.google.code.stackexchange.schema.User;

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
public class Dave{
	
	// ---- First method : getTopAnswerers  & Seconde Method : getTopTag ------------------------
	
	/*  Number of top answerers in a given tag (Dave 1).
	 *  Default value of 10
	 *  */
	private int nbUsers = 10;
	
	/*
	 * Time between 2 update of data concerning the top answerers (in hours).
	 * Default value : 24h.
	 */
	private int refreshRateTopAnswerers = 24;
	
	/*
	 * 	Variable allowing to force the update of the data of the first dave story (topAnswerers), 
	 *  even if the refreshRate is not exceeded.
	 *  Default value : false
	 */
	private boolean forceUpdateTopAnswerers = false;
	
	
	// ----------------- Third method : --------------------------------------
	
	/*
	 * Participation minimal (percetage) for a tag post count compared to the highest post count
	 * among all tags of the user.
	 * For example, a max post count of 500 for a user and a minParticipation of 0.1 signified
	 * that all tag where the post count is > 0.1*500 = 50 are taken.
	 */
	private float minParticipation =  0.1f;
	
	/*
	 * Time between 2 upddtate of data concerning the best tags of an user
	 */
	private int refreshRateTagUser = 24;
	/*
	 * Variable allowing to foce the update of the data concerning the best tags of an user.
	 */
	private boolean forceUpdateTagUser = false;
	
	
	/* Constructor */
	public Dave(){
		
	}
		
	
	// --------- Getters & Setters -------------
	
	public int getNbUsers() {
		return nbUsers;
	}

	public void setNbUsers(int nbUsers) {
		if (nbUsers >0 && nbUsers <=20){
			this.nbUsers = nbUsers;
		}		
	}

	public int getRefreshRateTopAnswerers() {
		return refreshRateTopAnswerers;
	}


	public void setRefreshRateTopAnswerers(int refreshRateTopAnswerers) {
		if (refreshRateTopAnswerers >0){
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
	
	/**
	 * Return a list of TagScore, sorted by post count(desc) of the top answerers in the given tag
	 * If the refresh rate (of top answerers) is exceeded, of if the user wants to force the update
	 * (variable forceUpdateTopAnswerers), data concerning the tags are updated.
	 * @param tag 
	 * @return <ul>
	 * 			<li> List of TagScore </li>
	 * 		    <li> null if the tag doesn't exist </li>
	 * 		  </ul>
	 */
	public List<TagScore> getTopAnswerers(String tag){
		int idTag = DatabaseManager.getTagId(tag);
		if (idTag == -1){
			return null;
		}
		 long time = DaveDatabaseManager.getTimeUpdateTopTag(idTag);
		 
		 if (time > refreshRateTopAnswerers*3600 || time == -1 || forceUpdateTopAnswerers){
			 
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
	 * Return the tagScore corresponding to the user who has the best score in a given tag (passed as a parameter)
	 * If the refresh rate (of top answerers) is exceeded, of if the user wants to force the update
	 * (variable forceUpdateTopAnswerers), data concerning the tags are updated.
	 * 
	 * @param tag
	 * @return <ul>
	 * 			<li> TagScore </li>
	 * 		    <li> null if the tag doesn't exist </li>
	 * 		  </ul>
	 */
	public TagScore getTopTag(String tag){
		int idTag = DatabaseManager.getTagId(tag);
		if(idTag == -1){
			return null;
		}
		long time = DaveDatabaseManager.getTimeUpdateTopTag(idTag);
		
		if (time > refreshRateTopAnswerers*3600 || time ==-1 ||forceUpdateTopAnswerers){
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
	private void updateTagDataForUser(int idUser, int postCountMax){
		boolean nextPage = true;
		int pageNumber = 1;
		// Tant que l'on n'a pas trouv� tous les tags o� l'utilisateur � un score sup�rieux au score mininum attendu
		//
		while(nextPage){
			Paging page = new Paging(pageNumber, 100);
			DaveApiManager manager = new DaveApiManager(ApiManager.APP_KEY, ApiManager.SITE);
			System.out.println(idUser + " : " + pageNumber);
			PagedList<Tag> list = manager.getTagsForUsers(page, idUser);
			
			DaveDatabaseManager.insertListTag(list, idUser);			
			// Si le dernider �l�ment de la liste a un post count plus petit que celui 
			// attendu, on s'arr�te
			if (list.get(list.size()-1).getCount() < postCountMax * minParticipation){
				nextPage = false;
			}
			pageNumber++;
			
		}
	}
	
	public TagScore getTopUserMultipleTags(ArrayList<String> tagNameList, int topPosition) throws SQLException{
		// Pour chaque tag recherch�
		for (int i =0 ; i< tagNameList.size() ; i++){
			int idTag = DaveDatabaseManager.getTagId(tagNameList.get(i));
			
			// R�cup�re les utilisateurs qui ont le plus particip� dans un tag.
			// Met � jour si besoin.
			ArrayList<TagScore> tagTopAnswerers= (ArrayList<TagScore>) getTopAnswerers(tagNameList.get(i));
			
			// Pour chacun de ces utilisateur
			for (int j =0 ; j <tagTopAnswerers.size(); j++){
				int idUser = (int) tagTopAnswerers.get(j).getUser().getUserId();
				// Temps �coul� depuis la derni�re mise � jour des deonn�s concernant l'utilisateur
				long time= DaveDatabaseManager.getTimeUpdateUserTags(idUser);
				
				// Si aucune donn�e n'est renseign� (-1), ou si la fr�quence de rafraichissement des donn�es est d�pass�e, 
				// ou si l'utilisateur souhaite force la mise � jour : on met � jour.
				if (time == -1 || time > refreshRateTagUser *3600 || forceUpdateTagUser){
					
					// On met � jour les donn�es concernant les meilleurs tag de l'utilisateur
					updateTagDataForUser(idUser, tagTopAnswerers.get(j).getPostCount());
					// On actualise la date de la derni�re mise � jour des meilleurs tag de l'utilisateur
					DaveDatabaseManager.updateDateUser(idUser);
				}
				
				
			}
		}
		return DaveDatabaseManager.getTopAnswererMultipleTag(tagNameList, topPosition);
	}


			
	

	
	// M�thode main pour d�mo
	public static void main(String[] args) throws SQLException  {
		

		Dave user = new Dave();
		user.getTopAnswerers("java");
	}
		
}
