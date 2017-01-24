package fr.tse.info4.project.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.code.stackexchange.schema.User;

import fr.tse.info4.project.model.datarecovery.DaveApiManager;
import fr.tse.info4.project.model.schema.TagScore;

public class DaveDatabaseManager extends DatabaseManager {

	public DaveDatabaseManager() {
	}

	/**
	 * 
	 * Retrieves the current number of users contained in the database for the
	 * given tag name
	 * 
	 * @param tagName
	 * @return number of users.
	 */
	public int getNbUsers(String tagName) {
		int idTag = getTagId(tagName);
		String sql = "SELECT COUNT(*)  FROM " + addDoubleQuotes(TablesTitle.TITLE_TAG_POST_TABLE) + " WHERE ID_TAG = ?";
		int nbUsers = 0;
		try {
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, idTag);
			ResultSet res = stmt.executeQuery();

			if (res.next())
				nbUsers = res.getInt(1);
			stmt.close();
			res.close();
		} catch (SQLException e) {
			System.err.println("Failed to execute SQL statement");
			e.printStackTrace();
		} finally {
			closeConnection();
		}

		return nbUsers;
	}

	/**
	 * Updates the last time the database was updated to the current date for each row with information regarding tag 
	 * idTag
	 * 
	 * @param idTag
	 */
	public void updateDateTag(int idTag) {
		String sql = "UPDATE " + addDoubleQuotes(TablesTitle.TITLE_TAG_TABLE)
				+ " SET LAST_UPDATE_DAVE = CURRENT_DATE WHERE ID_TAG = ?";
		try {
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, idTag);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			System.err.println("Failed to execute SQL statement");
			e.printStackTrace();
		} finally {
			closeConnection();
		}

	}

	/**
	 * Returns top tag user in given tag
	 * 
	 * @param tag : tag id
	 * @return Tagscore
	 */
	public TagScore getTopTag(int tag) {
		String sql = "SELECT ID_USER, SCORE FROM " + addDoubleQuotes(TablesTitle.TITLE_TAG_SCORE_TABLE)
				+ " WHERE ID_TAG = ? ORDER BY SCORE DESC";

		TagScore tagScore = new TagScore();
		try {
			PreparedStatement stmt = getConnection().prepareStatement(sql);
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
			System.err.println("Failed to execute SQL statement");
			e.printStackTrace();
		} finally {
			closeConnection();
		}

		return tagScore;
	}

	/**
	 * Returns top nbUsers answerers in given tag idTag
	 * 
	 * @param nbUsers
	 * @param idTag
	 * @return TagScore List contained the best users.
	 */
	public List<TagScore> getTopAnswerers(int nbUsers, int idTag) {
		String sql = "SELECT ID_USER, POST_COUNT FROM " + addDoubleQuotes(TablesTitle.TITLE_TAG_POST_TABLE)
				+ " WHERE ID_TAG = ? ORDER BY POST_COUNT DESC";

		List<TagScore> users = new ArrayList<TagScore>(nbUsers);
		try {
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, idTag);
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
				users.add(tagScore);
				cpt++;
			}
			stmt.close();
			res.close();
		} catch (SQLException e) {
			System.err.println("Failed to execute SQL Statement");
			e.printStackTrace();
		} finally {
			closeConnection();
		}

		return users;
	}

	/**
	 * Returns seconds since last update of data in teh given tag
	 * 
	 * @param idTag
	 * @return either the number of the seconds or -1 if the tag date isn't
	 *         specified.
	 */
	public long getTimeUpdateTopTag(int idTag) {
		String sql = "SELECT LAST_UPDATE_DAVE FROM " + addDoubleQuotes(TablesTitle.TITLE_TAG_TABLE)
				+ " WHERE ID_TAG = ?";
		Date lastUpdate = null;
		try {
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, idTag);
			ResultSet res = stmt.executeQuery();

			if (res.next())
				lastUpdate = res.getDate("LAST_UPDATE_DAVE");
			stmt.close();
			res.close();
		} catch (SQLException e) {
			System.err.println("Failde to execute SQL Statement");
			e.printStackTrace();
		} finally {
			closeConnection();
		}

		Date currentDate = new Date(new java.util.Date().getTime());

		if (lastUpdate != null)
			return (currentDate.getTime() / 1000) - (lastUpdate.getTime() / 1000);
		return -1;
	}

	/**
	 * Updates dave tables with the 20 top answerers in the specified tag. <br>
	 * If a user's id isn't found, creates new entries in both tables.
	 * @param idTag
	 * @throws SQLException
	 */
	public void fillDaveTables(int idTag) throws SQLException {
		String tagName = getTagName(idTag);

		DaveApiManager apiManager = new DaveApiManager(DaveApiManager.APP_KEY, DaveApiManager.SITE);
		List<TagScore> tagScore = apiManager.getTopAnswerers(tagName);

		Connection conn = getConnection();

		PreparedStatement stmtUpdatePost = conn
				.prepareStatement("UPDATE " + addDoubleQuotes(TablesTitle.TITLE_TAG_POST_TABLE)
						+ " SET post_count = ? WHERE id_user = ? AND id_tag = ?");
		PreparedStatement stmtUpdateScore = conn
				.prepareStatement("UPDATE " + addDoubleQuotes(TablesTitle.TITLE_TAG_SCORE_TABLE)
						+ " SET score = ? WHERE id_user = ? AND id_tag = ?");

		PreparedStatement stmtInsertPost = conn.prepareStatement("INSERT INTO "
				+ addDoubleQuotes(TablesTitle.TITLE_TAG_POST_TABLE) + " (id_User,id_tag,post_count) VALUES (?,?,?)");

		PreparedStatement stmtInsertScore = conn.prepareStatement("INSERT INTO "
				+ addDoubleQuotes(TablesTitle.TITLE_TAG_SCORE_TABLE) + " (id_User,id_tag,score) VALUES (?,?,?)");

		long idUser;
		int score, postCount;

		for (int i = 0; i < tagScore.size(); i++) {
			idUser = tagScore.get(i).getUser().getUserId();
			postCount = tagScore.get(i).getPostCount();
			score = tagScore.get(i).getScore();

			stmtUpdatePost.setInt(1, postCount);
			stmtUpdatePost.setLong(2, idUser);
			stmtUpdatePost.setInt(3, idTag);
			stmtUpdateScore.setInt(1, score);
			stmtUpdateScore.setLong(2, idUser);
			stmtUpdateScore.setInt(3, idTag);
			
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
		closeConnection();

	}
}
