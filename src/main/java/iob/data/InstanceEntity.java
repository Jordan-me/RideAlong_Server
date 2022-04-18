package iob.data;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



/*
INSTANCE_TABLE
------------------------------------------------------------------------------------------------------------------------------------------------------
instanceId		| TYPE	  		| NAME 			 | ACTIVE 			| CREATED_TIME_STAMP | LOCATION	  | CREATED_BY |INSTANCE_ATTRIBUTES
VARCHAR(255)	| VARCHAR(255)	|	VARCHAR(255) |VARCHAR(255)		|VARCHAR(255)        |VARCHAR(255)|VARCHAR(255)| ???
<PK>
*/
@Entity
@Table(name="INSTANCE_TABLE")
public class InstanceEntity {
	private String instanceId;
	private String type;
	private String name;
	private boolean active;
	private Date createdTimestamp;
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
	public boolean getActive() {
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
	@Column(name="LOCATION_lat")
	public double getLat() {
		return lat;
	}
	
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	@Column(name="LOCATION_lng")
	public double getLng() {
		return lng;
	}
	
	
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	@Column(name="CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Lob
	@Column(name="INSTANCE_ATTRIBUTES")
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
