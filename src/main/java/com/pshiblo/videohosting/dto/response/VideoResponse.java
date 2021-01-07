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

    @JsonProperty
    private String about;

    @JsonProperty
    private String name;

    @JsonProperty
    private Long createDate;

    @JsonProperty
    private Long views;

    @JsonProperty
    private int likes;

    @JsonProperty
    private boolean isPrivate;

    @JsonProperty
    private int dislikes;

    @JsonProperty("user")
    private UserResponse userResponse;

    @JsonIgnore
    public static VideoResponse fromVideo(Video video) {
        return VideoResponse.builder()
                .id(video.getId().toString())
                .userResponse(UserResponse.fromUser(video.getUser()))
                .video(video.getVideo())
                .about(video.getAbout())
                .name(video.getName())
                .createDate(video.getCreated().getTime())
                .views(video.getViews())
                .dislikes(0)
                .likes(0)
                .isPrivate(video.getIsPrivate())
                .build();
    }

    @JsonIgnore
    public static VideoResponse fromVideo(Video video, int likes, int dislikes) {
        return VideoResponse.builder()
                .id(video.getId().toString())
                .userResponse(UserResponse.fromUser(video.getUser()))
                .video(video.getVideo())
                .about(video.getAbout())
                .name(video.getName())
                .createDate(video.getCreated().getTime())
                .views(video.getViews())
                .dislikes(dislikes)
                .likes(likes)
                .isPrivate(video.getIsPrivate())
                .build();
    }
}
