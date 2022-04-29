package iob;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

import com.mongodb.client.MongoClients;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import iob.boundries.NewUserBoundary;
import iob.boundries.UserBoundary;
import iob.controllers.UserController;
import iob.data.UserEntity;

/*Tests: PostUser, PutUser, Login*/
@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
public class UsersRepositoryTests {
	private static final String CONNECTION_STRING = "mongodb+srv://mongo:Fgj0JwGGgd7Kubo1@integrativeproject-ride.fdmvs.mongodb.net/";
	private String domainName;
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	@Autowired
	private ServiceTest serviceTest;
	@Autowired
	private UserController userController;
	
	private MongodExecutable mongodExecutable;
	private MongoTemplate mongoTemplate;
	
    @BeforeEach
    public void setup() throws Exception {
        String ip = "localhost";
        int port = 27017;

        ImmutableMongodConfig mongodConfig = MongodConfig
            .builder()
            .version(Version.Main.PRODUCTION)
            .net(new Net(ip, port, Network.localhostIsIPv6()))
            .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        mongoTemplate = new MongoTemplate(MongoClients.create(CONNECTION_STRING), "embeddedDB");
    }
	
    @AfterEach
    public void clean() {
        mongodExecutable.stop();
    }
    
    @Test
    @DisplayName("Given user to save"
            + " when save user using MongoDB template"
            + " then user is saved")
    public void testCreateUser() {
    	//Given user to save
    	NewUserBoundary adminUser = new NewUserBoundary(DeleteTests.ADMIN_MAIL, "Admin", "Admin", "A");
    	NewUserBoundary managerUser = new NewUserBoundary(InstancesRepositoryTests.MANAGER_MAIL, "MANAGER", "MANAGER", "M");
    	NewUserBoundary playerUser = new NewUserBoundary(ActivitiesRepositoryTests.PLAYER_MAIL, "Player", "Player", "P");
    	// When create an instance
    	// Then it saved successfully
    	testCreateUser(serviceTest.insertUser(this.mongoTemplate,adminUser),adminUser);
    	testCreateUser(serviceTest.insertUser(this.mongoTemplate,managerUser),managerUser);
    	testCreateUser(serviceTest.insertUser(this.mongoTemplate,playerUser),playerUser);

    }

	public void testCreateUser(UserEntity savedUser, NewUserBoundary user) {
        assertThat(savedUser.getAvatar()).isEqualTo(user.getAvatar());
        assertThat(savedUser.getRole().toString().toUpperCase()).isEqualTo(user.getRole().toString().toUpperCase());
        assertThat(savedUser.getUsername()).isEqualTo(user.getUsername());
	}
	
	@Test
	@DisplayName("Given user on db & his mail"
			+ "when try to login using MongoDB template"
			+ "then user is logged in")
	public void testLogin(){
		//Given user on db & his mail
    	NewUserBoundary playerUser = new NewUserBoundary("demoUser@gmail.com", "demo-user", "Player", "DU");
    	serviceTest.insertUser(this.mongoTemplate,playerUser);
    	//when try to login using MongoDB template
    	UserBoundary loggedinUser = this.userController.login(this.domainName, playerUser.getEmail());
    	//then user is logged in
        assertThat(loggedinUser.getAvatar()).isEqualTo(playerUser.getAvatar());
        assertThat(loggedinUser.getRole().toString().toUpperCase()).isEqualTo(playerUser.getRole().toString().toUpperCase());
        assertThat(loggedinUser.getUsername()).isEqualTo(playerUser.getUsername());
	}
    @Test
    @DisplayName("Given User in db "
            + " when update User using MongoDB template  "
            + " then User is updated")
    public void testUpdateUser() {
		//Given User in db
    	NewUserBoundary playerUser = new NewUserBoundary("demoUser@gmail.com", "demo-user", "Player", "DU");
    	UserEntity userEntity = serviceTest.insertUser(this.mongoTemplate,playerUser);
    	UserBoundary userBoundary =  serviceTest.getUser(userEntity);
    	//when update User using MongoDB template 
    	userBoundary.setUsername("demo-user-update");
    	this.userController.updateUser(this.domainName, 
    			playerUser.getEmail(),userBoundary);
    	UserBoundary update = this.userController.login(this.domainName, playerUser.getEmail());
//    	then User is updated
    	assertThat(update.getAvatar()).isEqualTo(userBoundary.getAvatar());
        assertThat(update.getRole().toString().toUpperCase()).isEqualTo(userBoundary.getRole().toUpperCase());
        assertThat(update.getUsername()).isEqualTo(userBoundary.getUsername());
        assertThat(update.getUserId().getEmail()).isEqualTo(userBoundary.getUserId().getEmail());
    }

    
    
    
}
