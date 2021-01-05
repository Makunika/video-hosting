package com.pshiblo.videohosting.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Максим Пшибло
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class CreateCommentRequest {
    private String text;
    private String video_id;
    private Integer user_id;
}
