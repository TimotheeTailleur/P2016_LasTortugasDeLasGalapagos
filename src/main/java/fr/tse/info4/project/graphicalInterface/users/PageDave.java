package fr.tse.info4.project.graphicalInterface.users;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.tse.info4.project.graphicalInterface.ref.TabReference;

import fr.tse.info4.project.user.Dave;


public class PageDave extends TabReference {

	JTextField tagName = new JTextField(25);
	

/**
 * 
 */
	public PageDave() {
		super();

		JPanel text = new JPanel();
		tagName.setHorizontalAlignment(JTextField.CENTER);
		
		JPanel boutons = new JPanel();
		boutons.setLayout(new BoxLayout(boutons, BoxLayout.LINE_AXIS));

		JButton option1 = new JButton("bouton");
		JButton option2 = new JButton("bouton");

		boutons.add(option1);
		boutons.add(option2);
		
		option1.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent event){
			
				 String nomTag = tagName.getText(); 
				 int longueur=0;
				 
				 for(int i=0; i<=nomTag.length()-1; i++) { // count how many tag were entered by the user

					        if(nomTag.charAt(i)==' '){

					          longueur++;
					        }  
					
					      }                
			        longueur+=1;
					  
				 
				 if (longueur == 1){ // if one tag was written, launch function 1
					
				 }
				 else { // else launch function 3
					 nomTag.split(",;/:?!\\\n");					 
				 }
		}
		});
		

		option1.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent event){
				 String nomTag = tagName.getText(); // read the tad written by the user and launch function 2
				 
				 }
		});
		
			 
		

		text.add(tagName);
		text.setSize(1000, 900);
		this.getPanel().add(text);
		this.getPanel().add(boutons);
	}

	public boolean isListTag() {
		boolean isList = false;
		String text = tagName.getText();
		String[] tagEntered = text.split(" ");
		if (tagEntered.length != 1) {
			isList = true;
		}
		return isList;
	}
	
	
}
