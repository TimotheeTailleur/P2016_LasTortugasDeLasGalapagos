package tse.info4.project.database;
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
import java.util.Map.Entry;
import java.util.TreeMap;
import org.json.JSONException;
import org.apache.commons.lang.StringUtils;
import tse.info4.project.datarecovery.StackExchangeApiManager;

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
	private static final String EMBEDDED_DRIVER_PACKAGE = "org.apache.derby.jdbc.EmbeddedDriver";
	
	/**
	 * Relative path to the derby database repository
	 */
	private static final String DATABASE_PATH = "BDDProjetInfo";
	
	/**
	 * Name of the table in which all post counts for each user are contained 
	 */
	public static final String TITLE_TAG_POST_TABLE = "TagPostCount";
	
	/**
	 * Name of the table in which all scores for each user are contained 
	 */
	public static final String TITLE_TAG_SCORE_TABLE = "TagScore";

	/**
	 * Number of tags treated
	 */
	private static final  int NB_TAG = 30;
	
	//Other members
	/**
	 * List of tags from Stack Overflow
	 */
	private static ArrayList<String> tagNames = new ArrayList<String>();
	
	/**
	 * List of tags for which special treatment is required
	 */
	private static ArrayList<String> listOfTagExceptions = new ArrayList<String>();
	
	/**
	 * Connection item to manipulate the database
	 */
	public static Connection databaseConnection;
	
	
	//Methods
	
	/**
	 * Creates the list of tags that need to be between double quotes in SQL statements
	 */
	public static void createListExceptionTag(){
		listOfTagExceptions.add("c#");
		listOfTagExceptions.add("c++");
		listOfTagExceptions.add("sql");
		listOfTagExceptions.add("asp.net");
		listOfTagExceptions.add("objective-c");
		listOfTagExceptions.add("ruby-on-rails");
		listOfTagExceptions.add(".net");
		listOfTagExceptions.add("sql-server");
		listOfTagExceptions.add("xml");
		listOfTagExceptions.add("node.js");
		listOfTagExceptions.add("asp.net-mvc");
	}
	/**
	 * Getter for listOfTagExceptions
	 */
	public static ArrayList<String> getListOfTagExceptions() {
		return listOfTagExceptions;
	}
	
	/**
	 * Loads the apache derby embedded driver and sets up database connection
	 */
	public static void setup() throws JSONException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		// Loading of embedded driver for the database				
		Class driverClass = Class.forName(EMBEDDED_DRIVER_PACKAGE);
		DriverManager.registerDriver((Driver)driverClass.newInstance());
		// Database connection	
		databaseConnection = DriverManager.getConnection("jdbc:derby:" + DATABASE_PATH);
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
	 * Updates tables TagPostCount and TagScore.
	 * If a user's id isn't found in the database, creates new entries in both tables
	 * @throws JSONException
	 * @throws IOException
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws URISyntaxException
	 */
	public static void fillTablesTagPostCount_Score() throws JSONException, IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, URISyntaxException
	{
		
		createListExceptionTag();
		// Tag list creation
		tagNames=StackExchangeApiManager.getTags();
		
		// Data Insertion or updtating
		TreeMap<Integer,ArrayList<Integer>> resAns = new TreeMap<>();
		String name = "";
		
		for (int i=0;i<NB_TAG;i++)
		{
			//Get name of tag, score and post count
			name=tagNames.get(i);	//get tagName
			resAns=StackExchangeApiManager.getTopAnswerers(name);	// get userID and post_count and score corresponding for this tag
			
			if(listOfTagExceptions.contains(name)){
				name='"'+name+'"'; 	//need for sql to add "" to the word
			}
			
			// Construct the preparedStatement for Update
			PreparedStatement stmtUpdatePost = databaseConnection.prepareStatement("UPDATE "+TITLE_TAG_POST_TABLE+" SET "+name+" = ? WHERE Users_idUsers = ?");
			PreparedStatement stmtUpdateScore = databaseConnection.prepareStatement("UPDATE "+TITLE_TAG_SCORE_TABLE+" SET "+name+" = ? WHERE Users_idUsers = ?");
			
			/*
			 * Set variables of the prepared statements 
			 * Update the two tables 
			 * If the user's ID doesn't exist in either table insert data  		
			 */
			for (Entry<Integer, ArrayList<Integer>> userEntry : resAns.entrySet()){
				stmtUpdatePost.setInt(1,userEntry.getValue().get(1));		//PostCount for userId and tag concerned
				stmtUpdatePost.setInt(2,userEntry.getKey());				//userId
				stmtUpdateScore.setInt(1,userEntry.getValue().get(0));		//Score for userId and tag concerned
				stmtUpdateScore.setInt(2,userEntry.getKey());				//userId
				
		
				if(stmtUpdatePost.executeUpdate()==0)
				{
					PreparedStatement stmtUpdatePost2 = databaseConnection.prepareStatement("INSERT INTO "+TITLE_TAG_POST_TABLE+" (Users_idUsers,"+name+") VALUES (?,?)");
					
					stmtUpdatePost2.setInt(1,userEntry.getKey());
					stmtUpdatePost2.setInt(2,userEntry.getValue().get(1));
					stmtUpdatePost2.executeUpdate();
				}
				if(stmtUpdateScore.executeUpdate()==0)
				{
					PreparedStatement stmtUpdateScore2 = databaseConnection.prepareStatement("INSERT INTO "+TITLE_TAG_SCORE_TABLE+" (Users_idUsers,"+name+") VALUES (?,?)");		
					stmtUpdateScore2.setInt(1,userEntry.getKey());
					stmtUpdateScore2.setInt(2,userEntry.getValue().get(0));
					stmtUpdateScore2.executeUpdate();
				}
			}
		}	
	}
	
	/**
	 * SELECT * FROM table
	 * @param table
	 * @return Result of query SELECT * FROM table
	 * @throws SQLException
	 */
	
	public static ResultSet selectStatement(String table) throws SQLException {
		PreparedStatement selectPrepStatement = databaseConnection.prepareStatement("SELECT * FROM "+table);
		return selectPrepStatement.executeQuery();
	}
	
	
	/**
	 * SELECT columns FROM table
	 * @param columns ArrayList of columns names that you want to return
	 * @param table
	 * @return Result of SELECT columns FROM table
	 * @throws SQLException
	 */
	
	public static ResultSet selectStatement(ArrayList<String> columns,String table) throws SQLException{
		Integer size=columns.size();
		String statementVariables = StringUtils.repeat("?,",size);
		PreparedStatement selectPrepStatement = databaseConnection.prepareStatement("SELECT"+statementVariables+"? FROM "+table);
		int i=1;
		for (String s : columns)
		{
			selectPrepStatement.setNString(i, s);
			i++;
		}
		return selectPrepStatement.executeQuery();
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
		int result = statement.executeUpdate("TRUNCATE TABLE " + tableName);
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
	
	//Méthode main pour test
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, JSONException, IOException, SQLException, URISyntaxException 
	{		
					
		DatabaseManager.setup();
		truncateTable(TITLE_TAG_POST_TABLE);
		truncateTable(TITLE_TAG_SCORE_TABLE);
		
		
		DatabaseManager.fillTablesTagPostCount_Score();
		ResultSet postCountQuery = DatabaseManager.selectStatement(TITLE_TAG_POST_TABLE);
		System.out.println("Fin d'exécution");	
	}
}
