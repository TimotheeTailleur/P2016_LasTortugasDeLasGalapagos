package fr.tse.info4.project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class that manages the database (connection and driver setup, updates and
 * insertions, general queries, truncature of tables) Both tables use user id as
 * primary key and have columns named after the tags on Stack Overflow.
 * TagPostCount rows contain a user's id and a user's post count in each tag.
 * TagScore rows contain a user's id and a user's score in each tag.
 */

public class DatabaseManager {

	/**
	 * Package name of derby embedded driver
	 */
	protected final String driverClassName = "org.apache.derby.jdbc.EmbeddedDriver";

	/**
	 * Relative path to the derby database repository
	 */
	protected final String databasePath = "ProjectDatabase";


	/**
	 * Connection item to manipulate the database
	 */
	protected Connection conn;

	
	/**
	 * Constructor. <br>
	 * Loads the apache derby embedded driver.
	 */
	public DatabaseManager() {
		try {
			Class.forName(driverClassName);
		} catch (Exception e) {
			System.err.print("Driver not found");
		}
	}

	/**
	 * Sets up database connection.
	 * @return Connection item to manipulate the database.
	 */
	protected Connection getConnection() {
		try {
			conn = DriverManager.getConnection("jdbc:derby:" + databasePath);
		} catch (SQLException e) {
			System.err.print("Failed to create the database connection");
		}
		return conn;
	}

	/**
	 * Closes database connection.
	 */
	protected void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected String addDoubleQuotes(String str) {
		return '"' + str + '"';
	}

	protected String addSimpleQuotes(String str) {
		return '\'' + str + '\'';
	}
	
	/**
	 * Deletes data from the table passed as a parameter
	 * 
	 * @param tableName
	 * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
	 * @throws SQLException
	 */
	protected int truncateTable(String tableName) throws SQLException{
		Statement statement = getConnection().createStatement();
		int result = statement.executeUpdate("TRUNCATE TABLE " + addDoubleQuotes(tableName));
		statement.close();
		closeConnection();
		return result;
	}
	
	
	/**
	 *  Retrieves the id corresponding to the tag name in the database.*
	 * @param tagName
	 * @return either the tag id for the name passed as a parameter or -1 if the tag doesn't exist.
	 */
	public int getTagId(String tagName){
		String sql = "SELECT ID_TAG FROM " + addDoubleQuotes(TablesTitle.TITLE_TAG_TABLE) + " WHERE tag_name = ?";
		int idTag = -1;
		
		try{
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, addSimpleQuotes(tagName));
			ResultSet res = stmt.executeQuery();
			
			if (res.next())
				idTag = res.getInt("ID_TAG");
			stmt.close();
			res.close();
		}
		catch(SQLException e){
			System.err.println("Failed to execute SQL statement");
		}
		finally{
			closeConnection();
		}
		return idTag;
	}
	
	/**
	 * Retrives the name corresponding to the id in the database.
	 * 
	 * @param idTag
	 * @return either the tag name or the void String if the tag doesn't exist.s
	 */
	public String getTagName(int idTag){
		if (idTag<0){
			return "";
		}
		String sql ="SELECT tag_name FROM" + addDoubleQuotes(TablesTitle.TITLE_TAG_TABLE) + "WHERE id_tag= ?";
		String tagName = "";
		
		try{
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, idTag);
			ResultSet res = stmt.executeQuery();
			
			if (res.next())
				tagName = res.getString("tag_name");
			stmt.close();
			res.close();				
		}
		catch(SQLException e){
			System.err.println("Failed to execute SQL statement");
		}
		finally{
			closeConnection();
		}
		if (tagName != "")
			return tagName.substring(1, tagName.length()-1);
		
		return tagName;
	}
	
	/**
	 * Verifies if the tag exists in the tags' table.
	 * @param tagName
	 * @return True if the tag exists. False otherwise.
	 */
	public boolean tagExist(String tagName){
		return getTagId(tagName)>=0;
	}
	
}
