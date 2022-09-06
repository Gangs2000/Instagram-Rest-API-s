package org.instagram.layers.Services;

import org.instagram.layers.Exception.GlobalExceptions;
import org.instagram.layers.Interfaces.AccountInterface;
import org.instagram.layers.Models.Accounts;
import org.instagram.layers.Repositories.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements AccountInterface {

    @Autowired AccountsRepository repository;
    @Autowired
    Accounts account;

    @Override
    public Accounts createNewAccount(Accounts account) {
        if(repository.existsById(account.get_id()))
            throw new GlobalExceptions("Account ID is already exists.. Please type different account ID..");
        else
            return repository.save(account);
    }

    @Override
    public Accounts updateAccount(String accountID, Accounts updateAccount) {
        if(repository.existsById(accountID)){
            account=repository.findById(accountID).get();
            account.setUserName(updateAccount.getUserName());
            account.setMobileNumber(updateAccount.getMobileNumber());
            account.setGender(updateAccount.getGender());
            return repository.save(account);
        }
        else
            throw new GlobalExceptions("Account ID doesn't exist..");
    }

    @Override
    public Accounts updatePassword(String accountId, String password) {
        if(repository.existsById(accountId)){
            account=repository.findById(accountId).get();           //Fetching account details by account ID
            account.setPassword(password);                          //Setting new password
            return repository.save(account);                        //Saving modified account object
        }
        else
            throw new GlobalExceptions("Account doesn't exists.. Please create new account or enter valid account ID to update password..");
    }

    @Override
    public Accounts updateMobileNumber(String accountId, String mobileNumber) {
        if(repository.existsById(accountId)){
            account=repository.findById(accountId).get();           //Fetching account details by account ID
            account.setMobileNumber(mobileNumber);                  //Setting new mobile number
            return repository.save(account);                        //Saving modified account object
        }
        else
            throw new GlobalExceptions("Mobile number can't be updated since account doesn't exists in the records..");
    }

    @Override
    public String updateAccountType(String accountId) {
        if(repository.existsById(accountId)) {
            account = repository.findById(accountId).get();
            String flippedAccount = (account.getAccountType().equals("PUBLIC")) ? ("PRIVATE") : ("PUBLIC");
            account.setAccountType(flippedAccount);
            repository.save(account);
            return "Your account has been switched as " + flippedAccount;
        }
        else
            throw new GlobalExceptions("Account doesn't exists.. Please create new account or enter valid account ID..");
    }
}
