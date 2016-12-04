package fr.tse.info4.project.graphicalInterface.users;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import fr.tse.info4.project.graphicalInterface.ref.TabReference;

public class PageAlice extends TabReference {
	public PageAlice(int idUser) {

		super();
		this.setLayout(new FlowLayout());

		JPanel resultat = new JPanel();
		resultat.setLayout(new BoxLayout(resultat, BoxLayout.LINE_AXIS));

		JPanel newQestion = new JPanel();
		JPanel comparedBadges = new JPanel();
		JPanel sortQuestion = new JPanel();

		resultat.add(newQestion);
		resultat.add(comparedBadges);
		resultat.add(sortQuestion);

		this.getPanel().add(resultat);
	}

}
