package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.AbstractIntegrationTest;
import com.example.hotel_booking_service.web.dto.request.RoomRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoomControllerTest extends AbstractIntegrationTest {

    @Test
    public void whenGetRoomById_thenReturnIsOk() throws Exception{
        mockMvc.perform(get("/api/rooms/" + roomIds.get(5))
                        .with(httpBasic(USER_PARAM, USER_PARAM)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCreateRoom_thenReturnIsCreated() throws Exception{
        RoomRequestDto request = new RoomRequestDto();
        request.setName("Created_Room");
        request.setDescription("Test_Created_Room");
        request.setNumber(15);
        request.setHotelId(hotelIds.get(1));
        request.setMaxPeople(2);
        request.setPrice(BigDecimal.valueOf(1000));

        mockMvc.perform(post("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                        .with(httpBasic(ADMIN_PARAM, ADMIN_PARAM)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenUpdatedRoom_thenReturnIsOk() throws Exception{
        RoomRequestDto request = new RoomRequestDto();
        request.setName("Updated_Room");
        request.setDescription("Test_Updated_Room");
        request.setNumber(18);
        request.setHotelId(hotelIds.get(0));
        request.setMaxPeople(2);
        request.setPrice(BigDecimal.valueOf(1000));

        mockMvc.perform(put("/api/rooms/" + roomIds.get(5))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(httpBasic(ADMIN_PARAM, ADMIN_PARAM)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteRoom_thenReturnNoContent() throws Exception{
        mockMvc.perform(delete("/api/rooms/" + roomIds.get(2))
                        .with(httpBasic(ADMIN_PARAM, ADMIN_PARAM)))
                .andExpect(status().isNoContent());
    }
}
