package iob.logic;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import iob.data.InstanceEntity;

public interface InstanceCrud extends PagingAndSortingRepository<InstanceEntity, String>{

}
