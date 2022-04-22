//package iob.logic;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import iob.boundries.ActivityBoundary;
//import iob.boundries.NewUserBoundary;
//import iob.boundries.UserBoundary;
//import iob.boundries.UserID;
//import iob.data.UserEntity;
//
////@Service
//public class UsersServiceMockUp implements UsersService{
//	private Map<String, UserEntity> userDataBaseMockup;
//	private UsersConverter converter;
//
//	@Autowired
//	public UsersServiceMockUp(UsersConverter converter) {
//		super();
//		this.converter = converter;
//	}
//	
//	@Autowired
//	public void setConverter(UsersConverter converter) {
//		this.converter = converter;
//	}
//	
//	@PostConstruct
//	public void init() {
//		// Initialize thread safe Map
//		this.userDataBaseMockup = Collections.synchronizedMap(new HashMap<>());
//	}
//	
//	@Override
//	public UserBoundary createUser(UserBoundary user) {
//		System.out.println("enter MockUp");
//		// convert UserBoundary to UserEntity
//		UserEntity entity = this.converter.toEntity(user);
//		
//		// store entity to DB
//		this.userDataBaseMockup.put(user.getUserId().toString(), entity);
//		
//		// Convert UserEntity to UserBoundary
//		UserBoundary userBoundary = this.converter.toBoundary(entity);
//		return userBoundary;
//		
//	}
//
//	@Override
//	public UserBoundary login(String userDomain, String userEmail) {
//		UserID userId = new UserID(userDomain, userEmail);
//		
//		// Return user data from DB_MockUp
//		UserEntity entity = this.userDataBaseMockup.get(userId.toString());
//		if (entity == null)
//			throw new UserNotFoundException("User "+ userId.toString() + " could not found.");
//		
//		// Convert mockUp data to Boundary and return it
//		return this.converter.toBoundary(entity);
//	}
//
//	@Override
//	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
//		UserID userId = new UserID(userDomain, userEmail);
//
//		// Return user data from DB
//		UserEntity userEntity = this.userDataBaseMockup.get(userId.toString());
//		//check if user exist on db
//		if(userEntity == null) {
//			throw new UserNotFoundException("User "+ userId + " could not found.");
//		}
//		//update fields, if dirtyFlag- mean our user changed
//		boolean dirtyFlag = false;
//		if (update.getRole() != null) {
//			dirtyFlag = true;
//			userEntity.setRole(this.converter.toEntity(update.getRole()));
//		}
//		if (update.getUsername() != null && !update.getUsername().isEmpty()) {
//			dirtyFlag = true;
//			userEntity.setUsername(update.getUsername());
//		}
//
//		if (update.getAvatar() != null && !update.getAvatar().isEmpty()) {
//			dirtyFlag = true;
//			userEntity.setAvatar(update.getAvatar());
//		}
//
//		// store entity to DB if needed
//		if (dirtyFlag)
//			//The put method either updates the value or adds a new entry.
//			this.userDataBaseMockup.put(userId.toString(), userEntity);
//
//		return this.converter.toBoundary(userEntity);
//	}
//	
//// Admin permission
//	@Override
//	public List<UserBoundary> getAllUsers() {
//		List<UserBoundary> usersBoundary = userDataBaseMockup.values().stream()
//                .map(v -> converter.toBoundary(v))
//                .collect(Collectors.toList());
//        
//		return usersBoundary;
//	}
//
//	@Override
//	public void deleteAllUsers() {
//		this.userDataBaseMockup.clear();
//		
//	}
//
//}
