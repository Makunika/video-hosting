package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.annotations.IsUser;
import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.request.EditVideoRequest;
import com.pshiblo.videohosting.dto.response.VideoResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.Mark;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.models.Video;
import com.pshiblo.videohosting.repository.MarkRepository;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.repository.VideoRepository;
import com.pshiblo.videohosting.security.jwt.JwtUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author Максим Пшибло
 */
@RestController
@RequestMapping(EndPoints.API_VIDEO)
public class VideoRestController {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final MarkRepository markRepository;

    public VideoRestController(VideoRepository videoRepository, UserRepository userRepository, MarkRepository markRepository) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.markRepository = markRepository;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getVideo(@PathVariable String id) {
        Video video = videoRepository.findById(UUID.fromString(id)).orElse(null);
        if (video == null) {
            return ResponseJson.error().withErrorMessage("Видео не найдено");
        }
        video.setViews(video.getViews() + 1);
        videoRepository.save(video);


        VideoResponse videoDto = VideoResponse.fromVideo(video);
        return ResponseJson.success().withValue(videoDto);
    }

    @IsUser
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteVideo(@PathVariable String id,
                                         @AuthenticationPrincipal JwtUser jwtUser) throws IOException {
        Video video = videoRepository.findById(UUID.fromString(id)).orElse(null);
        if (video == null) {
            return ResponseJson.error().withErrorMessage("Видео не найдено");
        }

        User user = userRepository.findById(jwtUser.getId()).orElse(null);

        if (video.getUser().getId().equals(user.getId()) || user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            new File(".files/" + video.getVideo()).delete();
            videoRepository.delete(video);
            return ResponseJson.success().build();
        }
        return ResponseJson.error().withErrorMessage("Удаление невозможно");
    }

    @IsUser
    @PutMapping("{id}")
    public ResponseEntity<?> editVideo(@PathVariable String id,
                                         @RequestBody EditVideoRequest request,
                                         @AuthenticationPrincipal JwtUser jwtUser) {
        Video video = videoRepository.findById(UUID.fromString(id)).orElse(null);
        if (video == null) {
            return ResponseJson.error().withErrorMessage("Видео не найдено");
        }

        User user = userRepository.findById(jwtUser.getId()).orElse(null);

        if (video.getUser().getId().equals(user.getId()) || user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            video.setName(request.getName());
            video.setAbout(request.getAbout());
            video.setIsPrivate(request.getIsPrivate());
            videoRepository.save(video);
            return ResponseJson.success().build();
        }
        return ResponseJson.error().withErrorMessage("Изменение невозможно");
    }

    @GetMapping
    public Page<VideoResponse> getVideo(Pageable pageable, @RequestParam(required = false) String name) {
        Page<Video> videos;
        if (name != null) {
            videos = videoRepository.findByIsPrivateAndNameContainsIgnoreCase(false, name, pageable);
        } else {
            videos = videoRepository.findByIsPrivate(false, pageable);
        }
        return videos.map(VideoResponse::fromVideo);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getVideoByUser(@PathVariable Integer id, @AuthenticationPrincipal JwtUser jwtUser) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return ResponseJson.error().withErrorMessage("Такой пользователь не существует");
        List<Video> videos =
                jwtUser != null && jwtUser.getId().equals(user.getId()) ?
                        videoRepository.findByUser(user) :
                        videoRepository.findByUserAndIsPrivate(user, false);
        return ResponseJson.success().withValue(videos.stream().map(video -> VideoResponse.fromVideo(
                video,
                (int)markRepository.countByVideoAndMark(video, Mark.LIKE),
                (int)markRepository.countByVideoAndMark(video, Mark.DISLIKE)
        )));
    }
}
