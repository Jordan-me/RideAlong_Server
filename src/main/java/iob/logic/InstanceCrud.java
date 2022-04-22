package iob.logic;

import java.util.List;

import org.springframework.data.domain.Pageable;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import iob.data.InstanceEntity;

public interface InstanceCrud extends PagingAndSortingRepository<InstanceEntity, String>{
	public List<InstanceEntity> findAllByActive(
			@Param("active") boolean active, 
			Pageable pageable);
}
