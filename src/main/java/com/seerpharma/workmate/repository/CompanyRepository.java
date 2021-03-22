package com.seerpharma.workmate.repository;

import com.seerpharma.workmate.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByShortName(String shortName);
}
