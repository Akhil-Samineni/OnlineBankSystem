package com.userfront.service.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.userfront.Dao.UserDao;
import com.userfront.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserSecurityService implements UserDetailsService{
	
    private static final Logger LOG = LoggerFactory.getLogger(UserSecurityService.class);

	
	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 User user = userDao.findByUsername(username);
		 
		 System.out.println("in user details");
	        if (null == user) {
	            LOG.warn("Username {} not found", username);
	            throw new UsernameNotFoundException("Username " + username + " not found");
	        }
	        return user;
	}

	@Override
	public String toString() {
		return "UserSecurityService [userDao=" + userDao + "]";
	}
	

}
