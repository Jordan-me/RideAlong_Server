package iob.logic;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import iob.data.ActivityEntity;

public interface ActivityCrud extends MongoRepository<ActivityEntity, String>
{

}
