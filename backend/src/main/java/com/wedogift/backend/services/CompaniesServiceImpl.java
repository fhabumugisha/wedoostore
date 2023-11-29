package com.wedogift.backend.services;

import com.wedogift.backend.dtos.*;
import com.wedogift.backend.entities.CompanyEntity;
import com.wedogift.backend.entities.DepositEntity;
import com.wedogift.backend.entities.EmployeeEntity;
import com.wedogift.backend.exceptions.DuplicateResourceException;
import com.wedogift.backend.exceptions.NotEnoughBalanceException;
import com.wedogift.backend.exceptions.ResourceNotFoundException;
import com.wedogift.backend.mappers.CompaniesMapper;
import com.wedogift.backend.mappers.EmployeesMapper;
import com.wedogift.backend.repos.CompaniesRepo;
import com.wedogift.backend.repos.EmployeesRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CompaniesServiceImpl implements CompaniesService {

    private final CompaniesRepo companiesRepo;
    private final EmployeesRepo employeesRepo;

    private final CompaniesMapper companiesMapper;

    private final EmployeesMapper employeesMapper;

    private final PasswordEncoder passwordEncoder;

    public static final String NO_COMPANY_WITH_THE_GIVEN_ID_FOUND = "No company with the given id found";
    public static final String NO_USER_WITH_THE_GIVEN_ID_FOUND = "No employee with the given id found in the company";

    public CompaniesServiceImpl(CompaniesRepo companiesRepo, EmployeesRepo employeesRepo, CompaniesMapper companiesMapper, EmployeesMapper employeesMapper, PasswordEncoder passwordEncoder) {
        this.companiesRepo = companiesRepo;
        this.employeesRepo = employeesRepo;
        this.companiesMapper = companiesMapper;
        this.employeesMapper = employeesMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UUID addCompany(AddCompanyDto addCompanyDto) {
        //validate employee input
        companiesRepo.findByEmail(addCompanyDto.email())
                .ifPresent(c -> {
                    throw new DuplicateResourceException("Email already taken");
                });
        CompanyEntity company = companiesMapper.toEntity(addCompanyDto);
        company.setPassword(passwordEncoder.encode(company.getPassword()));
        return companiesRepo.save(company).getId();
    }

    @Override
    public List<DisplayCompanyDto> getAllCompanies() {
        return this.companiesRepo.findAll().stream().map(this.companiesMapper::toDto).toList();
    }

    @Override
    public DisplayCompanyDto getCompany(UUID companyId) {
        return this.companiesRepo.findById(companyId).map(companiesMapper::toDto).orElseThrow(() -> new ResourceNotFoundException(NO_COMPANY_WITH_THE_GIVEN_ID_FOUND));
    }

    @Override
    public void addEmployeeToCompany(UUID companyId, AddEmployeeDto addEmployee) {
        //validate employee input
        CompanyEntity company = this.companiesRepo.findById(companyId).orElseThrow(() -> new ResourceNotFoundException(NO_COMPANY_WITH_THE_GIVEN_ID_FOUND));
        EmployeeEntity employeeEntity = employeesMapper.toEntity(addEmployee);
        employeeEntity.setCompany(company);
        this.employeesRepo.save(employeeEntity);

    }

    @Override
    public List<DisplayEmployeeDto> getCompanyEmplyees(UUID companyId) {
        CompanyEntity company = this.companiesRepo.findById(companyId).orElseThrow(() -> new ResourceNotFoundException(NO_COMPANY_WITH_THE_GIVEN_ID_FOUND));
        return this.employeesRepo.findByCompany(company).stream().map(employeesMapper::toDto).toList();
    }

    @Override
    public DisplayEmployeeDto getCompanyEmployee(UUID companyId, UUID employeeId) {
        return this.employeesRepo.findById(employeeId).map(this.employeesMapper::toDto).orElseThrow(() -> new ResourceNotFoundException(NO_USER_WITH_THE_GIVEN_ID_FOUND));
    }


    @Override
    public void depositBalanceToEmployee(UUID companyId, UUID employeeId, DepositBalanceDto depositBalanceDto) {
        CompanyEntity company = this.companiesRepo.findById(companyId).orElseThrow(() -> new ResourceNotFoundException(NO_COMPANY_WITH_THE_GIVEN_ID_FOUND));

        EmployeeEntity employee = this.employeesRepo.findByIdAndCompany(employeeId, company).orElseThrow(() -> new ResourceNotFoundException(NO_USER_WITH_THE_GIVEN_ID_FOUND));

        if (company.getBalance() < depositBalanceDto.balance()) {
            throw new NotEnoughBalanceException("Not enough balance for company with ID: " + companyId);
        }
        //Add deposit to employee's deposits
        employee.addDeposit(
                DepositEntity.builder()
                        .balance(depositBalanceDto.balance())
                        .depositDate(depositBalanceDto.depositDate())
                        .depositType(depositBalanceDto.enumDepositType().name())
                        .build());
        employeesRepo.save(employee);

        //Update company
        company.setBalance(company.getBalance() - depositBalanceDto.balance());
        companiesRepo.save(company);
    }

    @Override
    public GetBalanceDto getEmployeeBalance(UUID companyId, UUID employeeId) {

        CompanyEntity company = this.companiesRepo.findById(companyId).orElseThrow(() -> new ResourceNotFoundException(NO_COMPANY_WITH_THE_GIVEN_ID_FOUND));

        EmployeeEntity employee = this.employeesRepo.findByIdAndCompany(employeeId, company).orElseThrow(() -> new ResourceNotFoundException(NO_USER_WITH_THE_GIVEN_ID_FOUND));
        Double employeeBalance = 0D;

        for (DepositEntity depositEntity : employee.getDeposits()) {
            String depositType = depositEntity.getDepositType();
            LocalDate depositDate = depositEntity.getDepositDate();
            LocalDate today = LocalDate.now();
            if (EnumDepositType.GIFTS.name().equals(depositType)) {
                //Gift deposits has 365 days lifespan,
                LocalDate giftExpireDate = depositDate.plusDays(365);
                if (today.isEqual(giftExpireDate) || today.isBefore(giftExpireDate)) {
                    employeeBalance += depositEntity.getBalance();
                }

            } else if (EnumDepositType.MEALS.name().equals(depositType)) {
                //meal deposits expires at the end of February of the year following the distribution date.
                LocalDate mealExpireDate = depositDate.plusYears(1).withMonth(2);
                if (today.isEqual(mealExpireDate) || today.isBefore(mealExpireDate)) {
                    employeeBalance += depositEntity.getBalance();
                }
            }
        }
        return GetBalanceDto.builder().balance(employeeBalance).build();


    }

    @Override
    public UserDetails loadUserByUsername(String employeename) throws UsernameNotFoundException {
        return companiesRepo.findByEmail(employeename).orElseThrow(() -> new UsernameNotFoundException("employeename " + employeename + "not found"));
    }
}
