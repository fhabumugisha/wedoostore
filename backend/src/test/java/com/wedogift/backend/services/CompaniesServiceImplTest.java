package com.wedogift.backend.services;

import com.wedogift.backend.dtos.*;
import com.wedogift.backend.entities.CompanyEntity;
import com.wedogift.backend.entities.DepositEntity;
import com.wedogift.backend.entities.UserEntity;
import com.wedogift.backend.exceptions.CompanyNonFoundException;
import com.wedogift.backend.exceptions.NotEnoughBalanceException;
import com.wedogift.backend.mappers.CompaniesMapper;
import com.wedogift.backend.mappers.UsersMapper;
import com.wedogift.backend.repos.CompaniesRepo;
import com.wedogift.backend.repos.UsersRepo;
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
    private UsersMapper usersMapper;
    @Mock
    private UsersRepo usersRepo;

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
        CompanyNonFoundException exception = assertThrows(CompanyNonFoundException.class,
                () -> companiesService.getCompany(companyId));

        // Then
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void addUserToCompany() {
        UUID companyId = UUID.randomUUID();
        AddUserDto addUserDto = AddUserDto.builder().build();
        CompanyEntity companyEntity = new CompanyEntity();
        when(companiesRepo.findById(companyId)).thenReturn(Optional.of(companyEntity));
        when(usersMapper.toEntity(addUserDto)).thenReturn(new UserEntity());

        companiesService.addUserToCompany(companyId, addUserDto);

        verify(companiesRepo, times(1)).findById(companyId);
        verify(usersRepo, times(1)).save(ArgumentMatchers.any(UserEntity.class));
    }

    @Test
    void getCompanyUsers() {
        UUID companyId = UUID.randomUUID();
        CompanyEntity companyEntity = new CompanyEntity();
        when(companiesRepo.findById(companyId)).thenReturn(Optional.of(companyEntity));
        when(usersRepo.findByCompany(ArgumentMatchers.any(CompanyEntity.class))).thenReturn(List.of(UserEntity.builder().id(UUID.randomUUID()).build()));
        when(usersMapper.toDto(ArgumentMatchers.any(UserEntity.class))).thenReturn(DisplayUserDto.builder().build());

        List<DisplayUserDto> result = companiesService.getCompanyUsers(companyId);

        verify(companiesRepo, times(1)).findById(companyId);
        verify(usersRepo, times(1)).findByCompany(ArgumentMatchers.any(CompanyEntity.class));
        verify(usersMapper, times(1)).toDto(ArgumentMatchers.any(UserEntity.class));

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getCompanyUser() {
        UUID userId = UUID.randomUUID();
        when(usersRepo.findById(userId)).thenReturn(Optional.of(new UserEntity()));
        when(usersMapper.toDto(ArgumentMatchers.any(UserEntity.class))).thenReturn(DisplayUserDto.builder().id(userId).build());

        DisplayUserDto result = companiesService.getCompanyUser(UUID.randomUUID(), userId);

        verify(usersRepo, times(1)).findById(userId);
        verify(usersMapper, times(1)).toDto(ArgumentMatchers.any(UserEntity.class));

        assertNotNull(result);
    }

    @Test
    void depositBalanceToUser() {
        UUID companyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        DepositBalanceDto depositBalanceDto = DepositBalanceDto.builder().depositDate(LocalDate.now()).balance(50.0).enumDepositType(EnumDepositType.GIFTS).build();
        CompanyEntity companyEntity = CompanyEntity.builder().id(companyId).balance(500.0).build();
        UserEntity userEntity = new UserEntity();

        when(companiesRepo.findById(companyId)).thenReturn(Optional.of(companyEntity));
        when(usersRepo.findByIdAndCompany(userId, companyEntity)).thenReturn(Optional.of(userEntity));
        when(usersRepo.save(ArgumentMatchers.any(UserEntity.class))).thenReturn(new UserEntity());

        companiesService.depositBalanceToUser(companyId, userId, depositBalanceDto);

        verify(companiesRepo, times(1)).findById(companyId);
        verify(usersRepo, times(1)).findByIdAndCompany(userId, companyEntity);
        verify(usersRepo, times(1)).save(ArgumentMatchers.any(UserEntity.class));
    }

    @Test
    void depositWIthNotEnoughtBalance_ShouldThrow_Exception() {

        //Given

        UUID companyId = UUID.randomUUID();
        String expectedErrorMessage = "Not enough balance for company with ID: " + companyId;
        UUID userId = UUID.randomUUID();
        DepositBalanceDto depositBalanceDto = DepositBalanceDto.builder().depositDate(LocalDate.now()).balance(50.0).enumDepositType(EnumDepositType.GIFTS).build();
        CompanyEntity companyEntity = CompanyEntity.builder().id(companyId).balance(5.0).build();
        UserEntity userEntity = new UserEntity();
        //When
        when(companiesRepo.findById(companyId)).thenReturn(Optional.of(companyEntity));
        when(usersRepo.findByIdAndCompany(userId, companyEntity)).thenReturn(Optional.of(userEntity));
        // Execute
        NotEnoughBalanceException exception = assertThrows(NotEnoughBalanceException.class,
                () -> companiesService.depositBalanceToUser(companyId, userId, depositBalanceDto));

        // Then
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void getUserBalance() {
        UUID companyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CompanyEntity companyEntity = new CompanyEntity();
        UserEntity userEntity = new UserEntity();
        DepositEntity depositEntity = new DepositEntity();
        depositEntity.setBalance(50.0);
        depositEntity.setDepositType(EnumDepositType.GIFTS.name());
        depositEntity.setDepositDate(LocalDate.now().minusDays(100));

        userEntity.addDeposit(depositEntity);
        companyEntity.setBalance(100.0);

        when(companiesRepo.findById(companyId)).thenReturn(Optional.of(companyEntity));
        when(usersRepo.findByIdAndCompany(userId, companyEntity)).thenReturn(Optional.of(userEntity));

        GetBalanceDto result = companiesService.getUserBalance(companyId, userId);

        verify(companiesRepo, times(1)).findById(companyId);
        verify(usersRepo, times(1)).findByIdAndCompany(userId, companyEntity);

        assertNotNull(result);
        assertEquals(50.0, result.balance());
    }
}
