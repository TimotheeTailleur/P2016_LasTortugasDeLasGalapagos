package fr.tse.info4.project.view.ref;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Superclass for user story tabs. 
 * <br> Defines a common footer for each tab
 * <br> Defines a common architecture for use of parameters (Each tab will then individually define 
 * <br> what its parameters are and their values)
 */
public class TabReference extends JPanel {
	/**
	 * Attribute modified by each tab to display results.
	 */
	JPanel panel = new JPanel();

	/**
	 * Attributes that contains the button for quitting the app ; common to all tabs 
	 */
	JPanel foot = new JPanel();

	/**
	 * Button modified by each tab and used to show & modify their parameters.
	 */
	JButton Parametres = new JButton("Parametres");

	public static final String PARAMETERS_PATH = "Data" + File.separator  + "parameters.properties";

	protected static Properties prop = new Properties();

	static {
		InputStream input = null;
		try {
			input = new FileInputStream(PARAMETERS_PATH);
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Constructor class which initializes the footer
	 * and the results JPanel of results for each subclass.
	 */
	public TabReference() {

		JButton Quitter = new JButton("Quitter");
		Quitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		foot.add(Quitter);
		foot.setAlignmentX(CENTER_ALIGNMENT);

		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setLocation(1700, 0);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(panel);

		foot.setLayout(new BoxLayout(foot, BoxLayout.PAGE_AXIS));
		this.add(foot);
	}

	/**
	 * JButton Parametres getter
	 * @return JButton Parametres
	 */
	public JButton getParametre() {
		return Parametres;
	}

	/**
	 * Results JPanel getter
	 * @return JPanel panel
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * Results JPanel setter
	 * @param panel
	 */
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	/**
	 * Footer getter
	 * @return JPanel foot
	 */
	public JPanel getFoot() {
		return foot;
	}

	/**
	 * Footer setter
	 * @param foot
	 */
	public void setFoot(JPanel foot) {
		this.foot = foot;
	}

}
