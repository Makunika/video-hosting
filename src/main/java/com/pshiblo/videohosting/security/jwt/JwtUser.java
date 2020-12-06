package com.pshiblo.videohosting.security.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * @author Максим Пшибло
 */
public class JwtUser implements UserDetails {

    private final Integer id;
    private final String name;
    private final String password;
    private final String email;
    private final String img;
    private final Date lastPasswordResetDate;
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUser(Integer id, String name, String password, String email, String img, Date lastPasswordResetDate, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.img = img;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.authorities = authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    public Integer getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }

    public String getImg() {
        return img;
    }

    @JsonIgnore
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }
}
