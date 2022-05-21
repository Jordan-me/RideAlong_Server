package iob.logic;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import iob.boundries.ActivityBoundary;
import iob.boundries.ActivityId;
import iob.boundries.CreatedBy;
import iob.boundries.InstanceBoundary;
import iob.boundries.Location;
import iob.boundries.UserID;
import iob.data.ActivityEntity;
import iob.data.InstanceEntity;
import iob.data.UserRole;

@Service
public class ActivitySrviceJpa implements ExtendedActivitiesService {
	private String domainName;
	private ActivitiesConverter activitiesConverter;
	private ActivityCrud activityCrud;
	private ExtendedUserService usersService;
	private InstanceCrud instancesCrud;
	private ExtendedInstancesService instancesService;
	private InstancesConverter instancesConverter;

	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	@Autowired
	public ActivitySrviceJpa(ActivityCrud activityCrud,
			ActivitiesConverter activitiesConverter, 
			UserCrud usersCrud,
			ExtendedUserService usersService,
			InstanceCrud instancesCrud, 
			ExtendedInstancesService instancesService,
			InstancesConverter instancesConverter) {
		this.activityCrud = activityCrud;
		this.activitiesConverter = activitiesConverter;
		this.usersService = usersService;
		this.instancesCrud = instancesCrud;
		this.instancesService = instancesService;
		this.instancesConverter = instancesConverter;
	}

	@Override
	public Object invokeActivity(ActivityBoundary activity) throws InstanceNotFoundException {
		System.err.println("insert invokeActivity");
		String userId = activity.getInvokedBy().getUserId().toString();
		System.err.println("getedInvokedBy");
		// Get user data from DB and check if PLAYER
		this.usersService.checkUserPermission(userId, UserRole.PLAYER, true);
		System.err.println("checkedUserPermission");
		// Get user inctance from DB and check if active
		String instanceId = activity.getInstance().getInstanceId().toString();
		System.err.println("gettedInstance");
		InstanceEntity instanceEntity = this.instancesCrud.findById(instanceId)
				.orElseThrow(() -> new RuntimeException("Could not find instance with id: " + instanceId));
		System.err.println("gettedInstanceEntity");

		if (instanceEntity.getActive() == false)
			throw new InstanceNotFoundException("Instance is not active");
		System.err.println("gettedInstancegetActive");
		// user is player and instance is active
		activity.setActivityId(new ActivityId(this.domainName, UUID.randomUUID().toString()));
		System.err.println("setActivityId");
		activity.setCreatedTimestamp(new Date());
		System.err.println("setCreatedTimestamp");
		if (activity.getType() == null || activity.getType().isEmpty()) {
			throw new RuntimeException("Type is not defined");
		}
		System.err.println("getedType");
		System.err.println("switch");
		switch (activity.getType()) {
		case "fetchSuggestedEvents":
			System.err.println("insert fetchSuggestedEvents case");
			double distance = 20.0;
			int size = 20;
			int page = 0;
			if (activity.getActivityAttributes() == null || activity.getActivityAttributes().isEmpty()) {
				throw new RuntimeException("insert required parameters is not defined");
			}
			if (activity.getActivityAttributes().containsKey("distance")) {
				distance = (double) activity.getActivityAttributes().get("distance");
			}
			if (activity.getActivityAttributes().containsKey("size")) {
				size = (int) activity.getActivityAttributes().get("size");
			}
			if (activity.getActivityAttributes().containsKey("page")) {
				page = (int) activity.getActivityAttributes().get("page");
			}
			if (!activity.getActivityAttributes().containsKey("instanceType")) {
				throw new RuntimeException(
						"cannot complete method " + "fetchSuggestedEvents - missing instanceType on parameters");
			}

			InstanceBoundary[] instances = this.instancesService.getInstancesByTypeAndLocationAndNotCreatedBy(
					this.domainName, activity.getInvokedBy().getUserId().getEmail(), instanceEntity.getLocation(),
					distance, (String) activity.getActivityAttributes().get("instanceType"),
					instanceEntity.getCreatedBy(), size, page).toArray(new InstanceBoundary[0]);
			return instances;
		default:

			ActivityEntity entity = activitiesConverter.toEntity(activity);
			this.activityCrud.save(entity);
			return this.activitiesConverter.toBoundary(entity);
		}

	}

	@Override
	public List<ActivityBoundary> getAllActivities() {
//		return StreamSupport.stream(this.activityCrud.findAll().spliterator(), false)
//				.map(this.activitiesConverter::toBoundary)
//				.collect(Collectors.toList());
		throw new RuntimeException("deprecated method - use getAllActivities with paginayion instead");
	}

	@Override
	public void deleteAllActivities() {
		throw new RuntimeException("deprecated method - use deleteAllActivities with permission check instead");

	}

	@Override
	public List<ActivityBoundary> getAllActivities(UserID userId, UserRole role, int size, int page) {
		this.usersService.checkUserPermission(userId.toString(), role, true);
		return this.activityCrud.findAll(PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "activityId"))
				.stream() // Stream<ActivityEntity>
				.map(this.activitiesConverter::toBoundary) // Stream<ActivityBoundary>
				.collect(Collectors.toList()); // List<ActivityBoundary>
	}

	@Override
	public void deleteAllActivities(UserID userId, UserRole role) {
		this.usersService.checkUserPermission(userId.toString(), role, true);
		this.activityCrud.deleteAll();

	}

}
