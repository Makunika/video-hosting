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
public class UserResponse {

    @JsonProperty
    private int id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String img;

    @JsonIgnore
    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .img(user.getImg())
                .name(user.getName())
                .build();
    }

}
