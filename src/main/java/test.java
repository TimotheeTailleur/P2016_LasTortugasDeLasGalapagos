import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class test extends JPanel {

	public test() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel label = new JLabel("My Label");
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(label);

		JScrollPane pane = new JScrollPane(new JTextArea(3, 15));
		pane.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(pane);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new test();
	}
}
