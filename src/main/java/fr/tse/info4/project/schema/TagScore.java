package fr.tse.info4.project.schema;

import com.google.code.stackexchange.schema.User;

/**
 * Class that regroups tag data (tag name, tag score and post count)
 * ALso implements an enum to pass periods of time as parameters in Api Urls
 *
 */
public class TagScore {


	/**
	 * Enum representing Period Tag Api Urls parameter
	 * 
	 *
	 */
	public static enum Period{
		ALL_TIME("all_time"),
		MOUTH("mouth");
		
		private final String period;
		
		private Period(final String period){
			this.period = period;
		}
		
		public String toString(){
			return period;
		}
		
		
	}
	/* Post count in the given tag */
	private int postCount;
	/* Score in the given tag */
	private int score;
	/* The user */
	private User user;
	
	/**
	 * Constructor
	 * @param postCount
	 * @param score
	 * @param user
	 */
	
	public TagScore(int postCount, int score, User user) {
		super();
		this.postCount = postCount;
		this.score = score;
		this.user = user;
	}
	
	
	/**
	 * Default constructor
	 */
	public TagScore() {
		super();
	}


	/**
	 * tag post count getter
	 * @return
	 */
	public int getPostCount() {
		return postCount;
	}
	/**
	 * tag post count setter
	 * @param postCount
	 */
	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}
	/**
	 * tag score getter
	 * @return
	 */
	public int getScore() {
		return score;
	}
	/**
	 * Score setter
	 * @param score
	 */
	public void setScore(int score) {
		this.score = score;
	}
	/**
	 * staeckexchange api sdk User Object getter
	 * @return
	 */
	public User getUser() {
		return user;
	}
	/**
	 * User setter
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	

	@Override
	/**
	 * Returns a String object representing TagScore object
	 */
	public String toString() {
		return "TagScore [postCount=" + postCount + ", score=" + score + ", user=[userId=" + user.getUserId() + ", display_name="+user.getDisplayName()+"]";
	}

	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagScore other = (TagScore) obj;
		if (postCount != other.postCount)
			return false;
		if (score != other.score)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	
	
	
	
}
