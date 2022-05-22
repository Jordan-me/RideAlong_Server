package iob.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import iob.boundries.CreatedBy;
import iob.boundries.InstanceBoundary;
import iob.boundries.InstanceId;
import iob.boundries.Location;
import iob.boundries.UserID;
import iob.data.InstanceEntity;
import iob.data.UserRole;

@Service
public class InstanceServiceJpa implements ExtendedInstancesService {
	private String domainName;
	private InstancesConverter instancesConverter;
	private InstanceCrud instanceCrud;
	private ExtendedUserService usersService;
	private MongoOperations mongoOperations;

	@Value("${spring.application.name}")
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	@Autowired
	public InstanceServiceJpa(InstanceCrud instanceCrud, InstancesConverter instancesConverter,
			ExtendedUserService usersService, MongoOperations mongoOperations) {
		this.instanceCrud = instanceCrud;
		this.instancesConverter = instancesConverter;
		this.usersService = usersService;
		this.mongoOperations = mongoOperations;
	}

	@Override
//	@LogThisMethod
	public InstanceBoundary createInstance(InstanceBoundary instance) {
		InstanceId id = new InstanceId(this.domainName, UUID.randomUUID().toString());
		instance.setInstanceId(id);
		if (instance.getType() == null || instance.getType().isEmpty()) {
			throw new RuntimeException("Type is not defined");
		}
		if (instance.getName() == null || instance.getName().isEmpty()) {
			throw new RuntimeException("Name is not defined");
		}
		if (instance.getActive() == null) {
			throw new RuntimeException("Active is not defined");
		}
		instance.setCreatedTimestamp(new Date());

		if (instance.getCreatedBy() == null || instance.getCreatedBy().getUserId().toString().isEmpty()) {
			throw new RuntimeException("createdBy field is not defined");
		}
		if (instance.getLocation() == null) {
			instance.setLocation(new Location(0.0, 0.0));
		}
		
		String userId = instance.getCreatedBy().getUserId().toString();
		// Get user data from DB and check if MANAGER
		this.usersService.checkUserPermission(userId, UserRole.MANAGER, true);

		InstanceEntity entity = this.instancesConverter.toEntity(instance);
		entity = this.instanceCrud.save(entity);
		return this.instancesConverter.toBoundary(entity);
	}

	@Override
	public InstanceBoundary updateInstance(UserID userId, String instanceDomain, String instanceId,
			InstanceBoundary update) throws InstanceNotFoundException {
		this.usersService.checkUserPermission(userId.toString(), UserRole.MANAGER, true);
		// Get instance data from DB
		InstanceBoundary boundary = this.getSpecificInstance(instanceDomain, instanceId);

		// Update instance details in DB
		if (update.getType() != null)
			boundary.setType(update.getType());

		if (update.getName() != null) {
			boundary.setName(update.getName());
		}
		if (update.getActive() != null) {
			boundary.setActive(update.getActive());
		}

		if (update.getLocation() != null) {
			boundary.setLocation(update.getLocation());
		}

		if (update.getInstanceAttributes() != null) {
			boundary.setInstanceAttributes(update.getInstanceAttributes());
		}
		// update DB
		InstanceEntity instanceEntity = this.instancesConverter.toEntity(boundary);
		instanceEntity = this.instanceCrud.save(instanceEntity);

		return this.instancesConverter.toBoundary(instanceEntity);
	}

	@Override
	public InstanceBoundary getSpecificInstance(String instanceDomain, String instanceId)
			throws InstanceNotFoundException {
		InstanceId instanceIdToCheck = new InstanceId(instanceDomain, instanceId);
		Optional<InstanceEntity> op = this.instanceCrud.findById(instanceIdToCheck.toString());

		if (op.isPresent()) {
			InstanceEntity entity = op.get();
			return instancesConverter.toBoundary(entity);
		} else {
			throw new InstanceNotFoundException("could not find instance by id: " + instanceId.toString());
		}

	}

