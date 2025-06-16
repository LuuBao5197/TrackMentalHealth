package fpt.aptech.trackmentalhealth.service.community;

import fpt.aptech.trackmentalhealth.entities.CommunityPost;
import fpt.aptech.trackmentalhealth.entities.PostComment;
import fpt.aptech.trackmentalhealth.entities.PostReaction;

import java.util.List;

public interface CommunityService {
    List<CommunityPost> getAllCommunityPosts();
    List<CommunityPost> getCommunityPostsOfUser(long userId);
    CommunityPost getCommunityPostsByID(long communityId);
    CommunityPost saveCommunityPost(CommunityPost communityPost);
    CommunityPost updateCommunityPost(CommunityPost communityPost);
    void deleteCommunityPost(Long communityPostId);

    List<PostComment> getCommentsOfPost(Long postId);
    PostComment getCommentsByID(Long commentId);
    PostComment saveComment(PostComment comment);
    PostComment updatePostComment(PostComment postComment);
    void deletePostComment(Long postCommentId);

    List<PostReaction> getReactions(long postId);
    PostReaction getReactionsByID(long reactionId);
    PostReaction saveReaction(PostReaction reaction);
    PostReaction updateReaction(PostReaction reaction);
    void deleteReaction(Long reactionId);
}


