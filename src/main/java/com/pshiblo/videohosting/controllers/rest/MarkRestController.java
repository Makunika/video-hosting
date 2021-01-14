package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.annotations.IsUser;
import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.request.CreateMarkRequest;
import com.pshiblo.videohosting.dto.response.MarkResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.Mark;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.models.Video;
import com.pshiblo.videohosting.repository.MarkRepository;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.repository.VideoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Максим Пшибло
 */
@RestController
@RequestMapping(EndPoints.API_MARK)
public class MarkRestController {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final MarkRepository markRepository;

    public MarkRestController(UserRepository userRepository, VideoRepository videoRepository, MarkRepository markRepository) {
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.markRepository = markRepository;
    }

    @IsUser
    @PostMapping
    public ResponseEntity createOrUpdateMark(@RequestBody CreateMarkRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            return ResponseJson.error().withErrorMessage("Такого пользователя не существует");
        }
        Video video = videoRepository.findById(UUID.fromString(request.getVideoId())).orElse(null);
        if (video == null) {
            return ResponseJson.error().withErrorMessage("Такого видео не существует");
        }
        Mark mark = markRepository.findByUserAndVideo(user, video);
        if (mark != null) {
            mark.setMark(request.getMark());
        } else {
            mark = Mark.builder()
                    .mark(request.getMark())
                    .user(user)
                    .video(video)
                    .build();
        }
        mark = markRepository.save(mark);
        return ResponseJson.success().withValue(MarkResponse.builder()
                .dislikes((int)markRepository.countByVideoAndMark(video, Mark.DISLIKE))
                .likes((int)markRepository.countByVideoAndMark(video, Mark.LIKE))
                .markOwner(mark.getMark())
                .videoId(request.getVideoId())
                .build());
    }

    @GetMapping("{videoId}/user/{userId}")
    public ResponseEntity getMark(@PathVariable String videoId, @PathVariable Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseJson.error().withErrorMessage("Такого пользователя не существует");
        }
        Video video = videoRepository.findById(UUID.fromString(videoId)).orElse(null);
        if (video == null) {
            return ResponseJson.error().withErrorMessage("Такого видео не существует");
        }
        Mark mark = markRepository.findByUserAndVideo(user, video);
        return ResponseJson.success().withValue(MarkResponse.builder()
                .dislikes((int)markRepository.countByVideoAndMark(video, Mark.DISLIKE))
                .likes((int)markRepository.countByVideoAndMark(video, Mark.LIKE))
                .markOwner(mark == null ? 0 : mark.getMark())
                .videoId(videoId)
                .build());
    }

    @GetMapping("{videoId}")
    public ResponseEntity getMark(@PathVariable String videoId) {
        Video video = videoRepository.findById(UUID.fromString(videoId)).orElse(null);
        if (video == null) {
            return ResponseJson.error().withErrorMessage("Такого видео не существует");
        }
        return ResponseJson.success().withValue(MarkResponse.builder()
                .dislikes((int)markRepository.countByVideoAndMark(video, Mark.DISLIKE))
                .likes((int)markRepository.countByVideoAndMark(video, Mark.LIKE))
                .markOwner(0)
                .videoId(videoId)
                .build());
    }
}
