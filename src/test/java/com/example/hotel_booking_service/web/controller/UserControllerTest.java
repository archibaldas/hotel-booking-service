package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.AbstractIntegrationTest;
import com.example.hotel_booking_service.model.entity.RoleType;
import com.example.hotel_booking_service.web.dto.request.UserRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractIntegrationTest {

    @Test
    public void whenGetUserById_thenReturnIsOk() throws Exception{
        mockMvc.perform(get("/api/users/" + userId)
                        .with(httpBasic(ADMIN_PARAM, ADMIN_PARAM)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetUserByUsername_thenReturnIsOk() throws Exception{
        mockMvc.perform(get("/api/users?username=" + USER_PARAM)
                        .with(httpBasic(USER_PARAM, USER_PARAM)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCreateNewUser_thenReturnIsCreated() throws Exception{
        UserRequestDto request = new UserRequestDto();
        request.setUsername("Created_User");
        request.setEmail("created-user@mail.ru");
        request.setPassword("createduser");

        mockMvc.perform(post("/api/users/registration?roleType=" + RoleType.USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        UserRequestDto invalidRequest = new UserRequestDto();
        invalidRequest.setUsername(USER_PARAM);
        invalidRequest.setEmail("invalid-user@mail.ru");
        invalidRequest.setPassword("123456");


        mockMvc.perform(post("/api/users/registration?roleType=" + RoleType.USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateNewUserWithExistingData_thenReturnIsBadRequest() throws Exception{
        UserRequestDto existingNameRequest = new UserRequestDto();
        existingNameRequest.setUsername(USER_PARAM);
        existingNameRequest.setEmail("invalid-user@mail.ru");
        existingNameRequest.setPassword("123456");


        mockMvc.perform(post("/api/users/registration?roleType=" + RoleType.USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingNameRequest)))
                .andExpect(status().isBadRequest());

        UserRequestDto existingEmailRequest = new UserRequestDto();
        existingEmailRequest.setUsername("invalid-user");
        existingEmailRequest.setEmail(USER_PARAM + "@mail.ru");
        existingEmailRequest.setPassword("123456");

        mockMvc.perform(post("/api/users/registration?roleType=" + RoleType.USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existingEmailRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdatedUsername_thenReturnIsOk() throws Exception{
        UserRequestDto request = new UserRequestDto();
        request.setUsername("Updated_User");
        request.setEmail(USER_PARAM + "@mail.ru");
        request.setPassword(USER_PARAM);

        mockMvc.perform(put("/api/users/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                        .with(httpBasic(USER_PARAM, USER_PARAM)))
                .andExpect(status().isOk());

    }

    @Test
    public void whenDeleteUserById_thenReturnNoContent() throws Exception{
        mockMvc.perform(delete("/api/users/" + userId)
                        .with(httpBasic(USER_PARAM, USER_PARAM)))
                .andExpect(status().isNoContent());
    }
}
