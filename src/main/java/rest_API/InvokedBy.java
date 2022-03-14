package rest_API;


public class InvokedBy {
	public UserID userId;
	
	public InvokedBy() {
	}

	public InvokedBy(UserID userId) {
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
