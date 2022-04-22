package iob.logic;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.boundries.InstanceBoundary;
import iob.boundries.InstanceId;
import iob.boundries.Location;
import iob.boundries.UserID;
import iob.data.InstanceEntity;
import iob.data.UserEntity;
import iob.data.UserRole;
@Service
public class InstanceServiceJpa implements ExtendedInstancesService{
	private String domainName;
	private InstancesConverter instancesConverter;
	private InstanceCrud instanceCrud;
	private ExtendedUserService usersService;
	
	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	@Autowired
	public InstanceServiceJpa(
			InstanceCrud instanceCrud,
			InstancesConverter instancesConverter,
			ExtendedUserService usersService) {
		this.instanceCrud = instanceCrud;
		this.instancesConverter = instancesConverter;
		this.usersService = usersService;
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
		
		if(instance.getCreatedBy() == null || instance.getCreatedBy().getUserId().toString().isEmpty()) {
			throw new RuntimeException("createdBy field is not defined");
		}
		if(instance.getLocation() == null) {
			instance.setLocation(new Location(0.0,0.0));
		}
		String userId = instance.getCreatedBy().getUserId().toString();
		// Get user data from DB and check if MANAGER
		this.usersService.checkUserPermission(userId, UserRole.MANAGER,true);
		
		InstanceEntity entity = this.instancesConverter.toEntity(instance);
		entity = this.instanceCrud.save(entity);
		return this.instancesConverter.toBoundary(entity);
	}

	@Override
	@Transactional
	public InstanceBoundary updateInstance(String instanceDomain, String instanceId, InstanceBoundary update) {

		// Get instance data from DB
		InstanceBoundary boundary = this.getSpecificInstance(instanceDomain, instanceId);

		// Update instance details in DB
		if (update.getInstanceId() != null) 
			boundary.setInstanceId(update.getInstanceId());

		if (update.getType() != null) 
			boundary.setType(update.getType());

		if (update.getName() != null) 
			boundary.setName(update.getName());

		if (update.getActive() != null) 
			boundary.setActive(update.getActive());

		if (update.getCreatedBy() != null) 
			//do nothing
		if (update.getCreatedTimestamp() != null) 
			// do nothing
		if (update.getLocation() != null) 
			boundary.setLocation(update.getLocation());
		
		if (update.getInstanceAttributes() != null) 
			boundary.setInstanceAttributes(update.getInstanceAttributes());
		// update DB
		InstanceEntity instanceEntity = this.instancesConverter.toEntity(boundary);
		instanceEntity = this.instanceCrud.save(instanceEntity);

		return this.instancesConverter.toBoundary(instanceEntity);
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
			throw new InstanceNotFoundException("could not find instance by id: " + instanceId.toString());
		}

	}
	
	@Override
	public InstanceBoundary getSpecificInstance(String userDomain, String userEmail, String instanceDomain,
			String instanceId) {
		if(this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.ADMIN,false)) {
			throw new RuntimeException("Access denied");
		}
		InstanceBoundary boundary = this.getSpecificInstance(instanceDomain, instanceId);
		// Get user data from DB and check if MANAGER or PLAYER
		if(this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.PLAYER,false)
				&& !boundary.getActive()) {
			throw new InstanceNotFoundException("could not find instance by id: " + instanceId.toString());
			
		}
		return boundary;
	}

	@Override
	@Transactional(readOnly = true)
	@Deprecated
	public List<InstanceBoundary> getAllInstances() {
//		return StreamSupport.stream(this.instanceCrud.findAll().spliterator(), false)
//				.map(this.instancesConverter::toBoundary)
//				.collect(Collectors.toList());
		throw new RuntimeException("getAllInstances is deprecated.");
	}
	@Override
	@Transactional(readOnly = true)
	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail, int size, int page) {
		// Get user data from DB and check if MANAGER or PLAYER
		if(this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.MANAGER,false)) {
			//manager access- return all instances, include not actives
			return this.instanceCrud.findAll(PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"))
			.stream() // Stream<instanceEntity>
			.map(this.instancesConverter::toBoundary) // Stream<instancetoBoundary>
			.collect(Collectors.toList()); // List<instancetoBoundary>
		}
		if(this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.PLAYER,false)) {
			//player access- return all instances, include not actives
			return this.instanceCrud.findAllByActive(true, PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"))
			.stream() // Stream<instanceEntity>
			.map(this.instancesConverter::toBoundary) // Stream<instancetoBoundary>
			.collect(Collectors.toList()); // List<instancetoBoundary>
		}
		throw new RuntimeException("Access denied");
		
		
	}
	
	@Override
	public List<InstanceBoundary> getInstancesByName(String userDomain, String userEmail, String name, int size,
			int page) {
		if(this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.ADMIN,false)) {
			throw new RuntimeException("Access denied");
		}
		List<InstanceEntity> instancesList = this.instanceCrud.findAllByName(name, PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"));
		if(this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.PLAYER,false)) {
			return instancesList
					.stream()
					.filter(o -> o.getActive())
					.map(this.instancesConverter::toBoundary)
					.collect(Collectors.toList());
		}
		
		return instancesList.stream().map(this.instancesConverter::toBoundary)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<InstanceBoundary> getInstancesByType(String userDomain, String userEmail,
			String instanceType, int size,int page) {
		if(this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.ADMIN,false)) {
			throw new RuntimeException("Access denied");
		}
		List<InstanceEntity> instancesList = this.instanceCrud.findAllByType(instanceType, PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"));
		if(this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.PLAYER,false)) {
			return instancesList
					.stream()
					.filter(o -> o.getActive())
					.map(this.instancesConverter::toBoundary)
					.collect(Collectors.toList());
		}
		
		return instancesList.stream().map(this.instancesConverter::toBoundary)
				.collect(Collectors.toList());
	}
	
	@Override
	public List<InstanceBoundary> getInstancesByLocation(String userDomain, String userEmail, Location location,
			double distance, int size, int page) {
		if(this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.ADMIN,false)) {
			throw new RuntimeException("Access denied");
		}
		if(this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.PLAYER,false)) {
			 return this.instanceCrud
				.findAll(PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"))
				.stream().filter(o -> o.getActive()).
				filter(o -> (new Location(o.getLat(),o.getLng())).isInRange(location, distance))
				.map(this.instancesConverter::toBoundary)
				.collect(Collectors.toList());
		}
		 return this.instanceCrud
			.findAll(PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"))
			.stream()
			.filter(o -> (new Location(o.getLat(),o.getLng())).isInRange(location, distance))
			.map(this.instancesConverter::toBoundary)
			.collect(Collectors.toList());
	}
	@Override
	@Transactional
	public void deleteAllInstances() {
		this.instanceCrud.deleteAll();

		
	}





	

}
