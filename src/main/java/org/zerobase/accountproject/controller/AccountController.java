package org.zerobase.accountproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zerobase.accountproject.domain.Account;
import org.zerobase.accountproject.dto.AccountInfo;
import org.zerobase.accountproject.dto.CreateAccount;
import org.zerobase.accountproject.dto.DeleteAccount;
import org.zerobase.accountproject.service.AccountService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @PostMapping("/account")
    public CreateAccount.Response createAccount(
            @RequestBody CreateAccount.Request request
    ) {
        return CreateAccount.Response.fromDto(
                accountService.createAccount(
                        request.getId(),
                        request.getInitialBalance()
                )
        );
    }


    @DeleteMapping("/account")
    public DeleteAccount.Response deleteAccount(
            @RequestBody @Valid DeleteAccount.Request request
    ) {
        return DeleteAccount.Response.from(
                accountService.deleteAccount(
                        request.getUserId(),
                        request.getAccountNumber()
                )
        );
    }

    @GetMapping("/account")
    public List<AccountInfo> getAccountsByUserId(
            @RequestParam("user_id") Long userId
    ) {
        return accountService.getAccountsByUserId(userId)
                .stream().map(accountDto ->
                        AccountInfo.builder()
                                .accountNumber(accountDto.getAccountNumber())
                                .balance(accountDto.getBalance())
                                .build())
                .collect(Collectors.toList());
    }

    @GetMapping("/account/{id}")
    public Account getAccount(
            @PathVariable Long id) {
        return accountService.getAccount(id);
    }
}
