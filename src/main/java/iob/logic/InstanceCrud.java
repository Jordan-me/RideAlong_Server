package iob.logic;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

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
}
