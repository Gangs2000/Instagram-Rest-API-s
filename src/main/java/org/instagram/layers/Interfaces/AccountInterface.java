package org.instagram.layers.Interfaces;

import org.instagram.layers.Models.Accounts;

public interface AccountInterface {
    Accounts createNewAccount(Accounts account);
    Accounts updateAccount(String accountID, Accounts updateAccount);
    Accounts updatePassword(String accountId, String password);
    Accounts updateMobileNumber(String accountId, String mobileNumber);
    String updateAccountType(String accountId);
}
