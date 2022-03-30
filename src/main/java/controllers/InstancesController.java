package controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import boundries.InstanceBoundary;
import boundries.InstanceId;
import boundries.Location;
import boundries.UserBoundary;
import boundries.UserID;

@RestController
public class InstancesController {
	// Retrieve instance
	@RequestMapping(
			path = "/iob/instances/{instanceDomain}/{instanceId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary getInstance(@PathVariable("instanceDomain") String domain,
			@PathVariable("instanceId") String id) {
		Random random = new Random(System.currentTimeMillis());

		
		InstanceId InstanceId = new InstanceId(domain,id);
		InstanceBoundary instance = new InstanceBoundary();
		
		instance.setInstanceId(InstanceId);
		instance.setType("demo type");
		instance.setType("Type"+random.nextInt(21));
		instance.setName("myName"+random.nextInt(1001));
		instance.setActive(random.nextBoolean());
		instance.setCreatedTimestamp(new Date());
		instance.setCreatedBy(new UserID("2022b.yarden.dahan","abc"+random.nextInt(21)+"@gmail.com"));
		instance.setLocation(new Location(random.nextDouble()*10+36,random.nextDouble()*10+36));
		
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("key1","can be set to any value you wish");
		attributes.put("key2","you can also name the attributes any name you like");
		attributes.put("key3",6.2);
		attributes.put("key4",false);
		instance.setInstanceAttributes(attributes);
		
		return instance;
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
		InstanceId instanceID = new InstanceId(intanceDomain,instanceId);
		instanceBoundary.setInstanceId(instanceID);
		System.err.println("update InstanceBoundary:\ndomain: " +instanceBoundary.getInstanceId().getDomain()+
				"\nId:" +instanceBoundary.getInstanceId().getId() );
	}
	

}
