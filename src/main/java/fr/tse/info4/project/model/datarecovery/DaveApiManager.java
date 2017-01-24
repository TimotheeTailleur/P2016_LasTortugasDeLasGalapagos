package fr.tse.info4.project.model.datarecovery;

import java.util.ArrayList;
import java.util.List;

import com.google.code.stackexchange.client.constant.StackExchangeApiMethods;
import com.google.code.stackexchange.client.provider.url.ApiUrlBuilder;

import com.google.code.stackexchange.common.PagedList;
import com.google.code.stackexchange.schema.Paging;
import com.google.code.stackexchange.schema.StackExchangeSite;
import com.google.code.stackexchange.schema.Tag;

public class DaveApiManager extends ApiManager {



	/**
	 * Constructor to use applicationKey and specify StackExchangeSite
	 * 
	 * @param applicationKey
	 * @param site
	 */
	public DaveApiManager(String applicationKey, StackExchangeSite site) {
		super(applicationKey, site);
	}

	/**
	 * 
	 * @param tagName
	 * @param list
	 * @return True if pagedList contains tag (tagName)
	 */
	public static boolean contains(String tagName, PagedList<Tag> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName().equals(tagName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * Overloads getTagsOnUsers to manage Paging if the querry creates more than one page of results
	 * 
	 * @param ids
	 * @param min
	 * @param page
	 * @return
	 */
	private PagedList<Tag> getTagsOnUsers(List<Long> ids, int min, Paging page) {
		ApiUrlBuilder builder = createStackOverflowApiUrlBuilder(StackExchangeApiMethods.GET_TAGS_ON_USER).withIds(ids)
				.withPaging(page);
		String apiUrl = builder.buildUrl() + "&min=" + min;
		return unmarshallList(Tag.class, callApiMethod(apiUrl));
	}

	/**
	 * Returns tags the users identified in ids have been active in. <br>
	 * The tags need to have a score > min
	 * 
	 * @param ids
	 * @param min
	 * @return
	 */
	public List<Tag> getTagsOnUsers(List<Long> ids, int min) {
		Paging firstPage = new Paging(1, 100);
		PagedList<Tag> userTagsPage = getTagsOnUsers(ids, min, firstPage);
		List<Tag> userTags = new ArrayList<Tag>();

		for (int i = 0; i < userTagsPage.size(); i++) {
			userTags.add(userTagsPage.get(i));
		}

		int pageNumber = 2;
		while (userTagsPage.hasMore()) {
			try {
				Thread.sleep(userTagsPage.getBackoff() * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(pageNumber);
			Paging page = new Paging(pageNumber, 100);
			userTagsPage = getTagsOnUsers(ids, min, page);

			for (int i = 0; i < userTagsPage.size(); i++) {
				userTags.add(userTagsPage.get(i));
			}
			pageNumber++;
		}

		return userTags;

	}

}
