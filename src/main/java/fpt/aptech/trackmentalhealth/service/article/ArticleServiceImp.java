package fpt.aptech.trackmentalhealth.service.article;

import fpt.aptech.trackmentalhealth.dto.ArticleDTO;
import fpt.aptech.trackmentalhealth.dto.ExerciseDTO;
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
        Article savedArticle = articleRepository.save(article);
        return new ArticleDTO(savedArticle);
    }

    // Phương thức này nhận một đối tượng Article đã được cập nhật từ controller
    // và lưu nó vào cơ sở dữ liệu.
    public ArticleDTO updateArticleDTO(Integer id, Article article) {
        // Id đã được kiểm tra ở Controller, nên ở đây chỉ cần save
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
