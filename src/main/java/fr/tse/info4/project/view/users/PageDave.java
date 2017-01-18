package fr.tse.info4.project.view.users;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.tse.info4.project.controller.UserFactory;
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
				 
				 if(isList == false ){ // if one tag was written, launch function 1 and display results
					 JPanel results = new JPanel();
					 Dave dave = (new UserFactory()).newDave().get();
					 
					 TagScore topTag = dave.getTopTag(tagName.getText());
					 
					 String str = Dave.getLink((int)topTag.getUser().getUserId()) + " avec un score de " + topTag.getScore();
					 
					 JLabel user = new JLabel(str);
					 results.add(user);
					 panel3.add(results);
					 
					 panel3.validate();
					 
					 results.setVisible(true);
					 
				 }else{
						JPanel results = new JPanel();
						 Dave dave = (new UserFactory()).newDave().get();
						 
					
						 String[] resultsTab = tagName.getText().split(" ");
						 List<String> resultsList = new ArrayList<String>();
						 
						 for(int i=0;i<resultsTab.length;i++){
							 resultsList.add(resultsTab[i]);
						 }
						 TopUser topTags = dave.getTopUserMultipleTags(resultsList);
						 
						 String str = Dave.getLink((int)topTags.getId());
						 
						 JLabel user = new JLabel(str);
						 results.add(user);
						 panel3.add(results);
						 panel3.validate();
						 
						 results.setVisible(true);
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
			    Dave dave = (new UserFactory()).newDave().get();
			
				 List<TagScore> topAnswerer = dave.getTopAnswerers(tagName.getText());
				
				 
				 for(int i=0;i<topAnswerer.size();i++){
					 String str=Dave.getLink((int)topAnswerer.get(i).getUser().getUserId()) + " avec " + topAnswerer.get(i).getPostCount() +" posts";
					 JLabel user = new JLabel(str);
					 results.add(user);
					 			 
				 }
				
				 panel3.add(results);	
				 panel3.validate();
				 results.setVisible(true);
				 		
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
