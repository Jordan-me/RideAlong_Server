package iob.data;

import java.util.Date;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Lob;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mongodb.lang.NonNull;

import org.springframework.data.annotation.Id;


/*
INSTANCE_TABLE
------------------------------------------------------------------------------------------------------------------------------------------------------
instanceId		| TYPE	  		| NAME 			 | ACTIVE 			| CREATED_TIME_STAMP | LOCATION	  | CREATED_BY |INSTANCE_ATTRIBUTES
VARCHAR(255)	| VARCHAR(255)	|	VARCHAR(255) |VARCHAR(255)		|VARCHAR(255)        |VARCHAR(255)|VARCHAR(255)| ???
<PK>
*/
//@Entity
//@Table(name="INSTANCE_TABLE")
@Document(collection = "Instances")
public class InstanceEntity {
	@Id
	private String instanceId;
	@NonNull
	private String type;
	@NonNull
	private String name;
	@NonNull
	private boolean active;
	private Date createdTimestamp;
	@NonNull
	private String createdBy;
	private double lat;
	private double lng;
	private String instanceAttributes;
	
	public InstanceEntity() {
	}
	
	@Id
	public String getInstanceId() {
		return instanceId;
	}
	
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	
	@Field(name = "TYPE")
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Field(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	@Field(name = "ACTIVE")
	public boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@Field(name="CREATED_TIME_STAMP")
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}
	
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	@Field(name="LOCATION_lat")
	public double getLat() {
		return lat;
	}
	
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	@Field(name="LOCATION_lng")
	public double getLng() {
		return lng;
	}
	
	
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	@Field(name="CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Field(name="INSTANCE_ATTRIBUTES")
	public String getInstanceAttributes() {
		return instanceAttributes;
	}


	public void setInstanceAttributes(String instanceAttributes) {
		this.instanceAttributes = instanceAttributes;
	}

	@Override
	public String toString() {
		return "InstanceEntity [instanceId=" + instanceId + ", type=" + type + ", name=" + name + ", active=" + active
				+ ", createdTimestamp=" + createdTimestamp + ", createdBy=" + createdBy + ", location=(" + lat + "," + lng + ")"
				+ ", instanceAttributes=" + instanceAttributes + "]";
	}

	
}
