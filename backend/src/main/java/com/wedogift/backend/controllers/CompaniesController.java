package com.wedogift.backend.controllers;

import com.wedogift.backend.dtos.*;
import com.wedogift.backend.jwt.JwtProvider;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Company-API", description = "The company API")
@RestController
@RequestMapping("/api/v1/companies")
public class CompaniesController {

    private final CompaniesService companiesService;
    private final JwtProvider jwtProvider;

    public CompaniesController(CompaniesService companiesService, JwtProvider jwtProvider) {
        this.companiesService = companiesService;
        this.jwtProvider = jwtProvider;
    }

    @Operation(summary = "Add a new company",
            description = "Add a new company in the wedoogift service",
            tags = {"Company-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content),
    })
    @PostMapping
    public ResponseEntity<String> addCompany(@Valid @Parameter(name = "companyDto")
                                             @RequestBody AddCompanyDto companyDto) {
        UUID id = companiesService.addCompany(companyDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{companyId}")
                .buildAndExpand(id)
                .toUri();
        String token = jwtProvider.issueToken(companyDto.email(), "ROLE_USER");
        return ResponseEntity.created(location).header(HttpHeaders.AUTHORIZATION, token).build();
    }

    @Operation(summary = "Returns a list of companies", description = "Returns the list of companies using wedoogift service", tags = {"Company-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = List.class))))
    })
    @GetMapping
    public ResponseEntity<List<DisplayCompanyDto>> getAllCompanies() {
        return ResponseEntity.ok(companiesService.getAllCompanies());
    }


    @Operation(summary = "Get a company", description = "Returns a company", tags = {"Company-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DisplayCompanyDto.class))),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content),
    })
    @GetMapping("{companyId}")
    public ResponseEntity<DisplayCompanyDto> getCompany(@Parameter(name = "companyId", description = "companyId") @PathVariable UUID companyId) {
        return ResponseEntity.ok(companiesService.getCompany(companyId));
    }


    @Operation(summary = "Add a new employee to a company",
            description = "Add a new employee to a company in the wedoogift service",
            tags = {"Company-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content),
    })
    @PostMapping("{companyId}/employees")
    public ResponseEntity<?> addEmployeeToCompany(@Parameter(name = "companyId", description = "companyId") @PathVariable UUID companyId,
                                                  @Parameter(name = "employeeDto") @Valid @RequestBody AddEmployeeDto employeeDto) {
        companiesService.addEmployeeToCompany(companyId, employeeDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Returns the employees of a company", description = "Returns the list of employees of company", tags = {"Company-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = List.class)))),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content),
    })
    @GetMapping("{companyId}/employees")
    public ResponseEntity<List<DisplayEmployeeDto>> getCompanyEmployees(@Parameter(name = "companyId", description = "companyId")
                                                                        @PathVariable UUID companyId) {
        return ResponseEntity.ok(companiesService.getCompanyEmplyees(companyId));
    }

    @Operation(summary = "Get employee of a company by Id",
            description = "Returns the employee ",
            tags = {"Company-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DisplayEmployeeDto.class))),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content),
    })
    @GetMapping("{companyId}/employees/{employeeId}")
    public ResponseEntity<DisplayEmployeeDto> getCompanyEmployee(@Parameter(name = "companyId", description = "companyId")
                                                                 @PathVariable UUID companyId,
                                                                 @Parameter(name = "employeeId", description = "employeeId")
                                                                 @PathVariable UUID employeeId) {
        return ResponseEntity.ok(companiesService.getCompanyEmployee(companyId, employeeId));
    }


    @Operation(summary = "Add a new balance to a employee of a company",
            description = "Add a new balance to a employee of a company",
            tags = {"Company-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content),
    })
    @PostMapping("{companyId}/employees/{employeeId}/deposit")
    public ResponseEntity<Void> depositEmployeeBalance(@Parameter(name = "companyId", description = "companyId") @PathVariable UUID companyId,
                                                       @Parameter(name = "employeeId", description = "employeeId") @PathVariable UUID employeeId,
                                                       @Parameter(name = "depositBalanceDto") @Valid @RequestBody DepositBalanceDto depositBalanceDto) {
        companiesService.depositBalanceToEmployee(companyId, employeeId, depositBalanceDto);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Get employee balance",
            description = "Returns the employee balance",
            tags = {"Company-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content),
    })
    @GetMapping("{companyId}/employees/{employeeId}/balance")
    public ResponseEntity<GetBalanceDto> getEmployeeBalance(@PathVariable UUID companyId, @PathVariable UUID employeeId) {
        return ResponseEntity.ok(companiesService.getEmployeeBalance(companyId, employeeId));
    }


}
