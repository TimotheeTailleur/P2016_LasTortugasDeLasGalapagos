package fr.tse.info4.project.model.datarecovery;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.code.stackexchange.client.constant.ApplicationConstants;
import com.google.code.stackexchange.client.provider.url.ApiUrlBuilder;
import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Paging;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.StackExchangeSite;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BobApiManager extends ApiManager {

	/**
	 * Search for similar questions with Stackexchange API using the SDK.
	 */
	public static final String SEARCH_SIMILAR_QUESTIONS = "com.google.code.stackexchange.client.searchSimilarQuestions";

	/**
	 * Filter used to get similar questions.
	 */
	public static final String SIMILAR_QUESTIONS_FILTER = "!bA1d_KFuD9Rmoo";

	/**
	 * Constructor to use applicationKey and specify StackExchangeSite
	 * 
	 * @param applicationKey
	 * @param site
	 */
	public BobApiManager(String applicationKey, StackExchangeSite site) {
		super(applicationKey, site);
	}

	/**
	 * Default Constructor
	 */
	public BobApiManager() {
		super();
	}

	/**
	 * Return new questions in the tag passed as parameter. <br>
	 * The returned questions have at least one answer
	 * 
	 * @param tag
	 * @param nbQuestions
	 * @return
	 */
	public List<Question> getAnsweredQuestionsByTag(String tag, int nbQuestions) {
		factory = StackExchangeApiQueryFactory.newInstance(ApiManager.APP_KEY, ApiManager.SITE);
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

	/**
	 * Builds Url to get similar Questions, calls the Api and returns a
	 * JsonArray to manipulate data
	 * 
	 * @param questionTitle
	 *            Title of the question the user wishes to submit
	 * @param nbQuestions
	 *            Number of similar questions to get (limited to 100)
	 * @return
	 */
	private JsonArray getJsonArrayWithSimilarQuestions(String questionTitle, int nbQuestions) {
		ApiUrlBuilder builder = createStackOverflowApiUrlBuilder(SEARCH_SIMILAR_QUESTIONS)
				.withParameter("title", questionTitle).withParameter("pagesize", new Integer(nbQuestions).toString())
				.withFilter(SIMILAR_QUESTIONS_FILTER);
		String apiUrl = builder.buildUrl();
		apiUrl += "&sort=relevance&order=desc";

		Charset UTF_8_CHAR_SET = Charset.forName(ApplicationConstants.CONTENT_ENCODING);
		JsonParser parser = new JsonParser();
		InputStream jsonContent = callApiMethod(apiUrl);
		JsonElement response = parser.parse(new InputStreamReader(jsonContent, UTF_8_CHAR_SET));
		JsonObject adaptee = response.getAsJsonObject();
		return adaptee.get("items").getAsJsonArray();
	}

	/**
	 * Find key words (tags) to suggest based on the title of the question the
	 * user wants to submit
	 * 
	 * @param questionTitle
	 * @return list of String Objects
	 */
	public List<String> findKeyWords(String questionTitle) {
		ArrayList<String> relatedTags = new ArrayList<String>();

		JsonArray elements = getJsonArrayWithSimilarQuestions(questionTitle, 30);

		for (JsonElement o : elements) {
			JsonObject jsonObject = o.getAsJsonObject();
			JsonElement tagNames = jsonObject.get("tags");
			String[] tags = tagNames.toString().replace('[', ' ').replace(']', ' ').trim().split(",");
			for (String s : tags) {
				if (!relatedTags.contains(s)) {
					relatedTags.add(s);
				}
			}
		}
		return relatedTags;
	}

	/**
	 * Find nbQuestions similar questions to the question the user wants to
	 * submit
	 * 
	 * @param questionTitle
	 * @param nbQuestions
	 *            Number of Questions you wish to look for (limited to 100)
	 * @return list of Question Objects
	 */
	public List<Question> findSimilarQuestions(String questionTitle, int nbQuestions) {
		if (nbQuestions > 100) {
			nbQuestions = 100;
		}
		ArrayList<Question> similarQuestions = new ArrayList<Question>(nbQuestions);

		JsonArray elements = getJsonArrayWithSimilarQuestions(questionTitle, nbQuestions);

		for (JsonElement o : elements) {
			JsonObject jsonObject = o.getAsJsonObject();
			int id = Integer.parseInt(jsonObject.get("question_id").toString());
			String title = jsonObject.get("title").toString();
			Question currentQuestion = new Question();
			currentQuestion.setTitle(title);
			currentQuestion.setQuestionId(id);
			similarQuestions.add(currentQuestion);
		}
		return similarQuestions;
	}

}
