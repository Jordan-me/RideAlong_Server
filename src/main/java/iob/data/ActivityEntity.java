package iob.data;

import java.util.Date;
import java.util.Map;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Lob;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
/*
 ACTIVITY_TABLE
------------------------------
activityId		| TYPE	  		| INSTANCE_ID	 |CREATED_TIME_STAMP |CREATED_BY |ACTIVITY_ATTRIBUTES
VARCHAR(255)	| VARCHAR(255)	|	VARCHAR(255) |VARCHAR(255)       |???		 | ???
<PK>
 */
import org.springframework.data.mongodb.core.mapping.Field;

import com.mongodb.lang.NonNull;  

@Document(collection = "Activities")
//@Entity
//@Table(name="ACTIVITY_TABLE")
public class ActivityEntity {
	@Id
	private String activityId;
	@NonNull
	private String type;
	@NonNull
	private String instance;
	private Date createdTimestamp;
	@NonNull
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
	@Field(name="TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Field(name="INSTANCE_ID")
	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}
	
	@Field(name="CREATED_TIME_STAMP")
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	@Field(name="CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	@Field(name="ACTIVITY_ATTRIBUTES")
	public Map<String, Object> getActivityAttributes() {
		return activityAttributes;
	}

	public void setActivityAttributes(Map<String, Object> map) {
		this.activityAttributes = map;
	}	
}
