package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.request.ResetGeneratePasswordRequest;
import com.pshiblo.videohosting.dto.request.ResetPasswordRequest;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.security.jwt.JwtTokenProvider;
import com.pshiblo.videohosting.service.MailService;
import com.pshiblo.videohosting.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author Максим Пшибло
 */
@RestController
@RequestMapping(EndPoints.API_RESET)
public class ResetPasswordController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    public ResetPasswordController(UserService userService, UserRepository userRepository, MailService mailService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping
    public ResponseEntity createToken(@RequestBody ResetGeneratePasswordRequest request) {
        try {
            User user = userService.generateResetToken(request.getEmail());
            mailService.send(request.getEmail(),
                    "Восстановление пароля",
                    "Ссылка для восстановление пароля: http://localhost:3000/reset?token=" + user.getToken() + "&id=" + user.getId());

            return ResponseJson.success().build();
        } catch (IOException e) {
            return ResponseJson.error().withErrorMessage("Email not found");
        }
    }

    @PutMapping
    public ResponseEntity resetPassword(@RequestBody ResetPasswordRequest request) {
        User user = userRepository.findByIdAndToken(request.getId(), request.getToken()).orElse(null);
        if (user == null) {
            return ResponseJson.error().withErrorMessage("Bad token");
        }
        user = userService.setNewPassword(user, request.getNewPassword());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), request.getNewPassword()));
        jwtTokenProvider.createToken(user.getName(), user.getRoles());
        return ResponseJson.success().build();
    }
}
