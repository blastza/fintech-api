package com.platform_domain.fintech_api.service;

import com.platform_domain.fintech_api.entity.Account;
import com.platform_domain.fintech_api.entity.Transaction;
import com.platform_domain.fintech_api.entity.User;
import com.platform_domain.fintech_api.event.TransactionEvent;
import com.platform_domain.fintech_api.exception.InsufficientFundsException;
import com.platform_domain.fintech_api.exception.ResourceNotFoundException;
import com.platform_domain.fintech_api.kafka.TransactionEventProducer;
import com.platform_domain.fintech_api.repository.AccountRepository;
import com.platform_domain.fintech_api.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionEventProducer eventProducer;

    public Account createAccount(User user, Account.AccountType type) {
        Account account = new Account();
        account.setUser(user);
        account.setAccountType(type);
        account.setAccountNumber("FT" + UUID.randomUUID().toString()
                .replaceAll("[^0-9]", "").substring(0, 10));
        return accountRepository.save(account);
    }

    public List<Account> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + id));
    }

    @Transactional
    public Transaction deposit(Long accountId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        Account account = getAccountById(accountId);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        return saveTransaction(account, null, amount, Transaction.TransactionType.DEPOSIT, description);
    }

    @Transactional
    public Transaction withdraw(Long accountId, BigDecimal amount, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be positive");

        Account account = getAccountById(accountId);

        if (account.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException("Insufficient funds. Balance: " + account.getBalance());

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        return saveTransaction(account, null, amount, Transaction.TransactionType.WITHDRAWAL, description);
    }

    @Transactional
    public List<Transaction> transfer(Long fromId, Long toId, BigDecimal amount,String description) {
        if (fromId.equals(toId))
            throw new IllegalArgumentException("Cannot transfer to the same account");

        Account from = getAccountById(fromId);
        Account to = getAccountById(toId);

        if (from.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException("Insufficient funds for transfer");

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        accountRepository.save(from);
        accountRepository.save(to);

        Transaction outTx = saveTransaction(from, to, amount, Transaction.TransactionType.TRANSFER_OUT, description);
        Transaction inTx = saveTransaction(to, from, amount, Transaction.TransactionType.TRANSFER_IN, description);

        return List.of(outTx, inTx);
    }

    private Transaction saveTransaction(Account account, Account counterpart,
                                        BigDecimal amount, Transaction.TransactionType type,
                                        String description) {
        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setCounterpartAccount(counterpart);
        tx.setAmount(amount);
        tx.setType(type);
        tx.setDescription(description);
        Transaction saved = transactionRepository.save(tx);
        publishEvent(saved, account.getUser().getEmail());
        return saved;
    }

    private void publishEvent(Transaction tx, String userEmail) {
        TransactionEvent event = new TransactionEvent(
                tx.getId(),
                tx.getAccount().getId(),
                tx.getCounterpartAccount() != null ? tx.getCounterpartAccount().getId() : null,
                tx.getType().name(),
                tx.getAmount(),
                tx.getDescription(),
                userEmail,
                tx.getCreatedAt()
        );
        eventProducer.publishTransactionEvent(event);
    }
}
