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
import iob.boundries.InstanceBoundary;
import iob.controllers.InstancesController;

/*Test Admin permission (get all users)*/
@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
public class ManagerPermissionTests {
	private static final String CONNECTION_STRING = "mongodb+srv://mongo:Fgj0JwGGgd7Kubo1@integrativeproject-ride.fdmvs.mongodb.net/";
	private String domainName;
	public final static String MANAGER_MAIL = "manager@google.com";
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
    @Autowired
    private ServiceTest serviceTest;
    @Autowired
    private InstancesController instanceController;
    
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
        this.mongoTemplate.getCollection("Instances").deleteMany(new Document());
        this.serviceTest.initialize(mongoTemplate, this.domainName);
    }
    
    @AfterEach
    public void clean() {
        mongodExecutable.stop();
    }
    @Test
    @DisplayName("Given user in db & role = \"Manager\""
            + " When get all instances using MongoDB template"
            + " Then the server return instance's collection (active & non-active")    
    public void testManagerGetAllInstancesHappyFlow() {
    	// GIVEN the server is up
    	//	    AND the database contains 3 users(admin, manager, player)
    	// WHEN: the user requesting is an manager(role)
    	//		& activtae manager controller functionallity 
    	InstanceBoundary[] results =		
					this.instanceController.getAllInstances(this.domainName, InstancesRepositoryTests.MANAGER_MAIL, 50, 0);
    	// THEN: the server APROVED manager & return all instances 
    	assertThat(results.length).isEqualTo((int)this.mongoTemplate.getCollection("Instances").countDocuments());
	}
    @Test
    @DisplayName("Given user in db & role = \"Admin\""
            + " When get all instances using MongoDB template"
            + " Then the server throws an error")    
    public void testManagerGetAllInstancesAdminRole() {
    	// GIVEN the server is up
    	//	    AND the database contains 3 users(admin, manager, player)
    	// WHEN: the user requesting is an admin(role)
    	//		& activtae manager controller functionallity 
    	// THEN: the server throws an error 
    	assertThrows(Exception.class, ()->this.instanceController.getAllInstances(this.domainName, DeleteTests.ADMIN_MAIL, 50, 0));
	}
    
    @Test
    @DisplayName("Given user in db & role = \"Player\""
            + " When get all instances using MongoDB template"
            + " Then the server return all activate instances")    
    public void testManagerGetAllInstancesPlayerRole() {
    	// GIVEN the server is up
    	//	    AND the database contains 3 users(admin, manager, player)
    	// WHEN: the user requesting is an player(role)
    	//		& activtae manager controller functionallity 
    	InstanceBoundary[] results =		
				this.instanceController.getAllInstances(this.domainName, ActivitiesRepositoryTests.PLAYER_MAIL, 50, 0);
    	// THEN: the server return all activate instances 
    	for(InstanceBoundary i: results) {
    		assertThat(i.getActive()).isEqualTo(true);
    	}
	}
}
