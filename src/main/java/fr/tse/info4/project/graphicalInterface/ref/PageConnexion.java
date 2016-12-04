package fr.tse.info4.project.graphicalInterface.ref;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import fr.tse.info4.project.graphicalInterface.test.HomePage;

public class PageConnexion extends JPanel {

	public PageConnexion(int user) {
		JButton connexion = new JButton("Connection");
		this.add(connexion);

		connexion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if ((JButton) e.getSource() == connexion) {
					int id = 0;

					if (user == 0) {
						HomePage page = new HomePage(user, id);

					} else if (user == 1) {
						HomePage page = new HomePage(user, id);
					}

				}
			}
		});
	}
}
