package fr.tse.info4.project.model.datarecovery;

import com.google.code.stackexchange.client.impl.StackExchangeApiJsonClient;
import com.google.code.stackexchange.client.query.impl.TagApiQueryImpl;
import com.google.code.stackexchange.client.query.impl.UserApiQueryImpl;
import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Paging;
import com.google.code.stackexchange.schema.StackExchangeSite;
import com.google.code.stackexchange.schema.Tag;
import com.google.code.stackexchange.schema.User;

public class ApiManager extends StackExchangeApiJsonClient  {

	/*  Application key */
	public final static String APP_KEY = "6WGYkZgsp2UiQfMqTj3LCQ((";
	/* Site (stack overflow) */
	public final static StackExchangeSite SITE = StackExchangeSite.STACK_OVERFLOW;
	
	/**
	 * Constructor to use application key with every api url execution
	 * and specify StackExchange website on which execute querries
	 * @param applicationKey
	 * @param site
	 */
	public ApiManager(String applicationKey, StackExchangeSite site) {
		super(applicationKey, site);

	}

	/**
	 * Default constructor.
	 */
	public ApiManager() {
		super(APP_KEY,SITE);
	}

	/**
	 * 
	 * Retrieves the id of user identified by his accessToken
	 * 
	 * @param accessToken
	 * @return the user id
	 */
	public static long getIdUser(String accessToken){
		String filter = "!6Xcz2t9HFnLLg";
		UserApiQueryImpl query = new UserApiQueryImpl(APP_KEY, SITE, accessToken);
		query.withFilter(filter);
		PagedList<User> list = query.listMe();
		
		
		return list.get(0).getUserId();
	}
	
	/**
	 * 
	 * Retrieves the best tags of the user (sorted by popularity)
	 * 
	 * @param nbTags
	 * @param idUser
	 * @return the best tags of a user.
	 */
	public static PagedList<Tag> getTags(int nbTags, int idUser) {
		final String filter = "!-.G.68grSaJo";
		final TagApiQueryImpl tags = new TagApiQueryImpl(ApiManager.APP_KEY, ApiManager.SITE);
		Paging page = new Paging(1, nbTags);

		tags.withUserIds(idUser);
		tags.withPaging(page);
		tags.withFilter(filter);
		tags.withSort(Tag.SortOrder.MOST_POPULAR);

		return tags.listTagsOnUser();
	}

	/**
	 * 
	 * Retrieves the best tags of the user (sorted by popularity)
	 * 
	 * @param nbTags
	 * @param accessToken
	 * @return the best tags of the user.
	 */
	public static PagedList<Tag> getTags(int nbTags, String accessToken) {
		return getTags(nbTags, (int) ApiManager.getIdUser(accessToken));
	}
	

}
