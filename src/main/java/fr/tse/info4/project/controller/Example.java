package fr.tse.info4.project.controller;

import fr.tse.info4.project.model.datarecovery.Authenticate;
import fr.tse.info4.project.model.user.Alice;
import fr.tse.info4.project.model.user.Bob;
import fr.tse.info4.project.model.user.Dave;

public class Example {
	public static void main(String[] args) {
		UserFactory factory = new UserFactory();
		Dave dave = factory.newDave().withNbUsers(20).withForceUpdate().withRefreshRate(3).get();
		System.out.println(dave.getTopTag("c++"));
		
		Alice alice = factory.newAlice().withId(1200).withNbQuestions(1).withNbTags(2).get();
		System.out.println(alice.getNewQuestions());
		
		UserFactory factory2 = new UserFactory(Authenticate.getAcessToken());
		Bob bob = factory2.newBob().withNbQuestions(3).get();
		System.out.println(bob.getNewQuestionsAnswered());
		
	}
}
