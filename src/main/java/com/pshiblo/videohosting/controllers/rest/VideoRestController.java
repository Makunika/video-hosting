package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.response.VideoResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.models.Video;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.repository.VideoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
        VideoResponse videoDto = VideoResponse.fromVideo(video);
        return ResponseJson.success().withValue(videoDto);
    }

    @GetMapping
    public Page<VideoResponse> getVideo(Pageable pageable) {
        Page<Video> videos = videoRepository.findAll(pageable);
        return videos.map(VideoResponse::fromVideo);
    }

}
