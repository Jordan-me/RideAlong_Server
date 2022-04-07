package iob.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iob.boundries.*;
import iob.logic.ActivitiesService;
import iob.logic.InstancesService;
import iob.logic.UsersService;



@RestController
public class UserController {
	private UsersService userService;
	 
	@Autowired
	public UserController(UsersService userService) {
		this.userService = userService;
	}
	// Create a new user
	@RequestMapping(
			path = "/iob/users",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createNewUser(
			@RequestBody NewUserBoundary input) {
// 		Test
//		input.setEmail("user@demo.com");
//		input.setUsername("MANAGER");
//		input.setRole("Demo User");
//		input.setAvatar("J");
		UserID userId = new UserID("2022b.yarden.dahan",input.getEmail());
		
		return new UserBoundary(userId, input.getRole(),input.getUsername(),input.getAvatar());				
	}
	
	// Login valid user and retrieve user details
	@RequestMapping(
			path = "/iob/users/login/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary getUser(@PathVariable("userDomain") String domain,
								@PathVariable("userEmail") String email) {
		
		UserID userId = new UserID(domain,email);
		UserBoundary user = new UserBoundary(userId,"Traveller","Demo User","J");
		
		return user;				
	}
	
	
	// Update user details
	@RequestMapping(
			path = "/iob/users/{userDomain}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(@PathVariable("userDomain") String domain,
						   @PathVariable("userEmail") String email,
						   @RequestBody UserBoundary input) {   
		input.getUserId().setDomain(domain);
		input.getUserId().setEmail(email);
		System.err.println("update userBoundary:\nemail: " + input.getUserId().getEmail()+
				"\nDomain:" +input.getUserId().getDomain() );
	}
}
