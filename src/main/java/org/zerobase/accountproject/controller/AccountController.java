package org.zerobase.accountproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.zerobase.accountproject.dto.CreateAccount;
import org.zerobase.accountproject.service.AccountService;

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
}
