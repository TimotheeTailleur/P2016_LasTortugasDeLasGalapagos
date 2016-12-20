package fr.tse.info4.project.controller.userParameter;

import fr.tse.info4.project.model.user.Bob;

public class BobParameter {
	private Bob bob;
	
	public BobParameter(){
		bob = new Bob();
	}
	
	public BobParameter(String accesToken){
		bob  = new Bob(accesToken);
	}
	
	public BobParameter withNbQuestions(int nbQuestions){
		bob.setNbQuestionsPerTag(nbQuestions);
		return this;
	}
	
	public BobParameter withNbSimilarQuestions(int nbSimilarQuestions){
		bob.setNbSimilarQuestions(nbSimilarQuestions);
		return this;
	}
	
	public BobParameter withNbTags(int nbTags){
		bob.setNbTags(nbTags);
		return this;
	}
	
	public BobParameter withId(int id){
		bob.setIdUser(id);
		return this;
	}
	
	public Bob get(){
		return bob;
	}
	
}
