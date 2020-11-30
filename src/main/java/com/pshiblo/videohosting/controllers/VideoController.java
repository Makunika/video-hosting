package com.pshiblo.videohosting.controllers;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Максим Пшибло
 */
@Controller
public class VideoController {



    public FileSystemResource getVideo(@RequestParam Long id) {

    }

}
