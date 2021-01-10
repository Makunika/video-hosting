package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.annotations.IsAdmin;
import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.request.CreateCommentRequest;
import com.pshiblo.videohosting.dto.response.CommentResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.Comment;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.models.Video;
import com.pshiblo.videohosting.repository.CommentRepository;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.repository.VideoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Максим Пшибло
 */
@RestController
@RequestMapping(EndPoints.API_COMMENTS)
public class CommentsRestController {

    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    public CommentsRestController(CommentRepository commentRepository, VideoRepository videoRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/video/{id}")
    public ResponseEntity getCommentsByVideo(@PathVariable String id) {
        Video video = videoRepository.findById(UUID.fromString(id)).orElse(null);
        if (video == null) {
            return ResponseJson.error().withErrorMessage("NOT_FOUND_VIDEO");
        }

        List<Comment> comments = commentRepository.findByVideo(video);

        return ResponseJson.success().withValue(comments.stream().map(CommentResponse::fromComment));
    }

    @PostMapping
    public ResponseEntity createCommentFroVideo(@RequestBody CreateCommentRequest request) {
        User user = userRepository.findById(request.getUser_id()).orElse(null);
        if (user == null)
            return ResponseJson.error().withErrorMessage("Пользователь не найден");
        Video video = videoRepository.findById(UUID.fromString(request.getVideo_id())).orElse(null);
        if (video == null)
            return ResponseJson.error().withErrorMessage("Видео не найдено");

        Comment comment = Comment.builder()
                .text(request.getText())
                .user(user)
                .video(video)
                .build();
        return ResponseJson.success().withValue(CommentResponse.fromComment(commentRepository.save(comment)));
    }


    @IsAdmin
    @DeleteMapping("{id}")
    public ResponseEntity deleteComment(@PathVariable("id") Integer id) {
        commentRepository.deleteById(id);
        return ResponseJson.success().build();
    }
}
