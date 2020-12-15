package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.request.RegisterRequest;
import com.pshiblo.videohosting.dto.response.UserResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.User;
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

    public RegisterRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) {

        User userFromDb = userService.findByName(registerRequest.getUsername());
        if (userFromDb != null) {
            return ResponseJson.error().withErrorMessage("User exist").build();
        }

        User regUser = userService.register(registerRequest.toEntity());
        if (regUser == null) {
            return ResponseJson.error().withErrorMessage("Error register").build();
        }
        return ResponseJson.success().withValue(UserResponse.fromUser(regUser));
    }
}
