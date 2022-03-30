package boundries;

public class NewUserBoundary {
// check if needed and the difference with UserBoundry
	private String email;
	private String username;
	private String role;
	private String avatar;
	
	
	public NewUserBoundary() {
		super();
	}

	public NewUserBoundary(String email, String username, String role, String avatar) {
		super();
		this.email = email;
		this.username = username;
		this.role = role;
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
}
