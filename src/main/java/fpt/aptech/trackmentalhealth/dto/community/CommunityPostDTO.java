package fpt.aptech.trackmentalhealth.dto.community;

import fpt.aptech.trackmentalhealth.entities.CommunityPost;
import fpt.aptech.trackmentalhealth.entities.CommunityPostMedia;
import fpt.aptech.trackmentalhealth.entities.PostComment;
import fpt.aptech.trackmentalhealth.entities.PostReaction;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class CommunityPostDTO {
    private Integer id;
    private String content;
    private LocalDate createAt;
    private Boolean isAnonymous;
    private String status;
    private String username; // hoặc userId nếu cần
    private Integer userID;
    private String avatarUser;
    private Set<PostReaction> reactions;
    private Set<PostComment> comments;
    private Set<CommunityPostMedia> mediaList;

    public CommunityPostDTO(CommunityPost post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.createAt = post.getCreateAt();
        this.isAnonymous = post.getIsAnonymous();
        this.status = post.getStatus();
        this.username = post.getUser().getUsername(); // hoặc getUsername() tùy bạn
        this.userID = post.getUser().getId();
        this.avatarUser = post.getUser().getAvatar();
        this.mediaList = post.getMediaList();
        this.reactions = post.getPostReactions();
        this.comments = post.getPostComments();
    }
}
