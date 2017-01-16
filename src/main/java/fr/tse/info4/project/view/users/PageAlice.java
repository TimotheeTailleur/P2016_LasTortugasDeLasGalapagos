package fr.tse.info4.project.view.users;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
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
	
	public PageAlice(String acessToken) throws IOException {

		super();
		al = new UserFactory(acessToken).newAlice().get();
		this.getPanel().add(printAliceResults(this));

	}
	
	public PageAlice(int id) throws IOException {

		super();
		al = new UserFactory().newAlice().get();
		al.setIdUser(id);
		this.getPanel().add(printAliceResults(this));

	}

	public JPanel printAliceResults(PageAlice alice) throws IOException {

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

	private JPanel showNewQuestion(PageAlice alice) throws IOException {
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
				JLabel tagQuestion = new JLabel("\t-- " + questionTitle + "\n "+ (Alice.getLinkQuestion((int)questionMap.get(i).getQuestionId())));
				tagQuestion.setMaximumSize(new Dimension(500, 20));
				result.add(tagQuestion);
			}
		}

		return result;
	}

	private JPanel showSortedAnsweredQuestions(PageAlice alice) {
		JPanel result = new JPanel();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

								
		List<Answer> listQuestion = al.getSortedAnswers();

		JLabel title = new JLabel("<html><b />Questions répondues triées : </html>");
		// title.setFont( title.getFont().deriveFont(title.BOLD) );
		result.add(title);

		for (int i = 0; i < listQuestion.size(); i++) {
			JLabel question = new JLabel(Alice.getLinkQuestion((int)listQuestion.get(i).getQuestionId()) + " avec un score de " + listQuestion.get(i).getScore());
			result.add(question);
		}

		return result;
	}

	private JPanel showComparedBadge(PageAlice alice) {
		JPanel result = new JPanel();
		
		ArrayList<Integer> list = al.compareBadge();
		result.setLayout(new BoxLayout(result, BoxLayout.PAGE_AXIS));

		return result;
	}
	
}
