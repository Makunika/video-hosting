package com.pshiblo.videohosting.controllers;

import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.response.VideoResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.models.Video;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.repository.VideoRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author Максим Пшибло
 */
@Controller
@RequestMapping(EndPoints.FILE_VIDEO)
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS}, allowedHeaders = {"Content-Type", "X-Requested-With", "accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
public class VideoFileController {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    public VideoFileController(UserRepository userRepository, VideoRepository videoRepository) {
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
    }


    @GetMapping(value = "{token}", produces = "video/mp4")
    @ResponseBody
    public FileSystemResource getVideoFile(@PathVariable String token) {
        File file = new File(".files/" + token);
        if (!file.exists()) {
            throw new IllegalArgumentException();
        }
        return new FileSystemResource(file);
    }

    @PostMapping
    public @ResponseBody ResponseEntity newFile(@RequestParam("name") String name,
                                                 @RequestParam("about") String about,
                                                 @RequestParam("userId") int id,
                                                 @RequestParam("file") MultipartFile file){
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseJson.error().withErrorMessage("User not exist");
        }

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String strVideoToken = RandomString.make(30);
                File file1 = new File(".files/" + strVideoToken + ".mp4");
                while(file1.exists()) {
                    strVideoToken = RandomString.make(30);
                    file1 = new File(".files/" + strVideoToken + ".mp4");
                }
                file1.createNewFile();

                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(file1));
                stream.write(bytes);
                stream.close();

                Video video = videoRepository.save(
                        Video.builder()
                                .user(user)
                                .video(strVideoToken + ".mp4")
                                .about(about)
                                .isPrivate(false)
                                .name(name)
                                .build()
                );

                return ResponseJson.success().withValue(VideoResponse.fromVideo(video));
            } catch (Exception e) {
                return ResponseJson.error().withErrorMessage(e.getMessage());
            }
        } else {
            return ResponseJson.error().build();
        }
    }
}