	@Override
	public InstanceBoundary getSpecificInstance(String userDomain, String userEmail, String instanceDomain,
			String instanceId) throws InstanceNotFoundException {
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.ADMIN,
				false)) {
			throw new RuntimeException("Access denied");
		}
		InstanceBoundary boundary = this.getSpecificInstance(instanceDomain, instanceId);
		// Get user data from DB and check if MANAGER or PLAYER
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.PLAYER, false)
				&& !boundary.getActive()) {
			throw new InstanceNotFoundException("could not find instance by id: " + instanceId.toString());

		}
		return boundary;
	}

	@Override
	@Deprecated
	public List<InstanceBoundary> getAllInstances() {
//		return StreamSupport.stream(this.instanceCrud.findAll().spliterator(), false)
//				.map(this.instancesConverter::toBoundary)
//				.collect(Collectors.toList());
		throw new RuntimeException("getAllInstances is deprecated use with pagination instead.");
	}

	@Override
	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail, int size, int page) {
		// Get user data from DB and check if MANAGER or PLAYER
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.MANAGER,
				false)) {
			// manager access- return all instances, include not actives
			return this.instanceCrud
					.findAll(PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId")).stream() // Stream<instanceEntity>
					.map(this.instancesConverter::toBoundary) // Stream<instancetoBoundary>
					.collect(Collectors.toList()); // List<instancetoBoundary>
		}
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.PLAYER,
				false)) {
			// player access- return all instances, include not actives
			return this.instanceCrud
					.findAllByActive(true, PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"))
					.stream() // Stream<instanceEntity>
					.map(this.instancesConverter::toBoundary) // Stream<instancetoBoundary>
					.collect(Collectors.toList()); // List<instancetoBoundary>
		}
		throw new RuntimeException("Access denied");

	}

	@Override
	public List<InstanceBoundary> getInstancesByName(String userDomain, String userEmail, String name, int size,
			int page) {
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.ADMIN,
				false)) {
			throw new RuntimeException("Access denied");
		}
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.PLAYER,
				false)) {
			return this.instanceCrud
					.findAllByActiveAndName(true, name,
							PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"))
					.stream().map(this.instancesConverter::toBoundary) // Stream<instancetoBoundary>
					.collect(Collectors.toList()); // List<instancetoBoundary>;
		}
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.MANAGER,
				false)) {
			return this.instanceCrud
					.findAllByName(name, PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"))
					.stream().map(this.instancesConverter::toBoundary) // Stream<instancetoBoundary>
					.collect(Collectors.toList()); // List<instancetoBoundary>;

		}
		return null;
	}

	@Override
	public List<InstanceBoundary> getInstancesByType(String userDomain, String userEmail, String instanceType, int size,
			int page) {
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.ADMIN,
				false)) {
			throw new RuntimeException("Access denied");
		}
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.PLAYER,
				false)) {
			return this.instanceCrud
					.findAllByActiveAndType(true, instanceType,
							PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"))
					.stream().map(this.instancesConverter::toBoundary) // Stream<instancetoBoundary>
					.collect(Collectors.toList()); // List<instancetoBoundary>;
		}
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.MANAGER,
				false)) {
			return this.instanceCrud
					.findAllByType(instanceType,
							PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"))
					.stream().map(this.instancesConverter::toBoundary) // Stream<instancetoBoundary>
					.collect(Collectors.toList()); // List<instancetoBoundary>;

		}
		return null;

	}

	public Circle getCircle(Location location, double distance) {
		Point basePoint = new Point(location.getLat(), location.getLng());
		Distance radius = new Distance(distance, Metrics.KILOMETERS);
		Circle area = new Circle(basePoint, radius);
		return area;
	}

	@Override
	public List<InstanceBoundary> getInstancesByLocation(String userDomain, String userEmail, Location location,
			double distance, int size, int page) {
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.ADMIN,
				false)) {
			throw new RuntimeException("Access denied");
		}

//		Query query = new Query();
//		query.addCriteria(Criteria.where("location").within(getCircle(location, distance)));

		double[] loc = {location.getLng(),location.getLat()};
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.PLAYER,
				false)) {
//			query.addCriteria(Criteria.where("active").is(true));
			return this.instanceCrud
					.findAllNearAndActive(loc,distance,true,
							PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"))
					.stream().map(this.instancesConverter::toBoundary) // Stream<instancetoBoundary>
					.collect(Collectors.toList()); // List<instancetoBoundary>;
		}
//		return this.mongoOperations.find(query, InstanceEntity.class).stream().map(this.instancesConverter::toBoundary)
//				.collect(Collectors.toList());
		
		return this.instanceCrud
				.findAllNear(loc,distance,
						PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"))
				.stream().map(this.instancesConverter::toBoundary) // Stream<instancetoBoundary>
				.collect(Collectors.toList()); // List<instancetoBoundary>;
	}

	@Override
	public void deleteAllInstances() {
		throw new RuntimeException("getAllInstances is deprecated use with pagination instead.");

	}

	@Override
	public void deleteAllInstances(UserID userId, UserRole role) {
		this.usersService.checkUserPermission(userId.toString(), role, true);
		this.instanceCrud.deleteAll();
	}

	@Override
	public List<InstanceBoundary> getInstancesByTypeAndLocationAndNotCreatedBy(String userDomain, String userEmail,
			double [] location, double distance, String instanceType, CreatedBy creator, int size, int page) {
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.ADMIN,
				false)) {
			throw new RuntimeException("Access denied");
		}
//		Document queryFilter  = new Document("type", "perennial");
//		mongoCollection.findOne(queryFilter).getAsync(task -> {
//		    if (task.isSuccess()) {
//		        Plant result = task.get();
//		        Log.v("EXAMPLE", "successfully found a document: " + result);
//		    } else {
//		        Log.e("EXAMPLE", "failed to find document with: ", task.getError());
//		    }
//		});
//		Query query = new Query();
//		query.addCriteria(Criteria.where("location").within(getCircle(location, distance)));
//		query.addCriteria(Criteria.where("type").is(instanceType));
//		query.addCriteria(Criteria.where("createdBy").ne(creator));
		if (this.usersService.checkUserPermission(new UserID(userDomain, userEmail).toString(), UserRole.PLAYER,
				false)) {
//			query.addCriteria(Criteria.where("active").is(true));
			System.err.println("306. getInstancesByTypeAndLocationAndNotCreatedBy");
			return this.instanceCrud
					.findAllByLocationNearAndTypeAndNotCreatedByAndActive(location,distance,instanceType,
							creator,true,
							PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"))
					.stream().map(this.instancesConverter::toBoundary) // Stream<instancetoBoundary>
					.collect(Collectors.toList()); // List<instancetoBoundary>;
		}
		return this.instanceCrud
				.findAllByLocationNearAndTypeAndNotCreatedBy(location,distance,instanceType,
						creator,
						PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "instanceId"))
				.stream().map(this.instancesConverter::toBoundary) // Stream<instancetoBoundary>
				.collect(Collectors.toList()); // List<instancetoBoundary>;
//		return this.mongoOperations.find(query, InstanceEntity.class).stream().map(this.instancesConverter::toBoundary)
//				.collect(Collectors.toList());
	}

}
