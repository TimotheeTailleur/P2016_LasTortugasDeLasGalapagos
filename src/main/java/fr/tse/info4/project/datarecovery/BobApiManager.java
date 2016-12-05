package fr.tse.info4.project.datarecovery;

import java.util.ArrayList;


import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;
import com.google.code.stackexchange.client.query.impl.TagApiQueryImpl;
import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Paging;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.Tag;

public class BobApiManager {

	

	/**
	 * Return the new questions in the tag passed as a parameter. <br>
	 * The questions returned have at least one answer
	 * 
	 * @param tag
	 * @param nbQuestions
	 * @return
	 */
	public static ArrayList<Question> getAnsweredQuestionsByTag(String tag, int nbQuestions) {
		final StackExchangeApiQueryFactory factory = StackExchangeApiQueryFactory.newInstance(ApiManager.APP_KEY,
				ApiManager.SITE);
		int numPage = 1;
		int nbQuestionsAdded = 0;
		ArrayList<Question> questionsAnswered = new ArrayList<Question>(nbQuestions);
		while (nbQuestionsAdded < nbQuestions) {

			Paging page = new Paging(numPage, 100);
			PagedList<Question> questions = factory.newQuestionApiQuery().withTags(tag).withPaging(page)
					.withSort(Question.SortOrder.MOST_RECENTLY_CREATED).list();

			

			for (int i = 0; i < questions.size(); i++) {
				if (questions.get(i).getAnswerCount() > 0 && nbQuestionsAdded < nbQuestions) {
					questionsAnswered.add(questions.get(i));
					nbQuestionsAdded++;
				}
			}
			numPage++;
			try {
				Thread.sleep(questions.getBackoff() * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return questionsAnswered;
	}

}
