package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.ArticleDTO;
import fpt.aptech.trackmentalhealth.dto.CommentDTO;
import fpt.aptech.trackmentalhealth.entities.Article;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.article.ArticleRepository;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import fpt.aptech.trackmentalhealth.service.article.ArticleService;
import fpt.aptech.trackmentalhealth.service.article.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/article")
public class ArticleController {
    @Autowired
    CommentService commentService;


    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;


    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveArticle(@PathVariable Integer id) {
        try {
            ArticleDTO approvedArticle = articleService.approveArticle(id);
            return ResponseEntity.ok(approvedArticle);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

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
    public ResponseEntity<?> createArticle(@RequestBody ArticleDTO request) {
        try {
            Article article = new Article();
            article.setTitle(request.getTitle());
            article.setContent(request.getContent());
            article.setCreatedAt(request.getCreatedAt());
            article.setStatus(request.getStatus());
            article.setPhoto(request.getPhoto());

            if (request.getAuthor() != null) {
                Users author = userRepository.findById(request.getAuthor())
                        .orElseThrow(() -> new RuntimeException("Author not found"));
                article.setAuthor(author);
            } else {
                article.setAuthor(null);
            }

            ArticleDTO articleDTO = articleService.createArticleDTO(article);
            return ResponseEntity.status(HttpStatus.CREATED).body(articleDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Update
    @PutMapping("/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable Integer id, @RequestBody ArticleDTO request) {
        try {
            // 1. Tìm Article
            Article existingArticle = articleRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));

            // 2. Cập nhật thông tin
            existingArticle.setTitle(request.getTitle());
            existingArticle.setContent(request.getContent());
            existingArticle.setPhoto(request.getPhoto());

            if (request.getStatus() != null) {
                existingArticle.setStatus(request.getStatus());
            }

            // 3. Xử lý author nếu có
            if (request.getAuthor() != null) {
                Optional<Users> optionalAuthor = userRepository.findById(request.getAuthor());
                if (optionalAuthor.isPresent()) {
                    existingArticle.setAuthor(optionalAuthor.get());
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Author not found with ID: " + request.getAuthor());
                }
            }

            // 4. Gọi service cập nhật
            ArticleDTO updated = articleService.updateArticleDTO(id, existingArticle);
            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: " + e.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Runtime error: " + e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
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

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByArticleId(@PathVariable Integer articleId) {
        List<CommentDTO> comments = commentService.getCommentsByArticleId(articleId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{articleId}/comments")
    public ResponseEntity<?> createComment(
            @PathVariable Integer articleId,
            @RequestBody CommentDTO commentDTO) {
        try {
            // Set articleId vào DTO từ URL
            commentDTO.setArticleId(articleId);

            CommentDTO saved = commentService.createComment(commentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
