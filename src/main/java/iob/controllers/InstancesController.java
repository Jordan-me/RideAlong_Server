package iob.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class InstancesController {
	private InstancesService instancesService;
	
	@Autowired
	public void setInstancesService(InstancesService instancesService) {
		this.instancesService = instancesService;
	}
	
	@RequestMapping(
			method = RequestMethod.GET,
			path ="/iob/instances",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public InstanceBoundary[] getAllInstances() {
			return this.instancesService.getAllInstances()
					.toArray(new InstanceBoundary[0]);
		}
	
	
	@RequestMapping(
			method = RequestMethod.POST,
			path = "/iob/instances",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
		public InstanceBoundary createInstance (@RequestBody InstanceBoundary boundary) {
			return this.instancesService.createInstance(boundary);
		}
	// Retrieve instance
	@RequestMapping(
			path = "/iob/instances/{instanceDomain}/{instanceId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary getInstance(@PathVariable("instanceDomain") String domain,
										@PathVariable("instanceId") String id) {
		return this.instancesService.getSpecificInstance(domain,id);

	}
	//update an instance
	@RequestMapping(
			path = "/iob/instances/{instanceDomain}/{instanceId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateInstance(	 				   
			 				   @PathVariable("instanceDomain") String intanceDomain,
			 				   @PathVariable("instanceId") String instanceId,
			 				   @RequestBody InstanceBoundary instanceBoundary) {
		
		this.instancesService.updateInstance(intanceDomain,instanceId,instanceBoundary);
	}
	 

}
