package tse.info4.project.user.dave;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONException;

import tse.info4.project.database.DatabaseManager;
import tse.info4.project.datarecovery.StackExchangeApiManager;

/**
 * 
 * Fonctions and demo main method for user story Dave
 *
 */
public class Dave {

	/**
	 * Returns the top (numberOfUsers) Answerers in given tag
	 * 
	 * @param tag
	 * @param numberOfUsers
	 * @return ResultSet to manipulate data
	 */

	public static ResultSet getTopAnswerers(String tag, int numberOfUsers)  {
		try {
			DatabaseManager.setup();
			DatabaseManager.createListExceptionTag();
			tag = tag.toLowerCase();
			if (DatabaseManager.getListOfTagExceptions().contains(tag)) {
				tag = '"'+tag+'"';
			}
			String stmtString = "SELECT * FROM TagPostCount WHERE " + tag + " IS NOT NULL ORDER BY " + tag
					+ " desc OFFSET 0 " + " ROWS FETCH NEXT "
					+ Integer.toString(numberOfUsers) + " ROWS ONLY";

			Statement stmt = DatabaseManager.databaseConnection.createStatement();
			ResultSet res = stmt.executeQuery(stmtString);
			return res;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
 	* Get top tag user id in given tag
	 * @param tag
	 * @return Top tag's user ID
	 */
	public static int getTopTag(String tag){
		try {
			DatabaseManager.setup();
			DatabaseManager.createListExceptionTag();
			tag = tag.toLowerCase();
			if (DatabaseManager.getListOfTagExceptions().contains(tag))
			{
				tag='"'+tag+'"';
			}
			String stmtString="SELECT Users_idUsers FROM TagScore WHERE "+tag+" =(SELECT max("+tag+") FROM TagScore)";
			Statement stmt = DatabaseManager.databaseConnection.createStatement();
			ResultSet res = stmt.executeQuery(stmtString);
			if (res.next())
			{
				return res.getInt("Users_idUsers");
			}
			return -1;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return -1;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return -1;
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		} 
		}

	/**
	 * Returns Stack Overflow profile URL of user (id)
	 * @param id
	 * @return Stack Overflow profile URL
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String getLink(int id) {
		return ("stackoverflow.com/u/"+id);
	}
	
	public static int menu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("");
		System.out.println("Iteration n1 -- Menu");
		System.out.println("0 - Mise a jour BDD \n1 - Top Tag \n2 - Top 10 Contributeurs tag donné \n3 - exit");
		System.out.print("Votre choix : ");
		int res = sc.nextInt();
		return res;
	}

	// Méthode main pour démo
	public static void main(String[] args) throws SQLException, IOException, JSONException, InstantiationException, IllegalAccessException, ClassNotFoundException, URISyntaxException {
		DatabaseManager.close();
		boolean quit=false;
		int choix;
		Scanner mainScanner = new Scanner(System.in);
		ArrayList<String> tagList = StackExchangeApiManager.getTags();
		DatabaseManager.setup();
		while (!quit)
		{
			choix=menu();
			switch(choix) {
			case 0:
				DatabaseManager.fillTablesTagPostCount_Score();
				System.out.println("Données actualisées");
				choix=menu();
			case 1:
				System.out.println("Rentrez le nom du Tag à chercher :");
				String tagNameTopTag = mainScanner.nextLine();
				while ( !tagList.contains(tagNameTopTag))
				{
					System.out.println("Veuillez entrer un nom de Tag valide");
					tagNameTopTag = mainScanner.nextLine();
				}
				System.out.print("Top Tag : ");
				System.out.println(getLink(getTopTag(tagNameTopTag)));
				choix=menu();
			case 2:
				System.out.println("Rentrez le nom du Tag à chercher :");
				String tagNameContributeurs = mainScanner.nextLine();
				while ( !tagList.contains(tagNameContributeurs))
				{
					System.out.println("Veuillez entrer un nom de Tag valide");
					tagNameContributeurs = mainScanner.nextLine();
				}
				System.out.println("Top 10 answerers : ");
				ResultSet resTopContributor = getTopAnswerers(tagNameContributeurs, 10);
				while (resTopContributor.next()) {
					System.out.println(getLink(resTopContributor.getInt("Users_idUsers")));
				}
				choix=menu();
			case 3:
				mainScanner.close();
				DatabaseManager.close();
				quit=true;
			}
		}
	}

}
