package com.wedogift.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedogift.backend.dtos.DepositBalanceDto;
import com.wedogift.backend.dtos.EnumDepositType;
import com.wedogift.backend.dtos.GetBalanceDto;
import com.wedogift.backend.services.CompaniesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompaniesController.class)
class CompaniesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompaniesService companiesService;
    @Test
    void testDepositUserBalance() throws Exception {
        UUID companyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        DepositBalanceDto depositBalanceDto = DepositBalanceDto.builder().depositDate(LocalDate.now()).balance(50.0).enumDepositType(EnumDepositType.GIFTS).build();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/companies/{companyId}/users/{userId}/deposit", companyId, userId)
                .content(objectMapper.writeValueAsString(depositBalanceDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

    }

    @Test
    void depositUserBalance_WithInvalidInput_SHouldReturn400() throws Exception {
        //Given
        UUID companyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        DepositBalanceDto depositBalanceDto = DepositBalanceDto.builder().balance(50.0).enumDepositType(EnumDepositType.GIFTS).build();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/companies/{companyId}/users/{userId}/deposit", companyId, userId)
                .content(objectMapper.writeValueAsString(depositBalanceDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);


        //Then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetUserBalance() throws Exception {
        UUID companyId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        GetBalanceDto getBalanceDto = GetBalanceDto.builder().balance(50.0).build();

        when(companiesService.getUserBalance(companyId, userId)).thenReturn(getBalanceDto);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/companies/{companyId}/users/{userId}/balance", companyId, userId)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult =   mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap()).andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(getBalanceDto));


    }
}