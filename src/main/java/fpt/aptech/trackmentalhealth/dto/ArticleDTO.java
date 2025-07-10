package fpt.aptech.trackmentalhealth.dto;

import fpt.aptech.trackmentalhealth.entities.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer author;
    private LocalDateTime createdAt;
    private String status;

    public ArticleDTO() {
    }

    public ArticleDTO(fpt.aptech.trackmentalhealth.entities.Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();

        // Tránh lỗi NullPointerException khi author là null
        this.author = (article.getAuthor() != null) ? article.getAuthor().getId() : null;

        this.createdAt = article.getCreatedAt();
        this.status = article.getStatus();
    }

}
