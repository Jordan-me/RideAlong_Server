package iob.logic;

import org.springframework.data.repository.CrudRepository;

import iob.data.UserEntity;

interface UserCrud extends CrudRepository<UserEntity, String>
{

}
