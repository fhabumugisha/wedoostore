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
     * Adds a new employee to a company
     *
     * @param companyId   the id of the company
     * @param addEmployee the employee to add
     */
    void addEmployeeToCompany(UUID companyId, AddEmployeeDto addEmployee);

    /**
     * Get all employees of a company
     *
     * @param companyId the id to the company
     * @return list of {@link  DisplayEmployeeDto}
     */

    List<DisplayEmployeeDto> getCompanyEmplyees(UUID companyId);


    /**
     * Get an  employee by id
     *
     * @param companyId  the id to the company
     * @param employeeId the id of the employee to read
     * @return list of {@link  DisplayEmployeeDto}
     */

    DisplayEmployeeDto getCompanyEmployee(UUID companyId, UUID employeeId);

    /**
     * Make a deposit
     *
     * @param companyId         the id to the company  which makes deposit
     * @param employeeId        the id of the employee who receive the deposit
     * @param depositBalanceDto the value of deposit {@link DepositBalanceDto}
     */
    void depositBalanceToEmployee(UUID companyId, UUID employeeId, DepositBalanceDto depositBalanceDto);


    /**
     * Returns the employee balance
     *
     * @param companyId  the id to the company of the employee
     * @param employeeId the id of the employee
     * @return the employee's balance
     */
    GetBalanceDto getEmployeeBalance(UUID companyId, UUID employeeId);

    UserDetails loadUserByUsername(String employeename);


}
