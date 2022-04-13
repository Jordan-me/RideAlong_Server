package iob.logic;

import org.springframework.data.repository.CrudRepository;

import iob.data.ActivityEntity;

public interface ActivityCrud extends CrudRepository<ActivityEntity, String>
  {

}
