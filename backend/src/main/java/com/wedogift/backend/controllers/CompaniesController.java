package com.wedogift.backend.controllers;

import com.wedogift.backend.dtos.AddCompanyDto;
import com.wedogift.backend.dtos.DisplayCompanyDto;
import com.wedogift.backend.dtos.DisplayEmployeeDto;
import com.wedogift.backend.jwt.JwtProvider;
import com.wedogift.backend.services.CompaniesService;
import io.swagger.v3.oas.annotations.Operation;
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
import java.security.Principal;
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
            @ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters",
                    content = @Content(schema = @Schema())),
    })
    @PostMapping
    public ResponseEntity<String> addCompany(@Valid
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

    @Operation(summary = "Returns a list of companies",
            description = "Returns the list of companies using wedoogift service", tags = {"Company-API"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DisplayEmployeeDto.class))))
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
                    content = @Content(schema = @Schema())),
    })
    @GetMapping("/me")
    public ResponseEntity<DisplayCompanyDto> getCompany(Principal principal) {
        return ResponseEntity.ok(companiesService.getCompany(principal.getName()));
    }


}
