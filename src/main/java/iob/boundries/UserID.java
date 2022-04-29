package iob.boundries;

import java.util.Objects;

public class UserID {

	private String domain;
	private String email;
	
	public UserID(){
		
	}
	
	public UserID(String domain, String email) {
		super();
		this.domain = domain;
		this.email = email;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(domain, email);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserID other = (UserID) obj;
		return this.domain.equals(other.domain) && this.email.equals(other.email);
	}
	
	@Override
	public String toString() {
		return "" + this.domain + "_" + this.email;
	}
	
}
