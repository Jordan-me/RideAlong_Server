package iob.data;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;

/*
USER_TABLE
------------------------------
User_ID		| userId	  | role 			 | username 			| avatar
VARCHAR(255)| VARCHAR(255)|	VARCHAR(255)	 |VARCHAR(255)			|VARCHAR(255)
<PK>
 	private UserID userId;
	private String role;
	private String username;
	private String avatar;
*/
@Entity
@Table(name="USER_TABLE")
public class UserEntity {
	private String userId;
	private UserRole role;
	private String username;
	private String avatar;
	
	public UserEntity() {
		
	}
	@Id
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Enumerated(EnumType.STRING)
	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	

}
