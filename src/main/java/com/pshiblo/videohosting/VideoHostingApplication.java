package com.pshiblo.videohosting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class VideoHostingApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoHostingApplication.class, args);
    }

}
