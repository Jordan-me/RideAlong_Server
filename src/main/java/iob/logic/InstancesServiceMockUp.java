package iob.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	private Map<String,InstanceEntity> instanceDataBaseMockup;
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
		this.instanceDataBaseMockup.put(instance.getInstanceId().toString(), entity);
		
		// Convert UserEntity to UserBoundary
		InstanceBoundary instanceBoundary = this.converter.toBoundary(entity);
		return instanceBoundary;
	}

	@Override
	public InstanceBoundary updateInstance(String instanceDomain, String instanceId, InstanceBoundary update) { 
		// Return user data from DB_MockUp
		InstanceEntity entity = this.converter.toEntity(this.getSpecificInstance(instanceDomain, instanceId));
		
		//update fields, if dirtyFlag- mean our instance changed
		boolean dirtyFlag = false;
		if (update.getActive() != null) {
			dirtyFlag = true;
			entity.setActive(update.getActive());			
		}
		if (update.getType() != null) {
			dirtyFlag = true;
			entity.setType(update.getType());			
		}

		if (update.getName() != null && !update.getName().isEmpty()) {
			dirtyFlag = true;
			entity.setName(update.getName());			
		}
		
		if (update.getLocation() != null) {
			dirtyFlag = true;
			entity.setLocation(update.getLocation().toString());
		}
		// store entity to DB if needed
		if (dirtyFlag)
			//The put method either updates the value or adds a new entry.
			this.instanceDataBaseMockup.put(entity.getInstanceId(), entity);

		InstanceBoundary boundary = this.converter.toBoundary(entity);
		return boundary;
	}

	@Override
	public InstanceBoundary getSpecificInstance(String instanceDomain, String instanceId) {

		InstanceId id = new InstanceId(instanceDomain, instanceId);
		InstanceEntity entity = this.instanceDataBaseMockup.get(id.toString());
		if(entity == null) {
			throw new RuntimeException("Instance "+ id.toString() + " could not found.");
		}
		InstanceBoundary boundary = this.converter.toBoundary(entity);
		return boundary;
	}

	@Override
	public List<InstanceBoundary> getAllInstances() {
		/*
		 * stream over database (map) Entries
		 * filter entires based on values (ActvityEntity) InvokedBy
		 * map the new entries to instanceBoundary
		 * Collect the data into a list
		 */
		List<InstanceBoundary> instancesBoundary = instanceDataBaseMockup.values().stream()
                .map(v -> converter.toBoundary(v))
                .collect(Collectors.toList());
        
		return instancesBoundary;
	}

	@Override
	public void deleteAllInstances() {
		this.instanceDataBaseMockup.clear();
		
	}

}
