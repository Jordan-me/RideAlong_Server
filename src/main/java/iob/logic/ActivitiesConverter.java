package iob.logic;

import org.springframework.stereotype.Component;

import iob.boundries.ActivityBoundary;
import iob.data.ActivityEntity;


@Component
public class ActivitiesConverter {
	
	public ActivityEntity toEntity(ActivityBoundary instance) {
		ActivityEntity entity = new ActivityEntity();
		
//		if(instance.getInstanceId()!= null) {
//			String iID = instance.getInstanceId().getDomain() + "," + instance.getInstanceId().getId();
//			entity.setInstanceId(iID);
//		}
//		if(instance.getType()!= null && !instance.getType().isEmpty()) {
//			entity.setType(instance.getType());
//		}else {
//			entity.setName("NONE");
//		}
//		if(instance.getName()!= null && !instance.getName().isEmpty()) {
//			entity.setName(instance.getName());
//		}else {
//			entity.setName("NONE");
//		}
//		if(instance.getActive()== null) {
//			entity.setActive((boolean)instance.getActive());
//		}else {
//			entity.setActive(false);
//		}
//		entity.setCreatedTimestamp(instance.getCreatedTimestamp());
//		if(instance.getCreatedBy()!= null) {
//			String createdBy = instance.getCreatedBy().getUserId().getDomain() + "," 
//								+ instance.getCreatedBy().getUserId().getEmail();
//			entity.setCreatedBy(createdBy);
//		}else {
//			entity.setCreatedBy("NONE");
//		}
//		if(instance.getLocation()!= null) {
//			entity.setLocation(instance.getLocation().toString());
//		}else {
//			entity.setLocation((new Location(0.0,0.0)).toString());
//		}
//		if(instance.getInstanceAttributes()!= null) {
//			entity.setInstanceAttributes(instance.getInstanceAttributes());
//		}
//		
		return entity;
	}
	
	public ActivityBoundary toBoundary(ActivityEntity entity) {
		ActivityBoundary boundary = new ActivityBoundary();
		
//		String[] splittedInstanceId = entity.getInstanceId().split(",");
//		boundary.setInstanceId(new InstanceId(splittedInstanceId[0], splittedInstanceId[1]));
//		boundary.setType(entity.getType());
//		boundary.setName(entity.getName());
//		boundary.setActive(entity.getActive());
//		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
//		
//		String[] splittedCreatedBy = entity.getInstanceId().split(",");
//		CreatedBy createdBy = new CreatedBy(new UserID(splittedCreatedBy[0], splittedCreatedBy[1]));
//		boundary.setCreatedBy(createdBy);
//		
//		String[] splittedLocation = entity.getLocation().split("-");
//		Location location = new Location(Double.parseDouble(splittedLocation[0]),
//										Double.parseDouble(splittedLocation[1]));
//		boundary.setLocation(location);
//		boundary.setInstanceAttributes(entity.getInstanceAttributes());
		
		return boundary;
	}
}
