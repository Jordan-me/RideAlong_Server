package iob.logic;

import java.util.List;

import iob.boundries.ActivityBoundary;
import iob.boundries.UserID;
import iob.data.UserRole;

public interface ExtendedActivitiesService extends ActivitiesService{

	public List<ActivityBoundary> getAllActivities(UserID userId,UserRole admin, int size, int page);
	public void deleteAllActivities(UserID userId ,UserRole role);
}
