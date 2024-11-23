package org.zerobase.accountproject.dto;

import lombok.*;
import org.zerobase.accountproject.aop.AccountLockIdInterface;
import org.zerobase.accountproject.type.TransactionResultType;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class CancelBalance {


    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request implements AccountLockIdInterface {
        @NotBlank
        private String transactionId;

        @NotBlank
        @Size(min = 10, max = 10)
        private String accountNumber;

        @NotNull
        @Min(10)
        @Max(1000_000_000)
        private Long amount;
    }



    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String accountNumber;
        private TransactionResultType transactionResult;
        private String transactionId;
        private Long amount;
        private LocalDateTime transactedAt;

    }
}
