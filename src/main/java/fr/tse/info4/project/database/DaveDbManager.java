package fr.tse.info4.project.database;

public class DaveDbManager extends DbManager {

	public DaveDbManager(){
		super();
	}
	

	
	public static void main(String[] args) {
		DbManager manager = new DaveDbManager();
		((DaveDbManager)manager).a();
	}
}
