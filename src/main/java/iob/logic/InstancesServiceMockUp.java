package iob.logic;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.boundries.InstanceBoundary;
import iob.data.InstanceEntity;
@Service 
public class InstancesServiceMockUp  implements InstancesService {
	private Map<String,InstanceEntity> storage;
	@Override
	public InstanceBoundary createInstance(InstanceBoundary instance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstanceBoundary updateInstance(String instanceDomain, String instanceId, InstanceBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstanceBoundary getSpecificInstance(String instanceDomain, String instanceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InstanceBoundary> getAllInstances() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllInstances() {
		// TODO Auto-generated method stub
		
	}

}
