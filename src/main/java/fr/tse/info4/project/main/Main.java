package fr.tse.info4.project.main;



import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import org.json.JSONException;

import fr.tse.info4.project.database.DatabaseManager;
import fr.tse.info4.project.user.Alice;
import fr.tse.info4.project.user.Dave;

import java.util.Map.Entry;

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
				.println("0 - retour \n1 - Mise à jour des données \n2 - Top Tag \n3 - Top 10 Contributeurs tag donné \n4 - Top contributeur pour un ensemble de tags donné");
		System.out.print("Votre choix : ");
		int res = sc.nextInt();
		return res;
	}

	public static int menuAlice() {
		Scanner sc = new Scanner(System.in);
		System.out.println("");
		System.out.println("Iteration n2 -- Menu -- Alice");
		System.out.println(
				"0 - retour \n1 - Nouvelles questions dans vos domaines de compétences \n2 - Questions auxquelles vous avez répondues triées par leur poppularité");
		System.out.print("Votre choix : ");
		int res = sc.nextInt();
		return res;
	}

	public static void main(String[] args) throws JSONException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, URISyntaxException, InterruptedException {

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
					case 4:
						System.out.println("Rentrez un des Tags voulus : \n(-1 pour terminer la saisie)");
						ArrayList<String> tagNameContributeursList = new ArrayList<String>();
						String tagName=mainScanner.nextLine();
						int nbTag=0;
						while (!DatabaseManager.tagExist(tagName))
						{
							System.out.println("Veuillez entrer un nom de Tag valide");
							tagName = mainScanner.nextLine();
						}
						while(!tagName.equals("-1")){
							tagNameContributeursList.add(tagName);
							nbTag=nbTag+1;
							System.out.println("Rentrez un des Tags voulus : \n(-1 pour terminer la saisie)");
							tagName=mainScanner.nextLine();
							while (!DatabaseManager.tagExist(tagName) && !tagName.equals("-1"))
							{
								System.out.println("Veuillez entrer un nom de Tag valide");
								tagName = mainScanner.nextLine();
							}
						}
						ArrayList<TreeMap<String, Integer>> resContributor = Dave.getTopTag(tagNameContributeursList,nbDays,dataMAJ);
						
						TreeMap<String, Integer> dataContibutor=resContributor.get(0);
						
						Integer idUser=dataContibutor.get("userId");
						Integer nbPostCount=dataContibutor.get("totalPostCount");
						
						System.out.println("Le top contributeur pour la liste entrée est ");
						System.out.println(Dave.getLink(idUser)+" avec un nombre de post total de "+nbPostCount+".");
						
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
								System.out.println(Alice.getLinkQuestion(idQuestion)+ " , " + questionTitle);
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
						ArrayList<TreeMap<String, Integer>> questionList = alice.getSortedAnsweredQuestions(nbQuestions, nbHours,
								dataMAJ);
						for (int i=0;i<questionList.size();i++){
							Integer idQuestion = questionList.get(i).get("idQuestion");
							Integer score = questionList.get(i).get("score");
							System.out.println(Alice.getLinkQuestion(idQuestion)+ " avec un score de " + score);
						}
						break;
					}
				}
				break;
			}
		}
	}
}
