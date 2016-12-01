package fr.tse.info4.project.datarecovery;

import com.google.code.stackexchange.client.impl.StackExchangeApiJsonClient;
import com.google.code.stackexchange.schema.StackExchangeSite;

public class ApiManager extends StackExchangeApiJsonClient  {

	
	public final static String APP_KEY = "6WGYkZgsp2UiQfMqTj3LCQ((";
	public final static StackExchangeSite SITE = StackExchangeSite.STACK_OVERFLOW;
	
	public ApiManager(String applicationKey, StackExchangeSite site) {
		super(applicationKey, site);

	}

}
