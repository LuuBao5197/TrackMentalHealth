package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.ArticleDTO;
import fpt.aptech.trackmentalhealth.dto.ExerciseDTO;
import fpt.aptech.trackmentalhealth.entities.Article;
import fpt.aptech.trackmentalhealth.entities.Exercise;
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

    // API CUA LESSON //
    @GetMapping("/")
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articles = articleService.getArticleDTOs();
        return ResponseEntity.ok(articles);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Integer id) {
        ArticleDTO articleDTO = articleService.getArticleDTOById(id);
        return ResponseEntity.ok().body(articleDTO);
    }

    @PostMapping("/")
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody Article article) {
        ArticleDTO articleDTO = articleService.createArticleDTO(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(articleDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Integer id, @RequestBody Article article) {
        ArticleDTO articleDTO = articleService.updateArticleDTO(id, article);
        return ResponseEntity.status(HttpStatus.OK).body(articleDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable Integer id) {
        articleService.deleteArticle(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}