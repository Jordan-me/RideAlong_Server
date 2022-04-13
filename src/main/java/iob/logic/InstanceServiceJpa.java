package iob.logic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import iob.boundries.InstanceBoundary;
import iob.boundries.InstanceId;
import iob.data.InstanceEntity;
import iob.data.UserEntity;
@Service
public class InstanceServiceJpa implements InstancesService{
	private String domainName;
	private InstancesConverter instancesConverter;
	private InstanceCrud instanceCrud;
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	@Autowired
	public InstanceServiceJpa(
			InstanceCrud instanceCrud,
			InstancesConverter instancesConverter) {
		this.instanceCrud = instanceCrud;
		this.instancesConverter = instancesConverter;
	}

	@Override
	@Transactional
	public InstanceBoundary createInstance(InstanceBoundary instance) {
		InstanceId id = new InstanceId(this.domainName,UUID.randomUUID().toString());
		instance.setInstanceId(id);
		InstanceEntity entity = this.instancesConverter.toEntity(instance);
		entity = this.instanceCrud.save(entity);
		return this.instancesConverter.toBoundary(entity);
	}

	@Override
	@Transactional
	public InstanceBoundary updateInstance(String instanceDomain, String instanceId, InstanceBoundary update) {

		// Get instance data from DB
		InstanceBoundary entity = this.getSpecificInstance(instanceDomain, instanceId);

		// Update instance details in DB
		if (update.getInstanceId() != null) 
			entity.setInstanceId(update.getInstanceId());

		if (update.getType() != null) 
			entity.setType(update.getType());

		if (update.getName() != null) 
			entity.setName(update.getName());

		if (update.getActive() != null) 
			entity.setActive(update.getActive());

		if (update.getCreatedTimestamp() != null) 
			entity.setCreatedTimestamp(update.getCreatedTimestamp());

		if (update.getCreatedBy() != null) 
			entity.setCreatedBy(update.getCreatedBy());

		if (update.getLocation() != null) 
			entity.setLocation(update.getLocation());

		// update DB
		InstanceEntity instanceinEentity = this.instancesConverter.toEntity(entity);
		instanceinEentity = this.instanceCrud.save(instanceinEentity);

		return this.instancesConverter.toBoundary(instanceinEentity);
	}

	@Override
	public InstanceBoundary getSpecificInstance(String instanceDomain, String instanceId) {
		InstanceId instanceIdToCheck = new InstanceId(instanceDomain, instanceId);
		Optional<InstanceEntity> op = this.instanceCrud
				.findById(instanceIdToCheck.toString());
			
		if (op.isPresent()) {
			InstanceEntity entity = op.get();
			return instancesConverter.toBoundary(entity);
		}else {
			throw new UserNotFoundException("could not find instance by id: " + instanceId.toString());
		}
	}

	@Override
	public List<InstanceBoundary> getAllInstances() {
		return StreamSupport.stream(this.instanceCrud.findAll().spliterator(), false)
				.map(this.instancesConverter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteAllInstances() {
		this.instanceCrud.deleteAll();

		
	}
	
	

}
