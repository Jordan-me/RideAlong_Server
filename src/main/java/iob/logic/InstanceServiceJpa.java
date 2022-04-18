package iob.logic;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.boundries.InstanceBoundary;
import iob.boundries.InstanceId;
import iob.data.InstanceEntity;
import iob.data.UserEntity;
import iob.data.UserRole;
@Service
public class InstanceServiceJpa implements InstancesService{
	private String domainName;
	private InstancesConverter instancesConverter;
	private InstanceCrud instanceCrud;
	private UserCrud usersCrud;
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	@Autowired
	public InstanceServiceJpa(
			InstanceCrud instanceCrud,
			InstancesConverter instancesConverter,
			UserCrud usersCrud) {
		this.instanceCrud = instanceCrud;
		this.instancesConverter = instancesConverter;
		this.usersCrud = usersCrud;
	}

	@Override
	@Transactional
	public InstanceBoundary createInstance(InstanceBoundary instance) {
		InstanceId id = new InstanceId(this.domainName,UUID.randomUUID().toString());
		instance.setInstanceId(id);
		if(instance.getType() == null || instance.getType().isEmpty()) {
			throw new RuntimeException("Type is not defined");
		}
		if(instance.getName() == null || instance.getName().isEmpty()) {
			throw new RuntimeException("Name is not defined");
		}
		if(instance.getActive() == null) {
			throw new RuntimeException("Active is not defined");
		}
		instance.setCreatedTimestamp(new Date());
		
		String userId = instance.getCreatedBy().getUserId().toString();
		// Get user data from DB and check if MANAGER
		UserEntity userEntity = this.usersCrud.findById(userId)
				.orElseThrow(()->
				new UserNotFoundException("Could not find user " + userId));
		if (!userEntity.getRole().equals(UserRole.MANAGER))
			throw new RuntimeException("User's role is not a Manager");
		
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

		if (update.getCreatedBy() != null) 
			entity.setCreatedBy(update.getCreatedBy());

		if (update.getLocation() != null) 
			entity.setLocation(update.getLocation());
		
		if (update.getInstanceAttributes() != null) 
			entity.setInstanceAttributes(update.getInstanceAttributes());
		// update DB
		InstanceEntity instanceinEentity = this.instancesConverter.toEntity(entity);
		instanceinEentity = this.instanceCrud.save(instanceinEentity);

		return this.instancesConverter.toBoundary(instanceinEentity);
	}

	@Override
	@Transactional(readOnly = true)
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
	@Transactional(readOnly = true)
	public List<InstanceBoundary> getAllInstances() {
		return StreamSupport.stream(this.instanceCrud.findAll().spliterator(), false)
				.map(this.instancesConverter::toBoundary)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteAllInstances() {
		this.instanceCrud.deleteAll();

		
	}
	
	

}
