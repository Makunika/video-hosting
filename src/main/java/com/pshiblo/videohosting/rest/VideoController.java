package com.pshiblo.videohosting.rest;

import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.UserDto;
import com.pshiblo.videohosting.dto.VideoDto;
import com.pshiblo.videohosting.models.Video;
import com.pshiblo.videohosting.repository.VideoRepository;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.VolatileImage;
import java.io.File;

/**
 * @author Максим Пшибло
 */
@RestController
@RequestMapping(EndPoints.API_VIDEO)
public class VideoController {

    private final VideoRepository videoRepository;

    public VideoController(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getVideo(@PathVariable String id) {
        Video video = videoRepository.findByVideo(id);
        if (video == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        VideoDto videoDto = VideoDto.fromVideo(video);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(value = "file/{id}", produces = "video/mp4")
    @ResponseBody
    public ResponseEntity<FileSystemResource> getVideoFile(@PathVariable String id) {

        Video video = videoRepository.findByVideo(id);
        if (video == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        File file = new File(".files/" + video.getVideo());
        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new FileSystemResource(file));
    }

}
