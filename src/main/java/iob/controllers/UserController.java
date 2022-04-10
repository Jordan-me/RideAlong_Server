package iob.controllers;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	private UsersService user;
	private String domainName;
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	@Autowired
	public UserController(UsersService user) {
		this.user = user;
	}
	//email validation (Example: username@domain.com)
	public static boolean patternMatches(String emailAddress) {
		String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
	    return Pattern.compile(regexPattern)
	      .matcher(emailAddress)
	      .matches();
	}
	// Create a new user
	@RequestMapping(
			path = "/iob/users",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createNewUser(
			@RequestBody NewUserBoundary input) {
		
		// Email validation 
		if(!patternMatches(input.getEmail()))
			throw new RuntimeException("Email is not valid.");
		else {
			UserID userId = new UserID(this.domainName,input.getEmail());
			return this.user.createUser(new UserBoundary(userId, input.getRole(),input.getUsername(),input.getAvatar()));	
		}
	}
	
	// Login valid user and retrieve user details
	@RequestMapping(
			path = "/iob/users/login/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary getUser(@PathVariable("userDomain") String domain,
								@PathVariable("userEmail") String email) {
		
		UserBoundary userLoggedIn = this.user.login(domain, email);
		if(userLoggedIn == null)
			throw new RuntimeException("User was not found.");
		else
			return userLoggedIn;			
	}
	
	
	// Update user details
	@RequestMapping(
			path = "/iob/users/{userDomain}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(@PathVariable("userDomain") String domain,
						   @PathVariable("userEmail") String email,
						   @RequestBody UserBoundary input) {   
 
		this.user.updateUser(domain, email, input);
	}
}
