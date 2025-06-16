package fpt.aptech.trackmentalhealth.repository.community;

import fpt.aptech.trackmentalhealth.entities.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    @Query(
            "Select p from PostComment p where p.post.id =:id"
    )
    List<PostComment> GetCommentByPostId(@Param("id") Long id);
}
