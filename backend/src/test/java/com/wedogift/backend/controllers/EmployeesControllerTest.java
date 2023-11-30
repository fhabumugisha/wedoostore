package com.wedogift.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedogift.backend.dtos.DepositBalanceDto;
import com.wedogift.backend.dtos.EnumDepositType;
import com.wedogift.backend.dtos.GetBalanceDto;
import com.wedogift.backend.jwt.JwtProvider;
import com.wedogift.backend.services.CompaniesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeesController.class)
class EmployeesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompaniesService companiesService;

    @MockBean
    private JwtProvider jwtProvider;


    @Test
    void depositUserBalance() throws Exception {
        UUID employeeId = UUID.randomUUID();
        DepositBalanceDto depositBalanceDto = DepositBalanceDto.builder().depositDate(LocalDate.now()).balance(50.0).enumDepositType(EnumDepositType.GIFTS).build();

        String jwtToken = "fake";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/employees/{employeeId}/deposit", employeeId)
                .with(jwt().authorities(
                                List.of(new SimpleGrantedAuthority("ROLE_USER")))
                        .jwt(builder -> builder.tokenValue(jwtToken)
                                .header("Authorization Bearer ", jwtToken)))
                .with(user("user"))
                .content(objectMapper.writeValueAsString(depositBalanceDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isNoContent());

    }

    @Test
    void depositUserBalance_WithInvalidInput_ShouldReturn400() throws Exception {
        //Given
        UUID employeeId = UUID.randomUUID();
        DepositBalanceDto depositBalanceDto = DepositBalanceDto.builder().balance(50.0).enumDepositType(EnumDepositType.GIFTS).build();
        String jwtToken = "FAKE";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/v1/employees/{employeeId}/deposit", employeeId)
                .with(jwt().authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                        .jwt(builder -> builder.
                                tokenValue(jwtToken)
                                .header("Authorization Bearer ", jwtToken)))
                .with(user("user"))

                .content(objectMapper.writeValueAsString(depositBalanceDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);


        //Then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserBalance() throws Exception {
        String companyEmail = "company@wedoostore.com";
        UUID employeeId = UUID.randomUUID();
        GetBalanceDto getBalanceDto = GetBalanceDto.builder().balance(50.0).build();

        when(companiesService.getEmployeeBalance(companyEmail, employeeId)).thenReturn(getBalanceDto);

        String jwtToken = "FAKE";
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/v1/employees/{employeeId}/balance", employeeId)
                .accept(MediaType.APPLICATION_JSON)
                .with(jwt().authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                        .jwt(builder -> builder.tokenValue(jwtToken)
                                .header("Authorization Bearer ", jwtToken)))
                .with(user(companyEmail));

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap()).andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(getBalanceDto));


    }
}