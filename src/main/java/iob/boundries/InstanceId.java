package iob.boundries;

import java.util.Objects;

public class InstanceId {

	private String domain;
	private String id;
	
	public InstanceId() {
	}
	
	public InstanceId(String domain, String id) {
		this();
		this.domain = domain;
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		return Objects.hash(domain, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InstanceId other = (InstanceId) obj;
		return this.domain.equals(other.domain) && this.id.equals(other.id);
	}
	
	@Override
	public String toString() {
		return domain + "_" + id ;
	}
}