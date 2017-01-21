 package fr.tse.info4.project.view.ref;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * 
 * This class is a reference for tabs of the class HomePage. It fixes the bottom
 * for each tabs.
 *
 */
public class TabReference extends JPanel {
	/**
	 * This attribute is modified by each tab for showing results.
	 */
	JPanel panel = new JPanel();
	
	/**
	 * This attribute is modified by each tab by settings chosen when method of
	 * users story are called.
	 */
	 JPanel foot = new JPanel();
	 
	 


	public TabReference() {
		
		JButton Quitter = new JButton("Quitter");
		Quitter.addActionListener(new ActionListener(){
			 public void actionPerformed(ActionEvent event){
				 System.exit(0); 
				 }
		});
		foot.add(Quitter);
		foot.setAlignmentX(CENTER_ALIGNMENT);
		
		JButton Parametres = new JButton("Parametres");
			
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(Parametres);
		panel.setLocation(1700, 0);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(panel);
		
		
		foot.setLayout(new BoxLayout(foot, BoxLayout.PAGE_AXIS));
		this.add(foot);
	}


public JButton getParametre() {
	return (JButton) panel.getComponent(0);
}

public JPanel getPanel() {
	return panel;
}


public void setPanel(JPanel panel) {
	this.panel = panel;
}


public JPanel getFoot() {
	return foot;
}

public void setFoot(JPanel foot) {
	this.foot = foot;
}

}
