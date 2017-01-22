package fr.tse.info4.project.view.test;

import java.awt.Font;
import java.awt.GridBagLayout;
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
import fr.tse.info4.project.view.users.PageAlice;
import fr.tse.info4.project.view.users.PageBob;
import fr.tse.info4.project.view.users.PageDave;

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
		
		JPanel welcome =new JPanel();
		welcome.setLayout(new GridBagLayout());
		JLabel hello = new JLabel("Bienvenue "+(new ApiManager()).getUserNAme((int)ApiManager.getIdUser(acessToken)));
		hello.setFont(new Font("Tahoma", Font.BOLD, 25));
		welcome.add(hello);
		
		PageAlice alice = new PageAlice(acessToken);
		PageBob bob = new PageBob(acessToken);
		
		onglets.addTab("Accueil", welcome);

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
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); //met en plein écran
		this.setVisible(true);
	}
	
	public HomePage(int id) throws IOException, URISyntaxException {
		JTabbedPane onglets = new JTabbedPane();
		
		JPanel welcome =new JPanel();
		JLabel hello = new JLabel("Bienvenue ! Profil de "+(new ApiManager()).getUserNAme(id));
		hello.setFont(new Font("Tahoma", Font.BOLD, 25));
		welcome.setLayout(new GridBagLayout());
		welcome.add(hello);
		
	//	PageAlice alice = new PageAlice(id);
		PageBob bob = new PageBob(id);
		
		onglets.addTab("Accueil", welcome);

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
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); //met en plein écran
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
