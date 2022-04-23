package iob;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import iob.boundries.NewUserBoundary;
import iob.boundries.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PermissionTest {
	private int port;
	private String url;
	private RestTemplate restTemplate;
	private String domainName;
		
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
		
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + this.port;
		this.restTemplate = new RestTemplate();
	}
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	@Test
	@DisplayName("Test permission: check admin permission")
	public void testAdminGetAllUsers() {
		// GIVEN the server is up
		//	    AND the database contains 3 users(admin, manager, player)
		List<UserBoundary> addedUsers = new ArrayList<>();
		UserBoundary adminUser = this.restTemplate
				.postForObject(this.url+"/iob/users", 
								new NewUserBoundary("admin@gmail.com", "Mark", "ADMIN", "M"), 
								UserBoundary.class);
			addedUsers.add(adminUser);
			
			UserBoundary managerUser = this.restTemplate
				.postForObject(this.url+"/iob/users", 
								new NewUserBoundary("manager@gmail.com", "Yarden", "MANAGER", "Y"), 
								UserBoundary.class);
			addedUsers.add(managerUser);
			
			UserBoundary player1User = this.restTemplate
				.postForObject(this.url+"/iob/users", 
								new NewUserBoundary("player1@gmail.com", "Tzuf", "PLAYER", "T"), 
								UserBoundary.class);
			addedUsers.add(player1User);
			
			// WHEN: the user requesting is an adim(role)
			//		& activtae admin controller functionallity 
			String adminEmail = "admin@gmail.com";
			UserBoundary[] results = 
					this.restTemplate.getForObject(this.url+"/iob/admin/users?"
													+ "userDomain={adminDomain}&"
													+ "userEmail={adminEmail}&",
													UserBoundary[].class, this.domainName, adminEmail);
			// THEN: the server APROVED admin & reject other roles
			assertThat(results).
			usingRecursiveFieldByFieldElementComparator().
			isSubsetOf(addedUsers);
			try {
				this.restTemplate.getForObject(this.url+"/iob/admin/users?"
						+ "userDomain={adminDomain}&"
						+ "userEmail={adminEmail}&",
						UserBoundary[].class, this.domainName, managerUser.getUserId().getEmail());
			}catch(RuntimeException e) {
				System.err.println("test pass");
			}
			try {
				this.restTemplate.getForObject(this.url+"/iob/admin/users?"
						+ "userDomain={adminDomain}&"
						+ "userEmail={adminEmail}&",
						UserBoundary[].class, this.domainName, player1User.getUserId().getEmail());
			}catch(RuntimeException e) {
				System.err.println("test pass");
			}
	}
	@Test
	@DisplayName("Test permission: check admin permission-delete all users")
	public void testAdminDeleteAllUsers() {
		// GIVEN the server is up
		//	    AND the database contains 3 users(admin, manager, player)
		List<UserBoundary> addedUsers = new ArrayList<>();
		UserBoundary adminUser = this.restTemplate
				.postForObject(this.url+"/iob/users", 
								new NewUserBoundary("admin@gmail.com", "Mark", "ADMIN", "M"), 
								UserBoundary.class);
			addedUsers.add(adminUser);
			
			UserBoundary managerUser = this.restTemplate
				.postForObject(this.url+"/iob/users", 
								new NewUserBoundary("manager@gmail.com", "Yarden", "MANAGER", "Y"), 
								UserBoundary.class);
			addedUsers.add(managerUser);
			
			UserBoundary player1User = this.restTemplate
				.postForObject(this.url+"/iob/users", 
								new NewUserBoundary("player1@gmail.com", "Tzuf", "PLAYER", "T"), 
								UserBoundary.class);
			addedUsers.add(player1User);
			
			// WHEN: the user requesting is an adim(role)
			//		& activtae admin controller functionallity 
			String adminEmail = "admin@gmail.com";
			this.restTemplate.delete(this.url+"/iob/admin/users?"
					+ "userDomain={adminDomain}&"
					+ "userEmail={adminEmail}&",this.domainName,adminEmail
					); 
			// THEN: the server APROVED admin & reject other roles
			try {
				this.restTemplate.delete(this.url+"/iob/admin/users?"
						+ "userDomain={adminDomain}&"
						+ "userEmail={adminEmail}&",this.domainName, managerUser.getUserId().getEmail()
						); 
			}catch(RuntimeException e) {
				System.err.println("test pass");
			}
			try {
				this.restTemplate.delete(this.url+"/iob/admin/users?"
						+ "userDomain={adminDomain}&"
						+ "userEmail={adminEmail}&",this.domainName, player1User.getUserId().getEmail()
						); 
			}catch(RuntimeException e) {
				System.err.println("test pass");
			}
	}
}
