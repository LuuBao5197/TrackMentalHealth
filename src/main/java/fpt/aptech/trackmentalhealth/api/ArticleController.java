package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Article;
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
    public ResponseEntity<List<Article>> getArticles() {
        List<Article> Articles =  articleService.getArticles();
        return ResponseEntity.ok().body(Articles);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Integer id) {
        Article article = articleService.getArticle(id);
        return ResponseEntity.ok().body(article);
    }
    @PostMapping("/")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(article));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Integer id, @RequestBody Article article) {
        return ResponseEntity.status(HttpStatus.OK).body(articleService.updateArticle(id, article));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable Integer id) {
        articleService.deleteArticle(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}