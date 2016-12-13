package fr.tse.info4.project.schema;

import java.util.HashMap;
import java.util.Map;

public class TopTagUser {

	private int postCount;
	Map<String, Integer> tagOccurences = new HashMap<>();
	
	
	public TopTagUser(int postCount, Map<String, Integer> tagOccurences) {
		super();
		this.postCount = postCount;
		this.tagOccurences = tagOccurences;
	}


	public int getPostCount() {
		return postCount;
	}


	public Map<String, Integer> getTagOccurences() {
		return tagOccurences;
	}


	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}


	public void setTagOccurences(Map<String, Integer> tagOccurences) {
		this.tagOccurences = tagOccurences;
	}


	
	

	
	
}
