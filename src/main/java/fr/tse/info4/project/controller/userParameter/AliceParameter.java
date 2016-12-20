package fr.tse.info4.project.controller.userParameter;

import fr.tse.info4.project.model.user.Alice;

public class AliceParameter {
	private Alice alice;
	
	public AliceParameter(){
		alice = new Alice();
	}
	
	public AliceParameter(String accessToken){
		alice = new Alice(accessToken);
	}
	
	public AliceParameter withNbAnwsers(int nbAnswers){
		alice.setNbAnswers(nbAnswers);
		return this;
	}
	
	public AliceParameter withNbQuestions(int nbQuestions){
		alice.setNbQuestionsPerTag(nbQuestions);
		return this;
	}
	
	public AliceParameter withNbTags(int nbTags){
		alice.setNbTags(nbTags);
		return this;
	}
	
	public AliceParameter withId(int id){
		alice.setIdUser(id);
		return this;
	}
	
	public Alice get(){
		return alice;
	}
}
