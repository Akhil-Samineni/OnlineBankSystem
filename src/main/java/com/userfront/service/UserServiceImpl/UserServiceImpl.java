package com.userfront.service.UserServiceImpl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.userfront.Dao.RoleDao;
import com.userfront.Dao.UserDao;
import com.userfront.domain.User;
import com.userfront.domain.security.UserRole;
import com.userfront.service.AccountService;
import com.userfront.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService{

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return userDao.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return userDao.findByEmail(email);
	}

	@Override
	public boolean checkUserExists(String username, String email) {
		// TODO Auto-generated method stub
		if(checkUsernameExists(username) || checkUserEmailExists(email)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkUsernameExists(String username) {
		// TODO Auto-generated method stub
		if(null != findByUsername(username)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkUserEmailExists(String email) {
		// TODO Auto-generated method stub
		if(null != findByEmail(email)) {
			return true;
		}
		return false;
	}

	@Override
	public void saveUser(User user) {
		// TODO Auto-generated method stub
		userDao.save(user);
	}

	@Override
	public User createUser(User user, Set<UserRole> userRoles) {
		User localuser=userDao.findByUsername(user.getUsername());
		if(localuser !=null) {
			LOG.info("User with username {} already exist. Nothing will be done. ", user.getUsername());
		}else {
			String encryptedPassword= passwordEncoder.encode(user.getPassword());
			user.setPassword(encryptedPassword);
			
			
            user.getUserRoles().addAll(userRoles);
            
			user.setPrimaryAccount(accountService.createPrimaryAccount());
			user.setSavingsAccount(accountService.createSavingsAccount());
			
			System.out.print(user.toString());
			localuser= userDao.save(user);
		}
		// TODO Auto-generated method stub
		return localuser;
	}

	@Override
	public User findById(Long userid) {
		// TODO Auto-generated method stub
		return userDao.findById(userid).orElse(null);
	}

	@Override
	public List<User> userList() {
		// TODO Auto-generated method stub
		return userDao.findAll();
	}

	@Override
	public void enableUser(String username) {
		// TODO Auto-generated method stub
		User user=userDao.findByUsername(username);
		user.setEnabled(true);
		userDao.save(user);
	}
	
	@Override
	public void disableUser(String username) {
		// TODO Auto-generated method stub
		User user=userDao.findByUsername(username);
		user.setEnabled(false);
		userDao.save(user);
	}

}
