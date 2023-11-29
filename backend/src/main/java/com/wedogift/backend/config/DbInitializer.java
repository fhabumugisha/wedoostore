package com.wedogift.backend.config;

import com.wedogift.backend.dtos.AddCompanyDto;
import com.wedogift.backend.services.CompaniesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DbInitializer implements CommandLineRunner {

    private final CompaniesService companiesService;

    public DbInitializer(CompaniesService companiesService) {
        this.companiesService = companiesService;
    }


    @Override
    public void run(String... args) {

        saveCompanyWithUser("Tesla", "tesla@wedoostore.com", 100.0, "John");
        saveCompanyWithUser("Addidas", "addidas@wedoostore.com", 150.0, "James");
        saveCompanyWithUser("Nike", "nike@wedoostore.com", 10.0, "Peter");
        companiesService.getAllCompanies().forEach(t -> log.info("{} Company : {} ", "[APILOG]", t.name()));
    }

    private void saveCompanyWithUser(String companyName, String email, Double balance, String password) {
        companiesService.addCompany(AddCompanyDto.builder().name(companyName)
                .email(email).balance(balance).password(password).build());
    }

}
