package org.instagram.layers.Interfaces;

import java.util.List;

public interface FollowerAndFolloweeInterface {
    List<String> getPendingRequests(String followerID);
    String followRequest(String followerID, String followeeID);
    String unfollowRequest(String followerID, String followeeID);
    String acceptRequest(String userID, String requestedID);
    String rejectRequest(String userID, String requestedID);
}
