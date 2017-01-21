package fr.tse.info4.project.view.users;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.User;

import fr.tse.info4.project.controller.BobMethod;
import fr.tse.info4.project.controller.UserFactory;
import fr.tse.info4.project.model.user.Bob;
import fr.tse.info4.project.view.ref.TabReference;


public class PageBob extends TabReference {
	
	Bob bob=null;
	
	public PageBob(String acessToken) throws IOException, URISyntaxException {

		super();
		bob = new UserFactory(acessToken).newBob().get();
		this.getPanel().add(printBobResults(this));

	}
	
	public PageBob(int id) throws IOException, URISyntaxException {

		super();
		bob = new UserFactory().newBob().get();
		bob.setIdUser(id);
		this.getPanel().add(printBobResults(this));

	}
	
	public JPanel printBobResults(PageBob bobPage) throws IOException, URISyntaxException {

		JPanel resultat = new JPanel();

		JPanel similarQuestion = bobPage.showQuestion(bobPage);
		JPanel keyWords = bobPage.showKeyWords(bobPage);
		JPanel experts = bobPage.showExperts(bobPage);
		JPanel newQuestionAnswered = bobPage.showNewQuestionAnswered(bobPage);


		resultat.setLayout(new BoxLayout(resultat, BoxLayout.LINE_AXIS));
		resultat.add(similarQuestion);
		resultat.add(keyWords);
		resultat.add(experts);
		resultat.add(newQuestionAnswered);
		resultat.setVisible(true);

		return resultat;
	}
	
	private JPanel showQuestion(PageBob bobPage) throws URISyntaxException{
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));
		JTextField text = new JTextField(35);
		text.setMaximumSize(new Dimension(850,20));
		JButton valid = new JButton("Rechercher");
		
		result.add(text);
		result.add(valid);
		
		
		valid.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
				if(BobMethod.isEmpty(text) == true){
					JLabel error = new JLabel("Rentrer une question, s'il vous plait.");
					result.add(error);
					result.validate();
				}else{
					List<Question> questions = bob.findSimilarQuestions(text.getText());
					String str = "Questions similaires à celle écrite : ";
					JLabel title = new JLabel(str);
					title.setFont(new Font("Tahoma", Font.BOLD, 17));
					title.setBorder(new EmptyBorder(0, 0, 10, 0));
					result.add(title);
					String str2="";
					if(BobMethod.hasQuestion(questions) == false){
						str2+="\n Pas de questions semblables trouvées.";
						JLabel similarQ = new JLabel(str2);
						result.add(similarQ);
						result.validate();
					}else{
						for (int i=0;i<questions.size();i++){
							URI uri = null;
							try {
								uri = new URI(Bob.getLinkQuestion((int)questions.get(i).getQuestionId()));
							} catch (URISyntaxException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
						    class OpenUrlAction implements ActionListener {
						      @Override public void actionPerformed(ActionEvent e) {
						        open(uri);
						      }
						    }
						    
							JButton link = new JButton();
							link.setText(questions.get(i).getTitle());
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
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		result.validate();
		result.setBorder(new EmptyBorder(10, 50, 10, 30));
		return result;	
	}
	
	
	private JPanel showKeyWords(PageBob bobPage){
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));
		JTextField text = new JTextField(35);
		text.setMaximumSize(new Dimension(850,20));
		JButton valid = new JButton("Suggérer");
		
		result.add(text);
		result.add(valid);
		
		valid.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
				if(BobMethod.isEmpty(text) == true){
					JLabel error = new JLabel("Rentrer une question, s'il vous plait.");
					result.add(error);
					result.validate();
				}else{
					List<String> keyWords = bob.findKeyWords(text.getText());
					String str = "Mots-clés pouvant être rajoutés : ";
					JLabel title = new JLabel(str);
					title.setFont(new Font("Tahoma", Font.BOLD, 17));
					title.setBorder(new EmptyBorder(0, 0, 10, 0));
					result.add(title);
					if(BobMethod.hasKeyWords(keyWords) == false){
						String str2="Pas de mots-clés trouvés.";
						JLabel similarQ = new JLabel(str2);
						result.add(similarQ);
						result.validate();
					}else{
						for (int i=0;i<keyWords.size();i++){
							String str2="- "+keyWords.get(i);
							JLabel similarQ = new JLabel(str2);
							result.add(similarQ);
							result.validate();
						}
					}
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		result.setBorder(new EmptyBorder(10, 30, 10, 30));
		return result;	
	}

	private JPanel showExperts(PageBob bobPage) throws URISyntaxException{
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));
		
		String show = "";
		Set<User> users = bob.getExperts();
		if(BobMethod.hasExpert(users) == false){
			show+="Pas d'user satisfaisant trouvé.";
			JLabel title = new JLabel(show);
			result.add(title);
		}else{
			show+="Liste d'user potentiellement intéressant : ";
			JLabel title = new JLabel(show);
			title.setFont(new Font("Tahoma", Font.BOLD, 17));
			title.setBorder(new EmptyBorder(0, 0, 10, 0));
			result.add(title);
			
			for(User user : users){
				final URI uri = new URI(Bob.getLink((int)user.getUserId()));
				
			    class OpenUrlAction implements ActionListener {
			      @Override public void actionPerformed(ActionEvent e) {
			        open(uri);
			      }
			    }
			    
				JButton link = new JButton();
				link.setText(user.getDisplayName().replace("\"", " "));
				link.setToolTipText(uri.toString());
				link.addActionListener(new OpenUrlAction());
				result.add(link);
				result.validate();
			}
		}
		result.setBorder(new EmptyBorder(10, 30, 10, 30));
		return result;	
	}


	private JPanel showNewQuestionAnswered(PageBob bobPage) throws URISyntaxException{
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));
		
		JLabel title = new JLabel("Questions répondues récemment : ");
		title.setFont(new Font("Tahoma", Font.BOLD, 17));
		result.add(title);
		title.setBorder(new EmptyBorder(0, 0, 10, 0));
		Map<String, List<Question>> newQuestions = bob.getNewQuestionsAnswered();
		for (Entry<String, List<Question>> questionEntry : newQuestions.entrySet()){
			String tagName = questionEntry.getKey();
			JLabel tag = new JLabel("\n- dans le tag "+tagName + " : ");
			tag.setBorder(new EmptyBorder(10, 0, 10, 0));
			result.add(tag);
			List<Question> questions = questionEntry.getValue();
			for (int i = 0; i < questions.size(); i++) {
				String questionTitle = questions.get(i).getTitle();
				
				final URI uri = new URI(Bob.getLinkQuestion((int)questions.get(i).getQuestionId()));
				
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
		result.setBorder(new EmptyBorder(10, 30, 10, 50));
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
