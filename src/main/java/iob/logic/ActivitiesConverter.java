package iob.logic;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import iob.boundries.ActivityBoundary;
import iob.boundries.ActivityId;
import iob.boundries.CreatedBy;
import iob.boundries.UserID;
import iob.data.ActivityEntity;


@Component
public class ActivitiesConverter {
	
	public ActivityEntity toEntity(ActivityBoundary activity) {
		System.out.println("Started running activities converter -> to entity");

		ActivityEntity entity = new ActivityEntity();
		if (activity.getActivityId() != null) {
			entity.setActivityId(activity.getActivityId().toString());
		}
		if (activity.getType() != null && !activity.getType().isEmpty())
			entity.setType(activity.getType());
		else {
			entity.setType("NONE");
		}
		if (activity.getInstance() != null && !activity.getInstance().getDomain().isEmpty()
				&& !activity.getInstance().getId().isEmpty())
			entity.setInstance(activity.getInstance().toString());
		else {
			throw new RuntimeException("Activity must connect to instance- InstanceId missing");
		}
		entity.setCreatedTimestamp(activity.getCreatedTimestamp());
		if(activity.getCreatedBy()!= null) {
			entity.setCreatedBy(activity.getCreatedBy().toString());
		}else {
			entity.setCreatedBy("NONE");
		}
		if(activity.getActivityAttributes()!= null) {
			Map<String, Object> map = activity.getActivityAttributes();
		    String mapAsString = map.keySet().stream()
		    	      .map(key -> key + "=" + map.get(key))
		    	      .collect(Collectors.joining("$", "{", "}"));
			entity.setActivityAttributes(mapAsString);
		}else {
			entity.setActivityAttributes("NONE");
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
		
		if(!entity.getCreatedBy().equals("NONE")) {
			String[] splittedCreatedBy = entity.getCreatedBy().split("_");
			CreatedBy createdBy = new CreatedBy(new UserID(splittedCreatedBy[0], splittedCreatedBy[1]));
			boundary.setCreatedBy(createdBy);
		}
		
		String mapAsString = entity.getActivityAttributes();
		if(!mapAsString.equals("NONE")) {
			Map<String, Object> map = Arrays.stream(mapAsString.split(","))
					.map(entry -> entry.split("="))
					.collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
			boundary.setActivityAttributes(map);			
		}else {
			boundary.setActivityAttributes(null);
		}
		
		return boundary;
	}
}
