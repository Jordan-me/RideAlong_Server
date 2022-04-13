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
import org.springframework.web.bind.annotation.RestController;

import iob.boundries.*;
import iob.logic.ActivitiesService;
import iob.logic.InstancesService;
import iob.logic.UsersService;


@RestController
public class AdminController {
	private UsersService admin;
	private InstancesService instancesService;
	
	@Autowired
	public void setInstancesService(InstancesService instancesService) {
		this.instancesService = instancesService;
	}
	@Autowired
	public void setUsersService(UsersService admin) {
		this.admin = admin;
	}
	@RequestMapping(
			method = RequestMethod.GET,
			path ="/iob/admin/users",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary[] getAllUsers() {
			return admin.getAllUsers().toArray(new UserBoundary[0]);
//			Random random = new Random(System.currentTimeMillis());
//			
//			return Stream.of(new UserBoundary[] {
//				new UserBoundary(),
//				new UserBoundary(),
//				new UserBoundary(),
//				new UserBoundary()}
//			).map(user->{
//				user.setUserId(new UserID("2022b.yarden.dahan",random.nextInt(21)+"abc@gmail.com"));
//				user.setAvatar(null);
//				user.setRole("Traveler");
//				user.setUsername("username"+random.nextInt(21));
//				return user;
//			})
//			.collect(Collectors.toList())
//			.toArray(new UserBoundary[0]);
		}
	
	@RequestMapping(
			method = RequestMethod.GET,
			path ="/iob/admin/activities",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public ActivityBoundary[] getAllActivities() {
		Random random = new Random(System.currentTimeMillis());
		
		return Stream.of(new ActivityBoundary[] {
			new ActivityBoundary(),
			new ActivityBoundary(),
			new ActivityBoundary(),
			new ActivityBoundary()}
		).map(boundary->{
			boundary.setActivityId(new ActivityId("2022b.yarden.dahan",UUID.randomUUID().toString()));
			boundary.setType("testing 1,2,3");
			boundary.setInstance(new InstanceId("2022b.yarden.dahan",UUID.randomUUID().toString()));
			boundary.setCreatedTimestamp(new Date());
			boundary.setCreatedBy(new CreatedBy(new UserID("2022b.yarden.dahan","abc"+random.nextInt(21)+"@gmail.com")));
			HashMap<String, Object> map = new HashMap<String, Object>(); 
			map.put("event", "event"+random.nextInt(1001));
			boundary.setActivityAttributes(map);
			return boundary;
		})
		.collect(Collectors.toList())
		.toArray(new ActivityBoundary[0]);
	}
	
	@RequestMapping(
			method = RequestMethod.DELETE,
			path = "/iob/admin/users")
	public void deleteAllUsers () {
		//TODO: validate admin permission
		admin.deleteAllUsers();
	}
	
	
	@RequestMapping(
			method = RequestMethod.DELETE,
			path = "/iob/admin/instances")
		public void deleteAllInstances () {
			//TODO: validate admin permission
			instancesService.deleteAllInstances();
		}
	
	@RequestMapping(
			method = RequestMethod.DELETE,
			path = "/iob/admin/activities")
	public void deleteAllActivities () {
		//TODO: validate admin permission
	}
}
