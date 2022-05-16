package iob.logic;

import java.util.List;

import iob.boundries.ActivityBoundary;

public interface ActivitiesService {
	
	public Object invokeActivity(ActivityBoundary activity) throws InstanceNotFoundException;
	
	@Deprecated
	public List<ActivityBoundary> getAllActivities();
	@Deprecated
	public void deleteAllActivities();
}
