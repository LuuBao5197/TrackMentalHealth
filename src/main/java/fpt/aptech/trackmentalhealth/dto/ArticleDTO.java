package fpt.aptech.trackmentalhealth.dto;

import fpt.aptech.trackmentalhealth.entities.Users; // Cần thiết nếu dùng trong constructor
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer author; // ID của tác giả
    private LocalDateTime createdAt;
    private String status; // Giữ nguyên là String để khớp với frontend gửi lên "true"/"false"

    public ArticleDTO() {
    }

    public ArticleDTO(fpt.aptech.trackmentalhealth.entities.Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();

        // Tránh lỗi NullPointerException khi author là null
        this.author = (article.getAuthor() != null) ? article.getAuthor().getId() : null;

        this.createdAt = article.getCreatedAt();
        this.status = article.getStatus(); // Giả định status trong entity cũng là String
    }
}