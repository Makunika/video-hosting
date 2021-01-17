package com.pshiblo.videohosting.controllers.rest;

import com.pshiblo.videohosting.annotations.IsUser;
import com.pshiblo.videohosting.consts.EndPoints;
import com.pshiblo.videohosting.dto.request.AuthenticationRequest;
import com.pshiblo.videohosting.dto.response.UserOwnerResponse;
import com.pshiblo.videohosting.dto.response.http.ResponseJson;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.repository.TokenRepository;
import com.pshiblo.videohosting.repository.UserRepository;
import com.pshiblo.videohosting.security.jwt.JwtTokenProvider;
import com.pshiblo.videohosting.security.jwt.JwtUser;
import com.pshiblo.videohosting.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

/**
 * @author Максим Пшибло
 */
@RestController
@RequestMapping(EndPoints.API_AUTH)
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthRestController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @PutMapping("/check")
    public ResponseEntity loginToken(final Principal principal) {
        if (principal == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "not auth"));
        }
        return ResponseEntity.ok(Map.of("success", true));
    }



    @PostMapping
    public ResponseEntity login(@RequestBody AuthenticationRequest requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userService.findByName(username);
            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }


            String token = jwtTokenProvider.createToken(username, user.getRoles());

            userService.saveToken(user, token);

            UserOwnerResponse response = UserOwnerResponse.fromUser(user, token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Неверный логин или пароль");
        }
    }

    @IsUser
    @DeleteMapping
    public ResponseEntity logout(
            @AuthenticationPrincipal JwtUser jwtUser,
            HttpServletRequest request) {
        User user = userRepository.findById(jwtUser.getId()).orElse(null);

        if (user == null) {
            return ResponseJson.error().withErrorMessage("Пользователь не найден");
        }

        String token = jwtTokenProvider.resolveToken(request);
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseJson.error().withErrorMessage("Токен неверный");
        }
        userService.logout(user, token);

        return ResponseJson.success().build();
    }
}
