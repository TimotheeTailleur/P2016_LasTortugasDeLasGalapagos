package fr.tse.info4.project.main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.json.JSONException;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Question;

import fr.tse.info4.project.model.database.DatabaseManager;
import fr.tse.info4.project.model.schema.TagScore;
import fr.tse.info4.project.model.user.Alice;
import fr.tse.info4.project.model.user.Bob;
import fr.tse.info4.project.model.user.Dave;

import java.util.Map.Entry;

public class Main {

	public static int menu() {
		Scanner sc = new Scanner(System.in);
		System.out.println("");
		System.out.println("Iteration n2 -- Menu");
		System.out.println("0 - exit \n1 - Alice \n2 - Bob \n3 - Dave");
		System.out.print("Votre choix : ");
		int res = sc.nextInt();
		return res;
	}

	public static int menuDave() {
		Scanner sc = new Scanner(System.in);
		System.out.println("");
		System.out.println("Iteration n2 -- Menu -- Dave");
		System.out.println(
				"0 - retour \n1 - Top Tag \n2 - Top 10 Contributeurs tag donné \n3 - Top contributeur pour un ensemble de tags donné");
		System.out.print("Votre choix : ");
		int res = sc.nextInt();
		return res;
	}

	public static int menuAlice() {
		Scanner sc = new Scanner(System.in);
		System.out.println("");
		System.out.println("Iteration n2 -- Menu -- Alice");
		System.out.println(
				"0 - retour \n1 - Nouvelles questions dans vos domaines de compétences \n2 - Questions auxquelles vous avez répondues triées par leur popularité");
		System.out.print("Votre choix : ");
		int res = sc.nextInt();
		return res;
	}

	public static int menuBob() {
		Scanner sc = new Scanner(System.in);
		System.out.println("");
		System.out.println("Iteration n2 -- Menu -- Bob");
		System.out.println(
				"0 - retour \n1 - Afficher les nouvelles questions (sans accessToken) \n2 - Afficher les nouvelles questions (avec accessToken)");
		System.out.print("Votre choix : ");
		int res = sc.nextInt();
		return res;
	}

