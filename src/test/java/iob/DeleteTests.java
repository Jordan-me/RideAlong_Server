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
import iob.boundries.ActivityBoundary;
import iob.boundries.CreatedBy;
import iob.boundries.Instance;
import iob.boundries.InstanceBoundary;
import iob.boundries.Location;
import iob.boundries.NewUserBoundary;
import iob.boundries.UserID;
import iob.controllers.AdminController;
import iob.data.InstanceEntity;
import iob.logic.InstanceNotFoundException;

@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
public class DeleteTests {
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
    	//Given admin user on db
        serviceTest.insertUser(this.mongoTemplate,new NewUserBoundary(ADMIN_MAIL, "Admin", "Admin", "A"));
    }
    @AfterEach
    public void clean() {
        mongodExecutable.stop();
        this.mongoTemplate.getCollection("Instances").deleteMany(new Document());
        this.mongoTemplate.getCollection("Activities").deleteMany(new Document());
        this.mongoTemplate.getCollection("Users").deleteMany(new Document());
    }
    
    @Test
    @DisplayName("Given user in db & role = \"ADMIN\""
            + " When delete Instance's collection using MongoDB template"
            + " Then Instance's collection is deleted")
    public void testDeleteInstancesCollection() {
    	//Given user in db & role = "ADMIN" & instance
    	serviceTest.insertUser(this.mongoTemplate,new NewUserBoundary(InstancesRepositoryTests.MANAGER_MAIL, "Manager", "Manager", "A"));
    	InstanceBoundary instance = new InstanceBoundary(null, "User", "idOfUser", true,null, new CreatedBy(
    			new UserID(this.domainName, InstancesRepositoryTests.MANAGER_MAIL)), new Location(10.50, 20.6), null);
    	serviceTest.insertInstance(this.mongoTemplate,instance,this.domainName, InstancesRepositoryTests.MANAGER_MAIL);
    	// When delete Instance's collection using MongoDB template
    	this.adminController.deleteAllInstances(this.domainName, DeleteTests.ADMIN_MAIL);
    	//Then Instance's collection is deleted
    	InstanceBoundary[] instances = this.serviceTest.getAllInstances(this.mongoTemplate,
    			this.domainName, InstancesRepositoryTests.MANAGER_MAIL);
    	assertThat(instances).isEmpty();
 
    }
    
    @Test
    @DisplayName("Given user in db & role = \"ADMIN\""
            + " When delete Activities collection using MongoDB template"
            + " Then Activities collection is deleted")
    public void testDeleteActivitiesCollection() throws InstanceNotFoundException {
    	//Given user in db & role = "ADMIN" & activity
    	
    	serviceTest.insertUser(this.mongoTemplate,new NewUserBoundary(ActivitiesRepositoryTests.PLAYER_MAIL, "PLAYER", "PLAYER", "P"));
    	InstanceBoundary instance = new InstanceBoundary(null, "User", "idOfUser", true,null, new CreatedBy(
    			new UserID(this.domainName, InstancesRepositoryTests.MANAGER_MAIL)), new Location(10.50, 20.6), null);
    	InstanceEntity in = serviceTest.insertInstance(this.mongoTemplate,instance,this.domainName, InstancesRepositoryTests.MANAGER_MAIL);
    	
    	ActivityBoundary activity = new ActivityBoundary(null, "FindPartner",new Instance(serviceTest.getInstanceId(in)), null,
     			new CreatedBy(new UserID(this.domainName, ActivitiesRepositoryTests.PLAYER_MAIL)), null);
    	serviceTest.insertActivity(this.mongoTemplate, activity);
    	
    	// When delete Activities collection using MongoDB template
    	this.adminController.deleteAllActivities(this.domainName, DeleteTests.ADMIN_MAIL);
    	//Then Instance's collection is deleted
    	ActivityBoundary[] activities = this.adminController.getAllActivities(this.domainName, DeleteTests.ADMIN_MAIL,10,0);
    	assertThat(activities).isEmpty();
 
    }
    @Test
    @DisplayName("Given user in db & role = \"ADMIN\""
            + " When delete users collection using MongoDB template"
            + " Then users collection is deleted")
    public void testDeleteUsersCollection() {
    	//Given user in db & role = "ADMIN" & user
    	
    	serviceTest.insertUser(this.mongoTemplate,new NewUserBoundary(ActivitiesRepositoryTests.PLAYER_MAIL, "PLAYER", "PLAYER", "P"));
 	
    	// When delete users collection using MongoDB template
    	this.adminController.deleteAllUsers(domainName, ADMIN_MAIL);
    	//Then Instance's collection is deleted
    	assertThrows(Exception.class,()->this.adminController.getAllUsers(this.domainName, DeleteTests.ADMIN_MAIL,10,0));
 
    }
}
