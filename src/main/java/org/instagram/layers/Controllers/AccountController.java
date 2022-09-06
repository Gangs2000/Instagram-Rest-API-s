package org.instagram.layers.Controllers;

import org.instagram.layers.Models.Accounts;
import org.instagram.layers.Services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedList;

@RestController
@RequestMapping("/app/instagram")
public class AccountController {
    @Autowired AccountService service;

    @PostMapping("/createAccount")
    public ResponseEntity<Accounts> createNewAccount(@RequestBody Accounts account){
        account.setFollowers(new LinkedList<>());                           //Assigning empty followers and following lists
        account.setFollowing(new LinkedList<>());
        account.setPostIds(new LinkedList<>());                             //Assigning empty list for this account as it is new account
        account.setAccountType("PUBLIC");
        account.setPendingRequest(new LinkedList<>());                      //Assigning empty pending request list for this account
        account.setCreatedAt(LocalDate.now());
        return new ResponseEntity<>(service.createNewAccount(account), HttpStatus.CREATED);
    }

    @PatchMapping("/userID/{userID}/updatePassword")
    public ResponseEntity<Accounts> updatePassword(@PathVariable("userID") String accountId,@RequestBody Accounts password){
        return new ResponseEntity<>(service.updatePassword(accountId,password.getPassword()),HttpStatus.ACCEPTED);
    }

    @PatchMapping("/userID/{userID}/updateMobileNumber/{mobileNumber}")
    public ResponseEntity<Accounts> updateMobileNumber(@PathVariable("userID") String accountId, @PathVariable("mobileNumber") String mobileNumber){
        return new ResponseEntity<>(service.updateMobileNumber(accountId, mobileNumber),HttpStatus.ACCEPTED);
    }

    @PutMapping("/updateAccount/userID/{userID}")
    public ResponseEntity<Accounts> updateAccountDetails(@PathVariable("userID") String accountID, @RequestBody Accounts updateAccount){
        return new ResponseEntity<>(service.updateAccount(accountID, updateAccount),HttpStatus.ACCEPTED);
    }

    @PatchMapping("/updateAccountType/{userID}")
    public ResponseEntity<String> updateAccountType(@PathVariable("userID") String accountId){
        return new ResponseEntity<>(service.updateAccountType(accountId),HttpStatus.OK);
    }
}
