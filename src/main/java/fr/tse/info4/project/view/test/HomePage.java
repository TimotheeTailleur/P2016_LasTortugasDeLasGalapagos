package fr.tse.info4.project.view.test;

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
		this.setSize(1900, 700);
		this.setVisible(true);
	}
	
	public HomePage(String acessToken) throws IOException, URISyntaxException {
		JTabbedPane onglets = new JTabbedPane();
		
		JPanel wellcome =new JPanel();
		JLabel hello = new JLabel("Bienvenue "+(new ApiManager()).getUserNAme((int)ApiManager.getIdUser(acessToken)));
		wellcome.add(hello);
		
		onglets.addTab("Bienvenue", wellcome);

		onglets.addTab("Alice", new JPanel());
		onglets.addTab("Bob", new JPanel());
		//  onglets.addTab("Charlie", new PageCharlie(acessToken));
		onglets.addTab("Dave", new PageDave());
		
		onglets.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				changeTab(onglets,acessToken,0);
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
				changeTab(onglets,null,id);
			}
			
		});
		this.getContentPane().add(onglets);
		this.setTitle("Las Tortugas De Las Galapagos - Projet Informatique - API");
		this.setResizable(true);
		this.setSize(1900, 900);
		this.setVisible(true);
	}

	
	public void changeTab(JTabbedPane onglets,String acess, int id){
		int index = onglets.getSelectedIndex();
		switch(index){
		case 1:
			JPanel alice = (JPanel) onglets.getComponentAt(1);
			alice.removeAll();
			try {
				if(acess==null){
					alice.add(new PageAlice(id));
				}else{
					alice.add(new PageAlice(acess));
				}
				
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2:
			JPanel bob = (JPanel) onglets.getComponentAt(2);
			bob.removeAll();
			try {
				if(acess==null){
					bob.add(new PageBob(id));
				}else{
					bob.add(new PageBob(acess));
				}
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
	}

	public static void main(String[] args) throws IOException {
		HomePage page = new HomePage();
	}
}
