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

    void deleteAllCompanies();

    /**
     * Get company the id
     *
     * @param companyEmail the email of the company to read
     * @return @{@link DisplayCompanyDto}
     */
    DisplayCompanyDto getCompany(String companyEmail);

    /**
     * Get user details
     *
     * @param username the username of logged in user
     * @return USerDetails
     */
    UserDetails loadUserByUsername(String username);

    /**
     * Adds a new employee to a company
     *
     * @param companyEmail the email of the company
     * @param employeeDto  the employee to add
     */
    void addEmployeeToCompany(String companyEmail, AddEmployeeDto employeeDto);

    /**
     * Get all employees of a company
     *
     * @param companyEmail the email to the company
     * @return list of {@link  DisplayEmployeeDto}
     */
    List<DisplayEmployeeDto> getCompanyEmplyees(String companyEmail);

    /**
     * Get an  employee by id
     *
     * @param companyEmail the email to the company
     * @param employeeId   the id of the employee to read
     * @return list of {@link  DisplayEmployeeDto}
     */

    DisplayEmployeeDto getCompanyEmployee(String companyEmail, UUID employeeId);

    /**
     * Make a deposit
     *
     * @param companyEmail      the email to the company  which makes deposit
     * @param employeeId        the id of the employee who receive the deposit
     * @param depositBalanceDto the value of deposit {@link DepositBalanceDto}
     */
    void depositBalanceToEmployee(String companyEmail, UUID employeeId, DepositBalanceDto depositBalanceDto);

    /**
     * Returns the employee balance
     *
     * @param companyEmail the email to the company of the employee
     * @param employeeId   the id of the employee
     * @return the employee's balance
     */
    GetBalanceDto getEmployeeBalance(String companyEmail, UUID employeeId);
}
