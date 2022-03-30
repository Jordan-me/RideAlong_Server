package boundries;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ActivityBoundary {
	private ActivityId activityId;
	private String type;
	private InstanceId instance;
	private Date createdTimestamp;
	private UserID invokedBy;
	private Map<String, Object> activityAttributes;
	
	public ActivityBoundary() {
		this.activityAttributes = new HashMap<String, Object>();
	}
	
	public ActivityBoundary(
			ActivityId activityId,
			String type,
			InstanceId instance,
			Date createdTimestamp,
			UserID invokedBy) 
	{
		this.activityId = null;
		this.type = type;
		this.instance = instance;
		this.createdTimestamp = createdTimestamp;
		this.invokedBy = invokedBy;
		this.activityAttributes = new HashMap<String, Object>();
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


	public InstanceId getInstance() {
		return instance;
	}


	public void setInstance(InstanceId instance) {
		this.instance = instance;
	}


	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}


	public UserID getInvokedBy() {
		return invokedBy;
	}


	public void setInvokedBy(UserID invokedBy) {
		this.invokedBy = invokedBy;
	}


	public Map<String, Object> getActivityAttributes() {
		return activityAttributes;
	}


	public void setActivityAttributes(String key, Object obj) {
		this.activityAttributes.put(key, obj);
	}
}
