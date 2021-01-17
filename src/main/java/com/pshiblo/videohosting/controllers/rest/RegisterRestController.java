package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.request.RegisterRequest;
import com.pshiblo.videohosting.dto.response.UserResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Максим Пшибло
 */
@RestController
@RequestMapping(EndPoints.API_REG)
public class RegisterRestController {

    private final UserService userService;
    private final UserRepository userRepository;

    public RegisterRestController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) {

        User userFromDbName = userService.findByName(registerRequest.getUsername());
        User userFromDbEmail = userRepository.findByEmail(registerRequest.getEmail()).orElse(null);
        if (userFromDbName != null || userFromDbEmail != null) {
            return ResponseJson.error().withErrorMessage("Такой пользователь уже существует");
        }

        User regUser = userService.register(registerRequest.toUser());
        if (regUser == null) {
            return ResponseJson.error().withErrorMessage("Error register");
        }
        return ResponseJson.success().withValue(UserResponse.fromUser(regUser));
    }
}
