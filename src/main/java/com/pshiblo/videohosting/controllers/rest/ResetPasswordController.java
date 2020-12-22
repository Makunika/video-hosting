package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.request.ResetGeneratePasswordRequest;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.service.MailService;
import com.pshiblo.videohosting.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @author Максим Пшибло
 */
@RestController
@RequestMapping(EndPoints.API_RESET)
public class ResetPasswordController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final MailService mailService;


    public ResetPasswordController(UserService userService, UserRepository userRepository, MailService mailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }


    @PostMapping
    public ResponseEntity createToken(@RequestBody ResetGeneratePasswordRequest request) {
        try {
            User user = userService.generateResetToken(request.getEmail());
            mailService.send(request.getEmail(),
                    "Восстановление пароля",
                    "Ссылка для восстановление пароля: http://localhost:3000/reset?token=" + user.getToken() + "&id=" + user.getId());

            return ResponseEntity.ok().body(Map.of("success", true));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Email not found"));
        }
    }
}
