package com.pshiblo.videohosting.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pshiblo.videohosting.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Максим Пшибло
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {

    @JsonProperty
    private Integer id;

    @JsonProperty
    private UserResponse user;

    @JsonProperty
    private String text;

    @JsonProperty
    private Long createDate;

    @JsonIgnore
    public static CommentResponse fromComment(Comment comment) {
        return CommentResponse.builder()
                .text(comment.getText())
                .user(UserResponse.fromUser(comment.getUser()))
                .createDate(comment.getCreated().getTime())
                .id(comment.getId())
                .build();
    }
}
