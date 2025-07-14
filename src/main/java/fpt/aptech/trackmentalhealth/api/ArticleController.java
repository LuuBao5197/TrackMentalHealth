package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.ArticleDTO;
import fpt.aptech.trackmentalhealth.entities.Article;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.article.ArticleRepository;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import fpt.aptech.trackmentalhealth.service.article.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    // GET ALL
    @GetMapping("/")
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articles = articleService.getArticleDTOs();
        return ResponseEntity.ok(articles);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Integer id) {
        ArticleDTO articleDTO = articleService.getArticleDTOById(id);
        return ResponseEntity.ok().body(articleDTO);
    }

    // CREATE
    @PostMapping("/")
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO request) {
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setCreatedAt(request.getCreatedAt());
        article.setStatus(request.getStatus());

        if (request.getAuthor() != null) {
            Users author = userRepository.findById(request.getAuthor())
                    .orElseThrow(() -> new RuntimeException("Author not found"));
            article.setAuthor(author);
        } else {
            article.setAuthor(null); // Cho phép null author
        }

        ArticleDTO articleDTO = articleService.createArticleDTO(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(articleDTO);
    }



    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable Integer id, @RequestBody ArticleDTO request) {
        try {
            Article article = articleRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Article not found"));

            article.setTitle(request.getTitle());
            article.setContent(request.getContent());
            article.setCreatedAt(request.getCreatedAt());
            article.setStatus(request.getStatus());

            if (request.getAuthor() != null) {
                Users author = userRepository.findById(request.getAuthor())
                        .orElseThrow(() -> new RuntimeException("Author not found"));
                article.setAuthor(author);
            } else {
                article.setAuthor(null);
            }

            ArticleDTO updated = articleService.updateArticleDTO(id, article);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable Integer id) {
        articleService.deleteArticle(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // GET all articles by creator
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<ArticleDTO>> getArticlesByCreator(@PathVariable Integer creatorId) {
        try {
            List<ArticleDTO> articles = articleService.getArticlesByCreatorId(creatorId);
            return ResponseEntity.ok(articles);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
