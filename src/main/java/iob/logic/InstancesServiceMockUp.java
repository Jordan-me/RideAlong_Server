package iob.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.boundries.InstanceBoundary;
import iob.boundries.InstanceId;
import iob.boundries.UserBoundary;
import iob.data.InstanceEntity;
import iob.data.UserEntity;
@Service 
public class InstancesServiceMockUp  implements InstancesService {
	private Map<InstanceId,InstanceEntity> instanceDataBaseMockup;
	private InstancesConverter converter;
	
	@Autowired
	public InstancesServiceMockUp(InstancesConverter converter) {
		super();
		this.converter = converter;
	}
	
	@Autowired
	public void setConverter(InstancesConverter converter) {
		this.converter = converter;
	}
	
	@PostConstruct
	public void init() {
		// thread safe Map
		this.instanceDataBaseMockup = Collections.synchronizedMap(new HashMap<>());
	}

	@Override
	public InstanceBoundary createInstance(InstanceBoundary instance) {
		
		// convert InstanceBoundary to InstanceEntity
		InstanceEntity entity = this.converter.toEntity(instance);
		
		// store entity to DB
		this.instanceDataBaseMockup.put(instance.getInstanceId(), entity);
		
		// Convert UserEntity to UserBoundary
		InstanceBoundary instanceBoundary = this.converter.toBoundary(entity);
		return instanceBoundary;
	}

	@Override
	public InstanceBoundary updateInstance(String instanceDomain, String instanceId, InstanceBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InstanceBoundary getSpecificInstance(String instanceDomain, String instanceId) {

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
