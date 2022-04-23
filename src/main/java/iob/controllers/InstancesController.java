package iob.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iob.boundries.*;
import iob.data.UserRole;
import iob.logic.ActivitiesService;
import iob.logic.ExtendedInstancesService;
import iob.logic.ExtendedUserService;
import iob.logic.InstanceNotFoundException;
import iob.logic.InstancesService;
import iob.logic.UsersService;

@RestController
public class InstancesController {
	private ExtendedUserService manager;
	private ExtendedInstancesService instancesService;
	
	@Autowired
	public void setInstancesService(ExtendedInstancesService instancesService) {
		this.instancesService = instancesService;
	}
	@Autowired
	public void setUsersService(ExtendedUserService manager) {
		this.manager = manager;
	}
	@RequestMapping(
			method = RequestMethod.GET,
			path ="/iob/instances",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public InstanceBoundary[] getAllInstances(
				@RequestParam(name="userDomain", required = true) String userDomain,
				@RequestParam(name="userEmail", required = true) String userEmail,
				@RequestParam(name="size", required = false, defaultValue = "10") int size,
				@RequestParam(name="page", required = false, defaultValue = "0") int page) {
			return this.instancesService.getAllInstances(userDomain,userEmail,size,page)
					.toArray(new InstanceBoundary[0]);
		}
	
	// Retrieve instance
	@RequestMapping(
			path = "/iob/instances/{instanceDomain}/{instanceId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary getInstance(
			@PathVariable("instanceDomain") String instanceDomain,
			@PathVariable("instanceId") String instanceId,
			@RequestParam(name="userDomain", required = true) String userDomain,
			@RequestParam(name="userEmail", required = true) String userEmail) throws InstanceNotFoundException
	{
		
		return this.instancesService.getSpecificInstance(userDomain,userEmail,
				instanceDomain,instanceId);
		
	}
	//Retrieve instances By Name
	@RequestMapping(
			method = RequestMethod.GET,
			path ="/iob/instances/search/byName/{name}",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary[] getInstancesByName(
			@PathVariable("name") String instanceName,
			@RequestParam(name="userDomain", required = true) String userDomain,
			@RequestParam(name="userEmail", required = true) String userEmail,
			@RequestParam(name="size", required = false, defaultValue = "10") int size,
			@RequestParam(name="page", required = false, defaultValue = "0") int page) throws InstanceNotFoundException {
		InstanceBoundary[] instances = this.instancesService.getInstancesByName(userDomain,userEmail,instanceName,
				size, page).toArray(new InstanceBoundary[0]);
		if(Arrays.isNullOrEmpty(instances))
			throw new InstanceNotFoundException("could not find instances with name- " + instanceName);
		return instances;
	}
	
	//Retrieve instances By Type
	@RequestMapping(
 			method = RequestMethod.GET,
			path ="/iob/instances/search/byType/{type}",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public InstanceBoundary[] getInstancesByType(
				@PathVariable("type") String instanceType,
				@RequestParam(name="userDomain", required = true) String userDomain,
				@RequestParam(name="userEmail", required = true) String userEmail,
				@RequestParam(name="size", required = false, defaultValue = "10") int size,
				@RequestParam(name="page", required = false, defaultValue = "0") int page) throws InstanceNotFoundException {
		InstanceBoundary[] instances = this.instancesService.getInstancesByType(userDomain,userEmail,instanceType,
				size, page).toArray(new InstanceBoundary[0]);
		if(Arrays.isNullOrEmpty(instances))
			throw new InstanceNotFoundException("could not find instances with type- " + instanceType);
		return instances;
		}
	
	//TODO: implement getnstancesByLocation
	@RequestMapping(
			method = RequestMethod.GET,
			path ="/iob/instances/search/near/{lat}/{lng}/{distance}",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public InstanceBoundary[] getAInstancesByLocation(
				@PathVariable("lat") double lat,
				@PathVariable("lng") double lng,
				@PathVariable("distance") double distance,
				@RequestParam(name="userDomain", required = true) String userDomain,
				@RequestParam(name="userEmail", required = true) String userEmail,
				@RequestParam(name="size", required = false, defaultValue = "10") int size,
				@RequestParam(name="page", required = false, defaultValue = "0") int page) throws InstanceNotFoundException {
		Location location = new Location(lat,lng);
		InstanceBoundary[] instances = this.instancesService.getInstancesByLocation(userDomain,userEmail,
				location,distance,size, page).toArray(new InstanceBoundary[0]);
		if(Arrays.isNullOrEmpty(instances))
			throw new InstanceNotFoundException("could not find instances near- " + location.toString());
		return instances;
		}		
	
	@RequestMapping(
			method = RequestMethod.POST,
			path = "/iob/instances",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
		public InstanceBoundary createInstance (@RequestBody InstanceBoundary boundary) {
			return this.instancesService.createInstance(boundary);
		}
	//update an instance
	@RequestMapping(
			path = "/iob/instances/{instanceDomain}/{instanceId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateInstance(
			@PathVariable("instanceDomain") String intanceDomain,
			@PathVariable("instanceId") String instanceId,
			@RequestParam(name="userDomain", required = true) String userDomain,
			@RequestParam(name="userEmail", required = true) String userEmail,
			@RequestBody InstanceBoundary instanceBoundary) throws InstanceNotFoundException {
		// Get user data from DB and check if ADMIN
		this.manager.checkUserPermission((new UserID(userDomain, userEmail)).toString(), UserRole.MANAGER,true);
		this.instancesService.updateInstance(intanceDomain,instanceId,instanceBoundary);
	}
	 

}
