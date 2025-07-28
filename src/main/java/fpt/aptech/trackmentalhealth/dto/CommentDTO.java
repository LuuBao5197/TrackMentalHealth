package fpt.aptech.trackmentalhealth.dto;

import fpt.aptech.trackmentalhealth.entities.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {
    private Integer id;
    private String content;
    private Integer articleId;
    private Integer userId;
    private LocalDateTime createdAt;

    public CommentDTO() {}

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.articleId = comment.getArticle().getId();
        this.userId = comment.getUser().getId();
        this.createdAt = comment.getCreatedAt();
    }
}
