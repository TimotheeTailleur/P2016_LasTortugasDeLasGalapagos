package fr.tse.info4.project.controller;

import java.util.Set;

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

}
