package com.wedogift.backend.controllers;

import com.wedogift.backend.dtos.*;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Tag(name = "Company-API", description = "The company API")
@RestController
@RequestMapping("/api/v1/companies")
public class CompaniesController {

    private final   CompaniesService  companiesService;

    public CompaniesController(CompaniesService companiesService) {
        this.companiesService = companiesService;
    }

    @Operation(summary = "Add a new company",
            description = "Add a new company in the wedoogift service",
            tags = { "Company-API" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content),
    })
    @PostMapping
    public ResponseEntity<String>  addCompany(@Valid @Parameter(name = "companyDto")  @RequestBody  AddCompanyDto companyDto){
        UUID id =  companiesService.addCompany(companyDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{productId}")
                .buildAndExpand(id)
                .toUri();
        return  ResponseEntity.created(location).build();
    }

    @Operation(summary = "Returns a list of companies", description = "Returns the list of companies using wedoogift service", tags = { "Company-API" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = List.class))))
    })
    @GetMapping
    public ResponseEntity<List<DisplayCompanyDto>> getAllCompanies(){
        return  ResponseEntity.ok(companiesService.getAllCompanies());
    }


    @Operation(summary = "Get a company", description = "Returns a company", tags = { "Company-API" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DisplayCompanyDto.class))),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content),
    })
    @GetMapping("{companyId}")
    public ResponseEntity<DisplayCompanyDto> getCompany( @Parameter(name = "companyId", description = "companyId")  @PathVariable UUID companyId){
        return  ResponseEntity.ok(companiesService.getCompany(companyId));
    }


    @Operation(summary = "Add a new user to a company",
            description = "Add a new user to a company in the wedoogift service",
            tags = { "Company-API" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content),
    })
    @PostMapping("{companyId}/users")
    public ResponseEntity<?> addUserToCompany( @Parameter(name = "companyId", description = "companyId")  @PathVariable UUID companyId,
                                               @Parameter(name = "userDto")  @Valid @RequestBody   AddUserDto userDto){
        companiesService.addUserToCompany(companyId, userDto);
        return  ResponseEntity.noContent().build();
    }

    @Operation(summary = "Returns the users of a company", description = "" +
            "Returns the list of users of company", tags = { "Company-API" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = List.class)))),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content),
    })
    @GetMapping("{companyId}/users")
    public ResponseEntity<List<DisplayUserDto>> getCompanyUsers(@Parameter(name = "companyId", description = "companyId")
                                                                    @PathVariable UUID companyId){
        return  ResponseEntity.ok(companiesService.getCompanyUsers(companyId));
    }

    @Operation( summary = "Get user of a company by Id",
            description = "Returns the user ",
            tags = { "Company-API" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DisplayUserDto.class))),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content),
    })
    @GetMapping("{companyId}/users/{userId}")
    public ResponseEntity<DisplayUserDto> getCompanyUser(@Parameter(name = "companyId", description = "companyId")
                                                             @PathVariable UUID companyId,
                                                         @Parameter(name = "userId", description = "userId")
                                                         @PathVariable UUID userId){
        return  ResponseEntity.ok(companiesService.getCompanyUser(companyId, userId));
    }

    
    @Operation(summary = "Add a new balance to a user of a company",
            description = "Add a new balance to a user of a company",
            tags = { "Company-API" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content),
    })
    @PostMapping("{companyId}/users/{userId}/deposit")
    public ResponseEntity<Void> depositUserBalance(@Parameter(name = "companyId", description = "companyId") @PathVariable UUID companyId,
                                                   @Parameter(name = "userId", description = "userId") @PathVariable UUID userId,
                                                   @Parameter(name = "depositBalanceDto")  @Valid @RequestBody  DepositBalanceDto depositBalanceDto){
        companiesService.depositBalanceToUser(companyId, userId, depositBalanceDto);
        return  ResponseEntity.noContent().build();
    }


    @Operation( summary = "Get user balance",
            description = "Returns the user balance",
            tags = { "Company-API" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "Company not found",
                    content = @Content),
    })
    @GetMapping("{companyId}/users/{userId}/balance")
    public ResponseEntity<GetBalanceDto> getUserBalance(@PathVariable UUID companyId, @PathVariable UUID userId){
        return  ResponseEntity.ok(companiesService.getUserBalance(companyId, userId));
    }


}
