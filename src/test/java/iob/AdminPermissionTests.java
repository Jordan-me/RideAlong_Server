package iob;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.bson.Document;
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
import iob.boundries.UserBoundary;
import iob.controllers.AdminController;
/*Test Admin permission (get all users)*/
@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
public class AdminPermissionTests {
	private static final String CONNECTION_STRING = "mongodb+srv://mongo:Fgj0JwGGgd7Kubo1@integrativeproject-ride.fdmvs.mongodb.net/";
	private String domainName;
	public final static String ADMIN_MAIL = "admin@google.com";
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
    @Autowired
    private ServiceTest serviceTest;
    @Autowired
    private AdminController adminController;
    
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
    	//Given user & instances & activities db initialize
        this.mongoTemplate.getCollection("Users").deleteMany(new Document());
        serviceTest.initialize(this.mongoTemplate,this.domainName);
    }
    @AfterEach
    public void clean() {
        mongodExecutable.stop();
    }
    @Test
    @DisplayName("Given user in db & role = \"ADMIN\""
            + " When get all users using MongoDB template"
            + " Then the server return user's collection")    
    public void testAdminGetAllUsersHappyFlow() {
    	// GIVEN the server is up
    	//	    AND the database contains 3 users(admin, manager, player)
    	// WHEN: the user requesting is an adim(role)
    	//		& activtae admin controller functionallity 
    	UserBoundary[] results =		
					this.adminController.getAllUsers(this.domainName, ADMIN_MAIL, 50, 0);
    	// THEN: the server APROVED admin & return all users 
			assertThat(results.length).isEqualTo((int)this.mongoTemplate.getCollection("Users").countDocuments());
	}
    
    @Test
    @DisplayName("Given user in db & role = \"MANAGER\""
            + " When get all users using MongoDB template"
            + " Then the server throws permission error")    
    public void testAdminGetAllUsersManagerRole() {
    	// GIVEN the server is up
    	//	    AND the database contains 3 users(admin, manager, player)
    	// WHEN: the user requesting is an manager(role)
    	//		& activtae admin controller functionallity 		
    	// THEN: the server throws an error
		assertThrows(Exception.class,()->this.adminController.getAllUsers(domainName, InstancesRepositoryTests.MANAGER_MAIL, 50, 0));
	}
    @Test
    @DisplayName("Given user in db & role = \"PLAYER\""
            + " When get all users using MongoDB template"
            + " Then the server throws permission error")    
    public void testAdminGetAllUsersPlayerRole() {
    	// GIVEN the server is up
    	//	    AND the database contains 3 users(admin, manager, player)
    	// WHEN: the user requesting is an player(role)
    	//		& activtae admin controller functionallity 		
    	// THEN: the server throws an error 
		assertThrows(Exception.class,()->this.adminController.getAllUsers(domainName, ActivitiesRepositoryTests.PLAYER_MAIL, 50, 0));
	}
}
