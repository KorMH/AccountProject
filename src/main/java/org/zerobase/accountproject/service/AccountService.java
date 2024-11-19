package org.zerobase.accountproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerobase.accountproject.domain.Account;
import org.zerobase.accountproject.domain.AccountUser;
import org.zerobase.accountproject.dto.AccountDto;
import org.zerobase.accountproject.exception.AccountException;
import org.zerobase.accountproject.repository.AccountRepository;
import org.zerobase.accountproject.repository.AccountUserRepository;
import org.zerobase.accountproject.type.AccountStatus;
import org.zerobase.accountproject.type.ErrorCode;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;


    @Transactional
    public AccountDto createAccount(Long userId, Long initialBalance){
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));

        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
                .map(account -> (Integer.parseInt(account.getAccountNumber())) + 1 + "")
                .orElse("1000000000");

        return AccountDto.fromEntity(accountRepository.save(Account.builder()
                .accountUser(accountUser)
                .accountStatus(AccountStatus.IN_USE)
                .accountNumber(newAccountNumber)
                .balance(initialBalance)
                .registeredAt(LocalDateTime.now())
                .build()

        ));

    }
}
