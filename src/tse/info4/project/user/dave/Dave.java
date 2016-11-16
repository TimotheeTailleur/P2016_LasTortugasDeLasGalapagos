package tse.info4.project.user.dave;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.json.JSONException;

import tse.info4.project.database.DatabaseManager;
import tse.info4.project.database.DaveDatabaseManager;

/**
 * 
 * Function and demo main method for user story Dave
 *
 */
public class Dave{
	

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

		tag=DatabaseManager.addSimpleQuotes(tag);
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

			DaveDatabaseManager.fillDaveTablesTopAnswerers(idTag); //Update of score & postcount

			String sqlUpdateDate = "UPDATE " + DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_TABLE)
					+ " SET LAST_UPDATE_DAVE = CURRENT_DATE WHERE ID_TAG = ?";
			PreparedStatement updateDatestmt = DatabaseManager.databaseConnection.prepareStatement(sqlUpdateDate); //Change last Update date to current time
			updateDatestmt.setInt(1, idTag);
			updateDatestmt.executeUpdate();
			updateDatestmt.close();
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

		stmtTopAnswerers.close();
		resQuery.close();
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
			DaveDatabaseManager.fillDaveTablesTopAnswerers(idTag);

			String sqlUpdateDate = "UPDATE " + DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_TABLE)
					+ " SET LAST_UPDATE_DAVE = CURRENT_DATE WHERE ID_TAG = ?";
			PreparedStatement updateDatestmt = DatabaseManager.databaseConnection.prepareStatement(sqlUpdateDate);
			updateDatestmt.setInt(1, idTag);
			updateDatestmt.executeUpdate();
			updateDatestmt.close();
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
		
		stmtTopAnswerers.close();
		resQuery.close();
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
		stmtSelectTag.close();
		resSelectTag.close();

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
 * 
 * Return a list of top user int the tags passed as a parameter. The list is sorted by the total post count for each user.
 * 
 * @param tagList : list of tag identified by their name (c++, java, c ...)
 * @param nbDays : number of days between 2 updates of the database
 * @param forceUpdate true if the user wants to force the update, false otherwise
 * @return [{userId : , totalPostCount : , tag1 :, tag2 : ...}, {userId : , totalPostCount : , tag3 :, tag 4 :, ...}, ...] <br>
 * 			For each map in the array list, there are : - the user id is indexed by the key userId <br>
 * 														- the total post count is indexed by totalPostCount <br>
 * 														- for each tag, the post count of this tag is identified by the id of the tag. <br>
 * @throws InstantiationException
 * @throws IllegalAccessException
 * @throws ClassNotFoundException
 * @throws JSONException
 * @throws IOException
 * @throws SQLException
 * @throws URISyntaxException
 */
	public static ArrayList<TreeMap<String, Integer>> getTopTag(ArrayList<String> tagList, int nbDays, boolean forceUpdate) throws InstantiationException, IllegalAccessException, ClassNotFoundException, JSONException, IOException, SQLException, URISyntaxException{
		DatabaseManager.setup();
		ArrayList<Integer> tagListId = new ArrayList<Integer>(); // the same list that the one which is passed as a parameter except this is a list of id (the first one was a list of name)
		for (int i = 0; i<tagList.size(); i++){

			int idTag = DaveDatabaseManager.getTagId(tagList.get(i));
			tagListId.add(idTag);
			
			long timeSpentSinceLastTagUpdate = DaveDatabaseManager.getTimeUpdateTopTag(idTag);
			
			// If the data in the database are too old or if the user wants to force the update
			
			if (timeSpentSinceLastTagUpdate == -1 || timeSpentSinceLastTagUpdate <nbDays || forceUpdate){
				
				// Filling of the table
				DaveDatabaseManager.fillDaveTablesTopAnswerers(idTag);
				// Update the date in the database (table tag)
				DaveDatabaseManager.updateDateTag(idTag);
			}
			
			
			// userList = [{id = , postCount = }, {id = , postCount = }, ...]
			ArrayList<TreeMap<String, Integer>> userList = DaveDatabaseManager.getTopAnswerers(20, idTag);
			
			
			
		
			
			
			for (int j =0 ; j<userList.size(); j++){
				
				int idUser = userList.get(j).get("id");
				long timeSpentSinceLastUserUpdate = DaveDatabaseManager.getTimeUpdateUserTag(idUser);
				
				if (timeSpentSinceLastUserUpdate == -1 || timeSpentSinceLastUserUpdate < nbDays * 86_400 || forceUpdate){
					int scoreMin = userList.get(j).get("postCount") / 10;
					// Filling of the tag_post_count table : adding of all tag for the user (idUser) where postCount > 0.1*maxPostCount
					DaveDatabaseManager.fillTablePostCount(userList.get(j).get("id"), scoreMin);
					DaveDatabaseManager.updateDateUser(idUser);
				}
				
				
			}
		}
		DatabaseManager.close();
		return DaveDatabaseManager.getTopAnswerers(tagListId);
	}
		
			
	

	
	// Méthode main pour démo
	public static void main(String[] args) throws SQLException, IOException, JSONException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, URISyntaxException {
		
		//DatabaseManager.setup();
		//DatabaseManager.truncateTable("tag_post_count");
		
		ArrayList<String> tagList = new ArrayList<String>();
		tagList.add("c++");
		tagList.add("java");
		//DaveDatabaseManager.updateDateUser(13);
		
		System.out.println(getTopTag(tagList, 5, false)); 

			
		}

}
