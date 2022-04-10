package iob.logic;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;

import iob.boundries.UserBoundary;
import iob.boundries.UserID;
import iob.data.UserEntity;

@Service
public class UsersServiceMockUp implements UsersService{
	private Map<UserID, UserEntity> userDataBaseMockup;
	private UsersConverter converter;

	@Autowired
	public UsersServiceMockUp(UsersConverter converter) {
		super();
		this.converter = converter;
	}
	
	@Autowired
	public void setConverter(UsersConverter converter) {
		this.converter = converter;
	}
	
	@PostConstruct
	public void init() {
		// thread safe Map
		this.userDataBaseMockup = Collections.synchronizedMap(new HashMap<>());
	}
	
	@Override
	public UserBoundary createUser(UserBoundary user) {
		
		// convert UserBoundary to entity
		UserEntity entity = this.converter.convertToEntity(user);
		
		// store entity to DB
		this.userDataBaseMockup.put(user.getUserId(), entity);
		
		// Convert data to Boundary and return it
//		return this.converter.convertToBoundary(entity);
		return user;
		
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {

		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public List<UserBoundary> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllUsers() {
		// TODO Auto-generated method stub
		
	}

}
