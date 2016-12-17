package fr.tse.info4.project.datarecovery;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.code.stackexchange.client.constant.ApplicationConstants;

import com.google.code.stackexchange.client.provider.url.ApiUrlBuilder;

import com.google.code.stackexchange.client.query.StackExchangeApiQueryFactory;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Answer;
import com.google.code.stackexchange.schema.Badge;
import com.google.code.stackexchange.schema.BadgeRank;
import com.google.code.stackexchange.schema.Paging;
import com.google.code.stackexchange.schema.Question;
import com.google.code.stackexchange.schema.StackExchangeSite;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AliceApiManager extends ApiManager {

	public static final String GET_BADGES_FOR_USERS = "com.google.code.stackexchange.client.getBadgesForUser";
	public static final String USER_BADGES_FILTER = "!1zSsisCpHXb2m(HIl6*I0";
	public static final String GET_SITE_BADGES = "com.google.code.stackexchange.client.getBadges";
	public static final String SITE_BADGES_FILTER = "!b8c7G.MYOLLIHp";

	public static final String BADGES_WITH_IDS_URL = "https://api.stackexchange.com/2.2/badges/";
	public static final String BADGES_WITH_IDS_FILTER = "";
	
	public AliceApiManager(String applicationKey, StackExchangeSite site) {
		super(applicationKey, site);
	}

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

	public static PagedList<Question> getSortedQuestions(List<Long> idQuestions) {
		final StackExchangeApiQueryFactory factory = StackExchangeApiQueryFactory.newInstance(ApiManager.APP_KEY,
				ApiManager.SITE);

		final PagedList<Question> questions = factory.newQuestionApiQuery().withQuestionIds(idQuestions)
				.withSort(Question.SortOrder.MOST_VOTED).list();

		return questions;
	}

	/**
	 * 
	 * Return the anwers of a user, sorted by questions score.
	 * 
	 * @param idUser
	 * @param nbAnswers
	 * @return
	 */
	public static List<Answer> getAnswers(int idUser, int nbAnswers) {
		StackExchangeApiQueryFactory queryFactory = StackExchangeApiQueryFactory.newInstance(ApiManager.APP_KEY,
				ApiManager.SITE);

		String filter = "!-*f(6t0WUWsK";
		if (nbAnswers <= 100) {
			Paging page = new Paging(1, nbAnswers);

			PagedList<Answer> answers = queryFactory.newAnswerApiQuery().withUserIds(idUser).withFilter(filter)
					.withSort(Answer.SortOrder.MOST_RECENTLY_UPDATED).withPaging(page).listByUsers();

			return answers;
		} else {
			float nbPages = (float) nbAnswers / (float) 100;
			System.out.println((int) nbPages);

			PagedList<Answer> answer = null;
			for (int i = 1; i <= (int) nbPages; i++) {
				Paging page = new Paging(i, 100);
				PagedList<Answer> answersPage = queryFactory.newAnswerApiQuery().withUserIds(idUser).withFilter(filter)
						.withSort(Answer.SortOrder.MOST_RECENTLY_UPDATED).withPaging(page).listByUsers();
				if (i == 1) {
					answer = answersPage;
				} else {
					for (int j = 0; j < answersPage.size(); j++) {
						answer.add(answersPage.get(j));
					}
				}

				try {
					Thread.sleep(answersPage.getBackoff() * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			int nbAnswersLastPage = nbAnswers - (int) nbPages * 100;
			System.out.println(nbAnswersLastPage);
			Paging lastPage = new Paging((int) nbPages + 1, 100);
			PagedList<Answer> answersPage = queryFactory.newAnswerApiQuery().withUserIds(idUser).withFilter(filter)
					.withSort(Answer.SortOrder.MOST_VOTED).withPaging(lastPage).listByUsers();
			for (int j = 0; j < nbAnswersLastPage; j++) {
				answer.add(answersPage.get(j));
			}
			try {
				Thread.sleep(answersPage.getBackoff() * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return answer;
		}

	}

	/**
	 * Get a user's Badges info (badge ids, user award count for each badge,
	 * badge Name and Rank) sorted by their award counts
	 * 
	 * @param userId
	 * @return A list of badges acquired by the user
	 */
	public List<Badge> getUserBadges(int userId) {
		// -------------Badges d'Alice----------------//
		ArrayList<Badge> userBadges = new ArrayList<Badge>(100);
		Charset UTF_8_CHAR_SET = Charset.forName(ApplicationConstants.CONTENT_ENCODING);
		JsonParser parser = new JsonParser();
		int nbPage = 1;
		boolean hasMore = true;

		ApiUrlBuilder builder = createStackOverflowApiUrlBuilder(GET_BADGES_FOR_USERS).withId(userId)
				.withFilter(USER_BADGES_FILTER);

		while (hasMore) {
			String apiUrl = builder.buildUrl();
			apiUrl += "&pagesize=100&sort=awarded&order=desc";
			apiUrl += "&page=" + nbPage;

			InputStream jsonContent = callApiMethod(apiUrl);
			JsonElement response = parser.parse(new InputStreamReader(jsonContent, UTF_8_CHAR_SET));
			JsonObject adaptee = response.getAsJsonObject();
			JsonArray elements = adaptee.get("items").getAsJsonArray();

			for (JsonElement o : elements) {
				JsonObject jsonObject = o.getAsJsonObject();
				int awardCount = Integer.parseInt(jsonObject.get("award_count").toString());
				int badgeId = Integer.parseInt(jsonObject.get("badge_id").toString());
				String badgeName = jsonObject.get("name").toString();
				String badgeRankName = jsonObject.get("rank").toString().replace('"', ' ').trim();
				Badge currentBadge = new Badge();
				currentBadge.setAwardCount(awardCount);
				currentBadge.setBadgeId(badgeId);
				currentBadge.setName(badgeName);

				switch (badgeRankName) {
				case "bronze":
					currentBadge.setRank(BadgeRank.BRONZE);
					break;
				case "silver":
					currentBadge.setRank(BadgeRank.SILVER);
					break;
				case "gold":
					currentBadge.setRank(BadgeRank.GOLD);
					break;
				}
				userBadges.add(currentBadge);
			}
			nbPage++;
			hasMore = adaptee.get("has_more").toString().equals("true");
		}
		return userBadges;
	}

	/**
	 * For each user badge, check if a next level of the badge exists. <br>
	 * If there is one, find users who have it and return the Badge and it's info
	 * If there isn't check other badges
	 * Ex : the bronze badge Nice Question's (Question Score of 10 or more) next level is Good Question (Question Score of 25 or more)
	 * @param userId
	 * @return A 2-list of Alice's badges and their next levels of size 9
	 */

	public List<List<Badge>> compareBadges(int userId) {
		List<Badge> userBadges = getUserBadges(userId);
		ArrayList<Long> badgePlusOneIds = new ArrayList<Long>(userBadges.size());
		
		for (Badge badge : userBadges)
		{
			Long id = badge.getBadgeId()+1;
			badgePlusOneIds.add(id);
		}
		
		ArrayList<Badge> idPlusOneBadges = new ArrayList<Badge>();
		ArrayList<Badge> aliceBadgesWithNextLevel= new ArrayList<Badge>(9);
		ArrayList<Badge> nextLevelBadges = new ArrayList<Badge>(9);
		
		Charset UTF_8_CHAR_SET = Charset.forName(ApplicationConstants.CONTENT_ENCODING);
		JsonParser parser = new JsonParser();
		int nbPage = 1;
		boolean hasMore = true;
		int size= 0;

		 String apiUrlBase = BADGES_WITH_IDS_URL + badgePlusOneIds.toString().replaceAll(",","%3B").replace('[',' ').replace(']',' ').trim() + "&filter="+BADGES_WITH_IDS_FILTER;

		while (hasMore) {
			String apiUrl = apiUrlBase;
			apiUrl += "order=desc&sort=rank" + "&pagesize=100" + "&page=" + nbPage + "&key="+ApiManager.APP_KEY + "site="+ "stackoverflow";

			InputStream jsonContent = callApiMethod(apiUrl);
			JsonElement response = parser.parse(new InputStreamReader(jsonContent, UTF_8_CHAR_SET));
			JsonObject adaptee = response.getAsJsonObject();
			JsonArray elements = adaptee.get("items").getAsJsonArray();

			for (JsonElement o : elements) {
				JsonObject jsonObject = o.getAsJsonObject();
				int awardCount = Integer.parseInt(jsonObject.get("award_count").toString());
				int badgeId = Integer.parseInt(jsonObject.get("badge_id").toString());
				String badgeName = jsonObject.get("name").toString();
				String badgeRankName = jsonObject.get("rank").toString().replace('"', ' ').trim();
				Badge currentBadge = new Badge();
				currentBadge.setAwardCount(awardCount);
				currentBadge.setBadgeId(badgeId);
				currentBadge.setName(badgeName);
				
				//TODO getUser data

				switch (badgeRankName) {
				case "bronze":
					currentBadge.setRank(BadgeRank.BRONZE);
					break;
				case "silver":
					currentBadge.setRank(BadgeRank.SILVER);
					break;
				case "gold":
					currentBadge.setRank(BadgeRank.GOLD);
					break;
				}
				idPlusOneBadges.add(currentBadge);
			}
			nbPage++;
			hasMore = adaptee.get("has_more").toString().equals("true");
		} 
		
		int index =0;
		while (size<9)
		{
		for (Badge badge : userBadges)
		{
			String badgeRankName = badge.getRank().value();
			Badge plusOneBadge=idPlusOneBadges.get(index);
			switch(badgeRankName)
			{
			case "bronze":
				if (plusOneBadge.getRank().value().equals("silver"))
				{
					nextLevelBadges.add(plusOneBadge);
					aliceBadgesWithNextLevel.add(badge);
					size++;
				}
				break;
			case "silver":
				if (plusOneBadge.getRank().value().equals("gold"))
				{
					nextLevelBadges.add(plusOneBadge);
					aliceBadgesWithNextLevel.add(badge);
					size++;
				}
				break;
			case "gold":
				break;
			}
			index++;
		}
		}
		// Add both lists to the returned variable
		List<List<Badge>> returnedList = new ArrayList<List<Badge>>();
		returnedList.add(aliceBadgesWithNextLevel);
		returnedList.add(nextLevelBadges);
		return returnedList;

	}

	public static void main(String[] args) {

		AliceApiManager manager = new AliceApiManager(APP_KEY, SITE);
		List<Badge> list = manager.getUserBadges(1);
		System.out.println(list.size());
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getName());
			System.out.println(list.get(i).getAwardCount());
			System.out.println(list.get(i).getRank());
		}
	}
}
