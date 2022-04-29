package iob;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import iob.boundries.InstanceId;
import iob.boundries.Location;
import iob.boundries.NewUserBoundary;
import iob.boundries.UserID;
import iob.controllers.ActivitiesController;
import iob.data.InstanceEntity;
import iob.logic.ExtendedActivitiesService;
import iob.logic.InstanceNotFoundException;

/*Tests: PostActivity*/
@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
public class ActivitiesRepositoryTests {
	private static final String CONNECTION_STRING = "mongodb+srv://mongo:Fgj0JwGGgd7Kubo1@integrativeproject-ride.fdmvs.mongodb.net/";
	private String domainName;
	public final static String PLAYER_MAIL = "player@google.com";
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
    @Autowired
    private ServiceTest serviceTest;
    @Autowired
    private ExtendedActivitiesService activitiesService;
    @Autowired
    private ActivitiesController activitiesController;
    
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
    	//Given player user on db
        serviceTest.insertUser(this.mongoTemplate,new NewUserBoundary(PLAYER_MAIL, "PLAYER", "Player", "P"));
    }
    
    @AfterEach
    public void clean() {
        mongodExecutable.stop();
    }
    
    @Test
    @DisplayName("Given activity to save"
            + " when save activity using MongoDB template"
            + " then activity is saved")
    public void testPostActivityHappyFlow() throws InstanceNotFoundException {
    	//Given activity to save
    	serviceTest.insertUser(this.mongoTemplate,new NewUserBoundary(InstancesRepositoryTests.MANAGER_MAIL, "Manager", "Manager", "A"));
    	serviceTest.insertUser(this.mongoTemplate,new NewUserBoundary(ActivitiesRepositoryTests.PLAYER_MAIL, "PLAYER", "PLAYER", "P"));
    	InstanceBoundary instance = new InstanceBoundary(null, "User", "idOfUser", true,null, new CreatedBy(
    			new UserID(this.domainName, InstancesRepositoryTests.MANAGER_MAIL)), new Location(10.50, 20.6), null);
    	InstanceEntity in = serviceTest.insertInstance(this.mongoTemplate,instance);
    	
    	// When create an activity
    	ActivityBoundary activity = new ActivityBoundary(null, "FindPartner",new Instance(serviceTest.getInstanceId(in)), null,
     			new CreatedBy(new UserID(this.domainName, ActivitiesRepositoryTests.PLAYER_MAIL)), null);
    	// Then it saved successfully
    	testInsertActivity(serviceTest.getActivity(serviceTest.insertActivity(this.mongoTemplate, activity)),activity);
    }

	private void testInsertActivity(ActivityBoundary activityBoundary, ActivityBoundary activity) {
//        assertThat(activityBoundary.getActivityAttributes()).isEqualTo(activity.getActivityAttributes());
        assertThat(activityBoundary.getActivityId().toString()).isEqualTo(activity.getActivityId().toString());
        assertThat(activityBoundary.getInstance()).isEqualTo(activity.getInstance());
        assertThat(activityBoundary.getCreatedTimestamp()).isEqualTo(activity.getCreatedTimestamp());
        assertThat(activityBoundary.getInvokedBy()).isEqualTo(activity.getInvokedBy());
        assertThat(activityBoundary.getType()).isEqualTo(activity.getType());
	}
	
    @Test
    @DisplayName("Given activity to save"
            + " when the instance is exist"
            + " then throw an error")
    public void testPostActivityWithoutInstance() throws InstanceNotFoundException {
    	//Given activity to save
    	serviceTest.insertUser(this.mongoTemplate,new NewUserBoundary(ActivitiesRepositoryTests.PLAYER_MAIL, "PLAYER", "PLAYER", "P"));
    	// When create an activity
    	ActivityBoundary activity = new ActivityBoundary(null, "FindPartner",
    			new Instance(
    					new InstanceId(this.domainName,"NotExistInstance")), null,
     			new CreatedBy(new UserID(this.domainName, ActivitiesRepositoryTests.PLAYER_MAIL)), null);
    	// Then throw an error
    	assertThrows(Exception.class,()->serviceTest.insertActivity(this.mongoTemplate, activity));
    }
}
