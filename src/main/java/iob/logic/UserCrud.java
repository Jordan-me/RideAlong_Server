package iob.logic;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import iob.data.UserEntity;

interface UserCrud extends PagingAndSortingRepository<UserEntity, String>
{

}
