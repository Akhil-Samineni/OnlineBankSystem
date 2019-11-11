package com.userfront.service.UserServiceImpl;

import static org.mockito.Mockito.doAnswer;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.Dao.PrimaryAccountDao;
import com.userfront.Dao.PrimaryTransactionDao;
import com.userfront.Dao.RecipientDao;
import com.userfront.Dao.SavingsAccountDao;
import com.userfront.Dao.SavingsTransactionDao;
import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.Recipient;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private UserService userService;
	
	@Autowired
	private PrimaryTransactionDao primaryTransactionDao;
	
	@Autowired
	private SavingsTransactionDao savingsTransactionDao;
	
	@Autowired
	private PrimaryAccountDao primaryAccountDao;
	@Autowired
	private SavingsAccountDao savingsAccountDao;
	
	@Autowired
	private RecipientDao recipientDao;
	
	@Override
	public List<PrimaryTransaction> primaryTransactionsList(Principal principal) {
		// TODO Auto-generated method stub
		User user=userService.findByUsername(principal.getName());
		PrimaryAccount primaryAccount=user.getPrimaryAccount();
		List<PrimaryTransaction> pList=primaryAccount.getPrimaryTransactionList();
		return pList;
	}

	@Override
	public List<SavingsTransaction> savingsTransactionsList(Principal principal) {
		// TODO Auto-generated method stub
		User user=userService.findByUsername(principal.getName());
		SavingsAccount savingsAccount=user.getSavingsAccount();
		List<SavingsTransaction> sList=savingsAccount.getSavingsTransactionList();
		return sList;
	}

	@Override
	public void savePrimaryDepositTransaction(PrimaryTransaction primaryTransaction) {
		// TODO Auto-generated method stub
		primaryTransactionDao.save(primaryTransaction);
		
	}

	@Override
	public void saveSavingsDepositTransaction(SavingsTransaction savingsTransaction) {
		// TODO Auto-generated method stub
		savingsTransactionDao.save(savingsTransaction);
	}

	@Override
	public void savePrimaryWithdrawTransaction(PrimaryTransaction primaryTransaction) {
		// TODO Auto-generated method stub
		primaryTransactionDao.save(primaryTransaction);
		
	}

	@Override
	public void saveSavingsWithdrawTransaction(SavingsTransaction savingsTransaction) {
		// TODO Auto-generated method stub
		savingsTransactionDao.save(savingsTransaction);
		
	}

	@Override
	public void betweenAccountsTransfer(String transferFrom, String transferTo, Double amount,
			PrimaryAccount primaryAccount, SavingsAccount savingsAccount) throws Exception {
		if(transferFrom.equalsIgnoreCase("Primary") && transferTo.equalsIgnoreCase("Savings")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
			primaryAccountDao.save(primaryAccount);
			
			Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Between account transfer from "+transferFrom+" to "+transferTo, "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            primaryTransactionDao.save(primaryTransaction);
	            
			
		}else if(transferFrom.equalsIgnoreCase("Savings") && transferTo.equalsIgnoreCase("Primary")) {
			primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
			savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
			savingsAccountDao.save(savingsAccount);
			Date date = new Date();

            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Between account transfer from "+transferFrom+" to "+transferTo, "Transfer", "Finished",amount, savingsAccount.getAccountBalance(), savingsAccount);
            savingsTransactionDao.save(savingsTransaction);
            
        }else {
        	throw new Exception("invalid transfer");
        }
		// TODO Auto-generated method stub
		
	}
	
	 public List<Recipient> findRecipientList(Principal principal) {
	        String username = principal.getName();
	        List<Recipient> recipientList = recipientDao.findAll().stream() 			//convert list to stream
	                .filter(recipient -> username.equals(recipient.getUser().getUsername()))	//filters the line, equals to username
	                .collect(Collectors.toList());

	        return recipientList;
	    }

	    public Recipient saveRecipient(Recipient recipient) {
	        return recipientDao.save(recipient);
	    }

	    public Recipient findRecipientByName(String recipientName) {
	        return recipientDao.findByName(recipientName);
	    }

	    public void deleteRecipientByName(String recipientName) {
	        recipientDao.deleteByName(recipientName);
	    }

	    public void toSomeoneElseTransfer(Recipient recipient, String accountType, String amount, PrimaryAccount primaryAccount, SavingsAccount savingsAccount) {
	        if (accountType.equalsIgnoreCase("Primary")) {
	            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
	            primaryAccountDao.save(primaryAccount);

	            Date date = new Date();

	            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Transfer to recipient "+recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), primaryAccount.getAccountBalance(), primaryAccount);
	            primaryTransactionDao.save(primaryTransaction);
	        } else if (accountType.equalsIgnoreCase("Savings")) {
	            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
	            savingsAccountDao.save(savingsAccount);

	            Date date = new Date();

	            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Transfer to recipient "+recipient.getName(), "Transfer", "Finished", Double.parseDouble(amount), savingsAccount.getAccountBalance(), savingsAccount);
	            savingsTransactionDao.save(savingsTransaction);
	        }
	    }

		@Override
		public List<PrimaryTransaction> findPrimaryTransactionList(String username) {
			// TODO Auto-generated method stub
			User user=userService.findByUsername(username);
			PrimaryAccount primaryAccount=user.getPrimaryAccount();
			List<PrimaryTransaction> primaryTransactions=primaryAccount.getPrimaryTransactionList();
			return primaryTransactions;
		}
		
		@Override
		public List<SavingsTransaction> findSavingsTransactionList(String username) {
			// TODO Auto-generated method stub
			User user=userService.findByUsername(username);
			SavingsAccount savingsAccount=user.getSavingsAccount();
			List<SavingsTransaction> savingsTransactions=savingsAccount.getSavingsTransactionList();
			return savingsTransactions;
		}

}
