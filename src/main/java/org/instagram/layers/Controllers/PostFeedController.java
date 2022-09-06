package org.instagram.layers.Controllers;

import org.instagram.layers.Models.Accounts;
import org.instagram.layers.Models.Comments;
import org.instagram.layers.Models.Posts;
import org.instagram.layers.Services.PostFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app/instagram")
public class PostFeedController {
    @Autowired PostFeedService service;
    @Autowired Comments comment;

    @GetMapping("/getPostsFeed")
    public ResponseEntity<List<Posts>> getPostsFeed(@RequestBody Accounts account){
        return new ResponseEntity<>(service.getPostsFeed(account),HttpStatus.OK);
    }

    @PostMapping("/postFeed")
    public ResponseEntity<Posts> postNewFeed(@RequestBody Posts post){
        return new ResponseEntity<>(service.postFeed(post), HttpStatus.CREATED);
    }

    @PatchMapping("/likePost/postId/{postId}")
    public ResponseEntity<String> likeOrUnlikePost(@PathVariable("postId") int postId, @RequestBody Accounts currentUser){
        List<Integer> postIds=service.getPostsFeed(currentUser).stream().map(object->object.get_id()).collect(Collectors.toList());
        if(postIds.contains(postId))
            return new ResponseEntity<>(service.likeOrUnlikePost(postId, currentUser),HttpStatus.OK);
        else
            return new ResponseEntity<>("You can't like this post since this account is not followed by you..",HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/commentPost")
    public ResponseEntity<Comments> postTheComment(@RequestBody Comments comment){
        return new ResponseEntity<>(service.commentPost(comment),HttpStatus.OK);
    }

    @PutMapping("/replyToComment/commentId/{commentId}")
    public ResponseEntity<Comments> replyToComment(@PathVariable("commentId") int commentId, @RequestBody Comments replyComment){
        comment=service.replyToComment(commentId, replyComment);
        return new ResponseEntity<>(service.commentPost(comment),HttpStatus.OK);
    }

    @DeleteMapping("/deleteComment/commentId/{commentId}/userId/{userId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") int commentId, @PathVariable("userId") String userId){
        return new ResponseEntity<>(service.deleteComment(commentId, userId),HttpStatus.OK);
    }

    @PatchMapping("/sharePost/postId/{postId}")
    public ResponseEntity<Posts> sharePost(@PathVariable("postId") int postId){
        return new ResponseEntity<>(service.sharePost(postId),HttpStatus.OK);
    }

    @PutMapping("/updateFeed/postId/{postId}/userId/{userId}")
    public ResponseEntity<Posts> updateFeed(@PathVariable("postId") int postId, @PathVariable("userId") String userId, @RequestBody Posts updatedFeed){
        return new ResponseEntity<>(service.updateFeed(postId, userId, updatedFeed),HttpStatus.OK);
    }

    @DeleteMapping("/deleteFeed/postId/{postId}/userId/{userId}")
    public ResponseEntity<String> deleteFeed(@PathVariable("postId") int postId, @PathVariable("userId") String userId){
        return new ResponseEntity<>(service.deleteFeed(postId, userId),HttpStatus.OK);
    }
}
