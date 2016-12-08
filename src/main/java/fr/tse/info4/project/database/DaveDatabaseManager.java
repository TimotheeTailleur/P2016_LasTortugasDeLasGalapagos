package fr.tse.info4.project.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import com.google.code.stackexchange.schema.Tag;
import com.google.code.stackexchange.schema.User;

import fr.tse.info4.project.datarecovery.DaveApiManager;
import fr.tse.info4.project.schema.TagScore;

/**
 * This class will manage the database tables used in functions implemented for
 * the Dave User Story
 *
 */
public class DaveDatabaseManager extends DatabaseManager {

	/**
	 * Updates tables tag_post_count and tag_score with the 20 top answerers in
	 * the tag specified. If a user's id isn't found in the database, creates
	 * new entries in both tables
	 * 
	 * @param idTag
	 *            Tag id
	 * 
	 * @throws SQLException
	 */
	public static void fillDaveTablesTopAnswerers(int idTag) throws SQLException {
		// Get tag name with tag id
		String tagName = getTagName(idTag);

		// Get Top Answerers stats in given tag
		DaveApiManager apiManager = new DaveApiManager(DaveApiManager.APP_KEY, DaveApiManager.SITE);
		ArrayList<TagScore> tagScoreList = (ArrayList<TagScore>) apiManager.getTopAnswerers(tagName);

		// Prepare Statements for updates and insertions
		setup();
		PreparedStatement stmtUpdatePost = databaseConnection.prepareStatement("UPDATE "
				+ addDoubleQuotes(TITLE_TAG_POST_TABLE) + " SET post_count = ? WHERE id_user = ? AND id_tag = ?");
		PreparedStatement stmtUpdateScore = databaseConnection.prepareStatement(
				"UPDATE " + addDoubleQuotes(TITLE_TAG_SCORE_TABLE) + " SET score = ? WHERE id_user = ? AND id_tag = ?");

		PreparedStatement stmtInsertPost = databaseConnection.prepareStatement(
				"INSERT INTO " + addDoubleQuotes(TITLE_TAG_POST_TABLE) + " (id_User,id_tag,post_count) VALUES (?,?,?)");

		PreparedStatement stmtInsertScore = databaseConnection.prepareStatement(
				"INSERT INTO " + addDoubleQuotes(TITLE_TAG_SCORE_TABLE) + " (id_User,id_tag,score) VALUES (?,?,?)");

		long idUser;
		int score, postCount;
		/*
		 * For each entry in the top Answerers ResultSet, get userID, user score
		 * and user Post_Count in given tag and set preparedStatements'
		 * variables accordingly
		 */
		for (int i = 0; i < tagScoreList.size(); i++) {

			idUser = tagScoreList.get(i).getUser().getUserId();
			postCount = tagScoreList.get(i).getPostCount();
			score = tagScoreList.get(i).getScore();

			stmtUpdatePost.setInt(1, postCount);
			stmtUpdatePost.setLong(2, idUser);
			stmtUpdatePost.setInt(3, idTag);
			stmtUpdateScore.setInt(1, score);
			stmtUpdateScore.setLong(2, idUser);
			stmtUpdateScore.setInt(3, idTag);

			// If a user's id isn't found in either table : execute Insertion
			// prepared Statements
			if (stmtUpdatePost.executeUpdate() == 0) {

				stmtInsertPost.setLong(1, idUser);
				stmtInsertPost.setInt(2, idTag);
				stmtInsertPost.setInt(3, postCount);
				stmtInsertPost.executeUpdate();

			}

			if (stmtUpdateScore.executeUpdate() == 0) {

				stmtInsertScore.setLong(1, idUser);
				stmtInsertScore.setInt(2, idTag);
				stmtInsertScore.setInt(3, score);
				stmtInsertScore.executeUpdate();

			}
		}
		stmtUpdatePost.close();
		stmtInsertPost.close();
		stmtUpdateScore.close();
		stmtInsertScore.close();
		close();
	}

