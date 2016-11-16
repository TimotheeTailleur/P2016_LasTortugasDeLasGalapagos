package tse.info4.project.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;

import tse.info4.project.datarecovery.StackExchangeApiManager;
import tse.info4.project.user.dave.Alice;

/**
 * This class will manage the database tables used in functions implemented for the Alice User Story
 *
 */

public class AliceDatabaseManager extends DatabaseManager {

	

	/**
	 * 
	 */
	public static void fillTableBadge(){
		try {
			databaseConnection.prepareStatement("SELECT *");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @throws IOException 
	 * @throws JSONException 
	 * 
	 */
	public static void fillTableQuestion(Alice user){
		setup();
		
		int idUser = user.getUserId();
		//

		TreeMap<Integer, Integer> posts = new TreeMap<Integer, Integer>();
		
		try {
			posts = StackExchangeApiManager.getQuestionsIdsAndScores(idUser);
		} catch (JSONException e1) {
			System.out.println("fillTableQuestion (AliceDatabaseManager) - Erreur lors du parsing du fichier Jsons");
			e1.printStackTrace();
		} catch (IOException e1) {
			System.out.println("fillTableQuestion (AliceDatabaseManager) - Erreur lors de la connexion à l'api ");
			e1.printStackTrace();
		}
		
		
		String sqlInsertQuestions = "INSERT INTO " + addDoubleQuotes(TITLE_QUESTIONS_TABLE) + "(ID_USER, ID_QUESTION, SCORE) VALUES (?, ?, ?)";
		String sqlInsertUSer = "INSERT INTO " + addDoubleQuotes(TITLE_USERS_TABLE) + "(ID_USER, LAST_UPDATE_QUESTIONS) VALUES (?, CURRENT_DATE)";
		String sqlUpdateQuestions = "UPDATE " + addDoubleQuotes(TITLE_QUESTIONS_TABLE) + " SET SCORE = ? WHERE ID_USER = ? AND ID_QUESTION = ?";
		String sqlUpdateUser = "UPDATE " + addDoubleQuotes(TITLE_USERS_TABLE) + " SET LAST_UPDATE_QUESTIONS = CURRENT_DATE WHERE ID_USER = ?";
		PreparedStatement stmtInsertQuestions = null;
		PreparedStatement stmtInsertUser = null;
		PreparedStatement stmtUpdateQuestions = null;
		PreparedStatement stmtUpdateUser = null;
		try {
			stmtInsertQuestions = databaseConnection.prepareStatement(sqlInsertQuestions);
			stmtInsertUser = databaseConnection.prepareStatement(sqlInsertUSer);
			stmtUpdateQuestions = databaseConnection.prepareStatement(sqlUpdateQuestions);
			stmtUpdateUser = databaseConnection.prepareStatement(sqlUpdateUser);
			
		} catch (SQLException e) {
			System.out.println("fillTableQuestion (AliceDatabaseManager) - Erreur lors de la création des prepareStatement");
			e.printStackTrace();
		}
		
		for (Map.Entry<Integer, Integer> entry : posts.entrySet()){
			
			try {
				
				stmtUpdateQuestions.setInt(1, entry.getValue());
				stmtUpdateQuestions.setInt(2, idUser);
				stmtUpdateQuestions.setInt(3, entry.getKey());

			
			// If a users's id isn't found in questions table:  execute insertion prepared statements
			if (stmtUpdateQuestions.executeUpdate() == 0){
				
				// Insertion of the question in the question table
				
				stmtInsertQuestions.setInt(1, idUser);
				stmtInsertQuestions.setInt(2, entry.getKey());
				stmtInsertQuestions.setInt(3, entry.getValue());
				stmtInsertQuestions.executeUpdate();		
				
			}
			
			stmtUpdateUser.setInt(1, idUser);
			
			String sql= "UPDATE \"users\" SET LAST_UPDATE_QUESTIONS = CURRENT_DATE WHERE ID_USER = 1200";
			Statement stmt = DatabaseManager.databaseConnection.createStatement();
			// if a user's id isnt'found in user table : 
			//if (stmtUpdateUser.executeUpdate() == 0){
			if (stmt.executeUpdate(sql)==0) {
				stmtInsertUser.setInt(1, idUser);
				stmtInsertUser.executeUpdate();
			}
			
			
				
			} catch (SQLException e) {
				System.out.println("fillTableQuestion (AliceDatabaseManager) - Erreur lors de l'affectation des paramètres du prepareStatement");
				e.printStackTrace();
			}
			
		}
		
	
		try {	
			stmtInsertQuestions.close();
			stmtInsertUser.close();
			stmtUpdateQuestions.close();
			stmtUpdateUser.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * 
	 * @return the time (seconds) between the present time and the last update of the data concerning the user <b>
	 * 		   -1 if the user dosn't exist
	 * @throws SQLException 
	 */
	public static long getTime(Alice user){
		setup();
		int idUser = user.getUserId();
		String sql = "SELECT LAST_UPDATE_QUESTIONS FROM " + addDoubleQuotes(TITLE_USERS_TABLE) + " WHERE ID_USER = ?";
		
		Date lastUpdate = null;
		PreparedStatement stmt;
		try {
			
			stmt = databaseConnection.prepareStatement(sql);
			stmt.setInt(1, idUser);
			ResultSet res = stmt.executeQuery();
			
			if (res.next()){
				lastUpdate = res.getDate("LAST_UPDATE_QUESTIONS");
			}
			
			stmt.close();
			res.close();
			
		} catch (SQLException e) {
			System.out.println("getTime (AliceDatabaseManager) - Erreur de la requête sql.");
			e.printStackTrace();
		}
		
		Date currentDate= new Date(new java.util.Date().getTime());
		
		if (lastUpdate != null){
			return (currentDate.getTime() / 1000) - (lastUpdate.getTime() / 1000);
		}	
		
		return -1;
		
	}
	public static void main(String[] args){
		Alice user = new Alice(1);
		System.out.println(getTime(user));
		
		fillTableQuestion(user);
		
	}
}
