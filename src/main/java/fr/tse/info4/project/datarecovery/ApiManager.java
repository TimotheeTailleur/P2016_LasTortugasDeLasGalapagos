package fr.tse.info4.project.datarecovery;

import com.google.code.stackexchange.client.impl.StackExchangeApiJsonClient;
import com.google.code.stackexchange.client.query.impl.UserApiQueryImpl;
import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.StackExchangeSite;
import com.google.code.stackexchange.schema.User;

public class ApiManager extends StackExchangeApiJsonClient  {

	/*  Key of the application */
	public final static String APP_KEY = "6WGYkZgsp2UiQfMqTj3LCQ((";
	/* Site (stack overflow) */
	public final static StackExchangeSite SITE = StackExchangeSite.STACK_OVERFLOW;
	
	public ApiManager(String applicationKey, StackExchangeSite site) {
		super(applicationKey, site);

	}

	/**
	 * 
	 * Return the id of the user identified with its acces token
	 * 
	 * @param accessToken
	 * @return
	 */
	public static long getIdUser(String accessToken){
		String filter = "!6Xcz2t9HFnLLg";
		UserApiQueryImpl query = new UserApiQueryImpl(APP_KEY, SITE, accessToken);
		query.withFilter(filter);
		PagedList<User> list = query.listMe();
		
		
		return list.get(0).getUserId();
	}
	

}
