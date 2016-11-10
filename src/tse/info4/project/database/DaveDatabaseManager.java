package tse.info4.project.database;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.json.JSONException;

import tse.info4.project.datarecovery.StackExchangeApiManager;

/**
 * This class will manage the database tables used in functions implemented for the Dave User Story
 *
 */
public class DaveDatabaseManager extends DatabaseManager {

	/**
	 * Updates tables tag_post_count and tag_score. If a user's id isn't found in
	 * the database, creates new entries in both tables
	 * @param idTag Tag id
	 * 
	 * @throws JSONException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws URISyntaxException
	 */
	public static void fillTablesTagPostCountScore(int idTag) throws JSONException, IOException, SQLException,
			InstantiationException, IllegalAccessException, ClassNotFoundException, URISyntaxException {

		//Get tag name with tag id
		String tagName= addSimpleQuotes(getTagName(idTag));
		
		//Get Top Answerers stats in given tag
		TreeMap<Integer, ArrayList<Integer>> resAns = StackExchangeApiManager.getTopAnswerers(tagName);
		
		//Prepare Statements for updates and insertions
		PreparedStatement stmtUpdatePost = databaseConnection
				.prepareStatement("UPDATE " +  addDoubleQuotes(TITLE_TAG_POST_TABLE)  + " SET post_count = ? WHERE id_user = ? AND id_tag = ?");
		PreparedStatement stmtUpdateScore = databaseConnection
				.prepareStatement("UPDATE " + addDoubleQuotes(TITLE_TAG_SCORE_TABLE)  + " SET score = ? WHERE id_user = ? AND id_tag = ?");
		
		PreparedStatement stmtInsertPost = databaseConnection.prepareStatement(
				"INSERT INTO " + addDoubleQuotes(TITLE_TAG_POST_TABLE) + " (id_User,id_tag,post_count) VALUES (?,?,?)");
		
		PreparedStatement stmtInsertScore = databaseConnection.prepareStatement(
				"INSERT INTO " +  addDoubleQuotes(TITLE_TAG_SCORE_TABLE) + " (id_User,id_tag,score) VALUES (?,?,?)");

		Integer userID, score,post_count;

		/*
		 * For each entry in the top Answerers ResultSet, get userID, user score and user Post_Count in given tag
		 * and set preparedStatements' variables accordingly
		 */
		for (Entry<Integer, ArrayList<Integer>> userEntry : resAns.entrySet()) {

			userID=userEntry.getKey(); post_count=userEntry.getValue().get(1); score=userEntry.getValue().get(0);
			
			stmtUpdatePost.setInt(1, post_count); 
			stmtUpdatePost.setInt(2, userID); 
			stmtUpdatePost.setInt(3, idTag);
			stmtUpdateScore.setInt(1, score);
			stmtUpdateScore.setInt(2, userID);
			stmtUpdateScore.setInt(3, idTag);

			// If a user's id isn't found in either table : execute Insertion prepared Statements 
			if (stmtUpdatePost.executeUpdate() == 0) {

				stmtInsertPost.setInt(1, userID);
				stmtInsertPost.setInt(2, idTag);
				stmtInsertPost.setInt(3, post_count);
				stmtInsertPost.executeUpdate();
			}
			
			if (stmtUpdateScore.executeUpdate() == 0) {

				stmtInsertScore.setInt(1, userID);
				stmtInsertScore.setInt(2, idTag);
				stmtInsertScore.setInt(3, score);
				stmtInsertScore.executeUpdate();
			}
		}
	}
	
	/**
	 * Search in the database the users who shares the tags passed as a parameter.
	 * 
	 * @param tagList ArrayList of tags
	 * @nbDays
	 * @forceUpdate
	 * @return An arrayList which contains the ids of the users who shares the tags in the tag list.
	 * @throws SQLException
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static ArrayList<Integer> userSharingTags(ArrayList<String> tagList, int nbDays, boolean forceUpdate) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, JSONException, IOException, URISyntaxException{
		int tagId;
		String sql;
		PreparedStatement stmt;
		ResultSet res;
		ArrayList<Integer> userList = new ArrayList<Integer>();
		long time;
		String tag;
		
				
		for (int i =0 ; i<tagList.size(); i++){
			tag = tagList.get(i);
			tagId = getTagId(tag);
			time = getTimeSinceLastUpdate(tag, nbDays);
			if (time > nbDays*86_400 || forceUpdate) {
				DaveDatabaseManager.fillTablesTagPostCountScore(tagId);
				String sqlUpdateDate = "UPDATE " + DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_TABLE)
						+ " SET LAST_UPDATE_DAVE = CURRENT_DATE WHERE ID_TAG = ?";
				PreparedStatement updateDatestmt = DatabaseManager.databaseConnection.prepareStatement(sqlUpdateDate);
				updateDatestmt.setInt(1, tagId);
				updateDatestmt.executeUpdate();
				System.out.println("données mises à jour");

			}
			
			
			sql = "SELECT ID_USER FROM " + addDoubleQuotes(TITLE_TAG_POST_TABLE)
			+ " WHERE ID_TAG = ?";
			stmt = databaseConnection.prepareStatement(sql);
			stmt.setInt(1, tagId);
			res = stmt.executeQuery();
			
			if (i == 0){
				while (res.next()){
					userList.add(res.getInt("ID_USER"));
				}
			}
			else{
				
				ArrayList<Integer> potentialUsersTemp = new ArrayList<Integer>();
				while (res.next()){
				int idUser = res.getInt("ID_USER");
				
				if (userList.contains(idUser)){
					potentialUsersTemp.add(idUser);
				}
			}
			userList = potentialUsersTemp;
			}
			
		}
		
		return userList;
	}
	
	
	/**
	 * 	Returns number of seconds since last update (in Dave tables) of the tag passed as a parameter
	 *  If last_update_dave is not given (null), returns a number bigger than nbDays in seconds.
	 * @param tag
	 * @return number of seconds
	 * @throws SQLException 
	 */
	public static long getTimeSinceLastUpdate(String tag, int nbDays) throws SQLException{
		
		String sql = "SELECT LAST_UPDATE_DAVE FROM " + addDoubleQuotes(TITLE_TAG_TABLE) + " WHERE tag_name = ?";
		PreparedStatement stmt = databaseConnection.prepareStatement(sql);
		stmt.setString(1, addSimpleQuotes(tag));
		ResultSet res = stmt.executeQuery();
		
		Date lastUpdateDave = null;
		
		if (res.next()){
			lastUpdateDave = res.getDate("LAST_UPDATE_DAVE");
		}
		else{
			System.out.println("getTimeSinceLastUpdate - Tag invalide");
			return 0;
		}
		
		Date currentDateSql = new Date(new java.util.Date().getTime());
		
		long nbSeconds = nbDays * 86_400;
		
		/*
		 *  The default value of diff is bigger than nbSeconds 
		 *  to force the insert if there is no data available for the considered tag.
		 */
		long diff = nbSeconds +1;
		
		//If the tables were updated, get time difference between now and last update time (in seconds)
		if (lastUpdateDave != null) {
			diff = (currentDateSql.getTime() / 1000) - (lastUpdateDave.getTime() / 1000);
		}		
		
		return diff;
	}
		
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, JSONException, IOException, SQLException, URISyntaxException {
		System.out.println(getTimeSinceLastUpdate("c++", 2));;
	}

}


