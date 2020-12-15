package com.pshiblo.videohosting.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pshiblo.videohosting.models.BaseEntity;
import com.pshiblo.videohosting.models.User;
import lombok.Data;

/**
 * @author Максим Пшибло
 */
@Data
public class RegisterRequest implements BaseJsonRequest<User> {
    private String username;
    private String email;
    private String password;

    @JsonIgnore
    @Override
    public User toEntity(BaseEntity... entities) {
        return User.builder()
                .email(email)
                .name(username)
                .passwordHash(password)
                .build();
    }
}
