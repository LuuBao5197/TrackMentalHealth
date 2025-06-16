package fpt.aptech.trackmentalhealth.repository.community;

import fpt.aptech.trackmentalhealth.entities.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {
    @Query(
            "select c from CommunityPost c where c.user.id =: userId"
    )
    List<CommunityPost> getCommunityPostsOfUser(long userId);
}
