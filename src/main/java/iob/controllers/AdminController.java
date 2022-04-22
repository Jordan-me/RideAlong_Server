package iob.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iob.boundries.*;
import iob.data.UserEntity;
import iob.data.UserRole;
import iob.logic.ActivitiesService;
import iob.logic.ExtendedUserService;
import iob.logic.InstancesService;
import iob.logic.UserNotFoundException;
import iob.logic.UsersService;


@RestController
public class AdminController {
	private ExtendedUserService admin;
	private InstancesService instancesService;
	private ActivitiesService activitiesService;
	
	@Autowired
	public void ActivitiesService(ActivitiesService activitiesService) {
		this.activitiesService = activitiesService;
	}
	@Autowired
	public void setInstancesService(InstancesService instancesService) {
		this.instancesService = instancesService;
	}
	@Autowired
	public void setUsersService(ExtendedUserService admin) {
		this.admin = admin;
	}
	
	@RequestMapping(
			method = RequestMethod.GET,
			path ="/iob/admin/activities",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActivityBoundary[] getAllActivities() {
		return activitiesService.getAllActivities().toArray(new ActivityBoundary[0]);
	}
	
	@RequestMapping(
			method = RequestMethod.GET,
			path ="/iob/admin/users",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary[] getAllUsers() {
			return admin.getAllUsers().toArray(new UserBoundary[0]);

		}
	
	@RequestMapping(
			method = RequestMethod.DELETE,
			path = "/iob/admin/users")
	public void deleteAllUsers (
			@RequestParam(name="userDomain", required = true) String domain,
			@RequestParam(name="userEmail", required = true) String email
			) {
		// Get user data from DB and check if ADMIN
		this.admin.checkUserPermission((new UserID(domain, email)).toString(), UserRole.ADMIN);
		admin.deleteAllUsers();
	}
	
	
	@RequestMapping(
			method = RequestMethod.DELETE,
			path = "/iob/admin/instances")
		public void deleteAllInstances (
				@RequestParam(name="userDomain", required = true) String domain,
				@RequestParam(name="userEmail", required = true) String email
				) {
			// Get user data from DB and check if ADMIN
			this.admin.checkUserPermission((new UserID(domain, email)).toString(), UserRole.ADMIN);
			instancesService.deleteAllInstances();
		}
	
	@RequestMapping(
			method = RequestMethod.DELETE,
			path = "/iob/admin/activities")
	public void deleteAllActivities (
			@RequestParam(name="userDomain", required = true) String domain,
			@RequestParam(name="userEmail", required = true) String email
			) {
		// Get user data from DB and check if ADMIN
		this.admin.checkUserPermission((new UserID(domain, email)).toString(), UserRole.ADMIN);
		activitiesService.deleteAllActivities();
	}
	
}
