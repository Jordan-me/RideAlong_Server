package iob.boundries;


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
	public String toString() {
		return "" + this.domain + "_" + this.email;
	}
	
}
