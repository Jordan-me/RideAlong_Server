package iob;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
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
public class UsersRestTests {
	private RestTemplate restTemplate;
	private String url;
	private int port;
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
	public void testContext() {
	}
	
//	@BeforeEach
//	@AfterEach
//	public void setup() {
//		this.restTemplate
//			.delete(this.url);
//	}

	
	@Test
	@DisplayName("User Test: create new user")
	public UserBoundary testCreateNewUser() throws Exception{
		UserBoundary existingUser = this.restTemplate
				.postForObject(this.url+"/iob/users", 
						new NewUserBoundary("admin@gmail.com", "Mark", "ADMIN", "M"), 
						UserBoundary.class);
		
			System.err.println(existingUser);
			return existingUser;
	}
 	
	@Test
	@DisplayName("User Test: Login and retrieve user details")
	public void testLogin() throws Exception{
		UserBoundary existingUser = testCreateNewUser();
		this.restTemplate
				.getForObject(this.url + "/iob/users/login/{userDomain}/{userEmail}",
						UserBoundary.class, existingUser.getUserId().getDomain(), existingUser.getUserId().getEmail());
		
 
	}
 	
	@Test
	@DisplayName("User Test: Update User Details")
	public void testUpdateUserDetails() throws Exception{
		UserBoundary existingUser = testCreateNewUser();
		UserBoundary update = new UserBoundary();
		
		update.setUsername("New Name");
		this.restTemplate
			.put(this.url + "/iob/users/{userDomain}/{userEmail}", update
					, existingUser.getUserId().getDomain(),
					existingUser.getUserId().getEmail());
		
		existingUser.setUsername(update.getUsername());
		
		
		assertThat(this.restTemplate
				.getForObject(this.url + "/iob/users/login/{userDomain}/{userEmail}",
						UserBoundary.class,
						existingUser.getUserId().getDomain(), existingUser.getUserId().getEmail()))
			.usingRecursiveComparison()
			.isEqualTo(existingUser);
				
	}


}