	public static void main(String[] args) throws JSONException, IOException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException, URISyntaxException, InterruptedException {

		DatabaseManager.close();
		boolean quit = false;
		boolean quit2 = false;
		boolean quit3 = false;
		boolean quit4 = false;
		int choix;
		int choixDave;
		int choixAlice = 0;
		int choixBob;
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
				Alice alice = new Alice();
				while (!quit3) {
					choixAlice = menuAlice();
					switch (choixAlice) {
					case 0:
						quit3 = true;
						break;
					case 1:
						Map<String, PagedList<Question>> res = alice.getNewQuestions(1200);
						for (Entry<String, PagedList<Question>> tagEntry : res.entrySet()) {
							String tagName = tagEntry.getKey();
							System.out.println("\n Nouvelles questions pour le tag " + tagName + " : ");
							PagedList<Question> questionList = tagEntry.getValue();
							for (int i = 0; i < questionList.size(); i++) {
								System.out.println(Alice.getLinkQuestion((int) questionList.get(i).getQuestionId())
										+ " , " + questionList.get(i).getTitle());
							}
						}
						break;
					case 2:
						List<Question> questionList = alice.getSortedAnsweredQuestions(1200);
						for (int i = 0; i < questionList.size(); i++) {
							System.out.println(Alice.getLinkQuestion((int) questionList.get(i).getQuestionId())
									+ " avec un score de " + questionList.get(i).getScore());
						}
						break;
					}
				}
				break;
			case 2:
				Bob bob = new Bob();
				while (!quit4) {
					choixBob = menuBob();
					switch (choixBob) {
					case 0:
						quit4 = true;
						break;
					case 1:
						System.out.println("Rentrer l'ID d'utilisateur voulue : ");
						String idUserStr = mainScanner.nextLine();
						int idUser = Integer.parseInt(idUserStr);
						TreeMap<String, ArrayList<Question>> mapNewQuestion = bob.getNewQuestionsAnswered(idUser);
						for (Entry<String, ArrayList<Question>> tagEntry : mapNewQuestion.entrySet()) {
							String tagName = tagEntry.getKey();
							System.out.println("\n Nouvelles questions pour le tag " + tagName + " : ");
							ArrayList<Question> newQuestion = tagEntry.getValue();
							for (int i = 0; i < newQuestion.size(); i++) {
								System.out.println(Alice.getLinkQuestion((int) newQuestion.get(i).getQuestionId())
										+ " , " + newQuestion.get(i).getTitle());
							}
						}
						break;
					case 2:
						TreeMap<String, ArrayList<Question>> mapNewQuestionToken = bob.getNewQuestionsAnswered();
						for (Entry<String, ArrayList<Question>> tagEntry : mapNewQuestionToken.entrySet()) {
							String tagName = tagEntry.getKey();
							System.out.println("\n Nouvelles questions pour le tag " + tagName + " : ");
							ArrayList<Question> newQuestionToken = tagEntry.getValue();
							for (int i = 0; i < newQuestionToken.size(); i++) {
								System.out.println(Alice.getLinkQuestion((int) newQuestionToken.get(i).getQuestionId())
										+ " , " + newQuestionToken.get(i).getTitle());
							}
						}
						break;
					}
				}
				break;
			case 3:
				while (!quit2) {
					Dave user = new Dave();
					choixDave = menuDave();
					switch (choixDave) {
					case 0:
						quit2 = true;
						break;
					case 1:
						System.out.println("Rentrez le nom du Tag à chercher :");
						String tagNameTopTag = mainScanner.nextLine();
						while (!DatabaseManager.tagExist(tagNameTopTag)) {
							System.out.println("Veuillez entrer un nom de Tag valide");
							tagNameTopTag = mainScanner.nextLine();
						}
						System.out.print("Top Tag : ");
						System.out.println(Dave.getLink((int) user.getTopTag(tagNameTopTag).getUser().getUserId()));
						break;
					case 2:
						System.out.println("Rentrez le nom du Tag à chercher :");
						String tagNameContributeurs = mainScanner.nextLine();
						while (!DatabaseManager.tagExist(tagNameContributeurs)) {
							System.out.println("Veuillez entrer un nom de Tag valide");
							tagNameContributeurs = mainScanner.nextLine();
						}
						System.out.println("Top 10 answerers : ");
						List<TagScore> resTopContributor = user.getTopAnswerers(tagNameContributeurs);
						for (int i = 0; i < resTopContributor.size(); i++) {
							System.out.println(Dave.getLink((int) resTopContributor.get(i).getUser().getUserId()));
						}
						break;
					case 3:
						System.out.println("Rentrez un des Tags voulus : \n(-1 pour terminer la saisie)");
						ArrayList<String> tagNameContributeursList = new ArrayList<String>();
						String tagName = mainScanner.nextLine();
						int nbTag = 0;
						while (!DatabaseManager.tagExist(tagName)) {
							System.out.println("Veuillez entrer un nom de Tag valide");
							tagName = mainScanner.nextLine();
						}
						while (!tagName.equals("-1")) {
							tagNameContributeursList.add(tagName);
							nbTag = nbTag + 1;
							System.out.println("Rentrez un des Tags voulus : \n(-1 pour terminer la saisie)");
							tagName = mainScanner.nextLine();
							while (!DatabaseManager.tagExist(tagName) && !tagName.equals("-1")) {
								System.out.println("Veuillez entrer un nom de Tag valide");
								tagName = mainScanner.nextLine();
							}
						}
						TagScore resContributor = user.getTopUserMultipleTags(tagNameContributeursList);

						if (resContributor != null) {
							System.out.println("Le top contributeur pour la liste entrée est ");
							System.out.println(Dave.getLink((int) resContributor.getUser().getUserId())
									+ " avec un nombre de post total de " + resContributor.getPostCount() + ".");
						} else {
							System.out.println("Pas de contributeur satisfaisant trouvé.");
						}
						break;
					}
				}
				break;
			}
		}
	}
}
