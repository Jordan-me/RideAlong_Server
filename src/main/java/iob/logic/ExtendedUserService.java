package iob.logic;

import java.util.List;

import iob.boundries.ActivityBoundary;
import iob.boundries.UserBoundary;
import iob.data.UserRole;

public interface ExtendedUserService extends UsersService{

	public boolean checkUserPermission(String userId, UserRole role, boolean throwable);

	public List<UserBoundary> getAllUsers(int size, int page);

}
