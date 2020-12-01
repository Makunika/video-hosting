package com.pshiblo.videohosting.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pshiblo.videohosting.models.Video;
import lombok.Builder;
import lombok.Data;

/**
 * @author Максим Пшибло
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoDto {

    @JsonProperty
    private int id;

    @JsonProperty
    private String video;

    @JsonProperty
    private UserDto userDto;

    @JsonIgnore
    public static VideoDto fromVideo(Video video) {
        return VideoDto.builder()
                .id(video.getId())
                .userDto(UserDto.fromUser(video.getUser()))
                .video(video.getVideo())
                .build();
    }
}
