package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.request.EditUserRequest;
import com.pshiblo.videohosting.dto.response.UserOwnerResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.security.jwt.JwtTokenProvider;
import com.pshiblo.videohosting.security.jwt.JwtUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Максим Пшибло
 */
@RestController
@RequestMapping(EndPoints.API_USER)
public class EditUserController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public EditUserController(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PutMapping
    public ResponseEntity editUser(@AuthenticationPrincipal JwtUser jwtUser,
                                   @RequestBody EditUserRequest request) {

        User user = userRepository.findById(jwtUser.getId()).orElse(null);
        user.setName(request.getUsername());
        String token = jwtTokenProvider.createToken(request.getUsername(), user.getRoles());

        UserOwnerResponse response = UserOwnerResponse.fromUser(user, token);
        return ResponseJson.success().withValue(response);
    }
}
