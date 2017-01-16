package fr.tse.info4.project.view.users;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.User;

import fr.tse.info4.project.controller.BobMethod;
import fr.tse.info4.project.controller.UserFactory;
import fr.tse.info4.project.model.user.Bob;
import fr.tse.info4.project.view.ref.TabReference;


public class PageBob extends TabReference {
	
	Bob bob=null;
	
	public PageBob(String acessToken) throws IOException {

		super();
		bob = new UserFactory(acessToken).newBob().get();
		this.getPanel().add(printBobResults(this));

	}
	
	public PageBob(int id) throws IOException {

		super();
		bob = new UserFactory().newBob().get();
		bob.setIdUser(id);
		this.getPanel().add(printBobResults(this));

	}
	
	public JPanel printBobResults(PageBob bobPage) throws IOException {

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
	
	private JPanel showQuestion(PageBob bobPage){
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
					if(BobMethod.hasQuestion(questions) == false){
						str+="\n Pas de questions semblables trouvées.";
					}else{
						for (int i=0;i<questions.size();i++){
							str+="\n- "+questions.get(i).getTitle()+"   "+Bob.getLinkQuestion((int)questions.get(i).getQuestionId());
						}
					}
					
					JLabel similarQ = new JLabel(str);
					result.add(similarQ);
					result.validate();
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
					if(BobMethod.hasKeyWords(keyWords) == false){
						str+="\n Pas de mots-clés trouvés.";
					}else{
						for (int i=0;i<keyWords.size();i++){
							str+="\n- "+keyWords.get(i);
						}
					}
					
					JLabel similarQ = new JLabel(str);
					result.add(similarQ);
					result.validate();
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
		
		return result;	
	}

	private JPanel showExperts(PageBob bobPage){
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
			result.add(title);
			
			for(User user : users){
				JLabel expert = new JLabel("- "+user.getDisplayName() + "    "+ Bob.getLink((int)user.getUserId()));
				result.add(expert);
			}
		}
		return result;	
	}


	private JPanel showNewQuestionAnswered(PageBob bobPage){
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));
		
		JLabel title = new JLabel("Questions répondues récemment : ");
		result.add(title);
		Map<String, List<Question>> newQuestions = bob.getNewQuestionsAnswered();
		for (Entry<String, List<Question>> questionEntry : newQuestions.entrySet()){
			String tagName = questionEntry.getKey();
			JLabel tag = new JLabel("\n- dans le tag "+tagName + " : ");
			result.add(tag);
			List<Question> questions = questionEntry.getValue();
			for (int i = 0; i < questions.size(); i++) {
				String questionTitle = questions.get(i).getTitle();
				JLabel tagQuestion = new JLabel("\t-- " + questionTitle + "\n "+ (Bob.getLinkQuestion((int)questions.get(i).getQuestionId())));
				tagQuestion.setMaximumSize(new Dimension(500, 20));
				result.add(tagQuestion);
			}
		}
		
		return result;	
	}

}
