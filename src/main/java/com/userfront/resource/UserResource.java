package com.userfront.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@RestController
@RequestMapping("/api")
public class UserResource {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping(value="/user/all", method=RequestMethod.GET)
	private List<User> userList(){
		return userService.userList();
	}
	@RequestMapping(value="/user/primary/transaction", method=RequestMethod.GET)
	private List<PrimaryTransaction> primaryTransactionsList(@RequestParam("username") String username){
		return transactionService.findPrimaryTransactionList(username);
	}
	
	@RequestMapping(value="/user/savings/transaction", method=RequestMethod.GET)
	private List<SavingsTransaction> savingsTransactionsList(@RequestParam("username") String username){
		return transactionService.findSavingsTransactionList(username);
	}
	
	@RequestMapping(value="/user/{username}/enable")
	public void enableUser(@PathVariable("username") String username) {
		userService.enableUser(username);
	}
	
	@RequestMapping(value="/user/{username}/disable")
	public void disableUser(@PathVariable("username") String username) {
		userService.disableUser(username);
	}
	
	
}
