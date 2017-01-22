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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Answer;
import com.google.code.stackexchange.schema.Question;

import fr.tse.info4.project.controller.UserFactory;
import fr.tse.info4.project.model.user.Alice;
import fr.tse.info4.project.view.ref.TabReference;

public class PageAlice extends TabReference {

	Alice al = null;
	JButton Parametres = new JButton();
	PageAlice page = this;

	public PageAlice(String acessToken) throws IOException, URISyntaxException {

		super();
		int nbTags = Integer.parseInt(prop.getProperty("nbTags"));
		int nbQuestionsPerTags = Integer.parseInt(prop.getProperty("nbQuestionsPerTag"));
		int nbAnswers = Integer.parseInt(prop.getProperty("nbAnswers"));

		al = new UserFactory(acessToken).newAlice().withNbTags(nbTags).withNbQuestions(nbQuestionsPerTags)
				.withNbAnwsers(nbAnswers).get();
		this.getPanel().add(Parametres = getParametre());
		this.getPanel().add(new JScrollPane(printAliceResults(this)));
		

		Parametres.addActionListener(new ActionListener() {
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

				parametre.pack();
				parametre.setResizable(true);
				parametre.setSize(300, 250);

				JTextField modifications1 = new JTextField(10);
				modifications1.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramAlice1 = new JLabel("Tags pris en compte : ");
				modifications1.setText(Integer.toString(al.getNbTags()));

				JTextField modifications2 = new JTextField(10);
				modifications2.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramAlice2 = new JLabel("Questions par tag : ");
				modifications2.setText(Integer.toString(al.getNbQuestionsPerTag()));

				JTextField modifications3 = new JTextField(10);
				modifications3.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramAlice3 = new JLabel("Réponses affichées : ");
				modifications3.setText(Integer.toString(al.getNbAnswers()));

				panelParam.add(paramAlice1);
				panelParam.add(modifications1);

				panelParam1.add(paramAlice2);
				panelParam1.add(modifications2);

				panelParam2.add(paramAlice3);
				panelParam2.add(modifications3);

				panelParam.add(panelParam1);
				panelParam.add(panelParam2);
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

						int nbTags = Integer.parseInt(modifications1.getText());
						int nbQuestionsPerTag = Integer.parseInt(modifications2.getText());
						int nbAnswers = Integer.parseInt(modifications3.getText());

						al.setNbTags(nbTags);
						al.setNbQuestionsPerTag(nbQuestionsPerTag);
						al.setNbAnswers(nbAnswers);

						page.removeAll();
						try {

							page.add(Parametres = getParametre());
							page.add(printAliceResults(page));
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
						try {
							PropertiesConfiguration config = new PropertiesConfiguration(PARAMETERS_PATH);
							config.setProperty("nbTags", nbTags);
							config.setProperty("nbQuestionsPerTag", nbQuestionsPerTag);
							config.setProperty("nbAnswers", nbAnswers);
							config.save();
						} catch (ConfigurationException e1) {
							e1.printStackTrace();
						}
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
						modifications1.setText("3");
						modifications2.setText("3");
						modifications3.setText("10");

						al.setNbTags(3);
						al.setNbQuestionsPerTag(3);
						al.setNbAnswers(10);

						page.removeAll();
						try {

							page.add(Parametres = getParametre());
							page.add(printAliceResults(page));
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

						try {
							PropertiesConfiguration config = new PropertiesConfiguration(PARAMETERS_PATH);
							config.setProperty("nbTags", 3);
							config.setProperty("nbQuestionsPerTag", 3);
							config.setProperty("nbAnswers", 10);
							config.save();
						} catch (ConfigurationException e1) {
							e1.printStackTrace();
						}
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

	public PageAlice(int id) throws IOException, URISyntaxException {

		super();
		int nbTags = Integer.parseInt(prop.getProperty("nbTags"));
		int nbQuestionsPerTags = Integer.parseInt(prop.getProperty("nbQuestionsPerTag"));
		int nbAnswers = Integer.parseInt(prop.getProperty("nbAnswers"));
		al = new UserFactory().newAlice().withNbTags(nbTags).withNbQuestions(nbQuestionsPerTags)
				.withNbAnwsers(nbAnswers).get();
		this.getPanel().add(Parametres = getParametre());
		al.setIdUser(id);
		JScrollPane scroll = new JScrollPane(printAliceResults(this));
		this.getPanel().add(scroll);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		Parametres.addActionListener(new ActionListener() {
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

				parametre.pack();
				parametre.setResizable(true);
				parametre.setSize(300, 250);

				JTextField modifications1 = new JTextField(10);
				modifications1.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramAlice1 = new JLabel("Tags pris en compte : ");
				modifications1.setText(Integer.toString(al.getNbTags()));

				JTextField modifications2 = new JTextField(10);
				modifications2.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramAlice2 = new JLabel("Questions par tag : ");
				modifications2.setText(Integer.toString(al.getNbQuestionsPerTag()));

				JTextField modifications3 = new JTextField(10);
				modifications3.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramAlice3 = new JLabel("Réponses affichées : ");
				modifications3.setText(Integer.toString(al.getNbAnswers()));

				panelParam.add(paramAlice1);
				panelParam.add(modifications1);

				panelParam1.add(paramAlice2);
				panelParam1.add(modifications2);

				panelParam2.add(paramAlice3);
				panelParam2.add(modifications3);

				panelParam.add(panelParam1);
				panelParam.add(panelParam2);
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

						int nbTags = Integer.parseInt(modifications1.getText());
						int nbQuestionsPerTag = Integer.parseInt(modifications2.getText());
						int nbAnswers = Integer.parseInt(modifications3.getText());

						al.setNbTags(nbTags);
						al.setNbQuestionsPerTag(nbQuestionsPerTag);
						al.setNbAnswers(nbAnswers);

						page.removeAll();
						try {

							page.add(Parametres = getParametre());
							page.add(printAliceResults(page));
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

						try {
							PropertiesConfiguration config = new PropertiesConfiguration(PARAMETERS_PATH);
							config.setProperty("nbTags", nbTags);
							config.setProperty("nbQuestionsPerTag", nbQuestionsPerTag);
							config.setProperty("nbAnswers", nbAnswers);
							config.save();
						} catch (ConfigurationException e1) {
							e1.printStackTrace();
						}

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
						modifications1.setText("3");
						modifications2.setText("3");
						modifications3.setText("10");

						al.setNbTags(3);
						al.setNbQuestionsPerTag(3);
						al.setNbAnswers(10);

						page.removeAll();
						try {

							page.add(Parametres = getParametre());
							page.add(printAliceResults(page));
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

						try {
							PropertiesConfiguration config = new PropertiesConfiguration(PARAMETERS_PATH);
							config.setProperty("nbTags", 3);
							config.setProperty("nbQuestionsPerTag", 3);
							config.setProperty("nbAnswers", 10);
							config.save();
						} catch (ConfigurationException e1) {
							e1.printStackTrace();
						}
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

	public JPanel printAliceResults(PageAlice alice) throws IOException, URISyntaxException {

		JPanel resultat = new JPanel();

		JPanel newQestion = alice.showNewQuestion(alice);
		// JPanel comparedBadges = showComparedBadge;//new JPanel();
		JPanel sortQuestion = alice.showSortedAnsweredQuestions(alice);

		resultat.setLayout(new BoxLayout(resultat, BoxLayout.LINE_AXIS));
		resultat.add(newQestion);
		// resultat.add(comparedBadges);
		resultat.add(sortQuestion);
		resultat.setVisible(true);

		return resultat;
	}

	private JPanel showNewQuestion(PageAlice alice) throws IOException, URISyntaxException {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

		Map<String[], PagedList<Question>> newQuestion = al.getNewQuestions();

		JLabel title = new JLabel("Les nouvelles questions : ");
		title.setFont(new Font("Tahoma", Font.BOLD, 19));
		title.setBorder(new EmptyBorder(0, 0, 5, 0));
		result.add(title);

		for (Entry<String[], PagedList<Question>> tagEntry : newQuestion.entrySet()) {
			String[] tagName = tagEntry.getKey();
			String str = "\n- dans le(s) tag(s) ";
			int nb = tagName.length;
			for (int i = 0; i < nb; i++) {
				if (i != (nb - 1) && nb != 1) {
					str += tagName[i] + ", ";
				} else {
					str += tagName[i];
				}
			}
			str += " : ";
			JLabel tag = new JLabel(str);
			tag.setBorder(new EmptyBorder(5, 0, 5, 0));
			result.add(tag);
			PagedList<Question> questionMap = tagEntry.getValue();
			for (int i = 0; i < questionMap.getPageSize(); i++) {
				String questionTitle = questionMap.get(i).getTitle();

				final URI uri = new URI(Alice.getLinkQuestion((int) questionMap.get(i).getQuestionId()));

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
				result.add(link);
				result.validate();
			}
		}
		result.setBorder(new EmptyBorder(10, 50, 10, 30));

		return result;
	}

	private JPanel showSortedAnsweredQuestions(PageAlice alice) throws URISyntaxException {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

		List<Answer> listQuestion = al.getSortedAnswers();

		JLabel title = new JLabel("Questions répondues triées : ");
		title.setFont(new Font("Tahoma", Font.BOLD, 19));
		title.setBorder(new EmptyBorder(0, 0, 10, 0));
		result.add(title);

		for (int i = 0; i < listQuestion.size(); i++) {
			final URI uri = new URI(Alice.getLinkQuestion((int) listQuestion.get(i).getQuestionId()));

			class OpenUrlAction implements ActionListener {
				@Override
				public void actionPerformed(ActionEvent e) {
					open(uri);
				}
			}

			JButton link = new JButton();
			link.setText("Réponse avec un score de " + listQuestion.get(i).getScore());
			link.setToolTipText(uri.toString());
			link.addActionListener(new OpenUrlAction());
			link.setMaximumSize(new Dimension(250, 30));
			result.add(link);
			result.validate();
		}

		result.setBorder(new EmptyBorder(10, 30, 10, 50));
		return result;
	}

	private JPanel showComparedBadge(PageAlice alice) {
		JPanel result = new JPanel();

		ArrayList<Integer> list = al.compareBadge();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

		result.setBorder(new EmptyBorder(10, 20, 10, 20));
		return result;
	}

	private static void open(URI uri) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
				/* TODO: error handling */ }
		} else {
			/* TODO: error handling */ }
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		new PageAlice(1200);
	}

}
