package iob.logic;

import org.springframework.stereotype.Component;

import iob.boundries.ActivityBoundary;
import iob.boundries.ActivityId;
import iob.boundries.Instance;
import iob.boundries.InstanceId;
import iob.data.ActivityEntity;


@Component
public class ActivitiesConverter {

	public ActivityEntity toEntity(ActivityBoundary activity) {
		
		ActivityEntity entity = new ActivityEntity();
		entity.setActivityId(activity.getActivityId().toString());
		entity.setType(activity.getType());
		entity.setInstance(activity.getInstance().toString());
		entity.setCreatedTimestamp(activity.getCreatedTimestamp());
		entity.setCreatedBy(activity.getInvokedBy());

		if (activity.getActivityAttributes() != null) {
			entity.setActivityAttributes(activity.getActivityAttributes());
		}
		return entity;
	}
	

	public ActivityBoundary toBoundary(ActivityEntity entity) {
		ActivityBoundary boundary = new ActivityBoundary();
		
		String[] splittedActiveId = entity.getActivityId().split("_");
		boundary.setActivityId(new ActivityId(splittedActiveId[0], splittedActiveId[1]));
		
		boundary.setType(entity.getType());
		boundary.setActivityId(new ActivityId(splittedActiveId[0], splittedActiveId[1]));
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		
//		String[] splittedCreatedBy = entity.getCreatedBy().split("_");
//		CreatedBy createdBy = new CreatedBy(new UserID(splittedCreatedBy[0], splittedCreatedBy[1]));
		boundary.setInvokedBy(entity.getCreatedBy());
		
		String[] splittedInstanceID = entity.getInstance().split("_");
		System.err.println(splittedInstanceID.toString());
		Instance instance = new Instance(new InstanceId(splittedInstanceID[0], splittedInstanceID[1]));
		boundary.setInstance(instance);
		if (entity.getActivityAttributes() != null) {
			boundary.setActivityAttributes(entity.getActivityAttributes());
		}
			
		return boundary;
	}

}
