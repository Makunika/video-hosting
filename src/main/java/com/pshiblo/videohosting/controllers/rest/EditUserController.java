package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.annotations.IsAdmin;
import com.pshiblo.videohosting.annotations.IsUser;
import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.request.EditUserAdminRequest;
import com.pshiblo.videohosting.dto.request.EditUserRequest;
import com.pshiblo.videohosting.dto.response.UserOwnerResponse;
import com.pshiblo.videohosting.dto.response.UserResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.repository.RoleRepository;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.repository.VideoRepository;
import com.pshiblo.videohosting.security.jwt.JwtTokenProvider;
import com.pshiblo.videohosting.security.jwt.JwtUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;


/**
 * @author Максим Пшибло
 */
@RestController
@RequestMapping(EndPoints.API_USER)
public class EditUserController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;
    private final VideoRepository videoRepository;

    public EditUserController(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, RoleRepository roleRepository, VideoRepository videoRepository) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleRepository = roleRepository;
        this.videoRepository = videoRepository;
    }

    @IsUser
    @PutMapping
    public ResponseEntity editUser(@AuthenticationPrincipal JwtUser jwtUser,
                                   @RequestBody EditUserRequest request,
                                   HttpServletRequest servletRequest) {

        User user = userRepository.findById(jwtUser.getId()).orElse(null);
        user.setName(request.getUsername());
        user.setImg(request.getImg());
        if (userRepository.existsByName(request.getUsername())) {
            return ResponseJson.error().withErrorMessage("Такой пользователь уже существует");
        }
        user = userRepository.save(user);

        UserOwnerResponse response = UserOwnerResponse.fromUser(user, jwtTokenProvider.resolveToken(servletRequest));
        return ResponseJson.success().withValue(response);
    }

    @IsAdmin
    @PutMapping("/admin")
    public ResponseEntity editUserAdmin(@AuthenticationPrincipal JwtUser jwtUser,
                                        @RequestBody EditUserAdminRequest request) {
        User user = userRepository.findById(request.getUserId()).orElse(null);
        if (user == null) {
            return ResponseJson.error().withErrorMessage("Такого пользователя не существует");
        }
        if (request.getIsAdmin()) {
            if (user.getRoles().stream().noneMatch((role -> role.getName().equals("ROLE_ADMIN")))) {
                user.getRoles().add(roleRepository.findByName("ROLE_ADMIN"));
                userRepository.save(user);
            }
        } else {
            user.getRoles().removeIf(role -> role.getName().equals("ROLE_ADMIN"));
            userRepository.save(user);
        }
        return ResponseJson.success().build();
    }

    @IsUser
    @GetMapping("{userId}")
    public ResponseEntity getUser(@PathVariable int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseJson.error().withErrorMessage("Такого пользователя не существует");
        }
        return ResponseJson.success().withValue(UserResponse.fromUser(user));
    }

    @IsAdmin
    @DeleteMapping("{userId}")
    public ResponseEntity deleteUser(@PathVariable int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseJson.error().withErrorMessage("Такого пользователя не существует");
        }
        videoRepository.findByUser(user).forEach(video -> {
            new File(".files/" + video.getVideo()).delete();
            videoRepository.delete(video);
        });

        userRepository.delete(user);
        return ResponseJson.success().withValue(UserResponse.fromUser(user));
    }
}
