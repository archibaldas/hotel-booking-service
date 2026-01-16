package com.example.hotel_booking_service.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RoomListResponseDto {
    private List<RoomResponseDto> rooms;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;

}
