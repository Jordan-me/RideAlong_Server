package iob.logic;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import iob.boundries.Location;
import iob.data.InstanceEntity;

public interface InstanceCrud extends MongoRepository<InstanceEntity, String>{
	public List<InstanceEntity> findAllByActive(
			@Param("active") boolean active, 
			Pageable pageable);
	
	public List<InstanceEntity> findAllByName(
			@Param("name") String name,
			Pageable pageable);

	public List<InstanceEntity> findAllByType(
			@Param("type") String instanceType,
			Pageable pageable);
	
	public List<InstanceEntity> findAllByActiveAndName(
			@Param("active") boolean active, 
			@Param("name") String name,
			Pageable pageable);

	public List<InstanceEntity> findAllByActiveAndType(
			@Param("active") boolean active, 
			@Param("type") String instanceType,
			PageRequest pageable);
	
	@Query("{location: { $near: ?0, $maxDistance: ?1}, active: ?2}")
	public List<InstanceEntity> findAllByNearAndActive(
			Location location, 
			double radius,
			boolean active,
			PageRequest pageable);
}
