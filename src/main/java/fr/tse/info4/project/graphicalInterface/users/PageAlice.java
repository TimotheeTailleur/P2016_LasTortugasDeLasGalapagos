package fr.tse.info4.project.graphicalInterface.users;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONException;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Question;

import fr.tse.info4.project.datarecovery.ApiManager;
import fr.tse.info4.project.datarecovery.Authenticate;
import fr.tse.info4.project.graphicalInterface.ref.TabReference;
import fr.tse.info4.project.user.Alice;

public class PageAlice extends TabReference {

	int idUser;

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public PageAlice() throws JSONException, IOException {

		super();
		// this.remove(TabReference.getFoot());
		this.getPanel().add(panelConnexion(this));
		// printAliceResults(this));

	}

	public JPanel panelConnexion(PageAlice alice) {

		JPanel connexion = new JPanel();

		JButton ButtonConnexion = new JButton("Connexion");
		connexion.add(ButtonConnexion);

		ButtonConnexion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if ((JButton) e.getSource() == ButtonConnexion) {
					String accessToken = Authenticate.getAcessToken();
					alice.setIdUser((int) ApiManager.getIdUser(accessToken));
					// alice.getPanel().remove(panelConnexion(alice));
					// alice.getPanel().add(printAliceResults(alice));
				}
			}
		});

		final JButton offline = new JButton("Sans compte Stackoverflow");
		connexion.add(offline);

		// Les actions marchent pas, le changement de sc�ne ne s'effectue pas

		offline.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				alice.setIdUser(1200);
				alice.getPanel().remove(ButtonConnexion);
				alice.getPanel().remove(offline);
				try {
					alice.getPanel().add(printAliceResults(alice));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		return connexion;
	}

	public JPanel printAliceResults(PageAlice alice) throws JSONException, IOException {

		JPanel resultat = new JPanel();

		JPanel newQestion = alice.showNewQuestion(alice);
		// JPanel comparedBadges = showComparedBadge;//new JPanel();
		JPanel sortQuestion = alice.showSortedAnsweredQuestions(alice);

		// autre solution qui marche toujours pas
		/*
		 * resultat.setLayout(new BorderLayout());
		 * resultat.getRootPane().add(newQestion, BorderLayout.WEST);
		 * resultat.getRootPane().add(sortQuestion, BorderLayout.EAST);
		 */

		resultat.setLayout(new BoxLayout(resultat, BoxLayout.LINE_AXIS));
		resultat.add(newQestion);
		// resultat.add(comparedBadges);
		resultat.add(sortQuestion);

		return resultat;
	}

	public JPanel showNewQuestion(PageAlice alice) throws JSONException, IOException {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

		Alice al = new Alice();

		Map<String, PagedList<Question>> newQuestion = al.getNewQuestions(1200);

		JLabel title = new JLabel("<html><b />Les nouvelles questions : </html>");
		// title.setFont( title.getFont().deriveFont(title.BOLD) );
		result.add(title);

		for (Entry<String, PagedList<Question>> tagEntry : newQuestion.entrySet()) {
			String tagName = tagEntry.getKey();
			JLabel tag = new JLabel("\n- dans le tag " + tagName + " : ");
			result.add(tag);
			PagedList<Question> questionMap = tagEntry.getValue();
			for (int i = 0; i < questionMap.getPageSize(); i++) {
				String questionTitle = questionMap.get(i).getTitle();
				JLabel tagQuestion = new JLabel("\t- " + questionTitle);
				tagQuestion.setMaximumSize(new Dimension(500, 20));
				result.add(tagQuestion);
			}
		}

		return result;
	}

	public JPanel showSortedAnsweredQuestions(PageAlice alice) {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

		Alice al = new Alice(); // param : int nbQuestions, int nbHours, boolean
								// forceUpdate
		List<Question> listQuestion = al.getSortedAnsweredQuestions(1200);

		JLabel title = new JLabel("<html><b />Questions r�pondues tri�es : </html>");
		// title.setFont( title.getFont().deriveFont(title.BOLD) );
		result.add(title);

		for (int i = 0; i < listQuestion.size(); i++) {
			JLabel question = new JLabel(Alice.getLinkQuestion((int)listQuestion.get(i).getQuestionId()) + " avec un score de " + listQuestion.get(i).getScore());
			result.add(question);
		}

		return result;
	}

	public JPanel showComparedBadge(PageAlice alice) {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

		return result;
	}

}