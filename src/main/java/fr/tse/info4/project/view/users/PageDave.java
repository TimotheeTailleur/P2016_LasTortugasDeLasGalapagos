package fr.tse.info4.project.view.users;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.tse.info4.project.view.ref.TabReference;



public class PageDave extends TabReference {

	boolean isList;
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

		JButton option1 = new JButton("Top Contributor");
		JButton option2 = new JButton("Top Tag");

		boutons.add(option1);
		boutons.add(option2);
		
		option1.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent event){
					
				 isListTag(); 
				 if(isList == false ){ // if one tag was written, launch function 1 and display results
					 System.out.println(("SOLOTAG"));
				 }
				 else { // else launch function 3 and display results
					System.out.println("MULTITAG");		
				 }
		}
		});
		

		option1.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent event){
				 String nomTag = tagName.getText(); // read the tad written by the user,launch function 2  and display results
				 
				 }
		});
			 
		text.add(tagName);
		text.setSize(1000, 900);
		this.getPanel().add(text);
		this.getPanel().add(boutons);
	}

	
	public boolean isListTag() {
		String text = tagName.getText();
		String[] tagEntered = text.split(" ");
		if (tagEntered.length != 1) {
			isList = true;
		}
		return isList;
	}
	
	
}
