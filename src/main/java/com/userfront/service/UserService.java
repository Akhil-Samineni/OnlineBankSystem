package com.userfront.service;

import java.util.List;
import java.util.Set;

import com.userfront.domain.User;
import com.userfront.domain.security.UserRole;

public interface UserService {
	User findByUsername(String username);
	User findByEmail(String email);
	boolean checkUserExists(String username, String email);
	boolean checkUsernameExists(String username);
	boolean checkUserEmailExists(String email);
	void saveUser(User user);
	User createUser(User user, Set<UserRole> userRoles);
	User findById(Long userid);
	List<User> userList();
	void enableUser(String username);
	void disableUser(String username);
}
