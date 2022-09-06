package org.instagram.layers.Controllers;

import org.instagram.layers.Services.FollowerAndFolloweeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/instagram")
public class FollowerAndFolloweeController {
    @Autowired
    FollowerAndFolloweeService service;

    @GetMapping("/getPendingRequests/{userID}")
    public ResponseEntity<List<String>> getPendingRequests(@PathVariable("userID") String accountID){
        return new ResponseEntity<>(service.getPendingRequests(accountID),HttpStatus.OK);
    }

    @PatchMapping("/follow/follower/{followerID}/followee/{followeeID}")
    public ResponseEntity<String> followRequest(@PathVariable("followerID") String followerID, @PathVariable("followeeID") String followeeID){
        String output=service.followRequest(followerID, followeeID);
        if(output.equals("Accept"))
            return new ResponseEntity<>(service.acceptRequest(followerID, followeeID),HttpStatus.OK);
        else
            return new ResponseEntity<>(output,HttpStatus.OK);
    }

    @PatchMapping("/acceptFollowRequest/{userID}/{requestedID}")
    public ResponseEntity<String> acceptRequest(@PathVariable("userID") String userID, @PathVariable("requestedID") String requestedID){
        return new ResponseEntity<>(service.acceptRequest(userID, requestedID),HttpStatus.OK);
    }

    @DeleteMapping("/unfollow/follower/{followerID}/followee/{followeeID}")
    public ResponseEntity<String> unfollowRequest(@PathVariable("followerID") String followerID, @PathVariable("followeeID") String followeeID){
        return new ResponseEntity<>(service.unfollowRequest(followerID, followeeID),HttpStatus.OK);
    }

    @DeleteMapping("/rejectFollowRequest/{userID}/{requestedID}")
    public ResponseEntity<String> rejectRequest(@PathVariable("userID") String userID, @PathVariable("requestedID") String requestedID){
        String output=service.rejectRequest(userID, requestedID);
        if(output.equals("Unfollow"))
            return new ResponseEntity<>(service.unfollowRequest(userID, requestedID),HttpStatus.OK);
        else
            return new ResponseEntity<>(output,HttpStatus.OK);
    }
}
