package fr.tse.info4.project.view.test;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import fr.tse.info4.project.view.ref.PageConnexion;
import fr.tse.info4.project.view.users.*;

public class HomePage extends JFrame {

	public HomePage() throws IOException {
		JTabbedPane onglets = new JTabbedPane();

		onglets.addTab("Connexion", new PageConnexion(this));

		this.getContentPane().add(onglets);
		this.setTitle("Las Tortugas De Las Galapagos - Projet Informatique - API");
		this.setResizable(true);
		this.setSize(1900, 700);
		this.setVisible(true);
	}
	
	public HomePage(String acessToken) throws IOException {
		JTabbedPane onglets = new JTabbedPane();

		onglets.addTab("Alice", new PageAlice(acessToken));
		onglets.addTab("Bob", new PageBob(acessToken));
		//  onglets.addTab("Charlie", new PageCharlie(acessToken));
		onglets.addTab("Dave", new PageDave());

		this.getContentPane().add(onglets);
		this.setTitle("Las Tortugas De Las Galapagos - Projet Informatique - API");
		this.setResizable(true);
		this.setSize(1900, 700);
		this.setVisible(true);
	}
	
	public HomePage(int id) throws IOException {
		JTabbedPane onglets = new JTabbedPane();

		onglets.addTab("Alice", new PageAlice(id));
		onglets.addTab("Bob", new PageBob(id));
		//  onglets.addTab("Charlie", new PageCharlie(id));
		onglets.addTab("Dave", new PageDave());

		this.getContentPane().add(onglets);
		this.setTitle("Las Tortugas De Las Galapagos - Projet Informatique - API");
		this.setResizable(true);
		this.setSize(1900, 700);
		this.setVisible(true);
	}


	public static void main(String[] args) throws IOException {
		HomePage page = new HomePage();
	}
}
