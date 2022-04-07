package iob.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 ACTIVITY_TABLE
------------------------------
activityId		| TYPE	  		| INSTANCE_ID	 |CREATED_TIME_STAMP |CREATED_BY |ACTIVITY_ATTRIBUTES
VARCHAR(255)	| VARCHAR(255)	|	VARCHAR(255) |VARCHAR(255)       |???		 | ???
<PK>
 */
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import iob.logic.IobMapConverter;

@Entity
@Table(name="ACTIVITY_TABLE")
public class ActivityEntity {
	private String activityId;
	private String type;
	private String instance;
	private Date createdTimestamp;
	private String createdBy;
	private Map<String, Object> activityAttributes;
	
	public ActivityEntity() {
	
	}

	@Id
	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	@Column(name="TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Column(name="INSTANCE_ID")
	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_TIME_STAMP")
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	@Column(name="CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	@Convert(converter = IobMapConverter.class)
	@Column(name="ACTIVITY_ATTRIBUTES")
	public Map<String, Object> getActivityAttributes() {
		return activityAttributes;
	}

	public void setActivityAttributes(Map<String, Object> activityAttributes) {
		this.activityAttributes = activityAttributes;
	}	
}
