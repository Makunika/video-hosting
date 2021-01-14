package com.pshiblo.videohosting.controllers;

import com.pshiblo.videohosting.annotations.IsUser;
import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.response.VideoResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.models.Video;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.repository.VideoRepository;
import com.pshiblo.videohosting.security.jwt.JwtUser;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

/**
 * @author Максим Пшибло
 */
@Controller
@RequestMapping(EndPoints.FILE_VIDEO)
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
    public @ResponseBody ResponseEntity newVideoFile(HttpServletRequest request){
        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                return ResponseJson.error().build();
            }

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            InputStream stream = multipartRequest.getFile("file").getInputStream();
            String strVideoToken = RandomString.make(30);
            File file = new File(".files/" + strVideoToken + ".mp4");
            while(file.exists()) {
                strVideoToken = RandomString.make(30);
                file = new File(".files/" + strVideoToken + ".mp4");
            }
            file.createNewFile();
            OutputStream out = new FileOutputStream(file);
            IOUtils.copy(stream, out);
            stream.close();
            out.close();

            User user = userRepository.findById(ServletRequestUtils.getRequiredIntParameter(request, "userId")).orElse(null);
            if (user == null) {
                return ResponseJson.error().withErrorMessage("User not exist");
            }
            Video video = videoRepository.save(
                    Video.builder()
                            .name(ServletRequestUtils.getRequiredStringParameter(request, "name"))
                            .isPrivate(ServletRequestUtils.getRequiredBooleanParameter(request, "isPrivate"))
                            .about(ServletRequestUtils.getRequiredStringParameter(request, "about"))
                            .views(0L)
                            .video(strVideoToken + ".mp4")
                            .user(user)
                            .build()
            );
            return ResponseJson.success().withValue(VideoResponse.fromVideo(video));
        } catch (IOException | ServletRequestBindingException e) {
            return ResponseJson.error().withErrorMessage("При загрузке файла появилась ошибка:" + e.getMessage());
        }
    }
}