	/**
	 * Returns number of seconds since last update (in Dave tables) of the tag
	 * passed as a parameter If last_update_dave is not given (null), returns a
	 * number bigger than nbDays in seconds.
	 * 
	 * @param tag
	 * @return
	 *         <ul>
	 *         <li>number of seconds</li>
	 *         <li>-1 if there is not date for the specified tag.</li>
	 *         </ul>
	 */
	public static long getTimeUpdateTopTag(int idTag) {
		setup();
		String sql = "SELECT LAST_UPDATE_DAVE FROM " + addDoubleQuotes(TITLE_TAG_TABLE) + " WHERE ID_TAG = ?";

		Date lastUpdate = null;
		PreparedStatement stmt;
		try {

			stmt = databaseConnection.prepareStatement(sql);
			stmt.setInt(1, idTag);
			ResultSet res = stmt.executeQuery();

			if (res.next()) {
				lastUpdate = res.getDate("LAST_UPDATE_DAVE");
			}

			stmt.close();
			res.close();

		} catch (SQLException e) {
			System.out.println("getTimeUpdateTopTag (DaveDatabaseManager) - Erreur de la requête sql.");
			e.printStackTrace();
		}

		Date currentDate = new Date(new java.util.Date().getTime());

		if (lastUpdate != null) {
			return (currentDate.getTime() / 1000) - (lastUpdate.getTime() / 1000);
		}
		close();
		return -1;

	}

	public static long getTimeUpdateUserTags(int idUser) {
		setup();
		String sql = "SELECT LAST_UPDATE_TAG FROM " + addDoubleQuotes(TITLE_USERS_TABLE) + " WHERE ID_USER = ?";

		Date lastUpdate = null;
		PreparedStatement stmt;
		try {

			stmt = databaseConnection.prepareStatement(sql);
			stmt.setInt(1, idUser);
			ResultSet res = stmt.executeQuery();

			if (res.next()) {
				lastUpdate = res.getDate("LAST_UPDATE_TAG");
			}
			stmt.close();
			res.close();

		} catch (SQLException e) {
			System.out.println("getTimeUpdateTopTag (DaveDatabaseManager) - Erreur de la requête sql.");
			e.printStackTrace();
		}

		Date currentDate = new Date(new java.util.Date().getTime());

		if (lastUpdate != null) {
			return (currentDate.getTime() / 1000) - (lastUpdate.getTime() / 1000);
		}

		return -1;

	}

