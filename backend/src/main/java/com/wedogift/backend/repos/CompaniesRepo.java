package com.wedogift.backend.repos;

import com.wedogift.backend.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompaniesRepo extends JpaRepository<CompanyEntity, UUID> {
    Optional<CompanyEntity> findByEmail(String email);
}
