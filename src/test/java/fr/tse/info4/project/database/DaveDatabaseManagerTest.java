package fr.tse.info4.project.database;

import static org.junit.Assert.*;

import org.junit.Test;

public class DaveDatabaseManagerTest {

	@Test
	public void getTimeUpdateTopTagTest(){
		DatabaseManager.setup();
		
		// This test can fail if the user update data for the tag 458 ('character-encoding').
		assertEquals(DaveDatabaseManager.getTimeUpdateTopTag(458), -1); 
		DatabaseManager.close();
	}

}
