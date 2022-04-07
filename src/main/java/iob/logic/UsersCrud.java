package iob.logic;

import org.springframework.data.repository.CrudRepository;

import iob.data.UserEntity;

 

public interface UserCrud extends CrudRepository<UserEntity, String>{ 
	
}
