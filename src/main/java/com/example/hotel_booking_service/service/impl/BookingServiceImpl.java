package com.example.hotel_booking_service.service.impl;


import com.example.hotel_booking_service.aop.LogExecution;
import com.example.hotel_booking_service.exception.NotChangeDataException;
import com.example.hotel_booking_service.model.entity.Booking;

import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.model.entity.User;
import com.example.hotel_booking_service.repository.BookingRepository;
import com.example.hotel_booking_service.service.BookingService;
import com.example.hotel_booking_service.service.RoomService;
import com.example.hotel_booking_service.service.UserService;
import com.example.hotel_booking_service.statistics.event.event_factory.EventFactory;
import com.example.hotel_booking_service.statistics.publisher.EventPublisher;
import com.example.hotel_booking_service.web.dto.request.BookingRequestDto;
import com.example.hotel_booking_service.web.dto.response.BookingResponseDto;

import com.example.hotel_booking_service.web.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final RoomService roomService;
    private final BookingRepository bookingRepository;
    private final EventFactory<Booking> eventFactory;
    private final EventPublisher publisher;

    @Override
    @LogExecution
    @Transactional
    public BookingResponseDto create(BookingRequestDto request) {
        if(roomService.isExistingPeriod(request.getRoomId(), request.getArrivalDate(), request.getDepartureDate()))
            throw new NotChangeDataException("В периоде с " + request.getArrivalDate() + " по " +
                    request.getDepartureDate() + " для комнаты с Id" + request.getRoomId() + " есть резервированные даты.");
        Booking booking = bookingMapper.requestToEntity(request);
        Room room = roomService.bookingDates(request.getRoomId(), request.getArrivalDate(), request.getDepartureDate());
        User user = userService.findById(request.getUserId());
        booking.setRoom(room);
        booking.setUser(user);
        booking = bookingRepository.save(booking);
        publisher.send(eventFactory.createEvent(booking));
       return bookingMapper.entityToResponse(booking);
    }

    @Override
    @LogExecution
    public Page<BookingResponseDto> findAllBookingsByPageable(Pageable pageable) {
        return bookingRepository.findAll(pageable).map(bookingMapper::entityToResponse);
    }
}
