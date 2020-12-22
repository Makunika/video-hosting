package com.pshiblo.videohosting.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Максим Пшибло
 */
@Data
public class ResetGeneratePasswordRequest {
    private String email;
}