	/**
	 * Insert a pageList of tag in the table post count. Update data if they are
	 * already present
	 * 
	 * @param tagList
	 * @param idUser
	 */
	public static void insertListTag(ArrayList<Tag> tagList) {
		setup();
		String sqlInsert = "INSERT INTO " + addDoubleQuotes(TITLE_TAG_POST_TABLE)
				+ "(ID_USER, ID_TAG, POST_COUNT) VALUES (?, ?, ?)";
		String sqlUpdate = "UPDATE " + addDoubleQuotes(TITLE_TAG_POST_TABLE)
				+ " SET POST_COUNT = ? WHERE ID_USER = ? AND ID_TAG = ?";

		PreparedStatement stmtInsert = null;
		PreparedStatement stmtUpdate = null;
		try {
			stmtInsert = databaseConnection.prepareStatement(sqlInsert);
			stmtUpdate = databaseConnection.prepareStatement(sqlUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < tagList.size(); i++) {
			int idTag = getTagId(tagList.get(i).getName());
			int postCount = (int) tagList.get(i).getCount();
			int idUser = (int) tagList.get(i).getUserId();

			try {
				stmtUpdate.setInt(1, postCount);
				stmtUpdate.setInt(2, idUser);
				stmtUpdate.setInt(3, idTag);

				// If no user was found, insert the new user and his data.
				if (stmtUpdate.executeUpdate() == 0) {

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
		close();
	}

	/**
	 * 
	 * Return a list of TagScore, sorted by post count(desc) of the top
	 * answerers in the given tab
	 * 
	 * @param nbUsers
	 * @param tag
	 *            : id which identified the tag in the tag table
	 * @return
	 */
	public static List<TagScore> getTopAnswerers(int nbUsers, int tag) {
		setup();
		String sql = "SELECT ID_USER, POST_COUNT FROM " + addDoubleQuotes(TITLE_TAG_POST_TABLE)
				+ " WHERE ID_TAG = ? ORDER BY POST_COUNT DESC";

		ArrayList<TagScore> userList = new ArrayList<TagScore>(nbUsers);
		try {
			PreparedStatement stmt = databaseConnection.prepareStatement(sql);
			stmt.setInt(1, tag);
			ResultSet res = stmt.executeQuery();

			int cpt = 0;
			while (res.next() && cpt < nbUsers) {

				int idUser = res.getInt("ID_USER");
				int postCount = res.getInt("POST_COUNT");
				User user = new User();
				user.setUserId(idUser);
				TagScore tagScore = new TagScore();
				tagScore.setUser(user);
				tagScore.setPostCount(postCount);
				userList.add(tagScore);
				cpt++;
			}
			stmt.close();
			res.close();
		} catch (SQLException e) {
			System.out.println("getTimeUpdateTopTag (DaveDatabaseManager) - Erreur de la requête sql.");
			e.printStackTrace();
		} finally {
			close();
		}

		return userList;
	}

	public static TagScore getTopTag(int tag) {
		setup();
		String sql = "SELECT ID_USER, SCORE FROM " + addDoubleQuotes(TITLE_TAG_SCORE_TABLE)
				+ " WHERE ID_TAG = ? ORDER BY SCORE DESC";
		TagScore tagScore = new TagScore();
		try {
			PreparedStatement stmt = databaseConnection.prepareStatement(sql);
			stmt.setInt(1, tag);
			ResultSet res = stmt.executeQuery();

			if (res.next()) {
				int idUser = res.getInt("ID_USER");
				int score = res.getInt("SCORE");
				User user = new User();
				user.setUserId(idUser);
				tagScore.setUser(user);
				tagScore.setScore(score);
			}
			stmt.close();
			res.close();
		} catch (SQLException e) {
			System.out.println("getTimeUpdateTopTag (DaveDatabaseManager) - Erreur de la requête sql.");
			e.printStackTrace();
		} finally {
			close();
		}

		return tagScore;
	}

	/**
	 * Update the date of the last update concerning post count and score tables
	 * (Dave) to the current date in the tag table
	 * 
	 * @param idTag
	 *            id of the tag referenced in the tag Table
	 */
	public static void updateDateTag(int idTag) {
		setup();
		String sql = "UPDATE " + addDoubleQuotes(TITLE_TAG_TABLE)
				+ " SET LAST_UPDATE_DAVE = CURRENT_DATE WHERE ID_TAG = ?";
		try {
			PreparedStatement stmt = databaseConnection.prepareStatement(sql);
			stmt.setInt(1, idTag);
			stmt.executeUpdate();

			stmt.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		close();

	}

	/**
	 * Update the date of the last update concerning the post count of a user to
	 * the current date in the user table Insert the user with the current date
	 * if the user doesn't exist
	 * 
	 * @param idUser
	 */
	public static void updateDateUser(int idUser) {
		setup();
		String sqlUpdate = "UPDATE " + addDoubleQuotes(TITLE_USERS_TABLE)
				+ " SET LAST_UPDATE_TAG = CURRENT_DATE WHERE ID_USER = ?";
		String sqlInsert = "INSERT INTO " + addDoubleQuotes(TITLE_USERS_TABLE)
				+ "(ID_USER, LAST_UPDATE_TAG) VALUES (?, CURRENT_DATE)";

		try {
			PreparedStatement stmtUpdate = databaseConnection.prepareStatement(sqlUpdate);
			stmtUpdate.setInt(1, idUser);

			// If the user doesn't exist : insertion of this user at the current
			// date
			if (stmtUpdate.executeUpdate() == 0) {
				PreparedStatement stmtInsert = databaseConnection.prepareStatement(sqlInsert);
				stmtInsert.setInt(1, idUser);

				stmtInsert.executeUpdate();
				stmtInsert.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
	}

	
	public static int getNbUsers(String tag) {

		int idTag = getTagId(tag);

		String sql = "SELECT COUNT(*) FROM " + addDoubleQuotes(TITLE_TAG_POST_TABLE) + " WHERE ID_TAG = ?";

		int nbUsers = 0;
		setup();
		try {
			PreparedStatement stmt = databaseConnection.prepareStatement(sql);
			stmt.setInt(1, idTag);
			ResultSet res = stmt.executeQuery();

			if (res.next()) {
				nbUsers = res.getInt(1);
			}

			stmt.close();
			res.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return nbUsers;

	}


	/**
	 * Return the max post count for a user in a set of tags
	 * 
	 * @param tagList
	 * @param idUser
	 * @return int : max post count
	 */
	public static int maxPostCountForTags(ArrayList<Integer> tagList, int idUser) {
		String sql = "SELECT MAX(POST_COUNT) FROM " + addDoubleQuotes(TITLE_TAG_POST_TABLE) + " WHERE ID_USER = ? AND ";

		for (int i = 0; i < tagList.size(); i++) {
			if (i != tagList.size() - 1) {
				sql += "ID_TAG = ? OR ";
			} else {
				sql += "ID_TAG = ?";
			}
		}

		setup();
		int maxPostCount = 0;
		try {
			PreparedStatement stmt = databaseConnection.prepareStatement(sql);
			stmt.setInt(1, idUser);
			for (int i = 0; i < tagList.size(); i++) {
				stmt.setInt(i + 2, tagList.get(i));
			}
			ResultSet res = stmt.executeQuery();

			if (res.next()) {
				maxPostCount = res.getInt(1);
			}
			stmt.close();
			res.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(sql);
		close();
		return maxPostCount;
	}

	public static void main(String[] args) throws SQLException {

		System.out.println(getNbUsers("ios"));

	}

}
