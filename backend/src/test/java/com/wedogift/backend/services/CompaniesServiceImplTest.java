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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompaniesServiceImplTest {

    @Mock
    private CompaniesMapper companiesMapper;
    @Mock
    private EmployeesMapper employeesMapper;
    @Mock
    private EmployeesRepo employeesRepo;

    @Mock
    private CompaniesRepo companiesRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private CompaniesServiceImpl companiesService;


    @Test
    void addCompany() {
        // Given
        AddCompanyDto addCompanyDto = AddCompanyDto.builder().name("Glady").build();

        // Mock the behavior of companiesRepo.save
        when(companiesRepo.save(ArgumentMatchers.any(CompanyEntity.class))).thenReturn(CompanyEntity.builder().id(UUID.randomUUID()).build());

        // Mock the behavior of companiesMapper.toEntity
        when(companiesMapper.toEntity(ArgumentMatchers.any(AddCompanyDto.class))).thenReturn(new CompanyEntity());

        // Call the method to test
        UUID companyId = companiesService.addCompany(addCompanyDto);

        // Verify that companiesRepo.save was called
        verify(companiesRepo, times(1)).save(ArgumentMatchers.any(CompanyEntity.class));

        assertNotNull(companyId);
    }

    @Test
    void addCompany_WithExistingEmail_ShouldThrowException() {
        //Given

        String expectedErrorMessage = "Email already taken";
        String email = "company@wedoostore.com";
        AddCompanyDto addCompanyDto = AddCompanyDto.builder().email(email).name("Glady").build();

        //When
        when(companiesRepo.findByEmail(email)).thenReturn(Optional.of(CompanyEntity.builder().build()));
        // Execute
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> companiesService.addCompany(addCompanyDto));

        // Then
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void getAllCompanies() {
        // Mock the behavior of companiesRepo.findAll
        when(companiesRepo.findAll()).thenReturn(new ArrayList<>());

        // Call the method to test
        List<DisplayCompanyDto> result = companiesService.getAllCompanies();

        // Verify that companiesRepo.findAll was called
        verify(companiesRepo, times(1)).findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getCompany() {
        UUID companyId = UUID.randomUUID();
        CompanyEntity companyEntity = new CompanyEntity();
        when(companiesRepo.findById(companyId)).thenReturn(Optional.of(companyEntity));
        when(companiesMapper.toDto(ArgumentMatchers.any(CompanyEntity.class))).thenReturn(DisplayCompanyDto.builder().id(companyId).build());

        DisplayCompanyDto result = companiesService.getCompany(companyId);

        verify(companiesRepo, times(1)).findById(companyId);
        verify(companiesMapper, times(1)).toDto(ArgumentMatchers.any(CompanyEntity.class));

        assertNotNull(result);
    }

    @Test
    void getCompany_WithNoExistingId_ShouldThrowException() {
        //Given
        UUID companyId = UUID.randomUUID();
        String expectedErrorMessage = "No company with the given id found";
        //When
        when(companiesRepo.findById(companyId)).thenReturn(Optional.empty());
        // Execute
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> companiesService.getCompany(companyId));

        // Then
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void addUserToCompany() {
        UUID companyId = UUID.randomUUID();
        AddEmployeeDto addEmployeeDto = AddEmployeeDto.builder().build();
        CompanyEntity companyEntity = new CompanyEntity();
        when(companiesRepo.findById(companyId)).thenReturn(Optional.of(companyEntity));
        when(employeesMapper.toEntity(addEmployeeDto)).thenReturn(new EmployeeEntity());

        companiesService.addEmployeeToCompany(companyId, addEmployeeDto);

        verify(companiesRepo, times(1)).findById(companyId);
        verify(employeesRepo, times(1)).save(ArgumentMatchers.any(EmployeeEntity.class));
    }

    @Test
    void getCompanyUsers() {
        UUID companyId = UUID.randomUUID();
        CompanyEntity companyEntity = new CompanyEntity();
        when(companiesRepo.findById(companyId)).thenReturn(Optional.of(companyEntity));
        when(employeesRepo.findByCompany(ArgumentMatchers.any(CompanyEntity.class))).thenReturn(List.of(EmployeeEntity.builder().id(UUID.randomUUID()).build()));
        when(employeesMapper.toDto(ArgumentMatchers.any(EmployeeEntity.class))).thenReturn(DisplayEmployeeDto.builder().build());

        List<DisplayEmployeeDto> result = companiesService.getCompanyEmplyees(companyId);

        verify(companiesRepo, times(1)).findById(companyId);
        verify(employeesRepo, times(1)).findByCompany(ArgumentMatchers.any(CompanyEntity.class));
        verify(employeesMapper, times(1)).toDto(ArgumentMatchers.any(EmployeeEntity.class));

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getCompanyUser() {
        UUID userId = UUID.randomUUID();
        when(employeesRepo.findById(userId)).thenReturn(Optional.of(new EmployeeEntity()));
        when(employeesMapper.toDto(ArgumentMatchers.any(EmployeeEntity.class))).thenReturn(DisplayEmployeeDto.builder().id(userId).build());

        DisplayEmployeeDto result = companiesService.getCompanyEmployee(UUID.randomUUID(), userId);

        verify(employeesRepo, times(1)).findById(userId);
        verify(employeesMapper, times(1)).toDto(ArgumentMatchers.any(EmployeeEntity.class));

        assertNotNull(result);
    }

    @Test
    void depositBalanceToUser() {
        UUID companyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        DepositBalanceDto depositBalanceDto = DepositBalanceDto.builder().depositDate(LocalDate.now()).balance(50.0).enumDepositType(EnumDepositType.GIFTS).build();
        CompanyEntity companyEntity = CompanyEntity.builder().id(companyId).balance(500.0).build();
        EmployeeEntity employeeEntity = new EmployeeEntity();

        when(companiesRepo.findById(companyId)).thenReturn(Optional.of(companyEntity));
        when(employeesRepo.findByIdAndCompany(userId, companyEntity)).thenReturn(Optional.of(employeeEntity));
        when(employeesRepo.save(ArgumentMatchers.any(EmployeeEntity.class))).thenReturn(new EmployeeEntity());

        companiesService.depositBalanceToEmployee(companyId, userId, depositBalanceDto);

        verify(companiesRepo, times(1)).findById(companyId);
        verify(employeesRepo, times(1)).findByIdAndCompany(userId, companyEntity);
        verify(employeesRepo, times(1)).save(ArgumentMatchers.any(EmployeeEntity.class));
    }

    @Test
    void depositWIthNotEnoughtBalance_ShouldThrow_Exception() {

        //Given

        UUID companyId = UUID.randomUUID();
        String expectedErrorMessage = "Not enough balance for company with ID: " + companyId;
        UUID userId = UUID.randomUUID();
        DepositBalanceDto depositBalanceDto = DepositBalanceDto.builder().depositDate(LocalDate.now()).balance(50.0).enumDepositType(EnumDepositType.GIFTS).build();
        CompanyEntity companyEntity = CompanyEntity.builder().id(companyId).balance(5.0).build();
        EmployeeEntity employeeEntity = new EmployeeEntity();
        //When
        when(companiesRepo.findById(companyId)).thenReturn(Optional.of(companyEntity));
        when(employeesRepo.findByIdAndCompany(userId, companyEntity)).thenReturn(Optional.of(employeeEntity));
        // Execute
        NotEnoughBalanceException exception = assertThrows(NotEnoughBalanceException.class,
                () -> companiesService.depositBalanceToEmployee(companyId, userId, depositBalanceDto));

        // Then
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void getUserBalance() {
        UUID companyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CompanyEntity companyEntity = new CompanyEntity();
        EmployeeEntity employeeEntity = new EmployeeEntity();
        DepositEntity depositEntity = new DepositEntity();
        depositEntity.setBalance(50.0);
        depositEntity.setDepositType(EnumDepositType.GIFTS.name());
        depositEntity.setDepositDate(LocalDate.now().minusDays(100));

        employeeEntity.addDeposit(depositEntity);
        companyEntity.setBalance(100.0);

        when(companiesRepo.findById(companyId)).thenReturn(Optional.of(companyEntity));
        when(employeesRepo.findByIdAndCompany(userId, companyEntity)).thenReturn(Optional.of(employeeEntity));

        GetBalanceDto result = companiesService.getEmployeeBalance(companyId, userId);

        verify(companiesRepo, times(1)).findById(companyId);
        verify(employeesRepo, times(1)).findByIdAndCompany(userId, companyEntity);

        assertNotNull(result);
        assertEquals(50.0, result.balance());
    }
}
