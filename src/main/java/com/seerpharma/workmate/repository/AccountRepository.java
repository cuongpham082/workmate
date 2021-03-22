package com.seerpharma.workmate.repository;

import com.seerpharma.workmate.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM account WHERE id = :id")
    Optional<Account> findById(Long id);
    Optional<Account> findByShortName(String shortName);
}
