package iob.boundries;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ActivityBoundary {
	private ActivityId activityId;
	private String type;
	private Instance instance;
	private Date createdTimestamp;
	private CreatedBy invokedBy;
	private Map<String, Object> activityAttributes;
	
	public ActivityBoundary() {
		this.activityAttributes = new HashMap<String, Object>();
	}
	
	public ActivityBoundary(
			ActivityId activityId,
			String type,
			Instance instance,
			Date createdTimestamp,
			CreatedBy invokedBy, Map<String, Object> activityAttributes) 
	{
		this.activityId = null;
		this.type = type;
		this.instance = instance;
		this.createdTimestamp = createdTimestamp;
		this.invokedBy = invokedBy;
//		this.activityAttributes = new HashMap<String, Object>();
		this.activityAttributes = activityAttributes;
	}

	public ActivityId getActivityId() {
		return activityId;
	}

	public void setActivityId(ActivityId activityId) {
		this.activityId = activityId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public Instance getInstance() {
		return instance;
	}


	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}


	public CreatedBy getInvokedBy() {
		return invokedBy;
	}


	public void setInvokedBy(CreatedBy invokedBy) {
		this.invokedBy = invokedBy;
	}


	public Map<String, Object> getActivityAttributes() {
		return activityAttributes;
	}


	public void setActivityAttributes(Map<String, Object> activityAttributes) {
		this.activityAttributes = activityAttributes;
	}
}
