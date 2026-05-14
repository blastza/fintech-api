package com.platform_domain.fintech_api.controller;

import com.platform_domain.fintech_api.entity.Account;
import com.platform_domain.fintech_api.entity.User;
import com.platform_domain.fintech_api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<?> createAccount(
            @AuthenticationPrincipal User currentUser,
            @RequestBody Map<String, String> body) {
        Account.AccountType type = Account.AccountType.valueOf(body.get("accountType"));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.createAccount(currentUser, type));
    }

    @GetMapping
    public ResponseEntity<?> getMyAccounts(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(accountService.getUserAccounts(currentUser.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long id,
                                     @RequestBody Map<String, Object> body) {
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        return ResponseEntity.ok(accountService.deposit(id, amount, (String) body.get("description")));
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable Long id,
                                      @RequestBody Map<String, Object> body) {
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        return ResponseEntity.ok(accountService.withdraw(id, amount, (String) body.get("description")));
    }

    @PostMapping("/{id}/transfer")
    public ResponseEntity<?> transfer(@PathVariable Long id,
                                      @RequestBody Map<String, Object> body) {
        Long toId = Long.valueOf(body.get("toAccountId").toString());
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        accountService.transfer(id, toId, amount, (String) body.get("description"));
        return ResponseEntity.ok(Map.of("message", "Transfer successful"));
    }
}
