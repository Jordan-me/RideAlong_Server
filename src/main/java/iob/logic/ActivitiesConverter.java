package iob.logic;

import org.springframework.stereotype.Component;

import iob.boundries.ActivityBoundary;
import iob.data.ActivityEntity;


@Component
public class ActivitiesConverter {
	
	public ActivityEntity toEntity(ActivityBoundary activity) {
		ActivityEntity entity = new ActivityEntity();
		if(activity.getActivityId() != null) {
			
		}else {
			entity.setActivityId(null);
		}
//		if(instance.getInstanceId()!= null) {
//			String iID = instance.getInstanceId().getDomain() + "," + instance.getInstanceId().getId();
//			entity.setInstanceId(iID);
//		}

		return entity;
	}
	
	public ActivityBoundary toBoundary(ActivityEntity entity) {
		ActivityBoundary boundary = new ActivityBoundary();
		
		
		return boundary;
	}
}
