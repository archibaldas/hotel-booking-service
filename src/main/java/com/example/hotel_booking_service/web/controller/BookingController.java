package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.model.service.BookingService;
import com.example.hotel_booking_service.web.dto.request.BookingRequestDto;
import com.example.hotel_booking_service.web.dto.response.BookingResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto bookingRoom(@Valid @RequestBody BookingRequestDto requestDto){
        return bookingService.create(requestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponseDto> getAllBookings(){
        return bookingService.findAll();
    }
}
