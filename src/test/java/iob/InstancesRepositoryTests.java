package iob;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
import iob.boundries.CreatedBy;
import iob.boundries.InstanceBoundary;
import iob.boundries.Location;
import iob.boundries.NewUserBoundary;
import iob.boundries.UserID;
import iob.controllers.InstancesController;
import iob.data.InstanceEntity;
import iob.logic.ExtendedInstancesService;
import iob.logic.InstanceNotFoundException;
/*Tests: PostInstance, PutInstance, SearchInstanceBy- Name,Type,Location*/
@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
public class InstancesRepositoryTests {
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
    private ExtendedInstancesService instancesService;
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
    	//Given manager user on db
        serviceTest.insertUser(this.mongoTemplate,new NewUserBoundary(MANAGER_MAIL, "Manager", "MANAGER", "M"));
    }
    @Test
    @DisplayName("Given instance to save"
            + " when save instance using MongoDB template"
            + " then Instance is saved")
    public void testCreateInstance() {
    	//Given instance to save
    	InstanceBoundary instance = new InstanceBoundary(null, "User", "idOfUser", true,null, new CreatedBy(
    			new UserID(this.domainName, MANAGER_MAIL)), new Location(10.50, 20.6), null);
    	// When create an instance
    	InstanceEntity savedInstance  = serviceTest.insertInstance(this.mongoTemplate,instance);
    	// Then it saved successfully
        assertThat(savedInstance.getInstanceId().toString()).isEqualTo(instance.getInstanceId().toString());
        assertThat(savedInstance.getActive()).isEqualTo(instance.getActive());
        assertThat(savedInstance.getCreatedBy()).isEqualTo(instance.getCreatedBy().toString());
        assertThat(savedInstance.getCreatedTimestamp()).isEqualTo(instance.getCreatedTimestamp());
        assertThat(savedInstance.getInstanceAttributes()).isEqualTo(instance.getInstanceAttributes());
        assertThat(savedInstance.getLocation()).isEqualTo(instance.getLocation());
//        assertThat(savedInstance.getLng()).isEqualTo(instance.getLocation().getLng());
        assertThat(savedInstance.getName()).isEqualTo(instance.getName());
        assertThat(savedInstance.getType()).isEqualTo(instance.getType());
    }
    @Test
    @DisplayName("Given Instance in db & type = \"User\""
            + " when search Instance using MongoDB template"
            + " then Instance is found")
    public void testFindInstanceByType() throws InstanceNotFoundException {
    	//Given Instance in db & type = "User"
    	int size = 10;
    	InstanceBoundary instance = new InstanceBoundary(null, "User", "idOfUser", true,null, new CreatedBy(
    			new UserID(this.domainName, MANAGER_MAIL)), new Location(10.50, 20.6), null);
    	serviceTest.insertInstance(this.mongoTemplate,instance);
    	//When find by type "User"
    	InstanceBoundary[] instances = this.instanceController.getAllInstancesByType(instance.getType(),
    			this.domainName,MANAGER_MAIL,size,0);
    	//then give all 10 (=size) instances with type "User" 
    	for(InstanceBoundary in: instances) {
            assertThat(in.getType()).isEqualTo(instance.getType());
    	}
		assertThat(instances).isNotNull().hasSizeGreaterThanOrEqualTo(1);
 
    }
    @Test
    @DisplayName("Given active & nonactive Instance in db & type = \"User\""
            + " when search Instance using MongoDB template with player permission"
            + " then only active Instance is found")
    public void testFindInstanceByTypeForPlayer() throws InstanceNotFoundException {
    	int size = 10;
    	
        serviceTest.insertUser(this.mongoTemplate,new NewUserBoundary(ActivitiesRepositoryTests.PLAYER_MAIL, "Player", "PLAYER", "P"));

    	InstanceBoundary instance = new InstanceBoundary(null, "TypeCheckForPlayer", "CheckForPlayer", true,null, new CreatedBy(
    			new UserID(this.domainName, MANAGER_MAIL)), new Location(8.50, 2.6), null);
    	InstanceBoundary instance2 = new InstanceBoundary(null, "TypeCheckForPlayer", "CheckForPlayer", false,null, new CreatedBy(
    			new UserID(this.domainName, MANAGER_MAIL)), new Location(8.50, 2.6), null);
    	serviceTest.insertInstance(this.mongoTemplate,instance);
    	serviceTest.insertInstance(this.mongoTemplate,instance2);
    	List<InstanceBoundary> instances = this.instancesService.getInstancesByType(this.domainName,ActivitiesRepositoryTests.PLAYER_MAIL,instance.getType(),size,0);
    	for(InstanceBoundary in: instances) {
    		System.out.println(in.toString());
            assertThat(in.getName()).isEqualTo(instance.getName());
    	}
    	assertThat(instances).isNotNull().hasSize(1);
 
    }
    @Test
    @DisplayName("Given Instance in db & Name = \"Eyal\""
            + " when search Instance using MongoDB template"
            + " then Instance is found")
    public void testFindInstanceByName() {
    	int size = 10;
    	InstanceBoundary instance = new InstanceBoundary(null, "User", "Eyal", true,null, new CreatedBy(
    			new UserID(this.domainName, MANAGER_MAIL)), new Location(8.50, 2.6), null);
    	serviceTest.insertInstance(this.mongoTemplate,instance);
    	List<InstanceBoundary> instances = this.instancesService.getInstancesByName(this.domainName,MANAGER_MAIL,instance.getName(),size,0);
    	for(InstanceBoundary in: instances) {
            assertThat(in.getName()).isEqualTo(instance.getName());
    	}
    	assertThat(instances).isNotNull().hasSizeGreaterThanOrEqualTo(1);
    }    
    @Test
    @DisplayName("Given Instance non active and active in db & Name = \"Eyal\""
            + " when search Instance with player permission using MongoDB template"
            + " retriev only active instance")
    public void testFindInstanceByNameForPlayer() {
    	int size = 10;
    	
        serviceTest.insertUser(this.mongoTemplate,new NewUserBoundary(ActivitiesRepositoryTests.PLAYER_MAIL, "Player", "PLAYER", "P"));

    	InstanceBoundary instance = new InstanceBoundary(null, "User", "CheckForPlayer", true,null, new CreatedBy(
    			new UserID(this.domainName, MANAGER_MAIL)), new Location(8.50, 2.6), null);
    	InstanceBoundary instance2 = new InstanceBoundary(null, "User", "CheckForPlayer", false,null, new CreatedBy(
    			new UserID(this.domainName, MANAGER_MAIL)), new Location(8.50, 2.6), null);
    	serviceTest.insertInstance(this.mongoTemplate,instance);
    	serviceTest.insertInstance(this.mongoTemplate,instance2);
    	List<InstanceBoundary> instances = this.instancesService.getInstancesByName(this.domainName,ActivitiesRepositoryTests.PLAYER_MAIL,instance.getName(),size,0);
    	for(InstanceBoundary in: instances) {
    		System.out.println(in.toString());
            assertThat(in.getName()).isEqualTo(instance.getName());
    	}
    	assertThat(instances).isNotNull().hasSize(1);
    }  
    @Test
    @DisplayName("Given Instance in db & Location = (lat,lng)"
            + " when search Instance using MongoDB template in radius "
            + " then Instances are found")
    public void testFindInstanceByLocation() {
    	int size = 10;
    	int radius = 5;
    	InstanceBoundary instance = new InstanceBoundary(null, "User", "idOfUser", true,null, new CreatedBy(
    			new UserID(this.domainName, MANAGER_MAIL)),
    			new Location(5.1,5.2), null);
    	serviceTest.insertInstance(this.mongoTemplate,instance);
    	List<InstanceBoundary> instances = this.instancesService.
    			getInstancesByLocation(
    			this.domainName,MANAGER_MAIL,instance.getLocation(),radius,size,0);

    	assertThat(instances).isNotNull().hasSizeGreaterThanOrEqualTo(1);
    }    
    @Test
    @DisplayName("Given Instance in db "
            + " when update Instance using MongoDB template  "
            + " then Instance is updated")
    public void testUpdateInstance() throws InstanceNotFoundException {
    	InstanceBoundary instance = new InstanceBoundary(null, "User", "idOfUser", true,null, new CreatedBy(
    			new UserID(this.domainName, MANAGER_MAIL)), new Location(5.1,5.2), null);
    	serviceTest.insertInstance(this.mongoTemplate,instance);
    	instance.setActive(false);
    	this.instanceController.updateInstance(instance.getInstanceId().getDomain(),
    			instance.getInstanceId().getId(), this.domainName, MANAGER_MAIL, instance);
    	InstanceBoundary update = this.instanceController.getInstance(instance.getInstanceId().getDomain(), 
    			instance.getInstanceId().getId(), this.domainName, MANAGER_MAIL);
        
    	assertThat(update.getInstanceId().toString()).isEqualTo(instance.getInstanceId().toString());
        assertThat(update.getActive()).isEqualTo(instance.getActive());
        assertThat(update.getCreatedBy().toString()).isEqualTo(instance.getCreatedBy().toString());
        assertThat(update.getCreatedTimestamp()).isEqualTo(instance.getCreatedTimestamp());
        assertThat(update.getInstanceAttributes()).isEqualTo(instance.getInstanceAttributes());
        assertThat(update.getLocation().toString()).isEqualTo(instance.getLocation().toString());
        assertThat(update.getName()).isEqualTo(instance.getName());
        assertThat(update.getType()).isEqualTo(instance.getType());
    }
    @AfterEach
    public void clean() {
        mongodExecutable.stop();
    }

}
 
 