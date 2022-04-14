package iob.logic;

import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import iob.boundries.CreatedBy;
import iob.boundries.InstanceBoundary;
import iob.boundries.InstanceId;
import iob.boundries.Location;
import iob.boundries.UserID;
import iob.data.InstanceEntity;

@Component
public class InstancesConverter {
	private ObjectMapper jackson;
	
	@PostConstruct
	public void init() {
		this.jackson = new ObjectMapper();
	}
	
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
		if (instance.getInstanceAttributes() != null) {
			entity.setInstanceAttributes(
			  this.toEntity(
					  instance.getInstanceAttributes()));
		}
		
		return entity;
	}
	
	public String toEntity (Map<String, Object> object) {
		try {
			return this.jackson
				.writeValueAsString(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
		
		if (entity.getInstanceAttributes() != null) {
			boundary.setInstanceAttributes(
				this.toBoundaryFromJsonString(entity.getInstanceAttributes()));
		}

		return boundary;
	}
	public Map<String, Object> toBoundaryFromJsonString (String json){
		try {
			return this.jackson
				.readValue(json, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
