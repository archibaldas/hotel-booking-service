package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.model.service.HotelService;
import com.example.hotel_booking_service.web.dto.request.HotelRequestDto;
import com.example.hotel_booking_service.web.dto.response.HotelResponseDto;
import com.example.hotel_booking_service.web.mapper.HotelMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
@Tag(name = "Hotel Admin API", description = "API для управления отелями ")
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @GetMapping
    @Operation(summary = "получение списка отелей")
    @ResponseStatus(HttpStatus.OK)
    public List<HotelResponseDto> getHotelList(){
        return hotelService.findAll().stream().map(hotelMapper::toResponseDto).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отель по ID")
    @ResponseStatus(HttpStatus.OK)
    public HotelResponseDto getHotelById(@PathVariable Long id) {
        return hotelMapper.toResponseDto(hotelService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Создать новый отель")
    @ResponseStatus(HttpStatus.CREATED)
    public HotelResponseDto createHotel(@Valid @RequestBody HotelRequestDto requestDto) {
        return hotelMapper.toResponseDto(hotelService.create(requestDto));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Обновить отель")
    @ResponseStatus(HttpStatus.OK)
    public HotelResponseDto updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelRequestDto requestDto) {
        return hotelMapper.toResponseDto(hotelService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отель (физическое удаление)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHotel(@PathVariable Long id) {
        hotelService.deleteById(id);
    }
}
