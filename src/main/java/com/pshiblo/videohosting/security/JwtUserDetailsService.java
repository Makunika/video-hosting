package com.pshiblo.videohosting.security;

import com.pshiblo.videohosting.models.Token;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.repository.TokenRepository;
import com.pshiblo.videohosting.security.jwt.JwtUser;
import com.pshiblo.videohosting.security.jwt.JwtUserFactory;
import com.pshiblo.videohosting.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Максим Пшибло
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final TokenRepository tokenRepository;

    public JwtUserDetailsService(UserService userService, TokenRepository tokenRepository) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        JwtUser jwtUser = JwtUserFactory.create(user);
        return jwtUser;
    }

    public boolean checkToken(String token, String username) {
        User user = userService.findByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        Token tokenFromDb = tokenRepository.findByToken(token);


        return tokenFromDb != null && tokenFromDb.getUser().getId().equals(user.getId());
    }

    public void deleteToken(String token) {
        tokenRepository.deleteTokenByToken(token);
    }
}
