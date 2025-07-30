package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.community.CommunityPostDTO;
import fpt.aptech.trackmentalhealth.entities.*;
import fpt.aptech.trackmentalhealth.service.community.CommunityService;
import fpt.aptech.trackmentalhealth.ultis.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.ObjectOutput;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/community")
public class CommunityController {
    final
    CommunityService communityService;
    CloudinaryService cloudinaryService;

    public CommunityController(CommunityService communityService, CloudinaryService cloudinaryService) {
        this.communityService = communityService;
        this.cloudinaryService = cloudinaryService;
    }

    @GetMapping("/post")
    public ResponseEntity<Page<CommunityPostDTO>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size
                ,Sort.by(Sort.Order.desc("createAt"), Sort.Order.desc("id"))
//                , Sort.by("createAt").descending()
        );
        Page<CommunityPost> posts = communityService.getAllCommunityPosts(pageable);
        Page<CommunityPostDTO> dtoPage = posts.map(CommunityPostDTO::new);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<CommunityPostDTO> getPostsByCommunityId(@PathVariable Integer postId) {
        CommunityPost communityPost = communityService.getCommunityPostsByID(postId);
        CommunityPostDTO dto = new CommunityPostDTO(communityPost);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/post")
    public ResponseEntity<CommunityPost> createPost(
            @RequestParam("content") String content,
            @RequestParam("isAnonymous") boolean isAnonymous,
            @RequestParam("userId") Integer userId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) throws IOException {

        if (images != null && images.size() > 3) {
            return ResponseEntity.badRequest().build();
        }
        // Lấy user
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }
        Users user = new Users();
        user.setId(userId);

        CommunityPost post = new CommunityPost();
        post.setContent(content);
        post.setIsAnonymous(isAnonymous);
        post.setUser(user);
        post.setCreateAt(LocalDate.now());
        post.setStatus("approved");

        Set<CommunityPostMedia> mediaList = new LinkedHashSet<>();
        if (images != null) {
            for (MultipartFile image : images) {
                // Lưu file vào server hoặc cloud nếu muốn
                String filename = cloudinaryService.uploadFile(image); // Viết hàm này để lưu và lấy URL
                CommunityPostMedia media = new CommunityPostMedia();
                media.setMediaUrl(filename); // hoặc URL nếu dùng cloud
                media.setPost(post); // gắn post cha
                mediaList.add(media);
            }
        }
        post.setMediaList(mediaList);
        communityService.saveCommunityPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<CommunityPost> updatePost(
            @PathVariable Integer id,
            @RequestParam("content") String content,
            @RequestParam("isAnonymous") boolean isAnonymous,
            @RequestParam("userId") Integer userId,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "oldUrlsToKeep", required = false) List<String> oldUrlsToKeep
    ) throws IOException {

        CommunityPost existingPost = communityService.getCommunityPostsByID(id);
        if (existingPost == null || !existingPost.getUser().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Set<CommunityPostMedia> currentMedia = existingPost.getMediaList();

        // B1: Xác định ảnh cần xóa
        Set<CommunityPostMedia> mediasToRemove = currentMedia.stream()
                .filter(media -> oldUrlsToKeep == null || !oldUrlsToKeep.contains(media.getMediaUrl()))
                .collect(Collectors.toSet());

        // B2: Xóa ảnh trên Cloudinary và xóa khỏi mediaList
        for (CommunityPostMedia media : mediasToRemove) {
            cloudinaryService.deleteFile(media.getMediaUrl());
            currentMedia.remove(media);
        }

        // B3: Upload ảnh mới nếu có
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String url = cloudinaryService.uploadFile(image);
                CommunityPostMedia newMedia = new CommunityPostMedia();
                newMedia.setMediaUrl(url);
                newMedia.setPost(existingPost);
                currentMedia.add(newMedia);
            }
        }

        // B4: Cập nhật nội dung bài viết
        existingPost.setContent(content);
        existingPost.setIsAnonymous(isAnonymous);
        communityService.saveCommunityPost(existingPost);

        return ResponseEntity.ok(existingPost);
    }


    // Ky thuat xoa mem cho ko xoa cung trong database
    @DeleteMapping("/post/{id}")
    public ResponseEntity<CommunityPost> deletePost(@PathVariable Long id) {
        CommunityPost communityPost = communityService.getCommunityPostsByID(id);
        if (communityPost != null) {
            communityPost.setIsDeleted(true);
            communityPost.setDeletedAt(LocalDate.now());
            communityService.saveCommunityPost(communityPost);
            return ResponseEntity.ok().body(communityPost);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

//    API comment

    @GetMapping("/post/{id}/comment")
    public ResponseEntity<List<PostComment>> getCommentsByPostId(@PathVariable Long id) {
        List<PostComment> list = communityService.getCommentsOfPost(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/post/comment/{id}")
    public ResponseEntity<PostComment> getCommentsById(@PathVariable Long id) {
        PostComment comment = communityService.getCommentsByID(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PostMapping("/post/comment/")
    public ResponseEntity<PostComment> createComment(@RequestBody PostComment comment) {
        communityService.saveComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PutMapping("/post/comment/{commentId}")
    public ResponseEntity<PostComment> updateComment(@PathVariable Long commentId, @RequestBody PostComment comment) {
        communityService.saveComment(comment);
        return ResponseEntity.ok().body(comment);
    }

    @DeleteMapping("/post/comment/{id}")
    public ResponseEntity<PostComment> deleteComment(@PathVariable Long id) {
        PostComment existingComment = communityService.getCommentsByID(id);
        if (existingComment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        communityService.deletePostComment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    //    API reaction
    @GetMapping("/post/{id}/reaction")
    public ResponseEntity<List<PostReaction>> getReactionsByPostId(@PathVariable Long id) {
        List<PostReaction> list = communityService.getReactions(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/post/{id}/reaction")
    public ResponseEntity<PostReaction> createReaction(@PathVariable Long id, @RequestBody PostReaction reaction) {
        CommunityPost communityPost = communityService.getCommunityPostsByID(id);
        if (communityPost != null) {
            communityService.saveReaction(reaction);
            return ResponseEntity.status(HttpStatus.CREATED).body(reaction);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);


    }

    @PutMapping("/reaction/{id}")
    public ResponseEntity<PostReaction> updateReaction(@PathVariable Long id, @RequestBody PostReaction reaction) {
        PostReaction existPostR = communityService.getReactionsByID(id);
        if (existPostR != null && existPostR.getId().equals(reaction.getId())) {
            communityService.saveReaction(reaction);
            return ResponseEntity.ok().body(reaction);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @DeleteMapping("/reaction/{id}")
    public ResponseEntity<PostReaction> deleteReaction(@PathVariable Long id) {
        PostReaction reaction = communityService.getReactionsByID(id);
        if (reaction != null) {
            communityService.deleteReaction(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
