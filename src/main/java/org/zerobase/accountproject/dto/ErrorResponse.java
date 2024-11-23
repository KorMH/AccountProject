package org.zerobase.accountproject.dto;

import lombok.*;
import org.zerobase.accountproject.type.ErrorCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
}