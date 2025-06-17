package fpt.aptech.trackmentalhealth.service.article;

import fpt.aptech.trackmentalhealth.entities.Article;

import java.util.List;

public interface ArticleService {
    // Business logic cua Article
    List<Article> getArticles();
    Article getArticle(Integer id);
    Article createArticle(Article article);
    Article updateArticle(Integer id, Article article);
    void deleteArticle(Integer id);
}
