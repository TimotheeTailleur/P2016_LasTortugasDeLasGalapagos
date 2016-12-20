package fr.tse.info4.project.view.ref;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import fr.tse.info4.project.model.datarecovery.Authenticate;
import fr.tse.info4.project.view.test.HomePage;

public class PageConnexion extends JPanel {

	public PageConnexion(int user,HomePage principal) {
		
		
		JButton connexion = new JButton("Connexion");
		this.add(connexion);

		connexion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if ((JButton) e.getSource() == connexion) {
					int id = 0;
					String accessToken = Authenticate.getAcessToken();
					if (user == 0) {
						principal =new HomePage();

					} else if (user == 1) {
						HomePage page =new HomePage();
					}

				}
			}
		});
		
		JButton connexionOut = new JButton("Hors-Connexion");
		this.add(connexionOut);
	}
}
