package tse.info4.project.user.dave;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import org.json.JSONException;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import tse.info4.project.database.DatabaseManager;
import tse.info4.project.database.DaveDatabaseManager;
import tse.info4.project.datarecovery.StackExchangeApiManager;

/**
 * 
 * Fonctions and demo main method for user story Dave
 *
 */
public class Dave {

	/**
	 * Return the top (nbUsers) answerers in given tag while 
	 * updating data if no update has been performed for more than nbDays
	 * 
	 * @param tag
	 * @param nbDays maximum number of days between 2 updates
	 * @param forceUpdate 
	 * @return ResultSet to manipulate data
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws JSONException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	
	
	public static ArrayList<ArrayList<Integer>> getTopAnswerers(String tag, int nbUsers, int nbDays,
			boolean forceUpdate) throws SQLException, InstantiationException, IllegalAccessException,
			ClassNotFoundException, JSONException, IOException, URISyntaxException {

		TreeMap<String,Long> map= getTagIdAndLastUpdateData(tag,nbDays);
		Integer idTag=map.get("idTag").intValue();
		Long nbSeconds=map.get("nbSeconds");
		Long diff=map.get("diff");
		/*
		 * If the data is too old or the user has chosen to force the update :
		 * Insert or update tag score and tag post count data
		 * Update or insert last update date
		 */
		if (diff > nbSeconds || forceUpdate) {
			DaveDatabaseManager.fillTablesTagPostCountScore(idTag); //Update of score & postcount
			String sqlUpdateDate = "UPDATE " + DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_TABLE)
					+ " SET LAST_UPDATE_DAVE = CURRENT_DATE WHERE ID_TAG = ?";
			PreparedStatement updateDatestmt = DatabaseManager.databaseConnection.prepareStatement(sqlUpdateDate); //Change last Update date to current time
			updateDatestmt.setInt(1, idTag);
			updateDatestmt.executeUpdate();
		}

		/*
		 * Once the tables have been updated : Get top Answerers in given tag and return an ArrayList of 
		 */
		String sqlSelectTopAnswerers = "SELECT * FROM "
				+ DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_POST_TABLE)
				+ "WHERE id_tag = ? ORDER BY POST_COUNT desc";

		PreparedStatement stmtTopAnswerers = DatabaseManager.databaseConnection.prepareStatement(sqlSelectTopAnswerers); //Get Top Answerers from database
		stmtTopAnswerers.setInt(1, idTag);
		ResultSet resQuery = stmtTopAnswerers.executeQuery();

		// Value returned by the method
		ArrayList<ArrayList<Integer>> resList = new ArrayList<ArrayList<Integer>>();

		int cpt = 0;
		while (resQuery.next() && cpt < nbUsers) {
			ArrayList<Integer> idAndPostCount = new ArrayList<Integer>();
			idAndPostCount.add(resQuery.getInt("id_user"));
			idAndPostCount.add(resQuery.getInt("post_count"));
			resList.add(idAndPostCount);
			cpt++;
		}

		DatabaseManager.close();
		return resList;

	}

	/**
	 * Get top tag user id in given tag while 
	 * updating data if no update has been performed for more than nbDays
	 * 
	 * @param tag
	 * @param nbDays
	 * @param forceUpdate
	 * @return
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws JSONException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static int getTopTag(String tag, int nbDays, boolean forceUpdate) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, JSONException, IOException, URISyntaxException {
		
		TreeMap<String,Long> map= getTagIdAndLastUpdateData(tag,nbDays);
		Integer idTag=map.get("idTag").intValue();
		Long nbSeconds=map.get("nbSeconds");
		Long diff=map.get("diff");

		// If the data are too old or if the user wants to force update,
		// the post count and the score are inserting (or updating if they
		// already exist)
		// and the date of the last update is updating to the current date.
		if (diff > nbSeconds || forceUpdate) {
			DaveDatabaseManager.fillTablesTagPostCountScore(idTag);
			String sqlUpdateDate = "UPDATE " + DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_TABLE)
					+ " SET LAST_UPDATE_DAVE = CURRENT_DATE WHERE ID_TAG = ?";
			PreparedStatement updateDatestmt = DatabaseManager.databaseConnection.prepareStatement(sqlUpdateDate);
			updateDatestmt.setInt(1, idTag);
			updateDatestmt.executeUpdate();
			System.out.println("données mises à jour");

		}

		String sqlSelectTopAnswerers = "SELECT id_user FROM "+ DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_SCORE_TABLE)+ 
				" WHERE id_tag = ? AND score = (SELECT max(score) FROM "
				+DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_SCORE_TABLE) + 
				" WHERE id_tag = ? )";
		
		PreparedStatement stmtTopAnswerers = DatabaseManager.databaseConnection.prepareStatement(sqlSelectTopAnswerers);
		stmtTopAnswerers.setInt(1, idTag);
		stmtTopAnswerers.setInt(2, idTag);
		
		ResultSet resQuery = stmtTopAnswerers.executeQuery();
		if (resQuery.next())
		{
			return resQuery.getInt("id_user");
		}
		
		return -1;

	}
	

	/**
	 * Gets tag id and last update data from database
	 * @param tag
	 * @param nbDays
	 * @return Map : keys :{"idtag","nbSeconds","diff"} values : {tag id, number of seconds in nbDays, number of seconds since last update}
	 * @throws SQLException
	 */
	public static TreeMap<String,Long> getTagIdAndLastUpdateData(String tag, int nbDays) throws SQLException {
		
		DatabaseManager.setup();
		// Sql query for select id_tag and last_update_dave depending on tag
		// passed as a parameter
		String sqlSelectTag = "SELECT ID_TAG, LAST_UPDATE_DAVE FROM "
				+ DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_TABLE) + " WHERE tag_name = ? ";
		PreparedStatement stmtSelectTag = DatabaseManager.databaseConnection.prepareStatement(sqlSelectTag);
		stmtSelectTag.setString(1, tag);
		ResultSet resSelectTag = stmtSelectTag.executeQuery();

		long idTag = 0L;
		Date lastUpdateDave = null;

		if (resSelectTag.next()) {
			idTag = resSelectTag.getInt("ID_TAG");
			lastUpdateDave = resSelectTag.getDate("LAST_UPDATE_DAVE");
		} else {
			System.out.println("Veuillez entrer un nom de Tag valide");
			return null;
		}

		// Get current time and date and convert it into a sql formatted date
		Date currentDateSql = new Date(new java.util.Date().getTime());

		//Number of seconds elapsed in nbDays days
		long nbSeconds = nbDays * 86_400; 

		/*
		 *  The default value of diff is bigger than nbSeconds 
		 *  to force the insert if there is no data available for the considered tag.
		 */

		long diff = nbSeconds + 1;

		
		if (lastUpdateDave != null) {
			diff = (currentDateSql.getTime() / 1000) - (lastUpdateDave.getTime() / 1000);
		}
		TreeMap<String,Long> resultMap = new TreeMap<String,Long> ();
		resultMap.put("idTag",idTag); resultMap.put("nbSeconds",nbSeconds); resultMap.put("diff",diff);
		return resultMap;
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
	
	
	/**
	 * Sort (desc) and ArrayList of ArrayList<Integer> depending on the indice 1
	 * @param list
	 */
	private static void sort(ArrayList<ArrayList<Integer>> list){
		for (int i =0 ; i<=list.size()-2 ; i++){
			for (int j = (list.size() -1) ; i<j;j--){
				if (list.get(j).get(1) > list.get(j-1).get(1)){
					ArrayList<Integer> temp = list.get(j-1);
					list.set(j-1, list.get(j));
					list.set(j, temp);
				}
			}
		}
	}
	
	/**
	 * Get top tag user id in the list of given tag (sum of post count for each tag)
	 * updating data if no update has been performed for more than nbDays
	 * 
	 * @param tagList
	 * @param nbDays
	 * @param forceUpdate The date update is forced if this parameter is true
	 * @return ArrayList<ArrayList<Integer>> in this form [[idUser1, post_count1], [idUsers2, post_count2], ...]. <br>
	 * 										 The arrayList is sorting in descending order depending on post_count.
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws JSONException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static ArrayList<ArrayList<Integer>> getTopTag(ArrayList<String> tagList, int nbDays, boolean forceUpdate) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, JSONException, IOException, URISyntaxException{
		if (tagList.size() <=1){
			System.out.println("Too small list");
			return null;
		}
		DatabaseManager.setup();		
		ArrayList<Integer> potentialUsers = DaveDatabaseManager.userSharingTags(tagList, nbDays, forceUpdate);
		
		ArrayList<ArrayList<Integer>> topTagUsers = new ArrayList<ArrayList<Integer>>();
		 
		int listLength = potentialUsers.size();
		if (listLength == 0){
			System.out.println("Aucun utilisateur ne correspond aux critères choisis");
			return null;
		}
		else{
			
			
			
			
			for (int j =0 ; j<potentialUsers.size();j++){
				int idUser = potentialUsers.get(j);
				int totalPostCount = 0;
				ArrayList<Integer> user = new ArrayList<Integer>();
				user.add(idUser);			
				
				for (int i =0; i<tagList.size(); i++){
					
					int idTag = DatabaseManager.getTagId(tagList.get(i));
					String sql = "SELECT POST_COUNT FROM " + DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_POST_TABLE) +
							" WHERE ID_USER = ? AND ID_TAG = ?";
					PreparedStatement stmt = DatabaseManager.databaseConnection.prepareStatement(sql);
					stmt.setInt(1, idUser);
					stmt.setInt(2, idTag);
					ResultSet res = stmt.executeQuery();
					
					if (res.next()){
						totalPostCount += res.getInt("POST_COUNT");
					}
				}
				
				user.add(totalPostCount);
				topTagUsers.add(user);			
			}
			
			
			DatabaseManager.close();	
		}
		
		sort(topTagUsers);
		return topTagUsers;
		
	}

	

	// Méthode main pour démo
	public static void main(String[] args) throws SQLException, IOException, JSONException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, URISyntaxException {
		

		ArrayList<String> tagList  = new ArrayList<>();
		tagList.add("sql");
		tagList.add("mysql");
		tagList.add("sql-server");
		tagList.add("oracle");
		ArrayList<ArrayList<Integer>> list = getTopTag(tagList,2,false); 
		for (int i = 0 ; i<list.size() ; i++)
		{
			System.out.print("Users " + list.get(i).get(0) + " : Total : " + list.get(i).get(1) + " ; ");
			for (int j = 0; j<tagList.size(); j++){
				DatabaseManager.setup();
				int tagId = DatabaseManager.getTagId(tagList.get(j));
				Statement stmt = DatabaseManager.databaseConnection.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT POST_COUNT FROM " + DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_POST_TABLE) + " WHERE ID_TAG = " + tagId + " AND ID_USER = " + list.get(i).get(0));
				int postInTag = 0;
				if (rs.next()){
					postInTag = rs.getInt("POST_COUNT");
				}
				float percentage = ((float)postInTag / (float)list.get(i).get(1)) * 100;
				System.out.print(tagList.get(j) + " : " + percentage +  " % ; ");
				
			}
			System.out.println("");
			
		}
	}

}
