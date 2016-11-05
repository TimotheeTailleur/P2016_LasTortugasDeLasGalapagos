package tse.info4.project.database;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.json.JSONException;

import tse.info4.project.datarecovery.StackExchangeApiManager;

public class DaveDatabaseManager {

	
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
	public static void fillTableTagPostCount(int idTag) throws JSONException, IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, URISyntaxException
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
	
	public static void fillTableTagScore(int idTag)
	{
		
	}

}
