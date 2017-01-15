package fr.tse.info4.project.controller;

import java.util.List;
import java.util.Set;

import javax.swing.JTextField;

import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.User;

public class BobMethod {
	
	/**
	 * Test if the Set of user is empty or not.
	 * @param users
	 * @return boolean
	 */
	public static boolean hasExpert(Set<User> users){
		boolean bool = true;
		if(users.size() == 0){
			bool = false;
		}
		return bool;
	}
	
	/**
	 * Test if list is empty or not.
	 * @param questions
	 * @return boolean
	 */
	public static boolean hasQuestion(List<Question> questions){
		boolean bool = true;
		if(questions.size() == 0){
			bool = false;
		}
		return bool;
	}
	
	/**
	 * Test if list empty or not
	 * @param keyWords
	 * @return boolean
	 */
	public static boolean hasKeyWords(List<String> keyWords){
		boolean bool = true;
		if(keyWords.size() == 0){
			bool = false;
		}
		return bool;
	}
	
	/**
	 * Test if a JTextField is empty or not.
	 * @param text
	 * @return boolean object
	 */
	public static boolean isEmpty(JTextField text){		
		return text.getText().isEmpty();
	}

}
