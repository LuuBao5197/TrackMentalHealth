package fpt.aptech.trackmentalhealth.service.article;

import fpt.aptech.trackmentalhealth.dto.CommentDTO;
import java.util.List;

public interface CommentService {
    List<CommentDTO> getCommentsByArticleId(Integer articleId);
    CommentDTO createComment(CommentDTO dto);
}
