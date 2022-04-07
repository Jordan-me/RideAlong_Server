package iob.logic;

import java.util.List;

import iob.boundries.ActivityBoundary;

public interface ActivitiesService {
	
	Object invokeActivity(ActivityBoundary activity);
	
	List<ActivityBoundary> getAllActivities();
	
	void deleteAllActivities();
}
