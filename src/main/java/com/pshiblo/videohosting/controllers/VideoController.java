package com.pshiblo.videohosting.controllers;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * @author Максим Пшибло
 */
@Controller
@RequestMapping("/videos")
public class VideoController {

    @GetMapping(produces = "video/mp4")
    @ResponseBody
    public FileSystemResource getVideo(@RequestParam Long id) {
        File file = new File(".files/video.mp4");
        if (file.exists()) {
            System.out.println("alo");
        }
        return new FileSystemResource(file);
    }

}
