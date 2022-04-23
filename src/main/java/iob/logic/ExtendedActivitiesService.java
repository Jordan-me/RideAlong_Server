package iob.logic;

import java.util.List;

import iob.boundries.ActivityBoundary;

public interface ExtendedActivitiesService extends ActivitiesService{

	public List<ActivityBoundary> getAllActivities(int size, int page);

}
