package fr.tse.info4.project.graphicalInterface.users;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONException;

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
		this.setLayout(new FlowLayout());
		// this.remove(TabReference.getFoot());
		this.getPanel().add(showNewQuestion(this));// panelConnexion(this));

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

		// Les actions marchent pas, le changement de scène ne s'effectue pas
		/*
		 * offline.addMouseListener(new MouseListener() {
		 * 
		 * @Override public void mouseReleased(MouseEvent e) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void mousePressed(MouseEvent e) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void mouseExited(MouseEvent e) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void mouseEntered(MouseEvent e) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void mouseClicked(MouseEvent e) { // TODO
		 * Auto-generated method stub alice.setIdUser(1200);
		 * alice.getPanel().remove(panelConnexion(alice));
		 * alice.getPanel().add(TabReference.getFoot());
		 * 
		 * } });
		 * 
		 * alice.addMouseListener(new MouseListener() {
		 * 
		 * public void actionPerformed(ActionEvent e) { // TODO Auto-generated
		 * method stub if ((JButton) e.getSource() == ButtonConnexion) {
		 * alice.setIdUser(1200);
		 * alice.getPanel().remove(panelConnexion(alice));
		 * alice.getPanel().add(TabReference.getFoot());
		 * 
		 * } }
		 * 
		 * @Override public void mouseClicked(MouseEvent e) { // TODO
		 * Auto-generated method stub if ((JButton) e.getSource() ==
		 * ButtonConnexion) { alice.setIdUser(1200);
		 * alice.getPanel().remove(panelConnexion(alice));
		 * alice.getPanel().add(TabReference.getFoot());
		 * 
		 * }
		 * 
		 * }
		 * 
		 * @Override public void mouseEntered(MouseEvent e) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void mouseExited(MouseEvent e) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void mousePressed(MouseEvent e) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void mouseReleased(MouseEvent e) { // TODO
		 * Auto-generated method stub
		 * 
		 * } });
		 */

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

		Alice al = new Alice(1200);

		TreeMap<String, TreeMap<Integer, String>> newQuestion = al.getNewQuestions();

		JLabel title = new JLabel("<html><b />Les nouvelles questions : </html>");
		// title.setFont( title.getFont().deriveFont(title.BOLD) );
		result.add(title);

		for (Entry<String, TreeMap<Integer, String>> tagEntry : newQuestion.entrySet()) {
			String tagName = tagEntry.getKey();
			JLabel tag = new JLabel("\n- dans le tag " + tagName + " : ");
			result.add(tag);
			TreeMap<Integer, String> questionMap = tagEntry.getValue();
			for (Entry<Integer, String> questionEntry : questionMap.entrySet()) {
				// int idQuestion = questionEntry.getKey();
				String questionTitle = questionEntry.getValue();
				JLabel tagQuestion = new JLabel("\t- " + questionTitle);
				result.add(tagQuestion);
			}
		}

		return result;
	}

	public JPanel showSortedAnsweredQuestions(PageAlice alice) {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

		Alice al = new Alice(1200);
		// param : int nbQuestions, int nbHours, boolean forceUpdate
		ArrayList<TreeMap<String, Integer>> listQuestion = al.getSortedAnsweredQuestions(3, 0, true);

		JLabel title = new JLabel("<html><b />Questions répondues triées : </html>");
		// title.setFont( title.getFont().deriveFont(title.BOLD) );
		result.add(title);

		for (int i = 0; i < listQuestion.size(); i++) {
			Integer idQuestion = listQuestion.get(i).get("idQuestion");
			Integer score = listQuestion.get(i).get("score");
			JLabel question = new JLabel(Alice.getLinkQuestion(idQuestion) + " avec un score de " + score);
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
