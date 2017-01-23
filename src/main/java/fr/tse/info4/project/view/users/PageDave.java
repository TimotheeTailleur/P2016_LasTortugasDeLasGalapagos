package fr.tse.info4.project.view.users;


import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import fr.tse.info4.project.controller.UserFactory;
import fr.tse.info4.project.model.datarecovery.ApiManager;
import fr.tse.info4.project.model.schema.TagScore;
import fr.tse.info4.project.model.schema.TopUser;
import fr.tse.info4.project.model.user.Dave;
import fr.tse.info4.project.view.ref.TabReference;

public class PageDave extends TabReference {

	boolean isList;
	JTextField tagName = new JTextField(35);
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();
	JPanel results = new JPanel();
	Dave dave = null;
	int appelTag = 0;
	int appelContributeur = 0;

	/**
	 * Dave Tab Constructor
	 * <br> Displays method results for each functionality of the Dave User Story
	 */
	public PageDave() {
		super();

		int nbUsers = Integer.parseInt(prop.getProperty("nbUsers"));
		int refreshRate = Integer.parseInt(prop.getProperty("refreshRate"));
		boolean forceUpdate = Boolean.parseBoolean(prop.getProperty("forceUpdate"));
		
		if (forceUpdate)
			dave= (new UserFactory()).newDave().withNbUsers(nbUsers).withRefreshRate(refreshRate).withForceUpdate().get();
		else
			dave= (new UserFactory()).newDave().withNbUsers(nbUsers).withRefreshRate(refreshRate).withoutForceUpdate().get();
		
		JLabel title = new JLabel("Veuillez entrer un ou plusieurs tags séparés par un espace : ");
		panel1.setLayout(new FlowLayout());
		panel2.setLayout(new FlowLayout());
		panel3.setLayout(new BoxLayout(panel3,BoxLayout.PAGE_AXIS ));
		tagName.setHorizontalAlignment(JTextField.CENTER);


		JButton option1 = new JButton("Meilleur tag");
		option1.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
							
			
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				
				// TODO Auto-generated method stub
				if (appelTag == 0 && appelContributeur==0) {
					appelTag += 1;
				} else {
					if(appelContributeur==1){
						int nbComponent = results.getComponentCount();
						for(int i=0;i<nbComponent;i++){
							results.remove(0);
							results.validate();
						}
						appelContributeur=0;
					}else{
						results.remove(0);
						results.validate();
						appelTag=0;
					}
					
				}
				isListTag(); 
				JButton link = new JButton();
				
				String str=" ";
				
				 if(isList == false ){ // if one tag was written, launch function 1 and display results
					
					 
					 TagScore topTag = dave.getTopTag(tagName.getText());
					 str = (new ApiManager()).getUserNAme((int)topTag.getUser().getUserId()) + " avec un score de " + topTag.getScore();
				
					 try {
						 final URI uri = new URI(Dave.getLink((int)topTag.getUser().getUserId()));
						  
						  class OpenUrlAction implements ActionListener {
						      @Override public void actionPerformed(ActionEvent e) {
						        open(uri);
						      }
						    }
						  
						  link.addActionListener(new OpenUrlAction()); // pour le rendre cliquable 
						  link.setToolTipText(uri.toString()); // reference du http
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 
					link.setText(str);						
					 
				 }else{					 
					 
					 
						 String[] resultsTab = tagName.getText().split(" ");
						 List<String> resultsList = new ArrayList<String>();
						 
						 for(int i=0;i<resultsTab.length;i++){
							 resultsList.add(resultsTab[i]);
						 }
						 TopUser topTags = dave.getTopUserMultipleTags(resultsList);
						 
						
						 str = (new ApiManager()).getUserNAme((int)topTags.getId());
					
						 try {
							 final URI uri = new URI(Dave.getLink((int)topTags.getId()));
							  
							  class OpenUrlAction implements ActionListener {
							      @Override public void actionPerformed(ActionEvent e) {
							        open(uri);
							      }
							    }
							  
							  link.addActionListener(new OpenUrlAction()); // pour le rendre cliquable 
							  link.setToolTipText(uri.toString()); // reference du http
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						 
				
				 }
					
					link.setForeground(Color.BLUE);
					link.setBorderPainted(false);
					link.setOpaque(false);
					link.setBackground(Color.WHITE);
					Font font = link.getFont();
					Map attributes = font.getAttributes();
					link.setText(str);
					results.add(link);
					results.validate();
					results.setVisible(true);
					panel3.add(results);
					attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					link.setFont(font.deriveFont(attributes));			
					panel3.validate();
				
									
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
				
			}
			
		});
		
		JButton option2 = new JButton("Meilleurs contributeurs");
		option2.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if (appelTag == 0 && appelContributeur==0) {
					appelContributeur += 1;
				} else {
					if(appelContributeur==1){
						int nbComponent = results.getComponentCount();
						for(int i=0;i<nbComponent;i++){
							results.remove(0);
							results.validate();
						}
						appelContributeur=0;
					}else{
						results.remove(0);
						results.validate();
						appelTag=0;
					}
					
				}
	
				String str="";
				results.setLayout(new BoxLayout(results,BoxLayout.PAGE_AXIS ));
				
				
				 List<TagScore> topAnswerer = dave.getTopAnswerers(tagName.getText());
					 
