package com.pshiblo.videohosting.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pshiblo.videohosting.models.User;
import lombok.*;

/**
 * @author Максим Пшибло
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserOwnerResponse {

    @JsonProperty
    private int id;

    @JsonProperty
    private String username;

    @JsonProperty
    private String img;

    @JsonProperty
    private String token;

    @JsonProperty
    private String email;

    @JsonProperty
    private boolean isAdmin;

    @JsonIgnore
    public static UserOwnerResponse fromUser(User user, String token) {
        return UserOwnerResponse.builder()
                .id(user.getId())
                .img(user.getImg())
                .username(user.getName())
                .token(token)
                .email(user.getEmail())
                .isAdmin(user.getRoles().stream().anyMatch((role -> role.getName().equals("ROLE_ADMIN"))))
                .build();
    }

}
