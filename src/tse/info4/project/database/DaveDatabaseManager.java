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
	 * Updates tables tag_post_count and tag_score with the 20 top answerers in the tag specified. If a user's id isn't found in
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
	public static void fillDaveTablesTopAnswerers(int idTag) throws JSONException, IOException, SQLException,
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
		stmtUpdatePost.close();
		stmtInsertPost.close();
		stmtUpdateScore.close();
		stmtInsertScore.close();
	}
	
	
	/**
	 * Insert or update the post count of a user in a given tag.
	 * @param idUser
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static void fillTablePostCount(int idUser, int minScore) throws JSONException, IOException{
		setup();
		
		// topTag = [{count = , name = }, {count = , name =} ...]
		ArrayList<TreeMap<String, String>> topTag = StackExchangeApiManager.getTagUserScore(idUser, minScore);
		String sqlInsert = "INSERT INTO " + addDoubleQuotes(TITLE_TAG_POST_TABLE) + "(ID_USER, ID_TAG, POST_COUNT) VALUES (?, ?, ?)";
		String sqlUpdate = "UPDATE " + addDoubleQuotes(TITLE_TAG_POST_TABLE) + " SET POST_COUNT = ? WHERE ID_USER = ? AND ID_TAG = ?";
		
		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		try {
			stmtInsert = databaseConnection.prepareStatement(sqlInsert);
			stmtUpdate = databaseConnection.prepareStatement(sqlUpdate);
		} catch (SQLException e) {
			System.out.println("fillTablePostCount (DaveDatabaseManager) - Problème de requête sql");
			e.printStackTrace();
		}
				
		for (int i = 0; i<topTag.size(); i++){
			int idTag = getTagId(topTag.get(i).get("name"));
			int postCount = Integer.parseInt(topTag.get(i).get("count"));
			
			try {
				stmtUpdate.setInt(1, postCount);
				stmtUpdate.setInt(2, idUser);
				stmtUpdate.setInt(3, idTag);
				
				// If no user was found, insert the new user and his data.
				if (stmtUpdate.executeUpdate() == 0){
					
					
					stmtInsert.setInt(1, idUser);
					stmtInsert.setInt(2, idTag);
					stmtInsert.setInt(3, postCount);
					stmtInsert.executeUpdate();
					
					
					
				}
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
			
			

		}
		
		try {
				stmtInsert.close();
				stmtUpdate.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	/**
	 * 	Returns number of seconds since last update (in Dave tables) of the tag passed as a parameter
	 *  If last_update_dave is not given (null), returns a number bigger than nbDays in seconds.
	 * @param tag
	 * @return number of seconds
	 * @throws SQLException 
	 */
	public static long getTimeUpdateTopTag(int idTag){
		setup();
		String sql = "SELECT LAST_UPDATE_DAVE FROM " + addDoubleQuotes(TITLE_TAG_TABLE) + " WHERE ID_TAG = ?";
		
		Date lastUpdate = null;
		PreparedStatement stmt;
		try {
			
			stmt = databaseConnection.prepareStatement(sql);
			stmt.setInt(1, idTag);
			ResultSet res = stmt.executeQuery();
			
			if (res.next()){
				lastUpdate = res.getDate("LAST_UPDATE_DAVE");
			}
			
			stmt.close();
			res.close();
			
		} catch (SQLException e) {
			System.out.println("getTimeUpdateTopTag (DaveDatabaseManager) - Erreur de la requête sql.");
			e.printStackTrace();
		}
		
		Date currentDate= new Date(new java.util.Date().getTime());
		
		if (lastUpdate != null){
			return (currentDate.getTime() / 1000) - (lastUpdate.getTime() / 1000);
		}	
		
		return -1;	
	
	}
	
	public static long getTimeUpdateUserTag(int idUser){
		setup();
		String sql = "SELECT LAST_UPDATE_TAG FROM " + addDoubleQuotes(TITLE_USERS_TABLE) + " WHERE ID_USER = ?";
		
		Date lastUpdate = null;
		PreparedStatement stmt;
		try {
			
			stmt = databaseConnection.prepareStatement(sql);
			stmt.setInt(1, idUser);
			ResultSet res = stmt.executeQuery();
			
			
			if (res.next()){
				lastUpdate = res.getDate("LAST_UPDATE_TAG");
			}
			stmt.close();
			res.close();
			
		} catch (SQLException e) {
			System.out.println("getTimeUpdateTopTag (DaveDatabaseManager) - Erreur de la requête sql.");
			e.printStackTrace();
		}
		
		Date currentDate= new Date(new java.util.Date().getTime());
		
		if (lastUpdate != null){
			return (currentDate.getTime() / 1000) - (lastUpdate.getTime() / 1000);
		}	
		
		return -1;	
		
	}
	
	
	/**
	 * 
	 * Return the top answerers in a tag
	 * 
	 * @param nbUsers : number of users
	 * @param tag : tag id
	 * @return [{id : , postCount :}, ...]
	 */
	public static ArrayList<TreeMap<String, Integer>> getTopAnswerers(int nbUsers, int tag){
		setup();
		String sql = "SELECT ID_USER, POST_COUNT FROM " + addDoubleQuotes(TITLE_TAG_POST_TABLE) + " WHERE ID_TAG = ? ORDER BY POST_COUNT DESC";
		
		ArrayList<TreeMap<String, Integer>> userList = new ArrayList<TreeMap<String, Integer>>();
		try{
			PreparedStatement stmt = databaseConnection.prepareStatement(sql);
			stmt.setInt(1, tag);
			ResultSet res = stmt.executeQuery();
			
			
			int cpt =0;
			while (res.next() && cpt <nbUsers){
				
				int idUser = res.getInt("ID_USER");
				int postCount = res.getInt("POST_COUNT");
				TreeMap<String, Integer> userData = new TreeMap<String, Integer>();
				userData.put("id", idUser);
				userData.put("postCount", postCount);
				userList.add(userData);
				cpt++;
			}
			stmt.close();
			res.close();
		} catch (SQLException e) {
		System.out.println("getTimeUpdateTopTag (DaveDatabaseManager) - Erreur de la requête sql.");
		e.printStackTrace();
	}
		
		return userList;
	}
		
	
	/**
	 * Update the date of the last update concerning post count and score tables (Dave) to the current date in the tag table
	 * 
	 * @param idTag id of the tag referenced in the tag Table
	 */
	public static void updateDateTag(int idTag){
		setup();
		String sql = "UPDATE " + addDoubleQuotes(TITLE_TAG_TABLE) + " SET LAST_UPDATE_DAVE = CURRENT_DATE WHERE ID_TAG = ?";
		try {
			PreparedStatement stmt = databaseConnection.prepareStatement(sql);
			stmt.setInt(1,idTag);
			stmt.executeUpdate();
			
			stmt.close();
			
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
	}
	
	/**
	 * Update the date of the last update concerning the post count of a user to the current date in the user table
	 * Insert the user with the current date if the user doesn't exist
	 * @param idUser
	 */
	public static void updateDateUser(int idUser){
		String sqlUpdate = "UPDATE " + addDoubleQuotes(TITLE_USERS_TABLE) + " SET LAST_UPDATE_TAG = CURRENT_DATE WHERE ID_USER = ?";
		String sqlInsert = "INSERT INTO " + addDoubleQuotes(TITLE_USERS_TABLE) + "(ID_USER, LAST_UPDATE_TAG) VALUES (?, CURRENT_DATE)";
		
		try {
			PreparedStatement stmtUpdate = databaseConnection.prepareStatement(sqlUpdate);
			stmtUpdate.setInt(1, idUser);
			
			// If the user doesn't exist : insertion of this user at the current date
			if (stmtUpdate.executeUpdate() == 0){
				PreparedStatement stmtInsert = databaseConnection.prepareStatement(sqlInsert);
				stmtInsert.setInt(1, idUser);
				
				stmtInsert.executeUpdate();
				stmtInsert.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Return a list of users' id who sharing the tags in the tag list passed as a parameter
	 * 
	 * @param tagList : tag are identified by their id in the tag table
	 * @return ArrayList<Integer>
	 */
	public static ArrayList<Integer> usersSharingTags(ArrayList<Integer> tagList){
		setup();
		ArrayList<Integer> userList = new ArrayList<Integer>();
		
		for (int i = 0 ; i< tagList.size(); i++){
			
			int idTag = tagList.get(i);
			
			String sql = "SELECT ID_USER FROM " + addDoubleQuotes(TITLE_TAG_POST_TABLE)	+ " WHERE ID_TAG = ?";
			

			try {
				PreparedStatement stmt = databaseConnection.prepareStatement(sql);
				stmt.setInt(1, idTag);
				ResultSet res = stmt.executeQuery();
				
				
				if(i== 0){
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
				
				stmt.close();
				res.close();
			} catch (SQLException e) {				
				e.printStackTrace();
			}
			
			
		}
		
		return userList;
			
		
	}
	
	
	/**
	 * Sort (desc) and ArrayList of TreeMap. Sort the arraylist depending the value identified by totalPostCount in the Treemap
	 * @param list
	 */
	private static void sort(ArrayList<TreeMap<String, Integer>> list){
		for (int i =0 ; i<=list.size()-2 ; i++){
			for (int j = (list.size() -1) ; i<j;j--){
				if (list.get(j).get("totalPostCount") > list.get(j-1).get("totalPostCount")){
					TreeMap<String, Integer> temp = list.get(j-1);
					list.set(j-1, list.get(j));
					list.set(j, temp);
				}
			}
		}
	}
	
	/**
	 * 
	 * Return a list of top users who share the tag in the tag list (passed as a parameter).
	 * The list is sorted by the total score of users.
	 * @param tagList : tag are identified by their id
	 * @return [{userId : , totalPostCount : , tag1 :, tag2 : ...}, {userId : , totalPostCount : , tag3 :, tag 4 :, ...}, ...] <br>
	 * 			For each map in the array list, there are : - the user id is indexed by the key userId <br>
	 * 														- the total post count is indexed by totalPostCount <br>
	 *   													- for each tag, the post count of this tag is identified by the id of the tag. <br>
	 * @throws SQLException 
	 *   
	 */
	public static ArrayList<TreeMap<String, Integer>> getTopAnswerers(ArrayList<Integer> tagList) throws SQLException{
		
		int nbTags = tagList.size();		
		ArrayList<Integer> potentialUsers = usersSharingTags(tagList);
		
		ArrayList<TreeMap<String, Integer>> topTagUsers = new ArrayList<TreeMap<String, Integer>>();
		
		for (int i = 0 ;i <potentialUsers.size(); i++){
			int idUser = potentialUsers.get(i);
			int totalPostCount = 0;
			TreeMap<String, Integer> user = new TreeMap<String, Integer>();
			user.put("userId", idUser);
			
			for (int j = 0 ; j< tagList.size(); j++){
				int idTag = tagList.get(j);
				String sql = "SELECT POST_COUNT FROM " + DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_POST_TABLE) +
						" WHERE ID_USER = ? AND ID_TAG = ?";
				PreparedStatement stmt = DatabaseManager.databaseConnection.prepareStatement(sql);
				stmt.setInt(1, idUser);
				stmt.setInt(2, idTag);
				ResultSet res = stmt.executeQuery();
				
				int postCount = 0;
				if (res.next()){
					postCount = res.getInt("POST_COUNT");
				}
				
				user.put(Integer.toString(idTag), postCount);
				totalPostCount +=postCount;
				stmt.close();
				res.close();
			}
			
			user.put("totalPostCount",	totalPostCount);
			topTagUsers.add(user);
			
			
		}
		sort(topTagUsers);
		return topTagUsers;
	}
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, JSONException, IOException, SQLException, URISyntaxException {
		ArrayList<Integer> tagList = new ArrayList<Integer>();
		tagList.add(8);
		tagList.add(133);
		updateDateUser(23);
	}

}


