package fr.tse.info4.project.graphicalInterface.ref;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * This class is a reference for tabs of the class HomePage. It fixes the bottom
 * for each tabs.
 *
 */
public class TabReference extends JPanel {
	/**
	 * This attribute is modified by each tab for schowing results.
	 */
	JPanel panel = new JPanel();
	/**
	 * This attribute is modified by each tab by settings chosen when method of
	 * users story are called.
	 */
	JLabel param = new JLabel("Paramètres :");

	 JPanel foot = new JPanel();

	public TabReference() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		this.add(panel);

		foot.setLayout(new BoxLayout(foot, BoxLayout.PAGE_AXIS));

		JPanel settings = new JPanel();
		settings.setLayout(new BoxLayout(settings, BoxLayout.LINE_AXIS));

		JButton boutonSettings = new JButton("Paramètres");
		JButton boutonLeave = new JButton("Quitter");

		settings.add(param);
		settings.add(boutonSettings);

		foot.add(settings);
		foot.add(boutonLeave);

		this.add(foot);
	}

	public void setSettings() {

		param.setText("");
	}

	public JPanel getFoot() {
		return foot;
	}

	public void setFoot(JPanel foot) {
		this.foot = foot;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public JLabel getParam() {
		return param;
	}

	public void setParam(JLabel param) {
		this.param = param;
	}

}
