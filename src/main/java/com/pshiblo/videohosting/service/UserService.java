package com.pshiblo.videohosting.service;

import com.pshiblo.videohosting.models.Role;
import com.pshiblo.videohosting.models.User;
import com.pshiblo.videohosting.repository.RoleRepository;
import com.pshiblo.videohosting.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
}
