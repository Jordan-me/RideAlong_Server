package iob.logic;

import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import iob.boundries.ActivityBoundary;
import iob.boundries.ActivityId;
import iob.boundries.CreatedBy;
import iob.boundries.UserID;
import iob.data.ActivityEntity;


@Component
public class ActivitiesConverter {
	private ObjectMapper jackson;
	
	@PostConstruct
	public void init() {
		this.jackson = new ObjectMapper();
	}
	
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
		if (activity.getActivityAttributes() != null) {
			entity.setActivityAttributes(
			  this.toEntity(
					  activity.getActivityAttributes()));
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
		if (entity.getActivityAttributes() != null) {
			boundary.setActivityAttributes(
				this.toBoundaryFromJsonString(entity.getActivityAttributes()));
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