				 for(int i=0;i<topAnswerer.size();i++){
					
					JButton link = new JButton();
					URI uri;
					try {
						uri = new URI(Dave.getLink((int)topAnswerer.get(i).getUser().getUserId()));
						class OpenUrlAction implements ActionListener {
						      @Override public void actionPerformed(ActionEvent e) {
						        open(uri);
						      }
						    }
						  
						  link.addActionListener(new OpenUrlAction()); // pour le rendre cliquable 
						  link.setToolTipText(uri.toString()); // reference du http
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 str = (new ApiManager()).getUserNAme((int)topAnswerer.get(i).getUser().getUserId()) +  " avec " + topAnswerer.get(i).getPostCount() +" messages"; 
					 
					 link.setForeground(Color.BLUE);
					 link.setBorderPainted(false);
					 link.setOpaque(false);
					 link.setBackground(Color.WHITE);
					 Font font = link.getFont();
					 Map attributes = font.getAttributes();
					 attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					 link.setFont(font.deriveFont(attributes));
					 link.setText(str);
					results.add(link);
					results.validate();	
					results.setVisible(true);
					panel3.add(results);
					panel3.validate();
					
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
		});
		
		
		panel1.add(title);
		panel3.setMaximumSize(new Dimension(800,1000));
		panel1.add(getParametre());
		panel1.add(tagName);
		panel2.add(option1);
		panel2.add(option2);
		panel3.add(panel1);
		panel3.add(panel2);
	    
		panel3.setVisible(true);
		panel3.revalidate();

		this.getPanel().add(panel3);
		
		getParametre().addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent event){
				JFrame parametre = new JFrame("Parametres");
				
				results.setLayout(new BoxLayout(panel3,BoxLayout.PAGE_AXIS ));
				JButton validation = new JButton("Valider");
				
				JButton reinitialiser = new JButton("Réinitialiser");
				
				ButtonGroup bg = new ButtonGroup();

				
				JPanel panelParam = new JPanel();				
				panelParam.setLayout(new FlowLayout());	
				
				JPanel panelParam1 = new JPanel();
				panelParam1.setLayout(new FlowLayout());
				
				JPanel panelParam2 = new JPanel();
				panelParam2.setLayout(new FlowLayout());
				
				parametre.pack();
				parametre.setResizable(true);
				parametre.setSize(300, 250);
				
				JTextField modifications1 = new JTextField(5);
				modifications1.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramDave1 = new JLabel( "Nombre d'users affichés : ");
				modifications1.setText(Integer.toString(dave.getNbUsers()));
				
				JTextField modifications2 = new JTextField(5);
				modifications2.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramDave2 = new JLabel( "Mise à jour toute les (heures) : ");
				modifications2.setText(Integer.toString(dave.getRefreshRateTopAnswerers()));
				
				
				 JRadioButton yes = new JRadioButton("Oui");
				 JRadioButton no = new JRadioButton("Non");
  
				 bg.add(yes);
				 bg.add(no);
		 
				JLabel paramDave3 = new JLabel( "Mise à jour forcée : ");	
				paramDave3.add(yes);
				paramDave3.add(no);
				 
				
				panelParam.add(paramDave1);
				panelParam.add(modifications1);
				
				panelParam1.add(paramDave2);
				panelParam1.add(modifications2);
				
				panelParam2.add(paramDave3);
				panelParam2.add(yes);
				panelParam2.add(no);
				
				panelParam.add(panelParam1);
				panelParam.add(panelParam2);
				panelParam.add(validation);
				panelParam.add(reinitialiser);
				parametre.getContentPane().add(panelParam);
				parametre.setVisible(true);
				
				validation.addMouseListener(new MouseListener(){

					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						
						int nbUsers = Integer.parseInt(modifications1.getText()) ;
						int refreshRateTopAnswerers= Integer.parseInt(modifications2.getText());
						 // int nbAnswers =Integer.parseInt(modifications3.getText());
						
						dave.setNbUsers(nbUsers);
						dave.setRefreshRateTopAnswerers(refreshRateTopAnswerers);
						// al.setNbAnswers(nbAnswers);
						
						if(yes.isSelected()){
						dave= (new UserFactory()).newDave().withNbUsers(nbUsers).withRefreshRate(refreshRateTopAnswerers).withForceUpdate().get();
						}else{
							dave= (new UserFactory()).newDave().withNbUsers(nbUsers).withRefreshRate(refreshRateTopAnswerers).withoutForceUpdate().get();
						}
						
						parametre.dispose();
						try {
							PropertiesConfiguration config = new PropertiesConfiguration(PARAMETERS_PATH);
							config.setProperty("nbUsers", nbUsers);
							config.setProperty("refreshRate", refreshRateTopAnswerers);
							if(yes.isSelected())
								config.setProperty("forceUpdate", "true");
							else
								config.setProperty("forceUpdate", "false");
							config.save();
						} catch (ConfigurationException e1) {
							e1.printStackTrace();
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
				});
				reinitialiser.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						modifications1.setText("10");
						modifications2.setText("24");

						dave= (new UserFactory()).newDave().withNbUsers(10).withRefreshRate(24).withoutForceUpdate().get();
						parametre.dispose();
						try {
							PropertiesConfiguration config = new PropertiesConfiguration(PARAMETERS_PATH);
							config.setProperty("nbUsers", 10);
							config.setProperty("refreshRate", 24);

							config.setProperty("forceUpdate", "false");
							config.save();

						} catch (ConfigurationException e1) {
							e1.printStackTrace();
						}
						

					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						
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
			 
		});
	}

	/**
	 * Checks if the tag list entered by the user isn't empty
	 * @return true if the list's length is > 1
	 */
	public boolean isListTag() {
		String text = tagName.getText();
		String[] tagEntered = text.split(" ");
		if (tagEntered.length != 1) {
			isList = true;
		}
		return isList;
	}

	/**
	 * Method used to make URIs from the GUI clickable by the user
	 * @param uri The URI the user wishes to access
	 */
	private static void open(URI uri) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
				/* TODO: error handling */ }
		} else {
			/* TODO: error handling */ }
	}

}
