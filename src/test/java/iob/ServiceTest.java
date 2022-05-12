package iob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import iob.boundries.ActivityBoundary;
import iob.boundries.CreatedBy;
import iob.boundries.Instance;
import iob.boundries.InstanceBoundary;
import iob.boundries.InstanceId;
import iob.boundries.Location;
import iob.boundries.NewUserBoundary;
import iob.boundries.UserBoundary;
import iob.boundries.UserID;
import iob.controllers.ActivitiesController;
import iob.controllers.InstancesController;
import iob.controllers.UserController;
import iob.data.ActivityEntity;
import iob.data.InstanceEntity;
import iob.data.UserEntity;
import iob.logic.ActivitiesConverter;
import iob.logic.InstanceNotFoundException;
import iob.logic.InstancesConverter;
import iob.logic.UsersConverter;

@Service
public class ServiceTest {

    @Autowired
    private UsersConverter userConverter;
    @Autowired
    private UserController userController;
    
    @Autowired
    private InstancesConverter instanceConverter;
    @Autowired
    private InstancesController instanceController;
    
    @Autowired
    private ActivitiesConverter activityConverter;
    @Autowired
    private ActivitiesController activityController;
    
    public UserEntity insertUser( MongoTemplate mongoTemplate, NewUserBoundary user) {
    	UserBoundary userB = this.userController.createNewUser(user);
    	UserEntity savedUser = this.userConverter.toEntity(userB);
    	mongoTemplate.save(savedUser, "Users");
    	return savedUser;
    }
    public InstanceEntity insertInstance( MongoTemplate mongoTemplate, InstanceBoundary instance) {
		instance = this.instanceController.createInstance(instance);
		InstanceEntity savedInstance = this.instanceConverter.toEntity(instance);
		mongoTemplate.save(savedInstance, "Instances");
		return savedInstance;
    }
    public ActivityEntity insertActivity(MongoTemplate mongoTemplate, ActivityBoundary activityBoundary) throws InstanceNotFoundException {
    	activityBoundary = this.activityController.createActivity(activityBoundary);
		ActivityEntity savedActivity= this.activityConverter.toEntity(activityBoundary);
		mongoTemplate.save(savedActivity, "Activities");
		return savedActivity;
    	
    }
    
    public InstanceBoundary[] getAllInstances( MongoTemplate mongoTemplate,String domain, String mail) {
		return this.instanceController.getAllInstances(domain,mail,100,0);

    }
	public InstanceId getInstanceId(InstanceEntity in) {
		InstanceBoundary instance = this.instanceConverter.toBoundary(in);
		return instance.getInstanceId();
	}
	public UserBoundary getUser(UserEntity uer) {
		return this.userConverter.toBoundary(uer);
	}
	public ActivityBoundary getActivity(ActivityEntity activity) {
		return this.activityConverter.toBoundary(activity);
	}
	public void initialize(MongoTemplate mongoTemplate,String domain) throws InstanceNotFoundException {
		this.insertUser(mongoTemplate, new NewUserBoundary(InstancesRepositoryTests.MANAGER_MAIL, "Manager", "MANAGER", "M"));
		this.insertUser(mongoTemplate, new NewUserBoundary(ActivitiesRepositoryTests.PLAYER_MAIL, "Player", "PLAYER", "P"));
		this.insertUser(mongoTemplate, new NewUserBoundary(DeleteTests.ADMIN_MAIL, "Admin", "ADMIN", "A"));
		InstanceBoundary instance = new InstanceBoundary(null, "User", "idOfUser", true,null, new CreatedBy(
    			new UserID(domain, InstancesRepositoryTests.MANAGER_MAIL)), new Location(10.50, 20.6), null);
		this.insertInstance(mongoTemplate, instance);
		InstanceBoundary instance2 = new InstanceBoundary(null, "User", "idOfUser", false,null, new CreatedBy(
    			new UserID(domain, InstancesRepositoryTests.MANAGER_MAIL)), new Location(10.50, 20.6), null);
		this.insertInstance(mongoTemplate, instance2);
		ActivityBoundary activity = new ActivityBoundary(null, "FindPartner",new Instance(instance.getInstanceId()), null,
     			new CreatedBy(new UserID(domain, ActivitiesRepositoryTests.PLAYER_MAIL)), null);
		this.insertActivity(mongoTemplate, activity);
	}
}
