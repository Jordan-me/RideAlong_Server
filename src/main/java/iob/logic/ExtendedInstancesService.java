package iob.logic;

import java.util.List;

import iob.boundries.CreatedBy;
import iob.boundries.InstanceBoundary;
import iob.boundries.Location;
import iob.boundries.UserID;
import iob.data.UserRole;

public interface ExtendedInstancesService extends InstancesService{

	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail, int size, int page);

	public InstanceBoundary getSpecificInstance(String userDomain, String userEmail, String instanceDomain,
			String instanceId) throws InstanceNotFoundException;

	public List<InstanceBoundary> getInstancesByName(String userDomain, String userEmail, String instanceName, int size,
			int page);

	public List<InstanceBoundary> getInstancesByLocation(String userDomain, String userEmail, Location location,
			double distance, int size, int page);

	public List<InstanceBoundary> getInstancesByType(String userDomain, String userEmail, String instanceType, int size,
			int page);
	
	public List<InstanceBoundary> getInstancesByTypeAndLocationAndNotCreatedBy(String userDomain, String userEmail,
			Location ds,double distance,
			String instanceType,
			CreatedBy creator,
			int size,
			int page);
	
	public void deleteAllInstances(UserID userId,UserRole role);

}
