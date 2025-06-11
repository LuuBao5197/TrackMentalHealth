package fpt.aptech.trackmentalhealth.service.article;

import fpt.aptech.trackmentalhealth.entities.Article;
import fpt.aptech.trackmentalhealth.entities.Exercise;
import fpt.aptech.trackmentalhealth.repository.article.ArticleRepository;
import fpt.aptech.trackmentalhealth.repository.exercise.ExerciseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImp implements ArticleService {
    ArticleRepository articleRepository;

    public ArticleServiceImp(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Article getArticle(Integer id) {
        return articleRepository.findById(id).orElseThrow(()->new RuntimeException("Article not found"));
    }

    @Override
    public Article createArticle(Article article) {
        articleRepository.save(article);
        return article;
    }

    @Override
    public Article updateArticle(Integer id, Article article) {
        articleRepository.save(article);
        return article;
    }

    @Override
    public void deleteArticle(Integer id) {
        Article articleDel = articleRepository.findById(id).orElseThrow(()->new RuntimeException("Article not found"));
        if(articleDel == null){
            throw new RuntimeException("Article not found");
        } else {
            articleRepository.delete(articleDel);
        }
    }
}
