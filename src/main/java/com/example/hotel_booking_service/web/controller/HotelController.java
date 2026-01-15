package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.model.filter.HotelFilter;
import com.example.hotel_booking_service.model.service.HotelService;
import com.example.hotel_booking_service.web.dto.request.HotelRequestDto;
import com.example.hotel_booking_service.web.dto.response.HotelListResponseDto;
import com.example.hotel_booking_service.web.dto.response.HotelResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
@Tag(name = "Hotel API", description = "API для управления отелями ")
public class HotelController {

    private final HotelService hotelService;

//    @GetMapping
//    @Operation(summary = "получение списка отелей")
//    @ResponseStatus(HttpStatus.OK)
//    public List<HotelResponseDto> getHotelList(){
//        return hotelService.findAll();
//    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public HotelListResponseDto getHotelsByFilter(@Valid HotelFilter hotelFilter){
        return hotelService.findAllByFilter(hotelFilter);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отель по ID")
    @ResponseStatus(HttpStatus.OK)
    public HotelResponseDto getHotelById(@PathVariable Long id) {
        return hotelService.getHotelResponseById(id);
    }

    @PostMapping
    @Operation(summary = "Создать новый отель")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public HotelResponseDto createHotel(@Valid @RequestBody HotelRequestDto requestDto) {
        return hotelService.create(requestDto);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Обновить отель")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public HotelResponseDto updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelRequestDto requestDto) {
        return hotelService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отель (физическое удаление)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteHotel(@PathVariable Long id) {
        hotelService.deleteById(id);
    }

    @PutMapping("/rating/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HotelResponseDto updateHotelRating(@PathVariable Long id,
                                  @RequestParam int newMark){
        return hotelService.updateHotelRating(id, newMark);
    }
}
