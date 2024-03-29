package com.userfront.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.AccountService;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransactionService transactionService;
	@RequestMapping("/primaryAccount")
	public String primaryAccount(Principal principal, Model model) {
		User user=userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount=user.getPrimaryAccount();
		List<PrimaryTransaction> primaryTransactionList=transactionService.primaryTransactionsList(principal);
		model.addAttribute("primaryAccount",primaryAccount);
		model.addAttribute("primaryTransactionList",primaryTransactionList);
		return "primaryAccount";
	}
	
	@RequestMapping("/savingsAccount")
	public String savingsAccount(Principal principal, Model model) {
		User user=userService.findByUsername(principal.getName());
		SavingsAccount savingsAccount=user.getSavingsAccount();
		List<SavingsTransaction> savingsTransactionsList= transactionService.savingsTransactionsList(principal);
		model.addAttribute("savingsAccount",savingsAccount);
		model.addAttribute("savingsTransactionList",savingsTransactionsList);
		return "savingsAccount";
	}
	
	@RequestMapping(value="/deposit",method=RequestMethod.GET)
	public String deposit(Model model) {
		model.addAttribute("accountType","");
		model.addAttribute("amount","");
		return "deposit";
	}
	

	@RequestMapping(value="/deposit",method=RequestMethod.POST)
	public String depositPOST(@ModelAttribute("accountType") String accountType, @ModelAttribute("amount") Double amount, Principal principal) {
		accountService.deposit(accountType, amount, principal);
		return "redirect:/userFront";
	}
	
	@RequestMapping(value="/withdraw",method=RequestMethod.GET)
	public String withdraw(Model model) {
		model.addAttribute("accountType","");
		model.addAttribute("amount","");
		return "withdraw";
	}
	

	@RequestMapping(value="/withdraw",method=RequestMethod.POST)
	public String withdrawPOST(@ModelAttribute("accountType") String accountType, @ModelAttribute("amount") Double amount, Principal principal) {
		accountService.withdraw(accountType, amount, principal);
		return "redirect:/userFront";
	}
	
	
}
