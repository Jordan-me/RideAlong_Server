package iob.logic;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;

import iob.boundries.CreatedBy;
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

	@Query("{location: { $near: {$geometry : {type: 'point', coordinates :?0}, $maxDistance: ?1}}, active: ?2}")
	public List<InstanceEntity> findAllNearAndActive(
			double [] location, 
			double radius,
			boolean active,
			PageRequest pageable);
	
	@Query("{location: { $near: {$geometry : {type: 'point', coordinates :?0}, $maxDistance: ?1}}}")
	public List<InstanceEntity> findAllNear(
			double[]  location, 
			double radius,
			PageRequest pageable);

	@Query("{location: { $near: ?0, $maxDistance: ?1}, type: ?2, createdBy: {$ne: ?3},active: ?4}")
	public List<InstanceEntity> findAllByLocationNearAndTypeAndNotCreatedByAndActive(
			double[]  location,double radius,String instanceType,
			CreatedBy creator,boolean active,
			PageRequest pageable);
	
	@Query("{location: { $near: ?0, $maxDistance: ?1}, type: ?2, createdBy: {$ne:?3}}")
	public List<InstanceEntity> findAllByLocationNearAndTypeAndNotCreatedBy(
			double[] location,double radius,String instanceType,
			CreatedBy creator,
			PageRequest pageable);
	

}
