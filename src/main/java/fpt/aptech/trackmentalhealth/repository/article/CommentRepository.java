package fpt.aptech.trackmentalhealth.repository.article;

import fpt.aptech.trackmentalhealth.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByArticle_Id(Integer articleId);
}
