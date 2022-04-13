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
			String aID = activity.getInstance().getDomain() + "," + activity.getInstance().getId();
			entity.setActivityId(aID);
		}
		if (activity.getType() != null && !activity.getType().isEmpty())
			entity.setType(activity.getType());
		else {
			entity.setType("NONE");
		}
		if (activity.getInstance() != null && !activity.getInstance().getDomain().isEmpty()
				&& !activity.getInstance().getId().isEmpty())
			entity.setInstance(activity.getInstance().getDomain() + "," + activity.getInstance().getId());
		else {
			entity.setInstance("NONE");
		}
		entity.setCreatedTimestamp(activity.getCreatedTimestamp());
		if(activity.getCreatedBy()!= null) {
			String createdBy = activity.getCreatedBy().getUserId().getDomain() + "," 
								+ activity.getCreatedBy().getUserId().getEmail();
			entity.setCreatedBy(createdBy);
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
		System.out.println("Started running activities converter -> to boundary");

		ActivityBoundary boundary = new ActivityBoundary();
		
		String[] splittedActiveId = entity.getActivityId().split(",");
		boundary.setActivityId(new ActivityId(splittedActiveId[0], splittedActiveId[1]));
		
		boundary.setType(entity.getType());
		boundary.setActivityId(new ActivityId(splittedActiveId[0], splittedActiveId[1]));
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		
		if(!entity.getCreatedBy().equals("NONE")) {
			String[] splittedCreatedBy = entity.getCreatedBy().split(",");
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
