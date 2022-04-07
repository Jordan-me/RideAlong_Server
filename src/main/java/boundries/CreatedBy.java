package boundries;

public class CreatedBy {
	private UserID userId;
	
	public CreatedBy() {
	}

	public CreatedBy(UserID userId) {
		this();
		this.userId = userId;
	}

	public UserID getUserId() {
		return userId;
	}

	public void setUserId(UserID userId) {
		this.userId = userId;
	}

}
