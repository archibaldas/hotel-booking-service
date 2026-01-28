package com.example.hotel_booking_service.web.mapper;

import com.example.hotel_booking_service.model.entity.Booking;
import com.example.hotel_booking_service.web.dto.request.BookingRequestDto;
import com.example.hotel_booking_service.web.dto.response.BookingResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
uses = {RoomMapper.class,
UserMapper.class})
public interface BookingMapper {


    Booking requestToEntity(BookingRequestDto requestDto);
    BookingResponseDto entityToResponse(Booking booking);
}
