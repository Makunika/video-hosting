package com.pshiblo.videohosting.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pshiblo.videohosting.models.User;
import lombok.Builder;
import lombok.Data;

/**
 * @author Максим Пшибло
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    @JsonProperty
    private int id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String img;


    @JsonIgnore
    public static UserDto fromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .img(user.getImg())
                .name(user.getName())
                .build();
    }

}
