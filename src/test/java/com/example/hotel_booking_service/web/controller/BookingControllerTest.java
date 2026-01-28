package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.AbstractIntegrationTest;
import com.example.hotel_booking_service.web.dto.request.BookingRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class BookingControllerTest extends AbstractIntegrationTest {

    @Test
    public void whenUserCreateBooking_thenOk() throws Exception{

        long countBefore = bookingRepository.count();

        BookingRequestDto request = new BookingRequestDto();
        request.setUserId(userId);
        request.setRoomId(roomIds.get(1));
        request.setArrivalDate(LocalDate.now());
        request.setDepartureDate(LocalDate.now().plusDays(3));

        mockMvc.perform(post("/api/booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(httpBasic(USER_PARAM, USER_PARAM)))
                .andExpect(status().isCreated());
        long countAfter = bookingRepository.count();
        assertEquals(countBefore + 1, countAfter);
    }

    @Test
    public void whenUserCreateBookingWithUnavailableDates_thenBadRequest() throws Exception{
        BookingRequestDto request = new BookingRequestDto();
        request.setUserId(userId);
        request.setRoomId(roomIds.get(3));
        request.setArrivalDate(LocalDate.now());
        request.setDepartureDate(LocalDate.now().plusDays(3));

        mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(httpBasic(USER_PARAM, USER_PARAM)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUnAuthorizedUserCreateBooking_thenReturnUnauthorized() throws Exception{
        BookingRequestDto request = new BookingRequestDto();
        request.setUserId(userId);
        request.setRoomId(roomIds.get(1));
        request.setArrivalDate(LocalDate.now());
        request.setDepartureDate(LocalDate.now().plusDays(3));

        mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenAdminGetAllBookings_thenReturnOk() throws Exception{
        mockMvc.perform(get("/api/booking")
                        .with(httpBasic(ADMIN_PARAM, ADMIN_PARAM)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenUserGetAllBookings_thenReturnForbidden() throws Exception{

        mockMvc.perform(get("/api/booking")
                        .with(httpBasic(USER_PARAM, USER_PARAM)))
                .andExpect(status().isForbidden());
    }
}
