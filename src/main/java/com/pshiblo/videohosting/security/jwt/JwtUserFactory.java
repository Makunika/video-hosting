package com.pshiblo.videohosting.security.jwt;

import com.pshiblo.videohosting.models.Role;
import com.pshiblo.videohosting.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Максим Пшибло
 */
public class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(user.getId(),
                user.getName(),
                user.getPasswordHash(),
                user.getEmail(),
                user.getImg(),
                user.getUpdated(),
                mapToGrantedAuthorities(new ArrayList<>(user.getRoles())));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
        return userRoles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }
}
