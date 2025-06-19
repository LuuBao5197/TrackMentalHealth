package fpt.aptech.trackmentalhealth.service.article;

import fpt.aptech.trackmentalhealth.dto.ArticleDTO;
import fpt.aptech.trackmentalhealth.dto.ExerciseDTO;
import fpt.aptech.trackmentalhealth.entities.Article;
import fpt.aptech.trackmentalhealth.entities.Exercise;

import java.util.List;

public interface ArticleService {
    // Business logic cua Article
    List<ArticleDTO> getArticleDTOs();
    ArticleDTO getArticleDTOById(Integer id);
    ArticleDTO createArticleDTO(Article article);
    ArticleDTO updateArticleDTO(Integer id, Article article);
    void deleteArticle(Integer id);
}
