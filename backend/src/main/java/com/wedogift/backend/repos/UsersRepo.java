package com.wedogift.backend.repos;


import com.wedogift.backend.entities.CompanyEntity;
import com.wedogift.backend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepo extends JpaRepository<UserEntity, UUID> {

    List<UserEntity> findByCompany(CompanyEntity company);

    Optional<UserEntity> findByIdAndCompany(UUID id, CompanyEntity companyEntity);
}
