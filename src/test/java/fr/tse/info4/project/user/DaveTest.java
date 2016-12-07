package fr.tse.info4.project.user;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.google.code.stackexchange.schema.User;

import fr.tse.info4.project.schema.TagScore;

public class DaveTest {

	@Test
	public void getTopAnswerersTest(){
		Dave user = new Dave();
		// Test with a tag which doesn't existed
		assertEquals(user.getTopAnswerers("dsqbhkj"), null);
	}
	
	@Test
	public void getTopTagTest(){
		Dave user = new Dave();
		
		assertEquals(user.getTopTag("dsqkjb"), null);
	}

	@Test
	public void containsTest(){
		Dave user = new Dave();
		ArrayList<TagScore> tagsScores = new ArrayList<TagScore>(2);
		TagScore tag1 = new TagScore();
		TagScore tag2 = new TagScore();
		User user1 = new User();
		user1.setUserId(1);
		User user2 = new User();
		user2.setUserId(2);
		tag1.setUser(user1);
		tag2.setUser(user2);
		
		tagsScores.add(tag1);
		tagsScores.add(tag2);
		
		assertTrue(user.contains(tagsScores, 1));
		assertFalse(user.contains(tagsScores, 3));
		
	}
}
