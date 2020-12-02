package com.pshiblo.videohosting.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pshiblo.videohosting.models.Video;
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
public class VideoResponse {

    @JsonProperty
    private String id;

    @JsonProperty
    private String video;

    @JsonProperty("user")
    private UserResponse userResponse;

    @JsonIgnore
    public static VideoResponse fromVideo(Video video) {
        return VideoResponse.builder()
                .id(video.getId().toString())
                .userResponse(UserResponse.fromUser(video.getUser()))
                .video(video.getVideo())
                .build();
    }
}
