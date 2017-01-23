package fr.tse.info4.project.view.users;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.User;

import fr.tse.info4.project.controller.BobMethod;
import fr.tse.info4.project.controller.UserFactory;
import fr.tse.info4.project.model.user.Bob;
import fr.tse.info4.project.view.ref.TabReference;

public class PageBob extends TabReference {

	Bob bob = null;
	int appelRecherche = 0;
	int appelSuggere = 0;
	PageBob page = this;
	
	/**
	 * Bob Tab Constructor
	 * <br> Displays method results for each functionality of the Bob User Story
	 * Overloaded to worh with both an accessToken & a user's ID
	 */
	public PageBob(String acessToken) throws IOException, URISyntaxException {

		super();
		int nbBestTags = Integer.parseInt(prop.getProperty("nbBestTags"));
		int nbQuestionsPerTag = Integer.parseInt(prop.getProperty("nbQuestionsPerTag"));
		int nbSimilarQuestions = Integer.parseInt(prop.getProperty("nbSimilarQuestions"));
		int nbExpertsPerTag = Integer.parseInt(prop.getProperty("nbExpertsPerTag"));
		bob = new UserFactory(acessToken).newBob().withNbBestTags(nbBestTags).withNbQuestions(nbQuestionsPerTag)
				.withNbSimilarQuestions(nbSimilarQuestions).withNbExperts(nbExpertsPerTag).get();
		JScrollPane scroll = new JScrollPane(printBobResults(this));
		double hauteur=java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double largeur=java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		scroll.setPreferredSize(new Dimension ((int)largeur-100,(int)hauteur-200));
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		this.getPanel().add(getParametre());
		getParametre().setAlignmentX(this.getPanel().CENTER_ALIGNMENT);
		this.getPanel().add(scroll);
		
		getParametre().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JFrame parametre = new JFrame("Parametres");

				JButton validation = new JButton("Valider");
				JButton reinitialiser = new JButton("Réinitialiser");

				JPanel panelParam = new JPanel();
				panelParam.setLayout(new FlowLayout());

				JPanel panelParam1 = new JPanel();
				panelParam1.setLayout(new FlowLayout());
 
				
				JPanel panelParam2 = new JPanel();
				panelParam2.setLayout(new FlowLayout());
				
				JPanel panelParam3 = new JPanel();
				panelParam3.setLayout(new FlowLayout());

				parametre.pack();
				parametre.setResizable(true);
				parametre.setSize(300, 250);

				JTextField modifications1 = new JTextField(10);
				modifications1.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramBob1 = new JLabel("Meilleurs tags: ");
				modifications1.setText(Integer.toString(bob.getNbTags()));

				JTextField modifications2 = new JTextField(10);
				modifications2.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramBob2 = new JLabel("Questions par tag : ");
				modifications2.setText(Integer.toString(bob.getNbQuestionsPerTag()));

				JTextField modifications3 = new JTextField(10);
				modifications3.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramBob3 = new JLabel("Questions simillaires : ");
				modifications3.setText(Integer.toString(bob.getNbSimilarQuestions()));
				
				JTextField modifications4 = new JTextField(10);
				modifications4.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramBob4 = new JLabel("Experts par tag : ");
				modifications4.setText(Integer.toString(bob.getNbExpertsPerTag()));


				panelParam.add(paramBob1);
				panelParam.add(modifications1);

				panelParam1.add(paramBob2);
				panelParam1.add(modifications2);

				panelParam2.add(paramBob3);
				panelParam2.add(modifications3);
				
				panelParam3.add(paramBob4);
				panelParam3.add(modifications4);

				panelParam.add(panelParam1);
				panelParam.add(panelParam2);
				panelParam.add(panelParam3);
				panelParam.add(validation);
				panelParam.add(reinitialiser);
				parametre.getContentPane().add(panelParam);
				parametre.setVisible(true);
				
				validation.addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub

						int  nbBestTags = Integer.parseInt(modifications1.getText());
						int nbQuestionsPerTag = Integer.parseInt(modifications2.getText());
						int nbSimilarQuestions = Integer.parseInt(modifications3.getText());
						int nbExpertsPerTag = Integer.parseInt(modifications4.getText());

						bob.setNbTags(nbBestTags);
						bob.setNbQuestionsPerTag(nbQuestionsPerTag);
						bob.setNbSimilarQuestions(nbSimilarQuestions);
						bob.setNbExpertsPerTag(nbExpertsPerTag);

						page.removeAll();
						try {

							page.add(getParametre());
							JScrollPane scroll = new JScrollPane(printBobResults(page));
							scroll.setPreferredSize(new Dimension ((int)largeur-100,(int)hauteur-200));
							scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
							scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
							page.add(scroll);
							page.add(getFoot());
							page.validate();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						parametre.dispose();
						
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub

					}
				});
				reinitialiser.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						modifications1.setText("5");
						modifications2.setText("3");
						modifications3.setText("10");
						modifications4.setText("3");

						bob.setNbTags(5);
						bob.setNbQuestionsPerTag(3);
						bob.setNbSimilarQuestions(10);
						bob.setNbExpertsPerTag(3);

						page.removeAll();
						try {

							page.add(getParametre());
							JScrollPane scroll = new JScrollPane(printBobResults(page));
							scroll.setPreferredSize(new Dimension ((int)largeur-100,(int)hauteur-200));
							scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
							scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
							page.add(scroll);
							page.add(getFoot());
							page.validate();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						parametre.dispose();

					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
				});

			}

		});
	}

	/**
	 * Bob Tab Constructor
	 * <br> Displays method results for each functionality of the Bob User Story
	 * Overloaded to worh with both an accessToken & a user's ID
	 */
	public PageBob(int id) throws IOException, URISyntaxException {

		super();
		int nbBestTags = Integer.parseInt(prop.getProperty("nbBestTags"));
		int nbQuestionsPerTag = Integer.parseInt(prop.getProperty("nbQuestionsPerTag"));
		int nbSimilarQuestions = Integer.parseInt(prop.getProperty("nbSimilarQuestions"));
		int nbExpertsPerTag = Integer.parseInt(prop.getProperty("nbExpertsPerTag"));
		bob = new UserFactory().newBob().withId(id).withNbBestTags(nbBestTags).withNbQuestions(nbQuestionsPerTag)
				.withNbSimilarQuestions(nbSimilarQuestions).withNbExperts(nbExpertsPerTag).get();
		JScrollPane scroll = new JScrollPane(printBobResults(this));
		double hauteur=java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double largeur=java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		scroll.setPreferredSize(new Dimension ((int)largeur-100,(int)hauteur-200));
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		this.getPanel().add(getParametre());
		getParametre().setAlignmentX(this.getPanel().CENTER_ALIGNMENT);
		this.getPanel().add(scroll);
		
		getParametre().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JFrame parametre = new JFrame("Parametres");

				JButton validation = new JButton("Valider");
				JButton reinitialiser = new JButton("Réinitialiser");

				JPanel panelParam = new JPanel();
				panelParam.setLayout(new FlowLayout());

				JPanel panelParam1 = new JPanel();
				panelParam1.setLayout(new FlowLayout());
 
				
				JPanel panelParam2 = new JPanel();
				panelParam2.setLayout(new FlowLayout());
				
				JPanel panelParam3 = new JPanel();
				panelParam3.setLayout(new FlowLayout());

				parametre.pack();
				parametre.setResizable(true);
				parametre.setSize(300, 250);

				JTextField modifications1 = new JTextField(10);
				modifications1.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramBob1 = new JLabel("Meilleurs tags: ");
				modifications1.setText(Integer.toString(bob.getNbTags()));

				JTextField modifications2 = new JTextField(10);
				modifications2.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramBob2 = new JLabel("Questions par tag : ");
				modifications2.setText(Integer.toString(bob.getNbQuestionsPerTag()));

				JTextField modifications3 = new JTextField(10);
				modifications3.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramBob3 = new JLabel("Questions simillaires : ");
				modifications3.setText(Integer.toString(bob.getNbSimilarQuestions()));
				
				JTextField modifications4 = new JTextField(10);
				modifications4.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramBob4 = new JLabel("Experts par tag : ");
				modifications4.setText(Integer.toString(bob.getNbExpertsPerTag()));


				panelParam.add(paramBob1);
				panelParam.add(modifications1);

				panelParam1.add(paramBob2);
				panelParam1.add(modifications2);

				panelParam2.add(paramBob3);
				panelParam2.add(modifications3);
				
				panelParam3.add(paramBob4);
				panelParam3.add(modifications4);

				panelParam.add(panelParam1);
				panelParam.add(panelParam2);
				panelParam.add(panelParam3);
				panelParam.add(validation);
				panelParam.add(reinitialiser);
				parametre.getContentPane().add(panelParam);
				parametre.setVisible(true);
				
				validation.addMouseListener(new MouseListener() {

					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub

						int  nbBestTags = Integer.parseInt(modifications1.getText());
						int nbQuestionsPerTag = Integer.parseInt(modifications2.getText());
						int nbSimilarQuestions = Integer.parseInt(modifications3.getText());
						int nbExpertsPerTag = Integer.parseInt(modifications4.getText());

						bob.setNbTags(nbBestTags);
						bob.setNbQuestionsPerTag(nbQuestionsPerTag);
						bob.setNbSimilarQuestions(nbSimilarQuestions);
						bob.setNbExpertsPerTag(nbExpertsPerTag);

						page.removeAll();
						try {

							page.add(getParametre());
							JScrollPane scroll = new JScrollPane(printBobResults(page));
							scroll.setPreferredSize(new Dimension ((int)largeur-100,(int)hauteur-200));
							scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
							scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
							page.add(scroll);
							page.add(getFoot());
							page.validate();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						parametre.dispose();
						
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub

					}
				});
				reinitialiser.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						modifications1.setText("5");
						modifications2.setText("3");
						modifications3.setText("10");
						modifications4.setText("3");

						bob.setNbTags(5);
						bob.setNbQuestionsPerTag(3);
						bob.setNbSimilarQuestions(10);
						bob.setNbExpertsPerTag(3);

						page.removeAll();
						try {

							page.add(getParametre());
							JScrollPane scroll = new JScrollPane(printBobResults(page));
							scroll.setPreferredSize(new Dimension ((int)largeur-100,(int)hauteur-200));
							scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
							scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
							page.add(scroll);
							page.add(getFoot());
							page.validate();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						parametre.dispose();

					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
				});

			}

		});

	}

		
		 
		
	
		

	public JPanel printBobResults(PageBob bobPage) throws IOException, URISyntaxException {

		JPanel resultat = new JPanel();

		JPanel similarQuestion = bobPage.showQuestion(bobPage);
		JPanel keyWords = bobPage.showKeyWords(bobPage);
		JPanel experts = bobPage.showExperts(bobPage);
		JPanel newQuestionAnswered = bobPage.showNewQuestionAnswered(bobPage);

		resultat.setLayout(new BoxLayout(resultat, BoxLayout.LINE_AXIS));
		resultat.add(similarQuestion);
		resultat.add(keyWords);
		resultat.add(experts);
		resultat.add(newQuestionAnswered);
		resultat.setAlignmentX(CENTER_ALIGNMENT);
		resultat.setVisible(true);

		return resultat;
	}

	private JPanel showQuestion(PageBob bobPage) throws URISyntaxException {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));
		JTextField text = new JTextField(35);
		text.setMaximumSize(new Dimension(600, 30));
		JButton valid = new JButton("Rechercher");

		String str = "Questions similaires à celle écrite : ";
		JLabel title = new JLabel(str);
		title.setFont(new Font("Tahoma", Font.BOLD, 15));
		title.setBorder(new EmptyBorder(0, 0, 5, 0));

		result.add(title);
		result.add(text);
		result.add(valid);

		valid.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (appelRecherche == 0) {
					appelRecherche += 1;
				} else {
					result.remove(3);
					result.validate();
				}
				if (BobMethod.isEmpty(text) == true) {
					JLabel error = new JLabel("Rentrer une question, s'il vous plait.");
					result.add(error);
					result.validate();
				} else {
					List<Question> questions = bob.findSimilarQuestions(text.getText());
					String str2 = "";
					if (BobMethod.hasQuestion(questions) == false) {
						str2 += "\n Pas de questions semblables trouvées.";
						JLabel similarQ = new JLabel(str2);
						result.add(similarQ);
						result.validate();
					} else {
						for (int i = 0; i < questions.size(); i++) {
							JButton link = new JButton();
							URI uri;
							try {
								uri = new URI(Bob.getLinkQuestion((int) questions.get(i).getQuestionId()));
								class OpenUrlAction implements ActionListener {
								      @Override public void actionPerformed(ActionEvent e) {
								        open(uri);
								      }
								    }
								  
								  link.addActionListener(new OpenUrlAction()); // pour le rendre cliquable 
								  link.setToolTipText(uri.toString()); // reference du http
							} catch (URISyntaxException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							link.setText(questions.get(i).getTitle());
							link.setForeground(Color.BLUE);
							link.setBorderPainted(false);
							link.setOpaque(false);
							link.setBackground(Color.WHITE);
							Font font = link.getFont();
							Map attributes = font.getAttributes();
							attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
							link.setFont(font.deriveFont(attributes));
							link.setMaximumSize(new Dimension(250, 30));
							result.add(link);
							result.validate();
						}
						result.validate();
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		result.validate();
		return result;
	}

	private JPanel showKeyWords(PageBob bobPage) {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));
		JTextField text = new JTextField(35);
		text.setMaximumSize(new Dimension(600, 30));
		JButton valid = new JButton("Suggérer");

		String str = "Mots-clés pouvant être rajoutés : ";
		JLabel title = new JLabel(str);
		title.setFont(new Font("Tahoma", Font.BOLD, 15));
		title.setBorder(new EmptyBorder(0, 0, 10, 0));

		result.add(title);
		result.add(text);
		result.add(valid);

		valid.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {

				if (appelSuggere == 0) {
					appelSuggere += 1;
				} else {
					result.remove(3);
					result.validate();
				}

				if (BobMethod.isEmpty(text) == true) {
					JLabel error = new JLabel("Rentrer une question, s'il vous plait.");
					result.add(error);
					result.validate();
				} else {
					List<String> keyWords = bob.findKeyWords(text.getText());

					if (BobMethod.hasKeyWords(keyWords) == false) {
						String str2 = "Pas de mots-clés trouvés.";
						JLabel similarQ = new JLabel(str2);
						result.add(similarQ);
						result.validate();
					} else {
						if (keyWords.size() <= 20) {
							for (int i = 0; i < keyWords.size(); i++) {
								String str2 = "- " + keyWords.get(i);
								JLabel similarQ = new JLabel(str2);
								result.add(similarQ);
								result.validate();
							}
						} else {
							for (int i = 0; i < 20; i++) {
								String str2 = "- " + keyWords.get(i);
								JLabel similarQ = new JLabel(str2);
								result.add(similarQ);
								result.validate();
							}
						}
						result.validate();
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		result.validate();
		return result;
	}

	private JPanel showExperts(PageBob bobPage) throws URISyntaxException {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

		String show = "";
		Set<User> users = bob.getExperts();
		if (BobMethod.hasExpert(users) == false) {
			show += "Pas d'user satisfaisant trouvé.";
			JLabel title = new JLabel(show);
			result.add(title);
		} else {
			show += "Liste d'utilisateurs potentiellement intéressants : ";
			JLabel title = new JLabel(show);
			title.setFont(new Font("Tahoma", Font.BOLD, 15));
			title.setBorder(new EmptyBorder(0, 0, 10, 0));
			result.add(title);

			for (User user : users) {
				final URI uri = new URI(Bob.getLink((int) user.getUserId()));

				class OpenUrlAction implements ActionListener {
					@Override
					public void actionPerformed(ActionEvent e) {
						open(uri);
					}
				}

				JButton link = new JButton();
				link.setText(user.getDisplayName().replace("\"", " "));
				link.setToolTipText(uri.toString());
				link.addActionListener(new OpenUrlAction());
				link.setMaximumSize(new Dimension(200, 30));
				result.add(link);
				result.validate();
			}
		}
		return result;
	}

	private JPanel showNewQuestionAnswered(PageBob bobPage) throws URISyntaxException {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

		JLabel title = new JLabel("Questions répondues récemment : ");
		title.setFont(new Font("Tahoma", Font.BOLD, 15));
		result.add(title);
		title.setBorder(new EmptyBorder(0, 0, 5, 0));
		Map<String, List<Question>> newQuestions = bob.getNewQuestionsAnswered();
		for (Entry<String, List<Question>> questionEntry : newQuestions.entrySet()) {
			String tagName = questionEntry.getKey();
			JLabel tag = new JLabel("\n- dans le tag " + tagName + " : ");
			tag.setBorder(new EmptyBorder(5, 0, 5, 0));
			result.add(tag);
			List<Question> questions = questionEntry.getValue();
			for (int i = 0; i < questions.size(); i++) {
				String questionTitle = questions.get(i).getTitle();

				final URI uri = new URI(Bob.getLinkQuestion((int) questions.get(i).getQuestionId()));

				class OpenUrlAction implements ActionListener {
					@Override
					public void actionPerformed(ActionEvent e) {
						open(uri);
					}
				}

				JButton link = new JButton();
				link.setText(questionTitle);
				link.setForeground(Color.BLUE);
				link.setBorderPainted(false);
				link.setOpaque(false);
				link.setBackground(Color.WHITE);
				Font font = link.getFont();
				Map attributes = font.getAttributes();
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				link.setFont(font.deriveFont(attributes));
				link.setToolTipText(uri.toString());
				link.addActionListener(new OpenUrlAction());
				link.setMaximumSize(new Dimension(400, 30));
				result.add(link);
				result.validate();
			}
		}
		return result;
	}

	/**
	 * Method which will open a browser with the URI in parameter
	 * if it is possible.
	 * @param uri
	 */
	private static void open(URI uri) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
				/* TODO: error handling */ }
		} else {
			/* TODO: error handling */ }
	}

}
