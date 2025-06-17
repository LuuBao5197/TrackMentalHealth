package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.CommunityPost;
import fpt.aptech.trackmentalhealth.entities.PostComment;
import fpt.aptech.trackmentalhealth.entities.PostReaction;
import fpt.aptech.trackmentalhealth.service.community.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ObjectOutput;
import java.text.MessageFormat;
import java.util.List;

@Controller
@RequestMapping("/api/community")
public class CommunityController {
    final
    CommunityService communityService;
    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping("/post")
    public ResponseEntity<List<CommunityPost>> getPosts(){
        List<CommunityPost> communityPosts = communityService.getAllCommunityPosts();
        return new ResponseEntity<>(communityPosts, HttpStatus.OK);
    }
    @GetMapping("/post/{postId}")
    public ResponseEntity<CommunityPost> getPostsByCommunityId(@PathVariable Integer postId) {
        CommunityPost communityPost = communityService.getCommunityPostsByID(postId);
        return ResponseEntity.ok().body(communityPost);
    }
    @PostMapping("/post")
    public ResponseEntity<CommunityPost> createPost(@RequestBody CommunityPost communityPost) {
        communityService.saveCommunityPost(communityPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(communityPost);
    }
    @PutMapping("/post/{id}")
  public  ResponseEntity<CommunityPost> updatePost(@PathVariable Integer id, @RequestBody CommunityPost communityPost) {
        CommunityPost existingPost = communityService.getCommunityPostsByID(id);
        if (existingPost != null && existingPost.getId().equals(communityPost.getId())) {
            communityService.saveCommunityPost(communityPost);
            return ResponseEntity.ok().body(communityPost);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(communityPost);
        }
    }
    @DeleteMapping("/post/{id}")
    public ResponseEntity<CommunityPost> deletePost(@PathVariable Long id) {
        CommunityPost communityPost = communityService.getCommunityPostsByID(id);
        if (communityPost != null) {
            communityService.deleteCommunityPost(id);
            return ResponseEntity.ok().body(communityPost);
        }else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

//    API comment

    @GetMapping("/post/{id}/comment")
    public ResponseEntity<List<PostComment>> getCommentsByPostId(@PathVariable Long id) {
        List<PostComment> list = communityService.getCommentsOfPost(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/post/comment/{id}")
    public ResponseEntity<PostComment> getCommentsById(@PathVariable Long id) {
        PostComment comment = communityService.getCommentsByID(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PostMapping("/post/comment/")
    public ResponseEntity<PostComment> createComment(@RequestBody PostComment comment) {
        communityService.saveComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PutMapping("/post/comment/{commentId}")
    public ResponseEntity<PostComment> updateComment(@PathVariable Long commentId, @RequestBody PostComment comment) {
        communityService.saveComment(comment);
        return ResponseEntity.ok().body(comment);
    }

    @DeleteMapping("/post/comment/{id}")
    public ResponseEntity<PostComment> deleteComment(@PathVariable Long id) {
        PostComment existingComment = communityService.getCommentsByID(id);
        if (existingComment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        communityService.deleteCommunityPost(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

//    API reaction
    @GetMapping("/post/{id}/reaction")
    public ResponseEntity<List<PostReaction>> getReactionsByPostId(@PathVariable Long id) {
        List<PostReaction> list = communityService.getReactions(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/post/{id}/reaction")
    public ResponseEntity<PostReaction> createReaction(@PathVariable Long id, @RequestBody PostReaction reaction) {
        CommunityPost communityPost = communityService.getCommunityPostsByID(id);
        if (communityPost != null) {
            communityService.saveReaction(reaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(reaction);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);


    }

    @PutMapping("/reaction/{id}")
    public ResponseEntity<PostReaction> updateReaction(@PathVariable Long id, @RequestBody PostReaction reaction) {
        PostReaction existPostR = communityService.getReactionsByID(id);
        if (existPostR != null && existPostR.getId().equals(reaction.getId())) {
            communityService.saveReaction(reaction);
            return ResponseEntity.ok().body(reaction);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @DeleteMapping("/reaction/{id}")
    public ResponseEntity<PostReaction> deleteReaction(@PathVariable Long id) {
        PostReaction reaction = communityService.getReactionsByID(id);
        if (reaction != null) {
            communityService.deleteReaction(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
