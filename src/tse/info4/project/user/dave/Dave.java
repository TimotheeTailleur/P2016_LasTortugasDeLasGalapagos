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
	 * Return the top answerer in given tag
	 * 
	 * @param tag
	 * @param nbDays
	 *            maximum number of days between 2 updates
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

		DatabaseManager.setup();

		// Sql query for select id_tag and last_update_dave depending on tag
		// passed as a parameter
		String sqlSelectTag = "SELECT ID_TAG, LAST_UPDATE_DAVE FROM "
				+ DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_TABLE) + " WHERE tag_name = ? ";
		PreparedStatement stmtSelectTag = DatabaseManager.databaseConnection.prepareStatement(sqlSelectTag);
		stmtSelectTag.setString(1, tag);
		ResultSet resSelectTag = stmtSelectTag.executeQuery();

		int idTag = 0;
		Date lastUpdateDave = null;

		if (resSelectTag.next()) {
			idTag = resSelectTag.getInt("ID_TAG");
			lastUpdateDave = resSelectTag.getDate("LAST_UPDATE_DAVE");
		} else {
			System.out.println("Veuillez entrer un nom de Tag valide");
			return null;
		}

		// Get the current date and convert it to the same format that sql
		// dates.
		java.util.Date currentDate = new java.util.Date();
		Date currentDateSql = new Date(currentDate.getTime());

		double nbSecondes = nbDays * 86_400; // 86 400 000 = 24*3600 = 1 day (s)

		// The default value of diff is bigger than nbSecondes to force the
		// insert if
		// there is no data available concerning the tag.
		double diff = nbSecondes + 1;

		if (lastUpdateDave != null) {
			diff = currentDateSql.getTime() / 1000 - lastUpdateDave.getTime() / 1000;
		}

		// If the data are too old or if the user wants to force update,
		// the post count and the score are inserting (or updating if they
		// already exist)
		// and the date of the last update is updating to the current date.
		if (diff > nbSecondes || forceUpdate) {
			DaveDatabaseManager.fillTablesTagPostCountScore(idTag);
			String sqlUpdateDate = "UPDATE " + DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_TABLE)
					+ " SET LAST_UPDATE_DAVE = CURRENT_DATE WHERE ID_TAG = ?";
			PreparedStatement updateDatestmt = DatabaseManager.databaseConnection.prepareStatement(sqlUpdateDate);
			updateDatestmt.setInt(1, idTag);
			updateDatestmt.executeUpdate();
			System.out.println("données mises à jour");

		}

		String sqlSelectTopAnswerers = "SELECT * FROM "
				+ DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_POST_TABLE)
				+ "WHERE id_tag = ? ORDER BY POST_COUNT desc";

		PreparedStatement stmtTopAnswerers = DatabaseManager.databaseConnection.prepareStatement(sqlSelectTopAnswerers);
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
	 * Get top tag user id in given tag
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
		DatabaseManager.setup();

		// Sql query for select id_tag and last_update_dave depending on tag
		// passed as a parameter
		String sqlSelectTag = "SELECT ID_TAG, LAST_UPDATE_DAVE FROM "
				+ DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_TABLE) + " WHERE tag_name = ? ";
		PreparedStatement stmtSelectTag = DatabaseManager.databaseConnection.prepareStatement(sqlSelectTag);
		stmtSelectTag.setString(1, tag);
		ResultSet resSelectTag = stmtSelectTag.executeQuery();

		int idTag = 0;
		Date lastUpdateDave = null;

		if (resSelectTag.next()) {
			idTag = resSelectTag.getInt("ID_TAG");
			lastUpdateDave = resSelectTag.getDate("LAST_UPDATE_DAVE");
		} else {
			System.out.println("Veuillez entrer un nom de Tag valide");
			return 0;
		}

		// Get the current date and convert it to the same format that sql
		// dates.
		java.util.Date currentDate = new java.util.Date();
		Date currentDateSql = new Date(currentDate.getTime());

		double nbSecondes = nbDays * 86_400; // 86 400 000 = 24*3600 = 1 day (s)

		// The default value of diff is bigger than nbSecondes to force the
		// insert if
		// there is no data available concerning the tag.
		double diff = nbSecondes + 1;

		if (lastUpdateDave != null) {
			diff = currentDateSql.getTime() / 1000 - lastUpdateDave.getTime() / 1000;
		}

		// If the data are too old or if the user wants to force update,
		// the post count and the score are inserting (or updating if they
		// already exist)
		// and the date of the last update is updating to the current date.
		if (diff > nbSecondes || forceUpdate) {
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

	

	// Méthode main pour démo
	public static void main(String[] args) throws SQLException, IOException, JSONException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, URISyntaxException {
		System.out.println("Top post count");
		System.out.println("[id_user, post_count]");
		System.out.println(getTopAnswerers("javascript", 10, 2, true).toString());
		System.out.println("\nTop score");
		System.out.println(getTopTag("javascript", 2, false));

	}

}
