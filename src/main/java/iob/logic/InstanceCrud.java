package iob.logic;

import org.springframework.data.repository.CrudRepository;

import iob.data.InstanceEntity;



public interface InstanceCrud extends CrudRepository<InstanceEntity, String>{
	  
}
