package com.wedogift.backend.repos;


import com.wedogift.backend.entities.CompanyEntity;
import com.wedogift.backend.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeesRepo extends JpaRepository<EmployeeEntity, UUID> {

    List<EmployeeEntity> findByCompany(CompanyEntity company);

    Optional<EmployeeEntity> findByIdAndCompany(UUID id, CompanyEntity companyEntity);
}
