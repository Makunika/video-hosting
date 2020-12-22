package com.pshiblo.videohosting.dto.request;

import lombok.Data;

/**
 * @author Максим Пшибло
 */
@Data
public class ResetPasswordRequest {
    private int id;
    private String token;
    private String newPassword;
}
