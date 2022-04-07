package iob.boundries;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.logic.UsersService;

public class UsersServiceJpa implements UsersService{

	@Override
	public UserBoundary createUser(UserBoundary user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserBoundary> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllUsers() {
		// TODO Auto-generated method stub
		
	}

}
