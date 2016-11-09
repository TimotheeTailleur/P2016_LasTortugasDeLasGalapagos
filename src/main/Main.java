package main;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

import tse.info4.project.database.DatabaseManager;
import tse.info4.project.datarecovery.StackExchangeApiManager;

public class Main {
	public static void main(String[] args) {
		
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
