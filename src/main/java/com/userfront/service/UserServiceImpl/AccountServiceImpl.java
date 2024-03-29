package com.userfront.service.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.Dao.PrimaryAccountDao;
import com.userfront.Dao.SavingsAccountDao;
import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.AccountService;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Service
public class AccountServiceImpl implements AccountService{
	
	private static int nextAccountNumber = 11223145;

	@Autowired
	private PrimaryAccountDao primaryAccountDao;
	
	@Autowired
	private SavingsAccountDao savingsAccountDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Override
	public PrimaryAccount createPrimaryAccount() {
		PrimaryAccount primaryAccount=new PrimaryAccount();
		primaryAccount.setAccountBalance(new BigDecimal(0.0));
		primaryAccount.setAccountNumber(accountGen());
		primaryAccountDao.save(primaryAccount);
		// TODO Auto-generated method stub
		return primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
	}

	@Override
	public SavingsAccount createSavingsAccount() {
		SavingsAccount savingsAccount=new SavingsAccount();
		savingsAccount.setAccountNumber(accountGen());
		savingsAccount.setAccountBalance(new BigDecimal(0.0));
		savingsAccountDao.save(savingsAccount);
		// TODO Auto-generated method stub
		return savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
	}
	
	public int accountGen() {
		return ++nextAccountNumber;
	}

	@Override
	public void deposit(String accountType, double amount, Principal principal) {
		User user=userService.findByUsername(principal.getName());
		if(accountType.equalsIgnoreCase("Primary")) {
			PrimaryAccount primaryAccount=user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			Date date=new Date();
            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Deposit to Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryDepositTransaction(primaryTransaction);
		}else if(accountType.equalsIgnoreCase("Savings")) {
			SavingsAccount savingsAccount=user.getSavingsAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);
			Date date=new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposit to savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsDepositTransaction(savingsTransaction);
		}
		
	}
	
	@Override
	public void withdraw(String accountType, double amount, Principal principal) {
		User user=userService.findByUsername(principal.getName());
		if(accountType.equalsIgnoreCase("Primary")) {
			PrimaryAccount primaryAccount=user.getPrimaryAccount();
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Withdraw from Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryWithdrawTransaction(primaryTransaction);
            
	            
		}else if(accountType.equalsIgnoreCase("Savings")) {
			SavingsAccount savingsAccount=user.getSavingsAccount();
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);
			Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Withdraw from savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
            
		}
		
	}

}
