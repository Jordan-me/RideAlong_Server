package iob.logic;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import iob.boundries.CreatedBy;
import iob.boundries.InstanceBoundary;
import iob.boundries.InstanceId;
import iob.boundries.Location;
import iob.boundries.UserID;
import iob.data.InstanceEntity;

@Component
public class InstancesConverter {

	public InstanceEntity toEntity(InstanceBoundary instance) {
		InstanceEntity entity = new InstanceEntity();
		
		if(instance.getInstanceId()!= null) {
			String iID = instance.getInstanceId().toString().toLowerCase();
			entity.setInstanceId(iID);
		}
		if(instance.getType()!= null && !instance.getType().isEmpty()) {
			entity.setType(instance.getType());
		}else {
			entity.setType("NONE");
		}
		if(instance.getName()!= null && !instance.getName().isEmpty()) {
			entity.setName(instance.getName());
		}else {
			entity.setName("NONE");
		}
		if(instance.getActive()!= null) {
			entity.setActive((boolean)instance.getActive());
		}else {
			entity.setActive(false);
		}
		if(instance.getCreatedTimestamp()!= null) {
			
		}
		entity.setCreatedTimestamp(instance.getCreatedTimestamp());
		if(instance.getCreatedBy()!= null) {
			String createdBy = instance.getCreatedBy().getUserId().getDomain() + "," 
								+ instance.getCreatedBy().getUserId().getEmail();
			entity.setCreatedBy(createdBy);
		}else {
			entity.setCreatedBy("NONE");
		}
		if(instance.getLocation()!= null) {
			entity.setLocation(instance.getLocation().toString());
		}else {
			entity.setLocation((new Location(0.0,0.0)).toString());
		}
		if(instance.getInstanceAttributes()!= null) {
			Map<String, Object> map = instance.getInstanceAttributes();
		    String mapAsString = map.keySet().stream()
		    	      .map(key -> key + "=" + map.get(key))
		    	      .collect(Collectors.joining("$", "{", "}"));
			entity.setInstanceAttributes(mapAsString);
		}else {
			entity.setInstanceAttributes("NONE");
		}
		
		return entity;
	}
	
	public InstanceBoundary toBoundary(InstanceEntity entity) {
		InstanceBoundary boundary = new InstanceBoundary();
		
		String[] splittedInstanceId = entity.getInstanceId().split("_");
		boundary.setInstanceId(new InstanceId(splittedInstanceId[0], splittedInstanceId[1]));
		
		boundary.setType(entity.getType());
		boundary.setName(entity.getName());
		boundary.setActive(entity.getActive());
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		
		if(!entity.getCreatedBy().equals("NONE")) {
			String[] splittedCreatedBy = entity.getCreatedBy().split(",");
			CreatedBy createdBy = new CreatedBy(new UserID(splittedCreatedBy[0], splittedCreatedBy[1]));
			boundary.setCreatedBy(createdBy);
		}
		
		String[] splittedLocation = entity.getLocation().split("-");
		Location location = new Location(Double.parseDouble(splittedLocation[0]),
										Double.parseDouble(splittedLocation[1]));
		boundary.setLocation(location);
		
		String mapAsString = entity.getInstanceAttributes();
		if(!mapAsString.equals("NONE")) {
			Map<String, Object> map = Arrays.stream(mapAsString.split(","))
					.map(entry -> entry.split("="))
					.collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
			boundary.setInstanceAttributes(map);			
		}else {
			boundary.setInstanceAttributes(null);
		}
		
		return boundary;
	}

}
