package fr.tse.info4.project.model.schema;

import java.util.HashMap;

import java.util.Map;

public class TopUser implements Comparable<TopUser> {

	/**
	 * ID of the user.
	 */
	private long id;
	/**
	 * Count of post of this user.
	 */
	private int postCount;
	/**
	 * Map with a tag as a key and the count of post for this tag as a value.
	 */
	private Map<String, Integer> tagPostCount = new HashMap<>();

	/**
	 * Constructor initializes the ID.
	 * @param id
	 */
	public TopUser(long id) {
		this.id = id;
	}

	/**
	 * Get count of post of this user.
	 * @return postCount
	 */
	public int getPostCount() {
		return postCount;
	}

	/**
	 * Set count of post of this user.
	 * @param postCount
	 */
	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}

	/**
	 * Get ID of this user.
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Get the map of this user.
	 * @return tagPostCount
	 */
	public Map<String, Integer> getTagPostCount(){
		return tagPostCount;
	}
	/**
	 * Adds (key,value)=(tag,postCount) to the map of this user.
	 * @param tag
	 * @param postCount
	 */
	public void addTag(String tag, int postCount) {
		if (!tagPostCount.containsKey(tag)) {
			tagPostCount.put(tag, postCount);
		}
	}

	/**
	 * 
	 * Retrieves the real post count sum for a tag list given. Indeed, the
	 * variable postCount can be different from the post count sum of each tag
	 * if a tag is chosen many times.
	 * 
	 * @return the real post count number for the set of tags given.
	 */
	public int getRealPostCount() {
		int realPostCount = 0;
		for (Map.Entry<String, Integer> e : tagPostCount.entrySet()) {
			realPostCount += e.getValue();
		}

		return realPostCount;
	}

	/**
	 * Increase count post of this user.
	 * @param postCount
	 */
	public void increasePostCount(int postCount) {
		this.postCount += postCount;
	}

	/**
	 * 
	 * Two TopUser objects are equals if they are the same id.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TopUser other = (TopUser) obj;
		if (id != other.id)
			return false;
		return true;
	}

	/**
	 * 
	 * Comparaison based on the post count variable.
	 */
	@Override
	public int compareTo(TopUser o) {
		if (this.postCount == o.getPostCount())
			return 0;
		if (this.postCount < o.getPostCount())
			return -1;
		return 1;

	}

	@Override
	public String toString() {
		return "TopUser [id=" + id + ", postCount=" + postCount + ", tagPostCount=" + tagPostCount + "]";
	}

}