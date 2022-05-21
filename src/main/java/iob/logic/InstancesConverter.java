package iob.logic;

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
	@PostConstruct
	public void init() {
		new ObjectMapper();
	}
	
	public InstanceEntity toEntity(InstanceBoundary instance) {
		InstanceEntity entity = new InstanceEntity();
		entity.setInstanceId(instance.getInstanceId().toString());
		entity.setType(instance.getType());
		entity.setName(instance.getName());
		entity.setActive((boolean)instance.getActive());
		entity.setCreatedTimestamp(instance.getCreatedTimestamp());
		entity.setCreatedBy(instance.getCreatedBy());
		entity.setLocation(instance.getLocation());

		if (instance.getInstanceAttributes() != null) {
			entity.setInstanceAttributes(instance.getInstanceAttributes());

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
		
//		String[] splittedCreatedBy = entity.getCreatedBy().split("_");
//		CreatedBy createdBy = new CreatedBy(new UserID(splittedCreatedBy[0], splittedCreatedBy[1]));
		boundary.setCreatedBy(entity.getCreatedBy());
		
		boundary.setLocation(entity.getLocation());
		
		if (entity.getInstanceAttributes() != null) {
			boundary.setInstanceAttributes(entity.getInstanceAttributes());
		}

		return boundary;
	}


}
