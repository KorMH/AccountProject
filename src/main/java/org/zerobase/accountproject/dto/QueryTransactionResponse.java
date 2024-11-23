package org.zerobase.accountproject.dto;


import lombok.*;
import org.zerobase.accountproject.domain.Transaction;
import org.zerobase.accountproject.type.TransactionResultType;
import org.zerobase.accountproject.type.TransactionType;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryTransactionResponse {
    private String accountNumber;
    private TransactionType transactionType;
    private TransactionResultType transactionResultType;
    private String transactionId;
    private Long amount;
    private LocalDateTime transactedAt;

//    public static QueryTransactionResponse from(Transaction transaction){
//        return QueryTransactionResponse.builder()
//                .accountNumber()
//    }
}
