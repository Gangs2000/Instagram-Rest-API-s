package org.instagram.layers.Services;

import org.instagram.layers.Exception.GlobalExceptions;
import org.instagram.layers.Interfaces.FollowerAndFolloweeInterface;
import org.instagram.layers.Models.Accounts;
import org.instagram.layers.Repositories.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowerAndFolloweeService implements FollowerAndFolloweeInterface {

    @Autowired Accounts account;
    @Autowired AccountsRepository repository;

    @Override
    public List<String> getPendingRequests(String followerID) {
        if(repository.existsById(followerID))
            return repository.findById(followerID).get().getPendingRequest();
        else
            throw new GlobalExceptions("Account ID doesn't exist.. Please create new Account ID..");
    }

    @Override
    public String followRequest(String followerID, String followeeID) {
        if(repository.existsById(followerID)){
            account=repository.findById(followerID).get();
            if(!account.getFollowing().contains(followeeID) && !account.getPendingRequest().contains(followeeID)) {
                if (repository.existsById(followeeID)) {
                    List<String> followingList = account.getFollowing();
                    followingList.add(followeeID);
                    repository.save(account);

                    account = repository.findById(followeeID).get();
                    List<String> pendingRequest = account.getPendingRequest();
                    List<String> followers = account.getFollowers();
                    pendingRequest.add(followerID);
                    followers.add(followerID);
                    repository.save(account);
                    return "You are now following " + followeeID;
                }
                else
                    throw new GlobalExceptions("No such followee ID found.. Please double check account ID..");
            }
            else if(account.getFollowing().contains(followeeID))
                throw new GlobalExceptions("You are already following "+followeeID);
            else
                return "Accept";
        }
        else
            throw new GlobalExceptions("Account ID doesn't exist.. Please create new Account ID..");
    }

    @Override
    public String unfollowRequest(String followerID, String followeeID) {
        if(repository.existsById(followerID)) {
            if (repository.findById(followerID).get().getFollowing().contains(followeeID)) {
                account = repository.findById(followeeID).get();
                List<String> pendingRequest = account.getPendingRequest();
                if (pendingRequest.contains(followerID))
                    pendingRequest.remove(followerID);
                account.setPendingRequest(pendingRequest);
                List<String> followers = account.getFollowers();
                if (followers.contains(followerID))
                    followers.remove(followerID);
                account.setFollowers(followers);
                repository.save(account);

                account = repository.findById(followerID).get();
                List<String> following = account.getFollowing();
                following.remove(followeeID);
                account.setFollowing(following);
                repository.save(account);
                return "You have unfollowed "+followeeID;
            }
            else
                throw new GlobalExceptions("You are not following "+followeeID);
        }
        else
            throw new GlobalExceptions("Account ID doesn't exist.. Please create new Account ID..");
    }

    @Override
    public String acceptRequest(String userID, String requestedID) {
        if(repository.existsById(userID)){
            account=repository.findById(userID).get();
            List<String> pendingRequest=account.getPendingRequest();
            if(pendingRequest.contains(requestedID)){
                List<String> following=account.getFollowing();
                if(following.contains(requestedID)) {
                    pendingRequest.remove(requestedID);
                    account.setPendingRequest(pendingRequest);
                    repository.save(account);
                    return "You are now following back " + requestedID;
                }
                else {
                    following.add(requestedID);
                    pendingRequest.remove(requestedID);
                    account.setFollowing(following);
                    account.setPendingRequest(pendingRequest);
                    repository.save(account);

                    account = repository.findById(requestedID).get();
                    List<String> followers = account.getFollowers();
                    followers.add(userID);
                    repository.save(account);
                    return "You are now following back " + requestedID;
                }
            }
            else
                throw new GlobalExceptions("Requested ID is not found in our record..");
        }
        else
            throw new GlobalExceptions("Account ID doesn't exist.. Please create new Account ID..");
    }

    @Override
    public String rejectRequest(String userID, String requestedID) {
        if(repository.existsById(userID)){
            account=repository.findById(userID).get();
            List<String> pendingRequest=account.getPendingRequest();
            if(pendingRequest.contains(requestedID)){
                pendingRequest.remove(requestedID);
                account.setPendingRequest(pendingRequest);
                repository.save(account);
                if(account.getFollowing().contains(requestedID))
                    return "Unfollow";
                return "You have rejected the following request sent by "+requestedID;
            }
            else
                throw new GlobalExceptions("Requested ID is not found in our record..");
        }
        else
            throw new GlobalExceptions("Account ID doesn't exist.. Please create new Account ID..");
    }
}
