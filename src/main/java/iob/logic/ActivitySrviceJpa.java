package iob.logic;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.boundries.ActivityBoundary;
import iob.boundries.ActivityId;
import iob.boundries.CreatedBy;
import iob.boundries.InstanceId;
import iob.boundries.UserID;
import iob.data.ActivityEntity;
import iob.data.InstanceEntity;
import iob.data.UserEntity;
import iob.data.UserRole;
@Service
public class ActivitySrviceJpa implements ActivitiesService {
	private String domainName;
	private ActivitiesConverter activitiesConverter;
	private ActivityCrud activityCrud;
	private UserCrud usersCrud;
	private InstanceCrud instancesCrud;
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	@Autowired
	public ActivitySrviceJpa(
			ActivityCrud activityCrud,
			ActivitiesConverter activitiesConverter,
			UserCrud usersCrud,
			InstanceCrud instancesCrud) {
		this.activityCrud = activityCrud;
		this.activitiesConverter = activitiesConverter;
		this.usersCrud = usersCrud;
		this.instancesCrud = instancesCrud;
	}

	@Override
	@Transactional
	public Object invokeActivity(ActivityBoundary activity) {

		String userId = activity.getInvokedBy().getUserId().toString();
		// Get user data from DB and check if PLAYER
		UserEntity userEntity = this.usersCrud.findById(userId)
				.orElseThrow(()->
				new UserNotFoundException("Could not find user " + userId));
		if (!userEntity.getRole().equals(UserRole.PLAYER))
			throw new RuntimeException("User's role is not a player");
		
		// Get user inctance from DB and check if active
		String instanceId = activity.getInstance().getInstanceId().toString();
		InstanceEntity instanceEntity = this.instancesCrud.findById(instanceId)
				.orElseThrow(()->new RuntimeException("Could not find instance with id: " + instanceId));

		if (instanceEntity.getActive() == false)
			throw new RuntimeException("Instance is not active");
		
		// user is player and instance is active
		activity.setActivityId(new ActivityId(this.domainName,UUID.randomUUID().toString()));
		activity.setCreatedTimestamp(new Date());
		
		if(activity.getType()==null || activity.getType().isEmpty()) {
			throw new RuntimeException("Type is not defined");
		}

		ActivityEntity entity = activitiesConverter.toEntity(activity);
		this.activityCrud.save(entity);
	
		return this.activitiesConverter.toBoundary(entity);
	}
	

	@Override
	@Transactional(readOnly = true)
	public List<ActivityBoundary> getAllActivities() {
		//TODO: Validate Admin permissions
		return StreamSupport.stream(this.activityCrud.findAll().spliterator(), false)
				.map(this.activitiesConverter::toBoundary)
				.collect(Collectors.toList());
	}



	@Override
	@Transactional
	public void deleteAllActivities() {
		//TODO: Validate Admin permissions
		this.activityCrud.deleteAll();
		
	}

}
