package iob.logic;

import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import iob.boundries.ActivityBoundary;
import iob.boundries.ActivityId;
import iob.boundries.CreatedBy;
import iob.boundries.Instance;
import iob.boundries.InstanceId;
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
		ActivityEntity entity = new ActivityEntity();
		entity.setActivityId(activity.getActivityId().toString());
		entity.setType(activity.getType());
		entity.setInstance(activity.getInstance().toString());
		entity.setCreatedTimestamp(activity.getCreatedTimestamp());
		entity.setCreatedBy(activity.getInvokedBy().toString());

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
		
		String[] splittedCreatedBy = entity.getCreatedBy().split("_");
		CreatedBy createdBy = new CreatedBy(new UserID(splittedCreatedBy[0], splittedCreatedBy[1]));
		boundary.setInvokedBy(createdBy);
		
		String[] splittedInstanceID = entity.getInstance().split("_");
		System.err.println(splittedInstanceID.toString());
		Instance instance = new Instance(new InstanceId(splittedInstanceID[0], splittedInstanceID[1]));
		boundary.setInstance(instance);
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
