package iob.logic;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import iob.boundries.ActivityBoundary;
import iob.boundries.ActivityId;
import iob.boundries.CreatedBy;
import iob.boundries.InstanceId;
import iob.boundries.UserID;
import iob.logic.ActivitiesService;

public class ActivitySrviceJpa implements ActivitiesService {
	private String domainName;
	
	
	@Value("${spring.application.name Default Message}")
	public void setdomainName(String domainName) {
		this.domainName = domainName;
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
		return null;
	}
	

	@Override
	public List<ActivityBoundary> getAllActivities() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void deleteAllActivities() {
		// TODO Auto-generated method stub
		
	}
}
