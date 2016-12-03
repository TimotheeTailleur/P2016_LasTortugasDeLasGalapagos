package fr.tse.info4.project.database;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.derby.catalog.GetProcedureColumns;
import org.json.JSONException;

import fr.tse.info4.project.datarecovery.StackExchangeApiManager;

/**
 * Class that manages the database (connection and driver setup, updates and insertions, general queries, truncature of tables)
 * Currently has 2 tables : TagPostCount and TagScore.
 * Both tables use user id as primary key and have columns named after the tags on Stack Overflow.
 * TagPostCount rows contain a user's id and a user's post count in each tag.
 * TagScore rows contain a user's id and a user's score in each tag.
 */

public class DatabaseManager {
	
	// Constant members
	/**
	 * Package name of derby embedded driver 
	 */	
	protected static final String EMBEDDED_DRIVER_PACKAGE = "org.apache.derby.jdbc.EmbeddedDriver";
	
	/**
	 * Relative path to the derby database repository
	 */
	protected static final String DATABASE_PATH ="ProjectDatabase";
	
	/**
	 * Name of the table in which all post counts for each user are contained 
	 */
	public static final String TITLE_TAG_POST_TABLE = "tag_post_count";
	
	/**
	 * Name of the table in which all scores for each user are contained 
	 */
	public static final String TITLE_TAG_SCORE_TABLE = "tag_score";
	
	public static final String TITLE_TAG_TABLE = "tag";
	
	/**
	 * Name of the table in which all data users who have used the application are contained.
	 */
	public static final String TITLE_USERS_TABLE = "users";
	
	/**
	 * Name of the table in which data questions are stored.
	 */
	public static final String TITLE_QUESTIONS_TABLE = "questions";
	
	//Other members
	
	/**
	 * Connection item to manipulate the database
	 */
	public static Connection databaseConnection;
	
	


	
	public static void getTags(int page) throws JSONException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		// Remplir la table tag � partir de l'api.
		// L'api limite � 30 appels par secondes et � 10 000 appels par jour (remplissage en plusieurs fois) !
		
		
		ArrayList<String> tagNames = StackExchangeApiManager.getTags(page);
		setup();
		PreparedStatement stmtSelect = databaseConnection.prepareStatement("SELECT ID_TAG FROM "+addDoubleQuotes(TITLE_TAG_TABLE)+"WHERE TAG_NAME =  ?",ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		PreparedStatement stmtInsert = databaseConnection.prepareStatement("INSERT INTO "+addDoubleQuotes(TITLE_TAG_TABLE)+"(TAG_NAME) VALUES (?)");
		
		for(String name : tagNames){			
			name= addSimpleQuotes(name);
			stmtSelect.setString(1,name);
			ResultSet res = stmtSelect.executeQuery();
			if(res.isBeforeFirst()==false){
				stmtInsert.setString(1,name);
				stmtInsert.executeUpdate();
			}			
		}
		close();
		
	}
	
	public static String addDoubleQuotes(String str){
		return '"' + str + '"';
	}
	
	public static String addSimpleQuotes(String str){
		return '\'' + str + '\'';
	}
	
	/**
	 * Return the name of the tag corresponding to the id passed at the parameter
	 * @param idTag the if of the tag (in table tag)
	 * @return tagName <br>
	 * 		   "" (void String) if the tag doesn't exist
	 */
	public static String getTagName(int idTag)
	{
		setup();
		String tagName = "";
		if (idTag<0){
			return tagName;
		}
		String sql="SELECT tag_name FROM" + addDoubleQuotes(TITLE_TAG_TABLE) + "WHERE id_tag="+idTag;
		PreparedStatement tagNameStmt = null;
		try {
			tagNameStmt = databaseConnection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ResultSet rs = null;
		try {
			rs = tagNameStmt.executeQuery();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			if (rs.next())
			{
				tagName = rs.getString("tag_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		close();
		if (tagName != ""){
			return tagName.substring(1, tagName.length()-1);
		}
		return "";
		
	}
	
	
	public static int getTagId(String tagName)
	{
		setup();
		String sqlSelectTag = "SELECT ID_TAG FROM "
				+ DatabaseManager.addDoubleQuotes(DatabaseManager.TITLE_TAG_TABLE) + " WHERE tag_name = ? ";
		int idTag = -1;

		try {
			PreparedStatement stmtSelectTag = DatabaseManager.databaseConnection.prepareStatement(sqlSelectTag);
			stmtSelectTag.setString(1, addSimpleQuotes(tagName));
			ResultSet resSelectTag = stmtSelectTag.executeQuery();
			
			if (resSelectTag.next()) {

				idTag = resSelectTag.getInt("ID_TAG");
			}
			stmtSelectTag.close();
			resSelectTag.close();
		} catch (SQLException e) {
			System.out.println("getTagId (DatabaseManager) - Probl�me lors de l'ex�cution de la requ�te sql");
			e.printStackTrace();
		}
	
		close();
		return idTag;
		
		
	}
	
	
	/**
	 * Loads the apache derby embedded driver and sets up database connection
	 */
	public static void setup() {		
		
		System.getProperty("user.dir");
		Class driverClass = null;
		try {
			// Loading of embedded driver for the database	
			driverClass = Class.forName(EMBEDDED_DRIVER_PACKAGE);			
		} 
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		try {
			DriverManager.registerDriver((Driver)driverClass.newInstance());
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Database connection	
		try {
			databaseConnection = DriverManager.getConnection("jdbc:derby:" + DATABASE_PATH);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Closes database connection
	 */
	public static void close(){
		if (databaseConnection != null){
			try{
				databaseConnection.close();
			} catch (SQLException e)
			{
				e.getMessage();
			}
			
		}
		
	}	
	
	
	/**	
	 * Delete data from the table passed as parameter
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static int truncateTable(String tableName) throws SQLException
	{
		Statement statement = databaseConnection.createStatement();
		int result = statement.executeUpdate("TRUNCATE TABLE " + addDoubleQuotes(tableName));
		databaseConnection.commit();
		return result;
	}
	
	/**
	 * SELECT columnName FROM table
	 * @param columnName
	 * @param table
	 * @return Result of SELECT columnName FROM table
	 */
	
	public static ResultSet selectStatement (String columnName, String table) throws SQLException
	{
		PreparedStatement selectPrepStatement = databaseConnection.prepareStatement("SELECT "+columnName+" FROM "+table);
		return selectPrepStatement.executeQuery();
	}


	/**
	 * this function verifies if the tag exists in the TagTable or not
	 * 
	 * @param tagName
	 * @return boolean meaning if the tag is in the database or not
	 * @throws SQLException
	 */
	public static boolean tagExist(String tagName){
		return getTagId(tagName)>=0;
	}

	// M�thode main pour test
	public static void main(String[] args) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, JSONException, IOException, SQLException, URISyntaxException

	{
		String tagName = "\'c\'";
		System.out.println(tagName.substring(1, tagName.length()-1));

	}
}