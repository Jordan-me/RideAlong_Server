package iob.logic;

import java.util.Map;

import iob.boundries.CreatedBy;
import iob.boundries.InstanceBoundary;
import iob.boundries.InstanceId;
import iob.boundries.UserID;
import iob.data.InstanceEntity;

public class InstancesConverter {

	public InstanceEntity toEntity(InstanceBoundary instance) {
		InstanceEntity entity = new InstanceEntity();
		
		if(instance.getInstanceId()!= null) {
			String iID = instance.getInstanceId().getDomain() + "," + instance.getInstanceId().getId();
			entity.setInstanceId(iID);
		}
		if(instance.getType()!= null && !instance.getType().isEmpty()) {
			entity.setType(instance.getType());
		}
		if(instance.getName()!= null && !instance.getName().isEmpty()) {
			entity.setName(instance.getName());
		}
		if(instance.getActive()!= null) {
			entity.setActive(instance.getActive());
		}
		entity.setCreatedTimestamp(instance.getCreatedTimestamp());
		if(instance.getCreatedBy()!= null) {
			String createdBy = instance.getCreatedBy().getUserId().getDomain() + "," 
								+ instance.getCreatedBy().getUserId().getEmail();
			entity.setCreatedBy(createdBy);
		}
		if(instance.getLocation()!= null) {
			entity.setLocation(instance.getLocation());
		}
		if(instance.getInstanceAttributes()!= null) {
			entity.setInstanceAttributes(instance.getInstanceAttributes());
		}
		
		return entity;
	}
	
	public InstanceBoundary toBoundary(InstanceEntity entity) {
		InstanceBoundary boundary = new InstanceBoundary();
		
		String[] splittedInstanceId = entity.getInstanceId().split(",");
		boundary.setInstanceId(new InstanceId(splittedInstanceId[0], splittedInstanceId[1]));
		boundary.setType(entity.getType());
		boundary.setName(entity.getName());
		boundary.setActive(entity.getActive());
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		
		String[] splittedCreatedBy = entity.getInstanceId().split(",");
		CreatedBy createdBy = new CreatedBy(new UserID(splittedCreatedBy[0], splittedCreatedBy[1]));
		boundary.setCreatedBy(createdBy);
		
		boundary.setLocation(entity.getLocation());
		boundary.setInstanceAttributes(entity.getInstanceAttributes());
		return boundary;
	}

}
