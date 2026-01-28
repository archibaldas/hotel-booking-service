package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.AbstractIntegrationTest;
import com.example.hotel_booking_service.web.dto.request.HotelRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class HotelControllerTest extends AbstractIntegrationTest {

    @Test
    public void whenUserGetsHotels_thenOk() throws Exception {
        mockMvc.perform(get("/api/hotels")
                        .with(httpBasic(USER_PARAM, USER_PARAM)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUserGetsHotelById_thenOk() throws Exception{
        mockMvc.perform(get("/api/hotels/" + hotelIds.get(2))
                        .with(httpBasic(USER_PARAM, USER_PARAM)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUserGetHotelByLostId_thenNoFound() throws Exception{
        mockMvc.perform(get("/api/hotels/" + hotelIds.get(3) + 5)
                        .with(httpBasic(USER_PARAM, USER_PARAM)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUserCreatesHotel_thenCreated() throws Exception {
        String createdHotel = "Test_Created_Hotel ";
        HotelRequestDto request = new HotelRequestDto();
        request.setName(createdHotel);
        request.setCity(createdHotel + "CITY");
        request.setAddress(createdHotel + "Address");
        request.setTitle(createdHotel);
        request.setDistanceFromCenter(0.2);

        mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(httpBasic(ADMIN_PARAM, ADMIN_PARAM)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenAdminCreateHotelWithoutAddress_thenBadRequest() throws Exception{
        String createdHotel = "Test_Created_Hotel ";
        HotelRequestDto request = new HotelRequestDto();
        request.setName(createdHotel);
        request.setCity(createdHotel + "CITY");
        request.setTitle(createdHotel);
        request.setDistanceFromCenter(0.2);

        mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(httpBasic(ADMIN_PARAM, ADMIN_PARAM)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAdminUpdateHotel_thenOk() throws Exception {
        String updatedHotel = "Test_Updated_Hotel ";
        HotelRequestDto request = new HotelRequestDto();
        request.setName(updatedHotel);
        request.setCity(updatedHotel + "CITY");
        request.setAddress(updatedHotel + "Address");
        request.setTitle(updatedHotel);
        request.setDistanceFromCenter(0.2);

        mockMvc.perform(put("/api/hotels/" + hotelIds.get(3))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(httpBasic(ADMIN_PARAM, ADMIN_PARAM)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenAdminDeleteHotel_thenNoContent() throws Exception {
        mockMvc.perform(delete("/api/hotels/" + hotelIds.get(3))
                        .with(httpBasic(ADMIN_PARAM, ADMIN_PARAM)))
                .andExpect(status().isNoContent());
    }

}
