package com.pshiblo.videohosting.service;

import com.pshiblo.videohosting.models.Role;
import com.pshiblo.videohosting.models.Token;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.repository.RoleRepository;
import com.pshiblo.videohosting.repository.TokenRepository;
import com.pshiblo.videohosting.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Максим Пшибло
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setRoles(userRoles);

        User registeredUser = userRepository.save(user);

        return registeredUser;
    }

    public User changePassword(User user, String password, String newPassword, String token) throws Exception {
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new Exception("Неверный пароль");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        clearTokens(user, token);
        return userRepository.save(user);
    }

    public User setNewPassword(User user, String password) {
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setToken(null);
        clearAllTokens(user);
        return userRepository.save(user);
    }

    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        return result;
    }

    public User findByName(String name) {
        User result = userRepository.findByName(name).orElse(null);
        return result;
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public User generateResetToken(String email) throws IOException {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new IOException();
        }
        String token = RandomString.make(35);
        user.setToken(token);
        return userRepository.save(user);
    }

    public void saveToken(User user, String token) {
        tokenRepository.save(
                Token.builder().
                        token(token).
                        user(user).
                        build());
    }

    public void clearTokens(User user, String token) {
        List<Token> tokenForUser = tokenRepository.findByUser(user);
        tokenForUser.forEach(t -> {
            if (!t.getToken().equals(token))
                tokenRepository.delete(t);
        });
    }

    public void clearAllTokens(User user) {
        List<Token> tokenForUser = tokenRepository.findByUser(user);
        tokenForUser.forEach(tokenRepository::delete);
    }

    @Transactional
    public void logout(User user, String token) {
        tokenRepository.deleteTokenByUserAndToken(user, token);
    }
}
