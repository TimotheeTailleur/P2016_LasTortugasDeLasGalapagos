package fr.tse.info4.project.view.users;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Answer;
import com.google.code.stackexchange.schema.Question;

import fr.tse.info4.project.controller.UserFactory;
import fr.tse.info4.project.model.user.Alice;
import fr.tse.info4.project.view.ref.TabReference;

public class PageAlice extends TabReference {
	
	Alice al=null;
	
	public PageAlice(String acessToken) throws IOException, URISyntaxException {

		super();
		al = new UserFactory(acessToken).newAlice().get();
		this.getPanel().add(printAliceResults(this));

	}
	
	public PageAlice(int id) throws IOException, URISyntaxException {

		super();
		al = new UserFactory().newAlice().get();
		al.setIdUser(id);
		this.getPanel().add(printAliceResults(this));

	}

	public JPanel printAliceResults(PageAlice alice) throws IOException, URISyntaxException {

		JPanel resultat = new JPanel();

		JPanel newQestion = alice.showNewQuestion(alice);
		// JPanel comparedBadges = showComparedBadge;//new JPanel();
		JPanel sortQuestion = alice.showSortedAnsweredQuestions(alice);


		resultat.setLayout(new BoxLayout(resultat, BoxLayout.LINE_AXIS));
		resultat.add(newQestion);
		// resultat.add(comparedBadges);
		resultat.add(sortQuestion);
		resultat.setVisible(true);

		return resultat;
	}

	private JPanel showNewQuestion(PageAlice alice) throws IOException, URISyntaxException {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));


		Map<String[], PagedList<Question>> newQuestion = al.getNewQuestions();

		JLabel title = new JLabel("<html><b />Les nouvelles questions : </html>");
		// title.setFont( title.getFont().deriveFont(title.BOLD) );
		result.add(title);
		

		for (Entry<String[], PagedList<Question>> tagEntry : newQuestion.entrySet()) {
			String[] tagName = tagEntry.getKey();
			String str= "\n- dans le(s) tag(s) ";
			int nb = tagName.length;
			for(int i = 0;i<nb;i++){
				if(i!=(nb-1) && nb != 1 ){
					str+= tagName[i] +", ";
				}else{
					str+= tagName[i];
				}
			}
			str += " : ";
			JLabel tag = new JLabel(str);
			result.add(tag);
			PagedList<Question> questionMap = tagEntry.getValue();
			for (int i = 0; i < questionMap.getPageSize(); i++) {
				String questionTitle = questionMap.get(i).getTitle();
				
				final URI uri = new URI(Alice.getLinkQuestion((int)questionMap.get(i).getQuestionId()));
				
			    class OpenUrlAction implements ActionListener {
			      @Override public void actionPerformed(ActionEvent e) {
			        open(uri);
			      }
			    }
			    
				JButton link = new JButton();
				link.setText(questionTitle);
				link.setForeground(Color.BLUE);
				link.setBorderPainted(false);
				link.setOpaque(false);
				link.setBackground(Color.WHITE);
				Font font = link.getFont();
				Map attributes = font.getAttributes();
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				link.setFont(font.deriveFont(attributes));
				link.setToolTipText(uri.toString()); 
				link.addActionListener(new OpenUrlAction());
				result.add(link);
				result.validate();
			}
		}

		return result;
	}

	private JPanel showSortedAnsweredQuestions(PageAlice alice) throws URISyntaxException {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

								
		List<Answer> listQuestion = al.getSortedAnswers();

		JLabel title = new JLabel("Questions répondues triées : ");
		result.add(title);

		for (int i = 0; i < listQuestion.size(); i++) {
			final URI uri = new URI(Alice.getLinkQuestion((int)listQuestion.get(i).getQuestionId()));
			
		    class OpenUrlAction implements ActionListener {
		      @Override public void actionPerformed(ActionEvent e) {
		        open(uri);
		      }
		    }
		    
			JButton link = new JButton();
			link.setText("... avec un score de " + listQuestion.get(i).getScore());
			link.setToolTipText(uri.toString());
			link.addActionListener(new OpenUrlAction());
			result.add(link);
			result.validate();
		}

		return result;
	}

	private JPanel showComparedBadge(PageAlice alice) {
		JPanel result = new JPanel();
		
		ArrayList<Integer> list = al.compareBadge();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

		return result;
	}
	
	private static void open(URI uri) {
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { /* TODO: error handling */ }
	    } else { /* TODO: error handling */ }
	}
	
}
