package fpt.aptech.trackmentalhealth.service.community;

import fpt.aptech.trackmentalhealth.entities.CommunityPost;
import fpt.aptech.trackmentalhealth.entities.PostComment;
import fpt.aptech.trackmentalhealth.entities.PostReaction;
import fpt.aptech.trackmentalhealth.repository.community.CommunityPostRepository;
import fpt.aptech.trackmentalhealth.repository.community.PostCommentRepository;
import fpt.aptech.trackmentalhealth.repository.community.PostReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityServiceImp implements CommunityService {
    @Autowired
    CommunityPostRepository communityPostRepository;
    @Autowired
    PostReactionRepository postReactionRepository;
    @Autowired
    PostCommentRepository postCommentRepository;

    @Override
    public Page<CommunityPost> getAllCommunityPosts(Pageable pageable) {
        return communityPostRepository.findAll(pageable);
    }

    @Override
    public List<CommunityPost> getCommunityPostsOfUser(long userId) {
        return communityPostRepository.getCommunityPostsOfUser(userId);
    }

    @Override
    public CommunityPost getCommunityPostsByID(long communityId) {
        return communityPostRepository.findById(communityId).orElseThrow(() -> new RuntimeException("Community Post Not Found"));
    }

    @Override
    public CommunityPost saveCommunityPost(CommunityPost communityPost) {
        communityPostRepository.save(communityPost);
        return communityPost;
    }

    @Override
    public CommunityPost updateCommunityPost(CommunityPost communityPost) {
        communityPostRepository.save(communityPost);
        return communityPost;
    }

    @Override
    public void deleteCommunityPost(Long communityPostId) {
        communityPostRepository.deleteById(communityPostId);
    }

    @Override
    public List<PostComment> getCommentsOfPost(Long postId) {
        return postCommentRepository.GetCommentByPostId(postId);
    }

    @Override
    public PostComment getCommentsByID(Long commentId) {
        return postCommentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment Not Found"));
    }

    @Override
    public PostComment saveComment(PostComment comment) {
        postCommentRepository.save(comment);
        return comment;
    }

    @Override
    public PostComment updatePostComment(PostComment postComment) {
        return postCommentRepository.save(postComment);
    }

    @Override
    public void deletePostComment(Long postCommentId) {
        postCommentRepository.deleteById(postCommentId);
    }

    @Override
    public List<PostReaction> getReactions(long postId) {
        return postReactionRepository.getReactionsByPostId(postId);
    }

    @Override
    public PostReaction getReactionsByID(long reactionId) {
        return postReactionRepository.findById(reactionId).orElseThrow(() -> new RuntimeException("Reaction Not Found"));
    }

    @Override
    public PostReaction saveReaction(PostReaction reaction) {
        postReactionRepository.save(reaction);
        return reaction;
    }

    @Override
    public PostReaction updateReaction(PostReaction reaction) {
        postReactionRepository.save(reaction);
        return reaction;
    }

    @Override
    public void deleteReaction(Long reactionId) {
        postReactionRepository.deleteById(reactionId);
    }

}
