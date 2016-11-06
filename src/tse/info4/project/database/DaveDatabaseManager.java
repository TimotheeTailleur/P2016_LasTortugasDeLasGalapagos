package tse.info4.project.database;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
		String tagName= getTagName(idTag);
		
		//Get Top Answerers stats in given tag
		TreeMap<Integer, ArrayList<Integer>> resAns = StackExchangeApiManager.getTopAnswerers(tagName);
		
		//Prepare Statements for updates and insertions
		PreparedStatement stmtUpdatePost = databaseConnection
				.prepareStatement("UPDATE " +  addDoubleQuotes(TITLE_TAG_POST_TABLE)  + " SET post_count = ? WHERE id_user = ?");
		PreparedStatement stmtUpdateScore = databaseConnection
				.prepareStatement("UPDATE " + addDoubleQuotes(TITLE_TAG_SCORE_TABLE)  + " SET score = ? WHERE id_user = ?");
		
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
			stmtUpdateScore.setInt(1, score);
			stmtUpdateScore.setInt(2, userID); 

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
	
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, JSONException, IOException, SQLException, URISyntaxException {
		setup();
		truncateTable(TITLE_TAG_POST_TABLE);
		truncateTable(TITLE_TAG_SCORE_TABLE);
		fillTablesTagPostCountScore(1);
	}

}


