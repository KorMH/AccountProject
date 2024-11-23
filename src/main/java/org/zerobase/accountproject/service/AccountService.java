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
import org.zerobase.accountproject.type.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static org.zerobase.accountproject.type.AccountStatus.IN_USE;
import static org.zerobase.accountproject.type.AccountStatus.UNREGISTERED;
import static org.zerobase.accountproject.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;


    @Transactional
    public AccountDto createAccount(Long userId, Long initialBalance){
        AccountUser accountUser = getAccountUser(userId);

        validateCreateAccount(accountUser);

        //10자리 랜덤 숫자를 이용한 계좌 생성
//        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
//                .map(account -> (Integer.parseInt(account.getAccountNumber())) + 1 + "")
//                .orElse("1000000000");
        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
                .map(account -> {
                    // 기존 로직: 기존 계좌 번호 + 1
                    String existingAccountNumber = account.getAccountNumber();
                    return String.valueOf((Integer.parseInt(existingAccountNumber)) + 1);
                })
                .orElseGet(() -> {
                    // 10자리 랜덤 계좌 번호 생성
                    Random random = new Random();
                    long randomAccountNumber = 1000000000L + (long) (random.nextDouble() * 9000000000L);
                    return String.valueOf(randomAccountNumber);
                });

        return AccountDto.fromEntity(accountRepository.save(Account.builder()
                .accountUser(accountUser)
                .accountStatus(IN_USE)
                .accountNumber(newAccountNumber)
                .balance(initialBalance)
                .registeredAt(LocalDateTime.now())
                .build()

        ));

    }


    /**
     * 한사람의 최대 계좌 생성 개수는 10개
     */
    private void validateCreateAccount(AccountUser accountUser){
        if(accountRepository.countByAccountUser(accountUser) >= 10){
            throw new AccountException(ErrorCode.MAX_ACCOUNT_PER_USER_10);
        }
    }



    @Transactional
    public Account getAccount(Long id){
        if(id < 0){
            throw new RuntimeException("Minus");
        }

        return accountRepository.findById(id).get();
    }


    public AccountDto deleteAccount(Long userId, String accountNumber){
        AccountUser accountUser = getAccountUser(userId);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));

        validateDeleteAccount(accountUser, account);

        account.setAccountStatus(UNREGISTERED);
        account.setUnRegisteredAt(LocalDateTime.now());

        accountRepository.save(account);

        return AccountDto.fromEntity(account);
    }

    private void validateDeleteAccount(AccountUser accountUser, Account account) {
        if (!Objects.equals(accountUser.getId(), account.getAccountUser().getId())) {
            throw new AccountException(USER_ACCOUNT_UN_MATCH);
        }
        if (account.getAccountStatus() == UNREGISTERED) {
            throw new AccountException(ACCOUNT_ALREADY_UNREGISTERED);
        }
        if (account.getBalance() > 0) {
            throw new AccountException(BALANCE_NOT_EMPTY);
        }
    }


    @Transactional
    public List<AccountDto> getAccountsByUserId(Long userId) {
        AccountUser accountUser = getAccountUser(userId);

        List<Account> accounts = accountRepository
                .findByAccountUser(accountUser);

        return accounts.stream()
                .map(AccountDto::fromEntity)
                .collect(Collectors.toList());
    }


    private AccountUser getAccountUser(Long userId){
        return accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(USER_NOT_FOUND));
    }
}
