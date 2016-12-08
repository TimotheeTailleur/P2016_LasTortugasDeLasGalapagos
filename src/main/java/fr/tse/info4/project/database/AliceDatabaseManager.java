package fr.tse.info4.project.database;

import java.sql.SQLException;

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
	

	
	public static void main(String[] args){
		
		
		
	}
}
