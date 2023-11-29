package com.wedogift.backend.config;

import com.wedogift.backend.entities.CompanyEntity;
import com.wedogift.backend.entities.UserEntity;
import com.wedogift.backend.repos.CompaniesRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DbInitializer implements CommandLineRunner {

    private final CompaniesRepo companiesRepo;

    public DbInitializer(CompaniesRepo companiesRepo) {
        this.companiesRepo = companiesRepo;
    }


    @Override
    public void run(String... args) {
        this.companiesRepo.deleteAll();
        saveCompanyWithUser("Tesla", "tesla@wedoostore.com", 100.0, "John");
        saveCompanyWithUser("Addidas", "addidas@wedoostore.com", 150.0, "James");
        saveCompanyWithUser("Nike", "nike@wedoostore.com", 10.0, "Peter");
        companiesRepo.findAll().forEach(t -> log.info("{} Company : {} ", "[APILOG]", t.getName()));
    }

    private void saveCompanyWithUser(String companyName, String email, Double balance, String userName) {
        CompanyEntity company = CompanyEntity.builder()
                .name(companyName)
                .email(email)
                .balance(balance)
                .build();

        UserEntity user = UserEntity.builder()
                .name(userName)
                .build();

        company.addUser(user);

        companiesRepo.save(company);
    }

}
