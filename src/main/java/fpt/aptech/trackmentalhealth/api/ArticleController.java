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
        article.setPhoto(request.getPhoto());
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
            // 1. Tìm Article cần update
            Article existingArticle = articleRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + id));

            // 2. Cập nhật các trường có thể thay đổi từ request DTO
            existingArticle.setTitle(request.getTitle());
            existingArticle.setContent(request.getContent());
            existingArticle.setPhoto(request.getPhoto());
            // 3. Xử lý trường 'status'
            // Đảm bảo status từ DTO được chuyển đổi đúng sang kiểu Boolean
            // Nếu bạn muốn status là boolean trong entity, hãy Parse từ string "true"/"false"
            if (request.getStatus() != null) {
                existingArticle.setStatus(request.getStatus()); // Nếu status trong Article là String
            }
            // HOẶC nếu status trong Article Entity là Boolean:
            // existingArticle.setStatus(Boolean.parseBoolean(request.getStatus()));


            // 4. KHÔNG NÊN CẬP NHẬT createdAt KHI UPDATE.
            // createdAt là thời điểm bài viết được tạo ra, không thay đổi.
            // existingArticle.setCreatedAt(request.getCreatedAt()); // <-- BỎ DÒNG NÀY

            // 5. Xử lý Author:
            // Thông thường, tác giả của một bài viết không thay đổi khi update.
            // Tuy nhiên, nếu bạn thực sự muốn cho phép thay đổi tác giả,
            // hãy đảm bảo logic tìm kiếm tác giả là đúng.
            // Nếu request.getAuthor() là null, giữ nguyên tác giả hiện tại
            if (request.getAuthor() != null) {
                Users author = userRepository.findById(request.getAuthor())
                        .orElseThrow(() -> new RuntimeException("Author not found with ID: " + request.getAuthor()));
                existingArticle.setAuthor(author);
            }
            // Nếu request.getAuthor() là null và bạn muốn SET NULL cho tác giả:
            // else {
            //     existingArticle.setAuthor(null);
            // }


            // 6. Cập nhật trường 'updatedAt' (nếu có trong Article Entity)
            // Đây là best practice để theo dõi lần cuối cùng một bản ghi được chỉnh sửa.
            // Hãy thêm trường 'updatedAt' (LocalDateTime updatedAt;) vào entity Article của bạn
            // existingArticle.setUpdatedAt(LocalDateTime.now()); // Thêm dòng này nếu bạn có trường updatedAt

            // 7. Gọi service để lưu các thay đổi
            ArticleDTO updated = articleService.updateArticleDTO(id, existingArticle); // Truyền entity đã được cập nhật
            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException e) {
            // Xử lý lỗi khi không tìm thấy Article
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            // Xử lý lỗi khi không tìm thấy Author
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Bắt tất cả các ngoại lệ khác để tránh lỗi 500 Internal Server Error và debug
            e.printStackTrace(); // In stack trace ra console để xem lỗi chi tiết
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
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
