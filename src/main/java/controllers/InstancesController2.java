package controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import boundries.*;


@RestController
public class InstancesController2 {
	@RequestMapping(
			method = RequestMethod.GET,
			path ="/iob/instances",
			produces = MediaType.APPLICATION_JSON_VALUE)
		public InstanceBoundary[] getAllActivities() {
			Random random = new Random(System.currentTimeMillis());
			
			return Stream.of(new InstanceBoundary[] {
				new InstanceBoundary(),
				new InstanceBoundary(),
				new InstanceBoundary(),
				new InstanceBoundary()}
			).map(boundary->{
				boundary.setInstanceId(new InstanceId("2022b.yarden.dahan",UUID.randomUUID().toString()));
				boundary.setType("Type"+random.nextInt(21));
				boundary.setName("myName"+random.nextInt(1001));
				boundary.setActive(random.nextBoolean());
				boundary.setCreatedTimestamp(new Date());
				boundary.setCreatedBy(new UserID("2022b.yarden.dahan","abc"+random.nextInt(21)+"@gmail.com"));
				boundary.setLocation(new Location(random.nextDouble()*10+36,random.nextDouble()*10+36));
				
				Map<String, Object> attributes = new HashMap<>();
				attributes.put("Traveler", 5);
				attributes.put("Traveler", 14);
				attributes.put("Route", "Haifa->Tel-aviv");
				boundary.setInstanceAttributes(attributes);

				return boundary;
			})
			.collect(Collectors.toList())
			.toArray(new InstanceBoundary[0]);
		}
	
	
	@RequestMapping(
			method = RequestMethod.POST,
			path = "/iob/instances",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
		public InstanceBoundary createMessage (@RequestBody InstanceBoundary boundary) {
			// MOCKING storing message in database
			boundary.setInstanceId(new InstanceId("2022b.yarden.dahan",UUID.randomUUID().toString()));

			return boundary;
		}
}
