package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.response.VideoResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.Role;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.models.Video;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.repository.VideoRepository;
import com.pshiblo.videohosting.security.jwt.JwtUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * @author Максим Пшибло
 */
@RestController
@RequestMapping(EndPoints.API_VIDEO)
public class VideoRestController {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    public VideoRestController(VideoRepository videoRepository, UserRepository userRepository) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getVideo(@PathVariable String id) {
        Video video = videoRepository.findById(UUID.fromString(id)).orElse(null);
        if (video == null) {
            return ResponseJson.error().withErrorMessage("NOT_FOUND_VIDEO");
        }
        video.setViews(video.getViews() + 1);
        videoRepository.save(video);
        VideoResponse videoDto = VideoResponse.fromVideo(video);
        return ResponseJson.success().withValue(videoDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteVideo(@PathVariable String id,
                                         @AuthenticationPrincipal JwtUser jwtUser) {
        Video video = videoRepository.findById(UUID.fromString(id)).orElse(null);
        if (video == null) {
            return ResponseJson.error().withErrorMessage("NOT_FOUND_VIDEO");
        }

        User user = userRepository.findById(jwtUser.getId()).orElse(null);

        if (video.getUser().getId().equals(user.getId()) || user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            videoRepository.delete(video);
            List<Video> videos = videoRepository.findByUser(user);
            return ResponseJson.success().withValue(videos.stream().map(VideoResponse::fromVideo));
        }
        return ResponseJson.error().withErrorMessage("Удаление невозможно");
    }

    @GetMapping
    public Page<VideoResponse> getVideo(Pageable pageable) {
        Page<Video> videos = videoRepository.findAll(pageable);
        return videos.map(VideoResponse::fromVideo);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getVideoBuUser(@PathVariable Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ResponseJson.error().withErrorMessage("User not exist");
        List<Video> videos = videoRepository.findByUser(user);
        return ResponseJson.success().withValue(videos.stream().map(VideoResponse::fromVideo));
    }
}
