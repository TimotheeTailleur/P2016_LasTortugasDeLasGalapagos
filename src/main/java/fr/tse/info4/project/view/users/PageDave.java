package fr.tse.info4.project.view.users;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.tse.info4.project.model.user.Dave;
import fr.tse.info4.project.view.ref.TabReference;



public class PageDave extends TabReference {

	boolean isList;
	JTextField tagName = new JTextField(35);
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();
/**
 * 
 */
	public PageDave() {
		super();
		
		panel1.setLayout(new FlowLayout());
		panel2.setLayout(new FlowLayout());
		panel3.setLayout(new BoxLayout(panel3,BoxLayout.PAGE_AXIS ));
		tagName.setHorizontalAlignment(JTextField.CENTER);

		JButton option1 = new JButton("Top Tag");
		option1.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent event){
					
				 isListTag(); 
				 
				 if(isList == false ){ // if one tag was written, launch function 1 and display results
					Dave dave=new Dave();
					dave.getTopTag(tagName.getText());
				 }
				 else { // else launch function 3 and display results
					System.out.println("MULTITAG");		
				 }
		}
		});
		
		JButton option2 = new JButton("Top Contributor");
		option2.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent event){
				 String nomTag = tagName.getText(); // read the tag written by the user,launch function 2  and display results
				 
				 }
		});
		
		

		panel1.add(tagName);
		panel2.add(option1);
		panel2.add(option2);
		
		panel3.add(panel1);
		panel3.add(panel2);
		
		this.getPanel().add(panel3);
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
