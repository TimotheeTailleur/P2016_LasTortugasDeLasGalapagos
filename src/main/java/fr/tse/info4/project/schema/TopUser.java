package fr.tse.info4.project.schema;

import java.util.HashMap;

import java.util.Map;

public class TopUser implements Comparable<TopUser> {

	private long id;
	private int postCount;
	private Map<String, Integer> tagPostCount = new HashMap<>();

	public TopUser(long id) {
		this.id = id;
	}

	public int getPostCount() {
		return postCount;
	}

	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}

	public long getId() {
		return id;
	}

	public Map<String, Integer> getTagPostCount(){
		return tagPostCount;
	}
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