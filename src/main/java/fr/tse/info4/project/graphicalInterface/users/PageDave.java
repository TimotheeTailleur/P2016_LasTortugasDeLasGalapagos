package fr.tse.info4.project.graphicalInterface.users;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.tse.info4.project.graphicalInterface.ref.TabReference;


public class PageDave extends TabReference {

	JTextField tagName = new JTextField("");

	public PageDave() {
		super();

		JPanel text = new JPanel();

		// tagName.setSize(40, 20); //marche pas
		JPanel boutons = new JPanel();
		boutons.setLayout(new BoxLayout(boutons, BoxLayout.LINE_AXIS));

		JButton option1 = new JButton("bouton");
		JButton option2 = new JButton("bouton");

		boutons.add(option1);
		boutons.add(option2);

		text.add(tagName);
		text.setSize(1000, 900);// marche pas
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
