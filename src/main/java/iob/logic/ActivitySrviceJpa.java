package iob.logic;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import iob.boundries.ActivityBoundary;
import iob.boundries.ActivityId;
import iob.boundries.CreatedBy;
import iob.boundries.InstanceId;
import iob.boundries.UserID;
import iob.data.ActivityEntity;
@Service
public class ActivitySrviceJpa implements ActivitiesService {
	private String domainName;
	private ActivitiesConverter activitiesConverter;
	private ActivityCrud activityCrud;
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	@Autowired
	public ActivitySrviceJpa(
			ActivityCrud activityCrud,
			ActivitiesConverter activitiesConverter) {
		this.activityCrud = activityCrud;
		this.activitiesConverter = activitiesConverter;
	}

	@Override
	public Object invokeActivity(ActivityBoundary activity) {
		activity.setActivityId(new ActivityId(this.domainName,UUID.randomUUID().toString()));
		activity.setCreatedTimestamp(new Date());
		if(activity.getCreatedBy()==null) {
			activity.setCreatedBy(new CreatedBy( new UserID(this.domainName,"test@gmail.com")));
		}
		if(activity.getType()==null) {
			activity.setType("NaN");
		}
		if(activity.getInstance()==null) {
			activity.setInstance(new InstanceId(this.domainName,UUID.randomUUID().toString()));
		}
		ActivityEntity entity = activitiesConverter.toEntity(activity);
		this.activityCrud.save(entity);
	
		return this.activitiesConverter.toBoundary(entity);
	}
	

	@Override
	public List<ActivityBoundary> getAllActivities() {
		//TODO: Validate Admin permissions
		return StreamSupport.stream(this.activityCrud.findAll().spliterator(), false)
				.map(this.activitiesConverter::toBoundary)
				.collect(Collectors.toList());
	}



	@Override
	public void deleteAllActivities() {
		//TODO: Validate Admin permissions
		this.activityCrud.deleteAll();
		
	}
}
