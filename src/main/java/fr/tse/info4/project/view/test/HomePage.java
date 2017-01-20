package fr.tse.info4.project.view.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.tse.info4.project.model.datarecovery.ApiManager;
import fr.tse.info4.project.view.ref.PageConnexion;
import fr.tse.info4.project.view.ref.TabReference;
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
	
	public HomePage(String acessToken) throws IOException, URISyntaxException {
		JTabbedPane onglets = new JTabbedPane();
		
		JPanel wellcome =new JPanel();
		wellcome.add(new JLabel("Bienvenue "));
		
		onglets.addTab("Bienvenue", wellcome);

		onglets.addTab("Alice", new PageAlice(acessToken));
		onglets.addTab("Bob", new PageBob(acessToken));
		//  onglets.addTab("Charlie", new PageCharlie(acessToken));
		onglets.addTab("Dave", new PageDave());

		this.getContentPane().add(onglets);
		this.setTitle("Las Tortugas De Las Galapagos - Projet Informatique - API");
		this.setResizable(true);
		this.setSize(1900, 900);
		this.setVisible(true);
	}
	
	public HomePage(int id) throws IOException, URISyntaxException {
		JTabbedPane onglets = new JTabbedPane();
		
		JPanel wellcome =new JPanel();
		JLabel hello = new JLabel("Bienvenue "+(new ApiManager()).getUserNAme(id));
		wellcome.add(hello);
		
		onglets.addTab("Bienvenue", wellcome);

		onglets.addTab("Alice", new JPanel());
		onglets.addTab("Bob", new JPanel());
		//onglets.addTab("Charlie", new PageCharlie(id));
		onglets.addTab("Dave", new PageDave());
		onglets.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				int index = onglets.getSelectedIndex();
				switch(index){
				case 1:
					JPanel alice = (JPanel) onglets.getComponentAt(1);
					alice.removeAll();
					try {
						alice.add(new PageAlice(id));
					} catch (IOException | URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 2:
					JPanel bob = (JPanel) onglets.getComponentAt(2);
					bob.removeAll();
					try {
						bob.add(new PageBob(id));
					} catch (IOException | URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
			
		});
		this.getContentPane().add(onglets);
		this.setTitle("Las Tortugas De Las Galapagos - Projet Informatique - API");
		this.setResizable(true);
		this.setSize(1900, 900);
		this.setVisible(true);
	}


	public static void main(String[] args) throws IOException {
		HomePage page = new HomePage();
	}
}
