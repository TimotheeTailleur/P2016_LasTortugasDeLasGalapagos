package fr.tse.info4.project.graphicalInterface.test;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import fr.tse.info4.project.graphicalInterface.ref.PageConnexion;
import fr.tse.info4.project.graphicalInterface.users.*;

public class HomePage extends JFrame {

	public HomePage() {
		JTabbedPane onglets = new JTabbedPane();

		onglets.addTab("Alice", new PageConnexion(0));
		onglets.addTab("Bob", new PageConnexion(1));
		onglets.addTab("Charlie", new PageCharlie());
		onglets.addTab("Dave", new PageDave());

		this.getContentPane().add(onglets);
		this.setTitle("test");
		this.setResizable(true);
		this.setSize(1000, 500);
		this.setVisible(true);
	}

	public HomePage(int choix, int idUser) {
		JTabbedPane onglets = new JTabbedPane();

		if (choix == 1) {
			onglets.addTab("Alice", new PageAlice(idUser));
			onglets.addTab("Bob", new PageConnexion(1));
			onglets.addTab("Charlie", new PageCharlie());
			onglets.addTab("Dave", new PageDave());
		} else if (choix == 1) {
			onglets.addTab("Alice", new PageConnexion(0));
			onglets.addTab("Bob", new PageBob(idUser));
			onglets.addTab("Charlie", new PageCharlie());
			onglets.addTab("Dave", new PageDave());
		}

		this.getContentPane().add(onglets);
		this.setTitle("test");
		this.setResizable(true);
		this.setSize(1000, 500);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		HomePage page = new HomePage();
	}
}
