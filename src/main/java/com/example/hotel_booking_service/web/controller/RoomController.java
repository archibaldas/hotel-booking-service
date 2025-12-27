package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.model.service.RoomService;
import com.example.hotel_booking_service.web.dto.request.RoomRequestDto;
import com.example.hotel_booking_service.web.dto.response.RoomResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoomResponseDto getRoomById(@PathVariable Long id){
        return roomService.getRoomResponseById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponseDto createRoom(@Valid @RequestBody RoomRequestDto roomRequestDto){
        return roomService.create(roomRequestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RoomResponseDto updatedRoom(@PathVariable Long id,
                                       @Valid @RequestBody RoomRequestDto roomRequestDto){
        return roomService.update(id, roomRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom (@PathVariable Long id){
        roomService.deleteById(id);
    }
}
