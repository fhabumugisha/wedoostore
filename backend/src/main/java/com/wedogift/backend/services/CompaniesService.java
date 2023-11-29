package com.wedogift.backend.services;

import com.wedogift.backend.dtos.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface CompaniesService extends UserDetailsService {


    /**
     * Adds a new company
     *
     * @param addCompanyDto the company to add
     * @return the id of the created company
     */
    UUID addCompany(AddCompanyDto addCompanyDto);

    /**
     * Get all companies
     *
     * @return @{@link DisplayCompanyDto}
     */
    List<DisplayCompanyDto> getAllCompanies();


    /**
     * Get company the id
     *
     * @param companyId the id of the company to read
     * @return @{@link DisplayCompanyDto}
     */
    DisplayCompanyDto getCompany(UUID companyId);

    /**
     * Adds a new user to a company
     *
     * @param companyId the id of the company
     * @param addUser   the user to add
     */
    void addUserToCompany(UUID companyId, AddUserDto addUser);

    /**
     * Get all users of a company
     *
     * @param companyId the id to the company
     * @return list of {@link  DisplayUserDto}
     */

    List<DisplayUserDto> getCompanyUsers(UUID companyId);


    /**
     * Get a  user by id
     *
     * @param companyId the id to the company
     * @param userId    the id of the user to read
     * @return list of {@link  DisplayUserDto}
     */

    DisplayUserDto getCompanyUser(UUID companyId, UUID userId);

    /**
     * Make a deposit
     *
     * @param companyId         the id to the company  which makes deposit
     * @param userId            the id of the user who receive the deposit
     * @param depositBalanceDto the value of deposit {@link DepositBalanceDto}
     */
    void depositBalanceToUser(UUID companyId, UUID userId, DepositBalanceDto depositBalanceDto);


    /**
     * Returns the user balance
     *
     * @param companyId the id to the company of the user
     * @param userId    the id of the user
     * @return the user's balance
     */
    GetBalanceDto getUserBalance(UUID companyId, UUID userId);

    UserDetails loadUserByUsername(String username);


}
