package fr.tse.info4.project.schema;

import com.google.code.stackexchange.schema.User;

public class TagScore {

	
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



	public int getPostCount() {
		return postCount;
	}
	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return "TagScore [postCount=" + postCount + ", score=" + score + ", user=[userId=" + user.getUserId() + ", display_name="+user.getDisplayName()+"]";
	}
	
	
	
	
}
