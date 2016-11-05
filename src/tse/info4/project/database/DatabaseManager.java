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
import org.json.JSONException;
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
	protected static final String EMBEDDED_DRIVER_PACKAGE = "org.apache.derby.jdbc.EmbeddedDriver";
	
	/**
	 * Relative path to the derby database repository
	 */
	protected static final String DATABASE_PATH = "ProjectDatabase";
	
	/**
	 * Name of the table in which all post counts for each user are contained 
	 */
	public static final String TITLE_TAG_POST_TABLE = "tag_post_count";
	
	/**
	 * Name of the table in which all scores for each user are contained 
	 */
	public static final String TITLE_TAG_SCORE_TABLE = "tag_score";

	
	//Other members
	/**
	 * List of tags from Stack Overflow
	 */
	protected static ArrayList<String> tagNames = new ArrayList<String>();
	
	/**
	 * Connection item to manipulate the database
	 */
	public static Connection databaseConnection;
	
	
	//Methods


	public static void fillTableTags(int start, int end) throws JSONException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		// Remplir la table tag � partir de l'api.
		// L'api limite � 30 appels par secondes et � 10 000 appels par jour (remplissage en plusieurs fois) !
		tagNames = StackExchangeApiManager.getTags(1);
		setup();
		//PreparedStatement stmtSelect = databaseConnection.prepareStatement("SELECT ID_TAG FROM "+'"'+"tag "+'"'+"WHERE TAG_NAME =  ?");
		PreparedStatement stmtInsert = databaseConnection.prepareStatement("INSERT INTO "+'"'+"tag"+'"'+"(TAG_NAME) VALUES ( ?)");
		
		for(String name : tagNames){
			//stmtSelect.setString(1,name);
			name="'"+name+"'";
			String stmtString="SELECT ID_TAG FROM "+'"'+"tag"+'"'+" WHERE TAG_NAME = "+name;
			Statement stmtSelect = DatabaseManager.databaseConnection.createStatement();
			ResultSet res = stmtSelect.executeQuery(stmtString);
			//if(stmtSelect.execute()==0){
			if(res==null){
				stmtInsert.setString(1,name);
				stmtInsert.executeUpdate();
			}			
		}
		close();
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
	
	
	//M�thode main pour test
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, JSONException, IOException, SQLException, URISyntaxException 
	{
		fillTableTags(0, 100);
	}
}
