package iob.logic;

import java.util.List;

import iob.boundries.InstanceBoundary;

public interface InstancesService {
	public InstanceBoundary createInstance(InstanceBoundary instance);
	public InstanceBoundary updateInstance(String instanceDomain,String instanceId, InstanceBoundary update) throws InstanceNotFoundException;
	public InstanceBoundary getSpecificInstance(String instanceDomain,String instanceId) throws InstanceNotFoundException;
	@Deprecated
	public List<InstanceBoundary> getAllInstances();
	public void deleteAllInstances();
}
