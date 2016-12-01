package fr.tse.info4.project.database;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

public class DatabaseManagerTest {

	@Test
	public void tagExistTest(){
		assertTrue(DatabaseManager.tagExist("java"));
		assertFalse(DatabaseManager.tagExist("azesaz"));		
	}
	
	@Test
	public void getTagIdTest(){
		assertEquals(DatabaseManager.getTagId("c++"), 8);
		assertEquals(DatabaseManager.getTagId("javascript"), 0);
		assertEquals(DatabaseManager.getTagId("kjsbqs"), -1);
	}
	
	@Test
	public void getTagNameTest(){
		assertEquals(DatabaseManager.getTagName(17), "c");
		assertEquals(DatabaseManager.getTagName(-1), "");
		assertEquals(DatabaseManager.getTagName(50_000), "");
		
	}
	

}
