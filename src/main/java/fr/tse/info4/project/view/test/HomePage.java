package fr.tse.info4.project.view.test;

import java.awt.Dimension;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.tse.info4.project.model.datarecovery.ApiManager;
import fr.tse.info4.project.view.ref.PageConnexion;
import fr.tse.info4.project.view.users.*;

public class HomePage extends JFrame {

	public HomePage() throws IOException {
		JTabbedPane onglets = new JTabbedPane();

		onglets.addTab("Connexion", new PageConnexion(this));

		this.getContentPane().add(onglets);
		this.setTitle("Las Tortugas De Las Galapagos - Projet Informatique - API");
		this.setResizable(true);
		this.setSize(700, 350);
		this.setVisible(true);
	}
	
	public HomePage(String acessToken) throws IOException, URISyntaxException {
		JTabbedPane onglets = new JTabbedPane();
		
		JPanel wellcome =new JPanel();
		JLabel hello = new JLabel("Bienvenue "+(new ApiManager()).getUserNAme((int)ApiManager.getIdUser(acessToken)));
		wellcome.add(hello);
		
		PageAlice alice = new PageAlice(acessToken);
		PageBob bob = new PageBob(acessToken);
		
		onglets.addTab("Bienvenue", wellcome);

		onglets.addTab("Alice", new JPanel());
		onglets.addTab("Bob", new JPanel());
		//  onglets.addTab("Charlie", new PageCharlie(acessToken));
		onglets.addTab("Dave", new PageDave());
		
		onglets.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				changeTab(onglets,alice,bob);
			}
			
		});

		this.getContentPane().add(onglets);
		this.setTitle("Las Tortugas De Las Galapagos - Projet Informatique - API");
		this.setResizable(true);
		this.setSize(1900, 900);
		this.setVisible(true);
	}
	
	public HomePage(int id) throws IOException, URISyntaxException {
		JTabbedPane onglets = new JTabbedPane();
		
		JPanel wellcome =new JPanel();
		JLabel hello = new JLabel("Bienvenue, profil de "+(new ApiManager()).getUserNAme(id));
		wellcome.add(hello);
		
		PageAlice alice = new PageAlice(id);
		PageBob bob = new PageBob(id);
		
		onglets.addTab("Bienvenue", wellcome);

		onglets.addTab("Alice", new JPanel());
		onglets.addTab("Bob", new JPanel());
		//onglets.addTab("Charlie", new PageCharlie(id));
		onglets.addTab("Dave", new PageDave());
		onglets.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				changeTab(onglets,alice,bob);
			}
			
		});
		this.getContentPane().add(onglets);
		this.setTitle("Las Tortugas De Las Galapagos - Projet Informatique - API");
		this.setResizable(true);
		
		Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int hauteur = (int)tailleEcran.getHeight();
		int largeur = (int)tailleEcran.getWidth();
		this.setSize(hauteur, largeur);
		this.setVisible(true);
	}

	public void changeTab(JTabbedPane onglets,PageAlice panelAlice,PageBob panelBob){
		int index = onglets.getSelectedIndex();
		switch(index){
		case 1:
			JPanel alice = (JPanel) onglets.getComponentAt(1);
			alice.removeAll();
			alice.add(panelAlice);
			break;
		case 2:
			JPanel bob = (JPanel) onglets.getComponentAt(2);
			bob.removeAll();
			bob.add(panelBob);
			break;
		}
	}

	public static void main(String[] args) throws IOException {
		HomePage page = new HomePage();
	}
}
