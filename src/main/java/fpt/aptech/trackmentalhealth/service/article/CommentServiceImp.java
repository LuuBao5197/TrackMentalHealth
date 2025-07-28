package fpt.aptech.trackmentalhealth.service.comment;

import fpt.aptech.trackmentalhealth.dto.CommentDTO;
import fpt.aptech.trackmentalhealth.entities.Article;
import fpt.aptech.trackmentalhealth.entities.Comment;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.article.ArticleRepository;
import fpt.aptech.trackmentalhealth.repository.article.CommentRepository;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import fpt.aptech.trackmentalhealth.service.article.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImp implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CommentDTO> getCommentsByArticleId(Integer articleId) {
        return commentRepository.findByArticle_Id(articleId).stream()
                .map(CommentDTO::new).toList();
    }

    @Override
    public CommentDTO createComment(CommentDTO dto) {
        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        Article article = articleRepository.findById(dto.getArticleId())
                .orElseThrow(() -> new RuntimeException("Article not found"));

        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        comment.setArticle(article);
        comment.setUser(user);

        Comment saved = commentRepository.save(comment);
        return new CommentDTO(saved);
    }
}
