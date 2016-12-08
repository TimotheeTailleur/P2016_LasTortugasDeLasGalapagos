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

}
