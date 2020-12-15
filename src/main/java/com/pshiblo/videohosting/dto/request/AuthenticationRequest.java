package com.pshiblo.videohosting.dto.request;

import lombok.Data;

/**
 * @author Максим Пшибло
 */
@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
