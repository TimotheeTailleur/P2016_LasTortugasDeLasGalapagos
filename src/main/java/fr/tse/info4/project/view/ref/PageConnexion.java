package fr.tse.info4.project.view.ref;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.tse.info4.project.controller.BobMethod;
import fr.tse.info4.project.model.datarecovery.ApiManager;
import fr.tse.info4.project.model.datarecovery.Authenticate;
import fr.tse.info4.project.view.test.HomePage;

public class PageConnexion extends TabReference{
	
	HomePage home=null;

	public PageConnexion(HomePage h){
		super();
		home=h;
		
		
		PageConnexion co = this;
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons,BoxLayout.LINE_AXIS));
		
		JButton ButtonConnexion = new JButton("Connexion");
		buttons.add(ButtonConnexion);

		final JButton offline = new JButton("Sans compte Stackoverflow");
		buttons.add(offline);
		this.getPanel().add(buttons);

		ButtonConnexion.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				String acessToken = Authenticate.getAcessToken();
				co.setVisible(false);
				try {
					try {
						HomePage page = new HomePage(acessToken);
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

		});



		offline.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
								
				ButtonConnexion.setVisible(false);
				offline.setVisible(false);
				
				askID(co);
				
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

		});
	}
	
	public void askID(PageConnexion co){
		
		JFrame userID = new JFrame("ID user");
		JPanel idParam = new JPanel();
		JPanel button = new JPanel();
		JPanel panel = new JPanel();
		JTextField idEntry = new JTextField(30);
		JLabel text = new JLabel("Entrer l'ID souhaité : ");
		
		idParam.setLayout(new BoxLayout(idParam,BoxLayout.LINE_AXIS));
		button.setLayout(new BoxLayout(button,BoxLayout.LINE_AXIS));
		panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
		userID.setLocationRelativeTo(null);		
		userID.pack();
		idParam.add(text);
		idParam.add(idEntry);
		
		JButton entry = new JButton("Entrer");
		button.add(entry);
		
		panel.add(idParam);
		panel.add(button);
		userID.add(panel);
		userID.setVisible(true);
		userID.setSize(500, 100);
		
		entry.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
				if(BobMethod.isEmpty(idEntry)==true){
					panel.add(new JLabel("Entrer un id"));
					panel.validate();
				}else{
					ApiManager api= new ApiManager();
					if (api.userExists(Integer.parseInt(idEntry.getText()))==true){
						userID.setVisible(false);
						home.setVisible(false);
						try {
							try {
								HomePage page = new HomePage(Integer.parseInt(idEntry.getText()));
							} catch (URISyntaxException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} catch (NumberFormatException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}else{
						panel.add(new JLabel("Id entré incorrect."));
						panel.validate();
					}
					
				}
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}
