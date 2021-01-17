package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.annotations.IsUser;
import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.request.ChangePasswordRequest;
import com.pshiblo.videohosting.dto.response.UserResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.security.jwt.JwtTokenProvider;
import com.pshiblo.videohosting.security.jwt.JwtUser;
import com.pshiblo.videohosting.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Максим Пшибло
 */
@RestController
@RequestMapping(EndPoints.API_CHANGE_PASSWORD)
public class ChangePasswordController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public ChangePasswordController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @IsUser
    @PutMapping
    public ResponseEntity changePassword(@AuthenticationPrincipal JwtUser jwtUser,
                                         @RequestBody ChangePasswordRequest request,
                                         HttpServletRequest servletRequest) {
        User user = userService.findById(jwtUser.getId());
        if (user == null) {
            return ResponseJson.error().build();
        }
        try {
            User changedUser = userService.changePassword(
                    user,
                    request.getPassword(),
                    request.getNewPassword(),
                    jwtTokenProvider.resolveToken(servletRequest));
            return ResponseJson.success().withValue(UserResponse.fromUser(changedUser));
        } catch (Exception e) {
            return ResponseJson.error().withErrorMessage(e.getMessage());
        }
    }
}
