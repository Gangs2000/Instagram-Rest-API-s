package org.instagram.layers.Services;

import org.instagram.layers.Exception.GlobalExceptions;
import org.instagram.layers.Interfaces.PostFeedInterface;
import org.instagram.layers.Models.Accounts;
import org.instagram.layers.Models.Comments;
import org.instagram.layers.Models.Posts;
import org.instagram.layers.Repositories.AccountsRepository;
import org.instagram.layers.Repositories.CommentRepository;
import org.instagram.layers.Repositories.PostFeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostFeedService implements PostFeedInterface {

    @Autowired Posts post;
    @Autowired Accounts account;
    @Autowired Comments comment;
    @Autowired PostFeedRepository postRepository;
    @Autowired AccountsRepository accountsRepository;
    @Autowired CommentRepository commentRepository;

    @Override
    public List<Posts> getPostsFeed(Accounts currentUser) {
        List<List<Integer>> postIds=new LinkedList<>();
        if(accountsRepository.existsById(currentUser.get_id()) && accountsRepository.findById(currentUser.get_id()).get().getPassword().equals(currentUser.getPassword())){
            account=accountsRepository.findById(currentUser.get_id()).get();
            postIds.add(account.getPostIds());
            List<String> following=account.getFollowing();
            following.stream().forEach(userId->{
                if(account.getFollowers().contains(userId))
                    postIds.add(accountsRepository.findById(userId).get().getPostIds());
                else {
                    account=accountsRepository.findById(userId).get();
                    if(account.getAccountType().equals("PUBLIC"))
                        postIds.add(account.getPostIds());
                }
            });
            List<Integer> flatIds=postIds.stream().flatMap(list->list.stream()).collect(Collectors.toList());
            List<Posts> posts= (List<Posts>) postRepository.findAllById(flatIds);
            Comparator<Posts> comparator= (t1, t2) -> (t1.getUploadedAt().isBefore(t2.getUploadedAt()))?(1):(-1);
            //Sorted LocalDateAndTime in descending order
            Collections.sort(posts, comparator);
            //Collecting the latest posts which are uploaded 14 hours before
            List<Posts> getLatestPosts=posts.stream().filter(object->Duration.between(object.getUploadedAt(), LocalDateTime.now()).toHours()<=24).collect(Collectors.toList());
            return getLatestPosts;
        }
        else
            throw new GlobalExceptions("Invalid Authorization request..");
    }

    @Override
    public Posts postFeed(Posts newFeed) {
        if(accountsRepository.existsById(newFeed.getAccountId())) {
            if (postRepository.count() == 0)
                newFeed.set_id(1);
            else
                newFeed.set_id(postRepository.findAll().stream().map(object -> object.get_id()).max(Integer::compareTo).get() + 1);
            newFeed.setLikes(new LinkedList<>()); newFeed.setComments(new LinkedList<>()); newFeed.setShares(0);
            newFeed.setUploadedAt(LocalDateTime.now());
            //Saving new Feed and collecting post object to update Post ID in respective account ID
            post=postRepository.save(newFeed);
            //Adding Post ID to respective account ID after publishing post
            account=accountsRepository.findById(newFeed.getAccountId()).get();
            account.getPostIds().add(post.get_id());
            account.setPostIds(account.getPostIds());
            accountsRepository.save(account);
            return post;
        }
        else
            throw new GlobalExceptions("Post can't be uploaded since Account ID doesn't exist.. Please create new Account ID..");
    }

    @Override
    public String likeOrUnlikePost(int postId, Accounts currentUser) {
        if(postRepository.existsById(postId)){
            if(accountsRepository.existsById(currentUser.get_id())){
                post=postRepository.findById(postId).get();
                List<String> likes=post.getLikes(); String message="";
                if(likes.contains(currentUser.get_id())){
                    likes.remove(currentUser.get_id());
                    message="You have unliked this post";
                }
                else {
                    likes.add(currentUser.get_id());
                    message="You have liked this post..";
                }
                post.setLikes(likes);
                postRepository.save(post);
                return message;
            }
            else
                throw new GlobalExceptions("Account ID doesn't exist.. Please create new Account ID..");
        }
        else
            throw new GlobalExceptions("Post ID doesn't exists..");
    }

    @Override
    public Comments commentPost(Comments postComment) {
        if(accountsRepository.existsById(postComment.getCommentedBy())){
            if(postRepository.existsById(postComment.getPostId())) {
                if (commentRepository.count() == 0)
                    postComment.set_id(1);
                else
                    postComment.set_id(commentRepository.findAll().stream().map(object -> object.get_id()).max(Integer::compareTo).get() + 1);
                postComment.setCommentedAt(LocalDateTime.now());
                //Saving Comment details in database
                comment = commentRepository.save(postComment);
                post = postRepository.findById(comment.getPostId()).get();
                post.getComments().add(comment);
                post.setComments(post.getComments());
                //Updating comment details in respective post ID
                postRepository.save(post);
                return comment;
            }
            else
                throw new GlobalExceptions("Post ID doesn't exists..");
        }
        else
            throw new GlobalExceptions("You are not allowed to comment since Account ID doesn't exist.. Please create new Account ID..");
    }

    @Override
    public Comments replyToComment(int commentId, Comments replyComment) {
        if(commentRepository.existsById(commentId)){
            comment=commentRepository.findById(commentId).get();
            if(accountsRepository.existsById(replyComment.getCommentedBy())) {
                String stamp = "";
                if (postRepository.findById(comment.getPostId()).get().getAccountId().equals(replyComment.getCommentedBy()))
                    stamp += "You replied to " + comment.getCommentedBy() + ", comment_id : " + commentId + " " + "-> ";
                else
                    stamp += replyComment.getCommentedBy() + " replied to " + comment.getCommentedBy() + ", comment_id : " + commentId + " " + "-> ";
                stamp += replyComment.getComment();
                replyComment.setComment(stamp);
                replyComment.setPostId(comment.getPostId());
                return replyComment;
            }
            else
                throw new GlobalExceptions("You are not allowed to comment since Account ID doesn't exist.. Please create new Account ID..");
        }
        else
            throw new GlobalExceptions("Comment ID doesn't exists.. Please enter valid comment ID..");
    }

    @Override
    public String deleteComment(int commentId, String userId) {
        if(accountsRepository.existsById(userId)){
            if(commentRepository.existsById(commentId)){
                comment=commentRepository.findById(commentId).get();
                if(comment.getCommentedBy().equals(userId)){
                    post=postRepository.findById(comment.getPostId()).get();
                    List<Comments> comments=post.getComments();
                    for(Comments cmts : comments){
                        if(cmts.get_id()==commentId) {
                            comments.remove(cmts);
                            break;
                        }
                    }
                    post.setComments(comments);
                    postRepository.save(post);
                    commentRepository.deleteById(commentId);
                    return "Comment has been deleted successfully..";
                }
                else
                    throw new GlobalExceptions("This comment can't be deleted since it is not posted by you..");
            }
            else
                throw new GlobalExceptions("Comment ID doesn't exists.. Please enter valid comment ID..");
        }
        else
            throw new GlobalExceptions("You are not allowed to delete comment since Account ID doesn't exist.. Please create new Account ID..");
    }

    @Override
    public Posts sharePost(int postId) {
        if(postRepository.existsById(postId)){
            post=postRepository.findById(postId).get();
            post.setShares(post.getShares()+1);
            return postRepository.save(post);
        }
        else
            throw new GlobalExceptions("Post ID doesn't exists..");
    }

    @Override
    public Posts updateFeed(int postId, String userId, Posts updatedFeed) {
        if(postRepository.existsById(postId) && accountsRepository.existsById(userId)){
            post=postRepository.findById(postId).get();
            if(post.getAccountId().equals(userId)){
                post.setPostTitle(updatedFeed.getPostTitle());
                post.setPostDescription(updatedFeed.getPostDescription());
                post.setPostTags(updatedFeed.getPostTags());
                return postRepository.save(post);
            }
            else
                throw new GlobalExceptions("You can't update this feed since it's not uploaded by you..");
        }
        else
            throw new GlobalExceptions("Invalid action.. Either post ID or account ID doesn't exists..");
    }

    @Override
    public String deleteFeed(int postId, String userId) {
        if(postRepository.existsById(postId) && accountsRepository.existsById(userId)) {
            post=postRepository.findById(postId).get();
            if(post.getAccountId().equals(userId)){
                List<Integer> commentIds=post.getComments().stream().map(object->object.get_id()).collect(Collectors.toList());
                //Deleting all comments from comment database
                commentRepository.deleteAllById(commentIds);
                account=accountsRepository.findById(userId).get();
                //Deleting post ID from respective account ID
                List<Integer> postIds=account.getPostIds();
                postIds.remove((Integer) postId);
                account.setPostIds(postIds);
                accountsRepository.save(account);
                //Deleting post from post database
                postRepository.deleteById(postId);
                return "Feed has been deleted successfully..";
            }
            else
                throw new GlobalExceptions("You can't delete this feed since it's not uploaded by you..");
        }
        else
            throw new GlobalExceptions("Invalid action.. Either post ID or account ID doesn't exists..");
    }
}
