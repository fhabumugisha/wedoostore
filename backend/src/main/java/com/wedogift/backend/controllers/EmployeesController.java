package com.wedogift.backend.controllers;

import com.wedogift.backend.dtos.AddEmployeeDto;
import com.wedogift.backend.dtos.DepositBalanceDto;
import com.wedogift.backend.dtos.DisplayEmployeeDto;
import com.wedogift.backend.dtos.GetBalanceDto;
import com.wedogift.backend.services.CompaniesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Tag(name = "Employee-API", description = "The Employee API")
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeesController {

    private final CompaniesService companiesService;


    public EmployeesController(CompaniesService companiesService) {
        this.companiesService = companiesService;

    }


    @Operation(summary = "Add a new employee to a company",
            description = "Add a new employee to a company in the wedoogift service",
            tags = {"Employee-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content()),
    })
    @PostMapping("")
    public ResponseEntity<?> addEmployeeToCompany(Principal principal,
                                                  @Valid @RequestBody AddEmployeeDto employeeDto) {
        companiesService.addEmployeeToCompany(principal.getName(), employeeDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Returns the employees of a company",
            description = "Returns the list of employees of company", tags = {"Employee-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DisplayEmployeeDto.class)))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(schema = @Schema())),
    })
    @GetMapping("")
    public ResponseEntity<List<DisplayEmployeeDto>> getCompanyEmployees(Principal principal) {
        return ResponseEntity.ok(companiesService.getCompanyEmplyees(principal.getName()));
    }

    @Operation(summary = "Get employee of a company by Id",
            description = "Returns the employee ",
            tags = {"Employee-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DisplayEmployeeDto.class))),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content),
    })
    @GetMapping("{employeeId}")
    public ResponseEntity<DisplayEmployeeDto> getCompanyEmployee(Principal principal,
                                                                 @Parameter(description = "employeeId")
                                                                 @PathVariable UUID employeeId) {
        return ResponseEntity.ok(companiesService.getCompanyEmployee(principal.getName(), employeeId));
    }


    @Operation(summary = "Add a new balance to an employee ",
            description = "Add a new balance to an employee ",
            tags = {"Employee-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "employee not found",
                    content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content(schema = @Schema())),
    })
    @PostMapping("{employeeId}/deposit")
    public ResponseEntity<Void> depositEmployeeBalance(Principal principal,
                                                       @Parameter(description = "employeeId") @PathVariable UUID employeeId,
                                                       @Valid @RequestBody DepositBalanceDto depositBalanceDto) {
        companiesService.depositBalanceToEmployee(principal.getName(), employeeId, depositBalanceDto);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Get employee balance",
            description = "Returns the employee balance",
            tags = {"Employee-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content),
    })
    @GetMapping("{employeeId}/balance")
    public ResponseEntity<GetBalanceDto> getEmployeeBalance(Principal principal, @PathVariable UUID employeeId) {
        return ResponseEntity.ok(companiesService.getEmployeeBalance(principal.getName(), employeeId));
    }


}
