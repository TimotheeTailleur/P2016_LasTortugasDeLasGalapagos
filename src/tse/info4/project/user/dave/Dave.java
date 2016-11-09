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

		//If the tables were updated, get time difference between now and last update time (in seconds)
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
	
	
	public static int getTopTag(ArrayList<String> tagList) throws SQLException{
		if (tagList.size() <=1){
			System.out.println("Too small list");
			return -1;
		}
		
		DatabaseManager.setup();
		int tagId = DatabaseManager.getTagId(DatabaseManager.addSimpleQuotes(tagList.get(0)));
		String sql = "SELECT ID_USER FROM " + DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_POST_TABLE)
				+ " WHERE ID_TAG = ?";
		PreparedStatement stmt = DatabaseManager.databaseConnection.prepareStatement(sql);
		stmt.setInt(1, tagId);
		ResultSet res = stmt.executeQuery();
		
		ArrayList<Integer> potentialUsers = new ArrayList<Integer>();
		
		while (res.next()){
			potentialUsers.add(res.getInt("ID_USER"));
		}
		
		for (int i =1 ; i<tagList.size(); i++){
			tagId = DatabaseManager.getTagId(DatabaseManager.addSimpleQuotes(tagList.get(i)));
			sql = "SELECT ID_USER FROM " + DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_POST_TABLE)
			+ " WHERE ID_TAG = ?";
			stmt = DatabaseManager.databaseConnection.prepareStatement(sql);
			stmt.setInt(1, tagId);
			res = stmt.executeQuery();
			
			ArrayList<Integer> potentialUsersTemp = new ArrayList<Integer>();
			while (res.next()){
				int idUser = res.getInt("ID_USER");
				
				if (potentialUsers.contains(idUser)){
					potentialUsersTemp.add(idUser);
				}
			}
			potentialUsers = potentialUsersTemp;
		}
		
		System.out.println(potentialUsers.toString());
		
		
		
		
		
		return 0;
		
	}

	

	// Méthode main pour démo
	public static void main(String[] args) throws SQLException, IOException, JSONException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, URISyntaxException {
		
		/*
		System.out.println("Top post count");
		System.out.println("[id_user, post_count]");
		String tag = DatabaseManager.addSimpleQuotes("c++");
		System.out.println(getTopAnswerers(tag, 10, 2, true).toString());
		System.out.println("\nTop score");
		System.out.println(getTopTag(tag, 2, false));*/
		
		ArrayList<String> tagList  = new ArrayList<>();
		tagList.add("c++");
		tagList.add("java");
		System.out.println(getTopTag(tagList));
		
	}

}
