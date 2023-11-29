package com.wedogift.backend.services;

import com.wedogift.backend.dtos.*;
import com.wedogift.backend.entities.CompanyEntity;
import com.wedogift.backend.entities.DepositEntity;
import com.wedogift.backend.entities.UserEntity;
import com.wedogift.backend.exceptions.CompanyNonFoundException;
import com.wedogift.backend.exceptions.DuplicateResourceException;
import com.wedogift.backend.exceptions.NotEnoughBalanceException;
import com.wedogift.backend.exceptions.UserNotFoundException;
import com.wedogift.backend.mappers.CompaniesMapper;
import com.wedogift.backend.mappers.UsersMapper;
import com.wedogift.backend.repos.CompaniesRepo;
import com.wedogift.backend.repos.UsersRepo;
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
    private final UsersRepo usersRepo;

    private final CompaniesMapper companiesMapper;

    private final UsersMapper usersMapper;

    private final PasswordEncoder passwordEncoder;

    public static final String NO_COMPANY_WITH_THE_GIVEN_ID_FOUND = "No company with the given id found";
    public static final String NO_USER_WITH_THE_GIVEN_ID_FOUND = "No user with the given id found in the company";

    public CompaniesServiceImpl(CompaniesRepo companiesRepo, UsersRepo usersRepo, CompaniesMapper companiesMapper, UsersMapper usersMapper, PasswordEncoder passwordEncoder) {
        this.companiesRepo = companiesRepo;
        this.usersRepo = usersRepo;
        this.companiesMapper = companiesMapper;
        this.usersMapper = usersMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UUID addCompany(AddCompanyDto addCompanyDto) {
        //validate user input
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
        return this.companiesRepo.findById(companyId).map(companiesMapper::toDto).orElseThrow(() -> new CompanyNonFoundException(NO_COMPANY_WITH_THE_GIVEN_ID_FOUND));
    }

    @Override
    public void addUserToCompany(UUID companyId, AddUserDto addUser) {
        //Todo validate user input
        CompanyEntity company = this.companiesRepo.findById(companyId).orElseThrow(() -> new CompanyNonFoundException(NO_COMPANY_WITH_THE_GIVEN_ID_FOUND));
        UserEntity userEntity = usersMapper.toEntity(addUser);
        userEntity.setCompany(company);
        this.usersRepo.save(userEntity);

    }

    @Override
    public List<DisplayUserDto> getCompanyUsers(UUID companyId) {
        CompanyEntity company = this.companiesRepo.findById(companyId).orElseThrow(() -> new CompanyNonFoundException(NO_COMPANY_WITH_THE_GIVEN_ID_FOUND));
        return this.usersRepo.findByCompany(company).stream().map(usersMapper::toDto).toList();
    }

    @Override
    public DisplayUserDto getCompanyUser(UUID companyId, UUID userId) {
        return this.usersRepo.findById(userId).map(this.usersMapper::toDto).orElseThrow(() -> new UserNotFoundException(NO_USER_WITH_THE_GIVEN_ID_FOUND));
    }


    @Override
    public void depositBalanceToUser(UUID companyId, UUID userId, DepositBalanceDto depositBalanceDto) {
        CompanyEntity company = this.companiesRepo.findById(companyId).orElseThrow(() -> new CompanyNonFoundException(NO_COMPANY_WITH_THE_GIVEN_ID_FOUND));

        UserEntity user = this.usersRepo.findByIdAndCompany(userId, company).orElseThrow(() -> new UserNotFoundException(NO_USER_WITH_THE_GIVEN_ID_FOUND));

        if (company.getBalance() < depositBalanceDto.balance()) {
            throw new NotEnoughBalanceException("Not enough balance for company with ID: " + companyId);
        }
        //Add deposit to user's deposits
        user.addDeposit(
                DepositEntity.builder()
                        .balance(depositBalanceDto.balance())
                        .depositDate(depositBalanceDto.depositDate())
                        .depositType(depositBalanceDto.enumDepositType().name())
                        .build());
        usersRepo.save(user);

        //Update company
        company.setBalance(company.getBalance() - depositBalanceDto.balance());
        companiesRepo.save(company);
    }

    @Override
    public GetBalanceDto getUserBalance(UUID companyId, UUID userId) {

        CompanyEntity company = this.companiesRepo.findById(companyId).orElseThrow(() -> new CompanyNonFoundException(NO_COMPANY_WITH_THE_GIVEN_ID_FOUND));

        UserEntity user = this.usersRepo.findByIdAndCompany(userId, company).orElseThrow(() -> new UserNotFoundException(NO_USER_WITH_THE_GIVEN_ID_FOUND));
        Double userBalance = 0D;

        for (DepositEntity depositEntity : user.getDeposits()) {
            String depositType = depositEntity.getDepositType();
            LocalDate depositDate = depositEntity.getDepositDate();
            LocalDate today = LocalDate.now();
            if (EnumDepositType.GIFTS.name().equals(depositType)) {
                //Gift deposits has 365 days lifespan,
                LocalDate giftExpireDate = depositDate.plusDays(365);
                if (today.isEqual(giftExpireDate) || today.isBefore(giftExpireDate)) {
                    userBalance += depositEntity.getBalance();
                }

            } else if (EnumDepositType.MEALS.name().equals(depositType)) {
                //meal deposits expires at the end of February of the year following the distribution date.
                LocalDate mealExpireDate = depositDate.plusYears(1).withMonth(2);
                if (today.isEqual(mealExpireDate) || today.isBefore(mealExpireDate)) {
                    userBalance += depositEntity.getBalance();
                }
            }
        }
        return GetBalanceDto.builder().balance(userBalance).build();


    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return companiesRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("username " + username + "not found"));
    }
}
