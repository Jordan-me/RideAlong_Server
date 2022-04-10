package iob.logic;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import iob.boundries.NewUserBoundary;
import iob.boundries.UserBoundary;
import iob.boundries.UserID;
import iob.data.UserEntity;
import iob.data.UserRole;

@Component
public class UsersConverter {
	private ObjectMapper jackson;

	@PostConstruct
	public void init() {
		this.jackson = new ObjectMapper();
	}

	public UserEntity toEntity (UserBoundary boundary) {
		UserEntity entity = new UserEntity();
		if(boundary.getUserId().getEmail() != null) {
			String uID = boundary.getUserId().getDomain() + "," + boundary.getUserId().getEmail();
			entity.setUserId(uID);
		}
		entity.setRole(
				toEntity
				(boundary.getRole()));

		if (boundary.getAvatar() != null && !boundary.getAvatar().isEmpty()) {
			entity.setAvatar(boundary.getAvatar());
		}
		if (boundary.getUsername() !=null && !boundary.getUsername().isEmpty()){
			entity.setUsername(boundary.getUsername());
		}
 
		return entity;
	}

	public UserRole toEntity (String boundaryRole) {
		if(boundaryRole != null) {
			String strRole = boundaryRole.toUpperCase();
			try {
				UserRole rv = UserRole.valueOf(strRole);				
			}catch(IllegalArgumentException e) {
				throw new RuntimeException("User's role is not valid.");
			}
		}
		return null;

	}
	
	public UserBoundary  toBoundary (UserEntity entity) {
		
		UserBoundary  boundary = new UserBoundary ();
		String[] splittedUserID = entity.getUserId().split(",");
		boundary.setUserId(new UserID(splittedUserID[0], splittedUserID[1]));
		boundary.setUsername(entity.getUsername());
		boundary.setRole(entity.getRole().name().toLowerCase());
		boundary.setAvatar(entity.getAvatar());
		
		return boundary;
	}
	
	public String toEntity (Map<String, Object> object) {
		try {
			return this.jackson
				.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Map<String, Object> toBoundaryFromJsonString (String json){
		try {
			return this.jackson
				.readValue(json, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


 
}
