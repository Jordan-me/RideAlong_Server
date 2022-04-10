package iob.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import iob.boundries.Location;


/*
INSTANCE_TABLE
------------------------------
instanceId		| TYPE	  		| NAME 			 | ACTIVE 			| CREATED_TIME_STAMP | LOCATION | CREATED_BY |INSTANCE_ATTRIBUTES
VARCHAR(255)	| VARCHAR(255)	|	VARCHAR(255) |VARCHAR(255)		|VARCHAR(255)        |???		|???		 | ???
<PK>
*/
@Entity
@Table(name="INSTANCE_TABLE")
public class InstanceEntity {
	private String instanceId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private String createdBy;
	private Location location;
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
	
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	@Column(name = "ACTIVE")
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_TIME_STAMP")
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}
	
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	@Lob
	@Column(name="LOCATION")
	public Location getLocation() {
		return location;
	}
	
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	@Lob
	@Column(name="CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Lob
	@Column(name="INSTANCE_ATTRIBUTES")
	public Map<String, Object> getInstanceAttributes() {
		return instanceAttributes;
	}


	public void setInstanceAttributes(Map<String, Object> instanceAttributes) {
		this.instanceAttributes = instanceAttributes;
	}

	
}
