package iob.data;

import java.util.Date;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Lob;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//import javax.validation.constraints.NotBlank;
import java.util.Map;

import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mongodb.lang.NonNull;

import iob.boundries.CreatedBy;
import iob.boundries.Location;

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
	private String type;
	private @Indexed String name;
	private @Indexed boolean active;
	private @Indexed Date createdTimestamp;
	private @Indexed CreatedBy createdBy;
	
	private @GeoSpatialIndexed(type=GeoSpatialIndexType.GEO_2D) double [] location;
	private Map<String, Object> instanceAttributes;
	
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
	
	@Field(name="LOCATION")
	public double[] getLocation() {
		return location;
	}
	
	
	public void setLocation(double[] location) {
		this.location = location;
	}
	
	@Field(name="CREATED_BY")
	public CreatedBy getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
	}
	
	@Field(name="INSTANCE_ATTRIBUTES")
	public Map<String, Object> getInstanceAttributes() {
		return instanceAttributes;
	}


	public void setInstanceAttributes(Map<String, Object> map) {
		this.instanceAttributes = map;
	}

	@Override
	public String toString() {
		return "InstanceEntity [instanceId=" + instanceId + ", type=" + type + ", name=" + name + ", active=" + active
				+ ", createdTimestamp=" + createdTimestamp + ", createdBy=" + createdBy +
				", location=("+location[0]+ ", " +location[1]+ ")"
				+ ", instanceAttributes=" + instanceAttributes + "]";
	}

	
}
