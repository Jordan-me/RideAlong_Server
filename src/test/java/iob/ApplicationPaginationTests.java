package iob;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import iob.boundries.ActivityBoundary;
import iob.boundries.CreatedBy;
import iob.boundries.Instance;
import iob.boundries.InstanceBoundary;
import iob.boundries.Location;
import iob.boundries.NewUserBoundary;
import iob.boundries.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationPaginationTests {
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
	
//	@AfterEach
//	public void teardown() {
//		this.restTemplate
//			.delete(this.url);
//	}
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	@Test
	public void testContext() {
	}
	
	@Test
	@DisplayName("Test Pagination: export all users in the domain")
	public void testGetAllUsersUsingPagination() {
		// GIVEN the server is up
		//	    AND the database contains 5 users(admin, manager, 3 player)
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
			
			UserBoundary player2User = this.restTemplate
					.postForObject(this.url+"/iob/users", 
								new NewUserBoundary("player2@gmail.com", "Tor", "PLAYER", "TH"), 
								UserBoundary.class);
			addedUsers.add(player2User);
			
			UserBoundary player3User = this.restTemplate
					.postForObject(this.url+"/iob/users", 
								new NewUserBoundary("player3@gmail.com", "Noa", "PLAYER", "N"), 
								UserBoundary.class);
			addedUsers.add(player3User);
			
			// WHEN: the user requesting is an adim(role)
			//		& I get all users in page: 0 , of size: 4
			String adminEmail = "admin@gmail.com";
			int size = 4;
			int page = 0;
			UserBoundary[] results = 
					this.restTemplate.getForObject(this.url+"/iob/admin/users?"
													+ "userDomain={adminDomain}&"
													+ "userEmail={adminEmail}&"
													+ "size={size}&page={page}",
													UserBoundary[].class, this.domainName, adminEmail, size, page);
			// THEN: the server returns 4 users and not 5 [on indexies 0-3]
			assertThat(results).
			hasSize(size).
			usingRecursiveFieldByFieldElementComparator().
			isSubsetOf(addedUsers);

	}

	@Test
	@DisplayName("Test Pagination: export all activities in the domain")
	public void testGetAllActivitiesUsingPagination() {
		// GIVEN the server is up
		//	    AND the database contains 2 activities
		List<ActivityBoundary> addedActivities = new ArrayList<>();
		UserBoundary adminUser = this.restTemplate
				.postForObject(this.url+"/iob/users", 
								new NewUserBoundary("admin@gmail.com", "Mark", "ADMIN", "M"), 
								UserBoundary.class);
		UserBoundary player1User = this.restTemplate
				.postForObject(this.url+"/iob/users", 
								new NewUserBoundary("player1@gmail.com", "Tzuf", "PLAYER", "T"), 
								UserBoundary.class);
		
		UserBoundary managerUser = this.restTemplate
				.postForObject(this.url+"/iob/users", 
								new NewUserBoundary("manager@gmail.com", "Yarden", "MANAGER", "Y"), 
								UserBoundary.class);
		
		InstanceBoundary instance = this.restTemplate
				.postForObject(this.url+"/iob/instances", 
							new InstanceBoundary(null,"traveller","Yossi",
									true,null,new CreatedBy(managerUser.getUserId()),
									new Location(1.5, 2.0),null), 
							InstanceBoundary.class);
		
		ActivityBoundary activity1 =  this.restTemplate
				.postForObject(this.url+"/iob/activities", 
						new ActivityBoundary(null,"FindPartner",
								new Instance(instance.getInstanceId()),
								null,new CreatedBy(player1User.getUserId()),
								null), 
						ActivityBoundary.class);
		addedActivities.add(activity1);
		
		ActivityBoundary activity2 =  this.restTemplate
				.postForObject(this.url+"/iob/activities", 
						new ActivityBoundary(null,"FindPartner",
								new Instance(instance.getInstanceId()),
								null,new CreatedBy(player1User.getUserId()),
								null), 
						ActivityBoundary.class);
		addedActivities.add(activity2);
		// WHEN: the user requesting is an adim(role)
		//		& I get all users in page: 0 , of size: 1
		int size = 1;
		int page = 0;
		ActivityBoundary[] results = 
				this.restTemplate.getForObject(this.url+"/iob/admin/activities?"
												+ "userDomain={adminDomain}&"
												+ "userEmail={adminEmail}&"
												+ "size={size}&page={page}",
												ActivityBoundary[].class, this.domainName, adminUser.getUserId().getEmail(), 
												size, page);
		// THEN: the server returns 1 users and not 2 [on indexies 0-1]
		assertThat(results).
		hasSize(size).
		usingRecursiveFieldByFieldElementComparator().
		isSubsetOf(addedActivities);
	}

}
