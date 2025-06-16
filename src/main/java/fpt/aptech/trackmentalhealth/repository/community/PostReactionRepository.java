package fpt.aptech.trackmentalhealth.repository.community;

import fpt.aptech.trackmentalhealth.entities.PostReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    @Query(
            "select r from PostReaction r where r.post.id = :postId"
    )
    List<PostReaction> getReactionsByPostId(@Param("postId") Long postId);
    @Query(
            "select r from PostReaction r where r.post.id = :pId and r.user.id =:userId"
    )
    PostReaction getPostReactionByPostIdAndUserId(Long postId, Long userId);

}
