package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.annotations.IsAdmin;
import com.pshiblo.videohosting.annotations.IsUser;
import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.request.EditUserAdminRequest;
import com.pshiblo.videohosting.dto.request.EditUserRequest;
import com.pshiblo.videohosting.dto.response.UserOwnerResponse;
import com.pshiblo.videohosting.dto.response.UserResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.Role;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.repository.RoleRepository;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.security.jwt.JwtTokenProvider;
import com.pshiblo.videohosting.security.jwt.JwtUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


/**
 * @author Максим Пшибло
 */
@RestController
@RequestMapping(EndPoints.API_USER)
public class EditUserController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoleRepository roleRepository;

    public EditUserController(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.roleRepository = roleRepository;
    }

    @IsUser
    @PutMapping
    public ResponseEntity editUser(@AuthenticationPrincipal JwtUser jwtUser,
                                   @RequestBody EditUserRequest request) {

        User user = userRepository.findById(jwtUser.getId()).orElse(null);
        user.setName(request.getUsername());
        String token = jwtTokenProvider.createToken(request.getUsername(), user.getRoles());

        UserOwnerResponse response = UserOwnerResponse.fromUser(user, token);
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
        if (request.getIsAdmin().booleanValue()) {
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
        userRepository.delete(user);
        return ResponseJson.success().withValue(UserResponse.fromUser(user));
    }
}
