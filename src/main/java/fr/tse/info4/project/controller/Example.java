package fr.tse.info4.project.controller;

import java.util.Set;

import com.google.code.stackexchange.schema.User;

import fr.tse.info4.project.model.user.Bob;

public class Example {
	public static void main(String[] args) {
		UserFactory factory = new UserFactory();
		/*Dave dave = factory.newDave().withNbUsers(20).withForceUpdate().withRefreshRate(3).get();
		System.out.println(dave.getTopTag("c++"));
		
		Alice alice = factory.newAlice().withId(1200).withNbQuestions(1).withNbTags(2).get();
		System.out.println(alice.getNewQuestions()); */
		
		Bob bob = factory.newBob().withId(1200).get();

		Set<User> experts = bob.getExperts();
		for (User expert : experts){
			System.out.println(expert.getUserId() + " " + expert.getDisplayName());
		}
		
	}
}
