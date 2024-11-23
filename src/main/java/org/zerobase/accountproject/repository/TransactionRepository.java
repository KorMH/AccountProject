package org.zerobase.accountproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerobase.accountproject.domain.Transaction;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionId(String transactionId);
}
