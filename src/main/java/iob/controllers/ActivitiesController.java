package iob.controllers;

import java.util.Date;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iob.boundries.*;
import iob.logic.ActivitiesService;

@RestController
public class ActivitiesController {
	private ActivitiesService activitiesService;
	@RequestMapping(
			path = "/iob/activities",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ActivityBoundary createActivity (@RequestBody ActivityBoundary boundary) {
		// MOCKING storing message in database
		boundary.setActivityId(new ActivityId("2022b.yarden.dahan",UUID.randomUUID().toString()));
		boundary.setType("testing 1,2");
		boundary.setInstance(new InstanceId("2022b.yarden.dahan",UUID.randomUUID().toString()));
		boundary.setCreatedTimestamp(new Date());
		boundary.setCreatedBy(new CreatedBy(new UserID("2022b.yarden.dahan","abc@gmail.com")));
		boundary.setActivityAttributes("event", "Flight From TLV");
		
		return boundary;
	}
}
