package fr.tse.info4.project.view.users;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.User;

import fr.tse.info4.project.controller.BobMethod;
import fr.tse.info4.project.controller.UserFactory;
import fr.tse.info4.project.model.datarecovery.Authenticate;
import fr.tse.info4.project.model.user.Alice;
import fr.tse.info4.project.model.user.Bob;
import fr.tse.info4.project.view.ref.TabReference;


public class PageBob extends TabReference {
	
	Bob bob=null;
	
	public PageBob() {

		super();
		this.getPanel().add(panelConnexion(this));
	}
	
	public JPanel panelConnexion(PageBob bobPage) {

		JPanel connexion = new JPanel();

		JButton ButtonConnexion = new JButton("Connexion");
		connexion.add(ButtonConnexion);

		final JButton offline = new JButton("Sans compte Stackoverflow");
		connexion.add(offline);

		ButtonConnexion.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				String accessToken = Authenticate.getAcessToken();
				bob = new UserFactory(accessToken).newBob().get();
				
				ButtonConnexion.setVisible(false);
				offline.setVisible(false);
				try {
					bobPage.getPanel().add(printBobResults(bobPage));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

		});



		offline.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				bob = new UserFactory().newBob().get();
				bob.setIdUser(1200);
				
				ButtonConnexion.setVisible(false);
				offline.setVisible(false);
				try {
					bobPage.getPanel().add(printBobResults(bobPage));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

		});

		return connexion;
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
	
	//List<Question> findSimilarQuestions(String questionTitle)
	private JPanel showQuestion(PageBob bobPage){
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));
		
		
		return result;	
	}
	
	//List<String> findKeyWords(String questionTitle)
	private JPanel showKeyWords(PageBob bobPage){
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));
		
		
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
