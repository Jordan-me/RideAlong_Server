package iob.logic;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
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
public class ActivitySrviceJpa implements ExtendedActivitiesService {
	private String domainName;
	private ActivitiesConverter activitiesConverter;
	private ActivityCrud activityCrud;
	private ExtendedUserService usersService;
	private InstanceCrud instancesCrud;
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	@Autowired
	public ActivitySrviceJpa(
			ActivityCrud activityCrud,
			ActivitiesConverter activitiesConverter,
			UserCrud usersCrud,ExtendedUserService usersService,
			InstanceCrud instancesCrud) {
		this.activityCrud = activityCrud;
		this.activitiesConverter = activitiesConverter;
		this.usersService = usersService;
		this.instancesCrud = instancesCrud;
	}

	@Override
	@Transactional
	public Object invokeActivity(ActivityBoundary activity) throws InstanceNotFoundException {

		String userId = activity.getInvokedBy().getUserId().toString();
		// Get user data from DB and check if PLAYER
		this.usersService.checkUserPermission(userId, UserRole.PLAYER,true);
		
		// Get user inctance from DB and check if active
		String instanceId = activity.getInstance().getInstanceId().toString();
		InstanceEntity instanceEntity = this.instancesCrud.findById(instanceId)
				.orElseThrow(()->new RuntimeException("Could not find instance with id: " + instanceId));

		if (instanceEntity.getActive() == false)
			throw new InstanceNotFoundException("Instance is not active");
		
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
//	@Transactional(readOnly = true)
	public List<ActivityBoundary> getAllActivities() {
//		return StreamSupport.stream(this.activityCrud.findAll().spliterator(), false)
//				.map(this.activitiesConverter::toBoundary)
//				.collect(Collectors.toList());
		throw new RuntimeException("deprecated method - use getAllActivities with paginayion instead");
	}



	@Override
	@Transactional
	public void deleteAllActivities() {
		this.activityCrud.deleteAll();
		
	}
	@Override
	public List<ActivityBoundary> getAllActivities(int size, int page) {
		return this.activityCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "activityId"))
				.stream() // Stream<ActivityEntity>
				.map(this.activitiesConverter::toBoundary) // Stream<ActivityBoundary>
				.collect(Collectors.toList()); // List<ActivityBoundary>
	}

}
