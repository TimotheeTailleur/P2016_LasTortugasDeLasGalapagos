package main;


import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import org.json.JSONException;

import java.util.Map.Entry;

import tse.info4.project.database.DatabaseManager;
import tse.info4.project.user.dave.Alice;
import tse.info4.project.user.dave.Dave;

public class Main {

	public static int menu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("");
		System.out.println("Iteration n2 -- Menu");
		System.out.println("0 - exit \n1 - Dave \n2 - Alice ");
		System.out.print("Votre choix : ");
		int res = sc.nextInt();
		return res;
	}

	public static int menuDave() {
		Scanner sc = new Scanner(System.in);
		System.out.println("");
		System.out.println("Iteration n2 -- Menu -- Dave");
		System.out
				.println("0 - retour \n1 - Mise à jour des données \n2 - Top Tag \n3 - Top 10 Contributeurs tag donné");
		System.out.print("Votre choix : ");
		int res = sc.nextInt();
		return res;
	}

	public static int menuAlice() {
		Scanner sc = new Scanner(System.in);
		System.out.println("");
		System.out.println("Iteration n2 -- Menu -- Alice");
		System.out.println(
				"0 - retour \n1 - Nouvelles questions dans compétences \n2 - Questions auxquelles vous avez répondues triées");
		System.out.print("Votre choix : ");
		int res = sc.nextInt();
		return res;
	}

	public static void main(String[] args) throws JSONException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, URISyntaxException {

		DatabaseManager.close();
		boolean quit = false;
		boolean quit2 = false;
		boolean quit3 = false;
		int choix;
		int choixDave;
		int choixAlice=0;
		Scanner mainScanner = new Scanner(System.in);
		DatabaseManager.setup();
		boolean dataMAJ = false;
		int nbDays = 0;
		int nbHours = 0;
		while (!quit) {
			choix = menu();
			switch (choix) {
			case 0:
				mainScanner.close();
				DatabaseManager.close();
				quit = true;
				break;
			case 1:
				while (!quit2) {
					choixDave = menuDave();
					switch (choixDave) {
					case 0:
						quit2 = true;
						break;
					case 1:
						System.out.println("Voulez-vous forcer la mise à jour des données ? (o/n)");
						String resMAJ = mainScanner.nextLine();
						if (resMAJ.equals("o")) {
							dataMAJ = true;
						} else {
							System.out.println("De combien de jours au maximum autorisez-vous les données à dater ?");
							nbDays = Integer.parseInt(mainScanner.nextLine());
						}
						break;
					case 2:
						System.out.println("Rentrez le nom du Tag à chercher :");
						String tagNameTopTag = mainScanner.nextLine();
						while (!DatabaseManager.tagExist(tagNameTopTag))
						{
							System.out.println("Veuillez entrer un nom de Tag valide");
							tagNameTopTag = mainScanner.nextLine();
						}
						System.out.print("Top Tag : ");
						System.out.println(Dave.getLink(Dave.getTopTag(DatabaseManager.addSimpleQuotes(tagNameTopTag), nbDays, dataMAJ)));
						break;
					case 3:
						System.out.println("Rentrez le nom du Tag à chercher :");
						String tagNameContributeurs = mainScanner.nextLine();
						while (!DatabaseManager.tagExist(tagNameContributeurs))
						{
							System.out.println("Veuillez entrer un nom de Tag valide");
							tagNameContributeurs = mainScanner.nextLine();
						}
						System.out.println("Top 10 answerers : ");
						ArrayList<ArrayList<Integer>> resTopContributor = Dave.getTopAnswerers(tagNameContributeurs, 10,
								nbDays, dataMAJ);
						int i = 0;
						for (ArrayList<Integer> intList : resTopContributor) {
							System.out.println(Dave.getLink(intList.get(i)));
						}
						break;
					}
				}
				break;
			case 2:
				Alice alice=new Alice(1200);
				while (!quit3) {
					choixAlice = menuAlice();
					switch (choixAlice) {
					case 0:
						quit3 = true;
						break;
					case 1:
						TreeMap<String, TreeMap<Integer, String>> res = alice.getNewQuestions();
						for (Entry<String, TreeMap<Integer, String>> tagEntry : res.entrySet()) {
							String tagName = tagEntry.getKey();
							System.out.println("\n Nouvelles questions pour le tag " + tagName + " : ");
							TreeMap<Integer, String> questionMap = tagEntry.getValue();
							for (Entry<Integer, String> questionEntry : questionMap.entrySet()) {
								int idQuestion = questionEntry.getKey();
								String questionTitle = questionEntry.getValue();
								System.out.println("stackoverflow.com/q/" + idQuestion + " , " + questionTitle);
							}
						}
						break;
					case 2:
						System.out.println("Voulez-vous forcer la mise à jour des données ? (o/n)");
						String questionMAJ = mainScanner.nextLine();
						if (questionMAJ.equals("o")) {
							dataMAJ = true;
						} else {
							System.out.println("De combien d'heures au maximum autorisez-vous les questions à dater ?");
							nbHours = Integer.parseInt(mainScanner.nextLine());
						}
						int nbQuestions=0;
						System.out.println("Combien de questions voulez-vous afficher ?");
						nbQuestions = Integer.parseInt(mainScanner.nextLine());
						ArrayList<TreeMap<String, Integer>> questionList = alice.sortQuestions(nbQuestions, nbHours,
								dataMAJ);
						for (TreeMap<String, Integer> questionData : questionList) {
							for (Entry<String, Integer> questionEntry : questionData.entrySet()) {
								String idQuestion = questionEntry.getKey();
								Integer score = questionEntry.getValue();
								System.out.println("\n " + idQuestion + " avec un score de " + score.toString());
							}
						}
						break;
					}
				}
				break;
			}
		}
	}
}
