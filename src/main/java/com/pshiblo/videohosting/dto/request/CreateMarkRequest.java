package com.pshiblo.videohosting.dto.request;

import lombok.Data;

/**
 * @author Максим Пшибло
 */
@Data
public class CreateMarkRequest {
    private String videoId;
    private int userId;
    private int mark;
}
