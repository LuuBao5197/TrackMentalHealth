package fpt.aptech.trackmentalhealth.service.article;

import fpt.aptech.trackmentalhealth.dto.ArticleDTO;
import fpt.aptech.trackmentalhealth.dto.ExerciseDTO;
import fpt.aptech.trackmentalhealth.entities.Article;
import fpt.aptech.trackmentalhealth.entities.Exercise;
import fpt.aptech.trackmentalhealth.repository.article.ArticleRepository;
import fpt.aptech.trackmentalhealth.repository.exercise.ExerciseRepository;
import fpt.aptech.trackmentalhealth.service.lesson.ContentModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImp implements ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ContentModerationService contentModerationService;


    @Override
    public ArticleDTO approveArticle(Integer id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with ID: " + id));

        article.setStatus("true"); // duyệt bài -> status = true
        articleRepository.save(article);

        return new ArticleDTO(article);
    }

    public ArticleServiceImp(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<ArticleDTO> getArticleDTOs() {
        List<Article> exercises = articleRepository.findAll();
        return exercises.stream().map(ArticleDTO::new).toList();
    }

    @Override
    public ArticleDTO getArticleDTOById(Integer id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
        return new ArticleDTO(article);
    }

    @Override
    public ArticleDTO createArticleDTO(Article article) {
        // Kiểm tra kết nối API
        contentModerationService.checkApiConnection();

        // Kiểm tra nội dung nhạy cảm
        if (article.getTitle() != null && contentModerationService.isSensitiveContent(article.getTitle())) {
            throw new RuntimeException("Article title contains sensitive content.");
        }
        if (article.getContent() != null && contentModerationService.isSensitiveContent(article.getContent())) {
            throw new RuntimeException("Article content contains sensitive content.");
        }

        Article savedArticle = articleRepository.save(article);
        return new ArticleDTO(savedArticle);
    }

    @Override
    public ArticleDTO updateArticleDTO(Integer id, Article article) {
        // Kiểm tra kết nối API
        contentModerationService.checkApiConnection();

        // Kiểm tra nội dung nhạy cảm
        if (article.getTitle() != null && contentModerationService.isSensitiveContent(article.getTitle())) {
            throw new RuntimeException("Article title contains sensitive content.");
        }
        if (article.getContent() != null && contentModerationService.isSensitiveContent(article.getContent())) {
            throw new RuntimeException("Article content contains sensitive content.");
        }

        Article updatedArticle = articleRepository.save(article);
        return new ArticleDTO(updatedArticle);
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

    @Override
    public List<ArticleDTO> getArticlesByCreatorId(Integer creatorId) {
        List<Article> articles = articleRepository.findByAuthor_Id(creatorId);
        return articles.stream().map(ArticleDTO::new).toList();
    }

}
