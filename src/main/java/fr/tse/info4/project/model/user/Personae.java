package fr.tse.info4.project.model.user;


import fr.tse.info4.project.model.database.DatabaseManager;
import fr.tse.info4.project.model.datarecovery.ApiManager;

public abstract class Personae {

	/**
	 * Database manager.
	 */
	protected DatabaseManager databaseManager;
	
	/**
	 * Api manager : object used to handle the data recovery.
	 */
	protected ApiManager apiManager;
	
	/**
	 * Id of the user.
	 */
	protected int idUser = 1;
	
	/**
	 * Application user access token. <br> Default value : null
	 */
	protected String accessToken = null;
	
	
	// Getter and setter 
	
	public String getAccesToken() {
		return accessToken;
	}

	public void setAccesToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public void setIdUser(int idUser){
		if (idUser >1)
			this.idUser = idUser;
	}
	
	public int getIdUser(){
		return idUser;
	}
	
	/**
	 * Returns Stack Overflow profile URL of user (id)
	 */
	public static String getLink(int id) {
		return ("stackoverflow.com/u/" + id);
	}
	
	/**
	 * Returns Stack Overflow URL of question (id) 
	 */
	public static String getLinkQuestion(int id) {
		return ("stackoverflow.com/q/" + id);
	}
	
	/**
	 * Returns Stack Overflow URL of answer (id) 
	 */
	public static String getLinkAnswer(int id) {
		return ("stackoverflow.com/a/" + id);
	}

}
