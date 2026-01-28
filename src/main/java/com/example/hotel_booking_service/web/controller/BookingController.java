package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.aop.LogExecution;
import com.example.hotel_booking_service.service.BookingService;
import com.example.hotel_booking_service.web.dto.request.BookingRequestDto;
import com.example.hotel_booking_service.web.dto.response.BookingResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @LogExecution
    public BookingResponseDto bookingRoom(@Valid @RequestBody BookingRequestDto requestDto){
        return bookingService.create(requestDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @LogExecution
    public Page<BookingResponseDto> getAllBookings(@RequestParam(defaultValue = "0")int page,
                                                   @RequestParam(defaultValue = "10") int size){
        return bookingService.findAllBookingsByPageable(PageRequest.of(page, size));
    }
}
