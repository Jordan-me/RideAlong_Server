package iob.data;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.Table;
//import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

//import javax.persistence.Column;

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
//@Entity
//@Table(name="USER_TABLE")
@Document(collection = "Users")
public class UserEntity {
	@Id
	private String userId;
	@NonNull
	private UserRole role;
	@NonNull
	private String username;
	@NonNull
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
