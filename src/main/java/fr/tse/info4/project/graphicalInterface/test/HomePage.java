package fr.tse.info4.project.graphicalInterface.test;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.json.JSONException;

import fr.tse.info4.project.graphicalInterface.ref.PageConnexion;
import fr.tse.info4.project.graphicalInterface.users.*;

public class HomePage extends JFrame {

	public HomePage() throws JSONException, IOException {
		JTabbedPane onglets = new JTabbedPane();

		onglets.addTab("Alice", new PageAlice());
		onglets.addTab("Bob", new PageConnexion(1, this));
		onglets.addTab("Charlie", new PageCharlie());
		onglets.addTab("Dave", new PageDave());

		this.getContentPane().add(onglets);
		this.setTitle("Las Tortugas De Las Galapagos - Projet Informatique - API");
		this.setResizable(true);
		this.setSize(1000, 500);
		this.setVisible(true);
	}

	/*public HomePage(int choix, int idUser) {
		JTabbedPane onglets = new JTabbedPane();

		if (choix == 1) {
			onglets.addTab("Alice", new PageAlice());
			onglets.addTab("Bob", new PageConnexion(1, this));
			onglets.addTab("Charlie", new PageCharlie());
			onglets.addTab("Dave", new PageDave());
		} else if (choix == 1) {
			onglets.addTab("Alice", new PageConnexion(0, this));
			onglets.addTab("Bob", new PageBob(idUser));
			onglets.addTab("Charlie", new PageCharlie());
			onglets.addTab("Dave", new PageDave());
		}

		this.getContentPane().add(onglets);
		this.setTitle("test");
		this.setResizable(true);
		this.setSize(1000, 500);
		this.setVisible(true);
	}*/

	public static void main(String[] args) throws JSONException, IOException {
		HomePage page = new HomePage();
	}
}
