package com.wedogift.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedogift.backend.dtos.AddCompanyDto;
import com.wedogift.backend.jwt.JwtProvider;
import com.wedogift.backend.services.CompaniesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompaniesController.class)
class CompaniesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompaniesService companiesService;

    @MockBean
    private JwtProvider jwtProvider;


    @Test
    void addCompany() throws Exception {
        //Given
        AddCompanyDto addCompanyDto = AddCompanyDto.builder().email("company@wedoostore.com")
                .name("company").balance(50.1).password("testttt").build();
        String jwtToken = "fake";

        //When
        when(companiesService.addCompany(any())).thenReturn(UUID.randomUUID());
        when(jwtProvider.issueToken(anyString(), anyString())).thenReturn(jwtToken);

        //Then
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/companies")
                .with(jwt())
                .content(objectMapper.writeValueAsString(addCompanyDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated());

    }
}