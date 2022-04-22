package iob.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.boundries.UserBoundary;
import iob.boundries.UserID;
import iob.data.UserEntity;
import iob.data.UserRole;

@Service
public class UserServiceJpa implements ExtendedUserService{
	private String domainName;
	private UserCrud userCrud;
	private UsersConverter userConverter;
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	@Autowired
	public UserServiceJpa(
			UserCrud userCrud,
			UsersConverter userConverter) {
		this.userCrud = userCrud;
		this.userConverter = userConverter;
	}

	@Override
	@Transactional//(readOnly = false)
	public UserBoundary createUser(UserBoundary user) {
		
		if(user.getRole() == null || user.getRole().isEmpty()) {
			throw new RuntimeException("User's role is not a defined");
		}
		if(user.getAvatar() == null || user.getAvatar().isEmpty() ) {
			throw new RuntimeException("User's avatar is not a defined");
		}
		if(user.getUsername() == null || user.getUsername().isEmpty() ) {
			throw new RuntimeException("Username is not a defined");
		}
		// convert NewUserBoundary to entity
		UserEntity entity = this.userConverter.toEntity(user);

		// store entity to DB
		entity = this.userCrud.save(entity);

		// convert entity to UserBoundary and return it
		return this.userConverter.toBoundary(entity);
	}
	@Transactional(readOnly = true)
	public UserEntity getUserEntityById(UserID userId) {
		Iterable<UserEntity> iter = this.userCrud
				.findAll();

			for (UserEntity user : iter) {
				if(user.getUserId().equals(userId.toString())) {
					return user;
				}
			}
			
			throw new UserNotFoundException("could not find user by id: " + userId.toString());
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userDomain, String userEmail) {
		UserID userId = new UserID(userDomain, userEmail);
		// Get user data from DB
		// Convert data to Boundary and return it
		return this.userConverter.toBoundary(this.getUserEntityById(userId));
	}
	
	@Override
	@Transactional
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		UserID userId = new UserID(userDomain, userEmail);
		// Get user data from DB
		UserEntity entity = this.getUserEntityById(userId);
						
		// Update user details in DB
		if (update.getRole() != null) 
			entity.setRole(this.userConverter.toEntity(update.getRole()));
				
		if (update.getUsername() != null) 
			entity.setUsername(update.getUsername());
				
		if (update.getAvatar() != null) 
			entity.setAvatar(update.getAvatar());
				
		// update DB
		entity = this.userCrud.save(entity);

		return this.userConverter.toBoundary(entity);
 
	}
	@Override
//	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers() {
//		return StreamSupport.stream(this.userCrud.findAll().spliterator(), false)
//				.map(this.userConverter::toBoundary)
//				.collect(Collectors.toList());
		throw new RuntimeException("deprecated method - use getAllUsers with paginayion instead");
	}
	
	@Override
	@Transactional
	public void deleteAllUsers() {
		// TODO: Validate Admin Permissions
		//TODO: Delete only non-admin users
		this.userCrud.deleteAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(int size, int page) {
		return this.userCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, "role", "userId"))
				.stream() // Stream<UserEntity>
				.map(this.userConverter::toBoundary) // Stream<UserBoundary>
				.collect(Collectors.toList()); // List<UserBoundary>
	}
	
	@Override
	public void checkUserPermission(String userId, UserRole role) {
		UserEntity userEntity = this.userCrud.findById(userId)
				.orElseThrow(()->
				new UserNotFoundException("Could not find user " + userId));
		if (!userEntity.getRole().equals(role))
			throw new RuntimeException("User's role is not a " + role.name());
	}
}
