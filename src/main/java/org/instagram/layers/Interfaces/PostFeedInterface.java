package org.instagram.layers.Interfaces;

import org.instagram.layers.Models.Accounts;
import org.instagram.layers.Models.Comments;
import org.instagram.layers.Models.Posts;

import java.util.List;

public interface PostFeedInterface {
    List<Posts> getPostsFeed(Accounts account);
    Posts postFeed(Posts newFeed);
    String likeOrUnlikePost(int postId, Accounts currentUser);
    Comments commentPost(Comments postComment);
    Comments replyToComment(int commentId, Comments replyToComment);
    String deleteComment(int commentId, String userId);
    Posts sharePost(int postId);
    Posts updateFeed(int postId, String userId, Posts updatedFeed);
    String deleteFeed(int postId, String userId);
}
