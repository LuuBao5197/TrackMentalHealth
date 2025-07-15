package fpt.aptech.trackmentalhealth.repository.article;

import fpt.aptech.trackmentalhealth.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findByAuthor_Id(Integer authorId);
}
