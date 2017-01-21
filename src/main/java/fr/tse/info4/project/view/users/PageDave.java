package fr.tse.info4.project.view.users;

import java.awt.Color;
import java.awt.Desktop;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	
	
	 Dave dave = (new UserFactory()).newDave().get();
/**
 * 
 */
	public PageDave() {
		super();
		
		JLabel title = new JLabel("Veuillez entrer un ou plusieurs tags séparés par un espace : ");
		panel1.setLayout(new FlowLayout());
		panel2.setLayout(new FlowLayout());
		panel3.setLayout(new BoxLayout(panel3,BoxLayout.PAGE_AXIS ));
		tagName.setHorizontalAlignment(JTextField.CENTER);

		JButton option1 = new JButton("Top Tag");
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
				isListTag(); 
				JButton link = new JButton();
				JPanel results = new JPanel();
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
						 
						link.setText(str);	
				 }
					
					link.setForeground(Color.BLUE);
					link.setBorderPainted(false);
					link.setOpaque(false);
					link.setBackground(Color.WHITE);
					Font font = link.getFont();
					Map attributes = font.getAttributes();
					attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					link.setFont(font.deriveFont(attributes));
					results.add(link);
					results.validate();
					
						
					results.setVisible(true);
					panel3.add(results);
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
		
		JButton option2 = new JButton("Top Contributor");
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
				JPanel results = new JPanel();
				String str="";
				JButton link = new JButton();
			    Dave dave = (new UserFactory()).newDave().get();
			
				 List<TagScore> topAnswerer = dave.getTopAnswerers(tagName.getText());
					 
				 for(int i=0;i<topAnswerer.size();i++){
					 
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
					 str = (new ApiManager()).getUserNAme((int)topAnswerer.get(i).getUser().getUserId()) +  " avec " + topAnswerer.get(i).getPostCount() +" posts"; 
					 
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
					
					panel3.setVisible(true);
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
		panel1.add(tagName);
		panel1.add(getParametre());
		panel2.add(option1);
		panel2.add(option2);
		
		panel3.add(panel1);
		panel3.add(panel2);
		
		this.getPanel().add(panel3);
		
		getParametre().addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent event){
				JFrame parametre = new JFrame("Parametres");
				
				JButton validation = new JButton("Valider");
				
				JPanel panelParam = new JPanel();				
				panelParam.setLayout(new FlowLayout());	
				
				JPanel panelParam1 = new JPanel();
				panelParam1.setLayout(new FlowLayout());
				
				JPanel panelParam2 = new JPanel();
				panelParam2.setLayout(new FlowLayout());
				
				parametre.pack();
				parametre.setResizable(true);
				parametre.setSize(300, 250);
				
				JTextField modifications1 = new JTextField(10);
				modifications1.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramDave1 = new JLabel( "Nombre d'users affichés : ");
				modifications1.setText(Integer.toString(dave.getNbUsers()));
				
				JTextField modifications2 = new JTextField(10);
				modifications2.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramDave2 = new JLabel( "Mise à jour toute les (heures) : ");
				modifications2.setText(Integer.toString(dave.getRefreshRateTopAnswerers()));
				
				JTextField modifications3 = new JTextField(10);
				modifications3.setHorizontalAlignment(JTextField.CENTER);
				JLabel paramDave3 = new JLabel( "Réponses affichées : ");
				modifications3.setText(Integer.toString(dave.getForceUpdateTopAnswerers()));
				
				panelParam.add(paramDave1);
				panelParam.add(modifications1);
				
				panelParam1.add(paramDave2);
				panelParam1.add(modifications2);
				
				panelParam2.add(paramDave3);
				panelParam2.add(modifications3);
				
				panelParam.add(panelParam1);
				panelParam.add(panelParam2);
				panelParam.add(validation);
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
						
						int nbTags = Integer.parseInt(modifications1.getText()) ;
						int nbQuestionsPerTag= Integer.parseInt(modifications2.getText());
						int nbAnswers =Integer.parseInt(modifications3.getText());
						
						al.setNbTags(nbTags);
						al.setNbQuestionsPerTag(nbQuestionsPerTag);
						al.setNbAnswers(nbAnswers);
						
						parametre.dispose();
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
				
			}
			 
		});

	}


	
	public boolean isListTag() {
		String text = tagName.getText();
		String[] tagEntered = text.split(" ");
		if (tagEntered.length != 1) {
			isList = true;
		}
		return isList;
	}
	
	private static void open(URI uri) {
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { /* TODO: error handling */ }
	    } else { /* TODO: error handling */ }
	}
	
}


