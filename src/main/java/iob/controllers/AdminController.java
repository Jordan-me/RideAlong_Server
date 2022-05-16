package iob.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iob.boundries.ActivityBoundary;
import iob.boundries.UserBoundary;
import iob.boundries.UserID;
import iob.data.UserRole;
import iob.logic.ExtendedActivitiesService;
import iob.logic.ExtendedInstancesService;
import iob.logic.ExtendedUserService;


@RestController
public class AdminController {
	private ExtendedUserService admin;
	private ExtendedInstancesService instancesService;
	private ExtendedActivitiesService activitiesService;
	
	@Autowired
	public void ActivitiesService(ExtendedActivitiesService activitiesService) {
		this.activitiesService = activitiesService;
	}
	@Autowired
	public void setInstancesService(ExtendedInstancesService instancesService) {
		this.instancesService = instancesService;
	}
	@Autowired
	public void setUsersService(ExtendedUserService admin) {
		this.admin = admin;
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(
			method = RequestMethod.GET,
			path ="/iob/admin/activities",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActivityBoundary[] getAllActivities(
			@RequestParam(name="userDomain", required = true) String domain,
			@RequestParam(name="userEmail", required = true) String email,
			@RequestParam(name="size", required = false, defaultValue = "10") int size,
			@RequestParam(name="page", required = false, defaultValue = "0") int page
			) {
		// Get user data from DB and check if ADMIN
		return activitiesService.getAllActivities(new UserID(domain, email),UserRole.ADMIN,size,page).toArray(new ActivityBoundary[0]);
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(
			method = RequestMethod.GET,
			path ="/iob/admin/users",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary[] getAllUsers(
				@RequestParam(name="userDomain", required = true) String domain,
				@RequestParam(name="userEmail", required = true) String email,
				@RequestParam(name="size", required = false, defaultValue = "10") int size,
				@RequestParam(name="page", required = false, defaultValue = "0") int page
				) {
			//Get user data from DB and check if ADMIN
			return this.admin.getAllUsers(new UserID(domain, email),UserRole.ADMIN,size, page).toArray(new UserBoundary[0]);
		}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(
			method = RequestMethod.DELETE,
			path = "/iob/admin/users")
	public void deleteAllUsers (
			@RequestParam(name="userDomain", required = true) String domain,
			@RequestParam(name="userEmail", required = true) String email
			) {
		// Get user data from DB and check if ADMIN
		admin.deleteAllUsers(new UserID(domain, email), UserRole.ADMIN);
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(
			method = RequestMethod.DELETE,
			path = "/iob/admin/instances")
		public void deleteAllInstances (
				@RequestParam(name="userDomain", required = true) String domain,
				@RequestParam(name="userEmail", required = true) String email
				) {
			// Get user data from DB and check if ADMIN
			instancesService.deleteAllInstances(new UserID(domain, email), UserRole.ADMIN);
		}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(
			method = RequestMethod.DELETE,
			path = "/iob/admin/activities")
	public void deleteAllActivities (
			@RequestParam(name="userDomain", required = true) String domain,
			@RequestParam(name="userEmail", required = true) String email
			) {
		// Get user data from DB and check if ADMIN
		activitiesService.deleteAllActivities(new UserID(domain, email), UserRole.ADMIN);
	}
	
}
