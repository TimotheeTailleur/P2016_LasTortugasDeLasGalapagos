package fr.tse.info4.project.datarecovery;

import java.awt.RenderingHints.Key;

import com.google.code.stackexchange.client.StackExchangeApiClient;
import com.google.code.stackexchange.client.StackExchangeApiClientFactory;
import com.google.code.stackexchange.client.impl.StackExchangeApiJsonClient;
import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;
import com.google.code.stackexchange.client.query.impl.AnswerApiQueryImpl;
import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Answer;
import com.google.code.stackexchange.schema.Paging;
import com.google.code.stackexchange.schema.Question;

public class AliceApiManager {

	/**
	 * Return the new questions in the tag passed as a parameter.
	 * 
	 * @param tag
	 * @param nbQuestions
	 * @return
	 */
	public static PagedList<Question> getNewQuestionsByTag(String tag, int nbQuestions) {
		final StackExchangeApiQueryFactory factory = StackExchangeApiQueryFactory.newInstance(ApiManager.APP_KEY,
				ApiManager.SITE);
		Paging page = new Paging(1, nbQuestions);
		PagedList<Question> questions = factory.newQuestionApiQuery().withTags(tag).withPaging(page)
				.withSort(Question.SortOrder.MOST_RECENTLY_CREATED).list();
		try {
			Thread.sleep(questions.getBackoff() * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return questions;
	}
	
	
	/**
	 * 
	 * Return the anwers of a user, sorted by questions score.
	 * 
	 * @param idUser
	 * @param nbQuestions
	 * @return
	 */
	public static PagedList<Answer> getAnswersId(int idUser, int nbQuestions) {
		StackExchangeApiQueryFactory queryFactory = StackExchangeApiQueryFactory.newInstance(ApiManager.APP_KEY,
				ApiManager.SITE);

		String filter = "!-*f(6t0WUWsK";
		if (nbQuestions<=100){
			Paging page = new Paging(1,nbQuestions);
			
			PagedList<Answer> answers = queryFactory.newAnswerApiQuery().withUserIds(idUser).withFilter(filter)
					.withSort(Answer.SortOrder.MOST_VOTED).withPaging(page).listByUsers();

			return answers;
		}
		else{
			float nbPages = (float) nbQuestions / (float) 100;
			System.out.println((int)nbPages);
			
			PagedList<Answer> answer = null;
			for (int i = 1; i<=(int)nbPages ; i++){
				Paging page = new Paging(i, 100);
				PagedList<Answer> answersPage = queryFactory.newAnswerApiQuery().withUserIds(idUser).withFilter(filter)
						.withSort(Answer.SortOrder.MOST_VOTED).withPaging(page).listByUsers();
				if (i == 1){
					answer = answersPage;
				}
				else{
					for (int j = 0; j<answersPage.size(); j++){
						answer.add(answersPage.get(j));
					}					
				}
				
				try {
					Thread.sleep(answersPage.getBackoff() *1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			int nbAnswersLastPage = nbQuestions - (int)nbPages*100;
			System.out.println(nbAnswersLastPage);
			Paging lastPage = new Paging((int)nbPages+1, 100);
			PagedList<Answer> answersPage = queryFactory.newAnswerApiQuery().withUserIds(idUser).withFilter(filter)
					.withSort(Answer.SortOrder.MOST_VOTED).withPaging(lastPage).listByUsers();
			for (int j = 0; j<nbAnswersLastPage; j++){
				answer.add(answersPage.get(j));
			}	
			try {
				Thread.sleep(answersPage.getBackoff() *1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return answer;
		}
		
	}

	public static void main(String[] args) {
		StackExchangeApiQueryFactory queryFactory = StackExchangeApiQueryFactory.newInstance(ApiManager.APP_KEY,
				ApiManager.SITE);
		PagedList<Question> answers = queryFactory.newQuestionApiQuery().withSort(Question.SortOrder.MOST_VOTED).withUserIds(1200).list();
		for (int i =0; i<answers.size(); i++){
			System.out.println(answers.get(i).getTitle());
			System.out.println(answers.get(i).getScore());
		}
	}

}
