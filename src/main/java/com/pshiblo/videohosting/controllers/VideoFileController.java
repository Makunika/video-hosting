package com.pshiblo.videohosting.controllers;

import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.models.Video;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

/**
 * @author Максим Пшибло
 */
@Controller
@RequestMapping(EndPoints.FILE_VIDEO)
public class VideoFileController {

    @GetMapping(value = "{token}", produces = "video/mp4")
    @ResponseBody
    public FileSystemResource getVideoFile(@PathVariable String token) {
        File file = new File(".files/" + token);
        if (!file.exists()) {
            throw new IllegalArgumentException();
        }
        return new FileSystemResource(file);
    }
}
