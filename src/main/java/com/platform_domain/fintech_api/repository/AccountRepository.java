package com.platform_domain.fintech_api.repository;

import com.platform_domain.fintech_api.entity.Account;
import com.platform_domain.fintech_api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);
}
