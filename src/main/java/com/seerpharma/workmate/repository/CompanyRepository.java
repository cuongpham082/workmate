package com.seerpharma.workmate.repository;

import com.seerpharma.workmate.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByShortName(String shortName);
}
