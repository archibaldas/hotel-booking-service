package com.example.hotel_booking_service.model.service.impl;


import com.example.hotel_booking_service.exception.NoFoundEntityException;
import com.example.hotel_booking_service.exception.NotChangeDataException;
import com.example.hotel_booking_service.model.entity.Booking;

import com.example.hotel_booking_service.model.repository.BookingRepository;
import com.example.hotel_booking_service.model.service.BookingService;
import com.example.hotel_booking_service.model.service.RoomService;
import com.example.hotel_booking_service.model.service.UserService;
import com.example.hotel_booking_service.web.dto.request.BookingRequestDto;
import com.example.hotel_booking_service.web.dto.response.BookingResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {
    private final UserService userService;
    private final RoomService roomService;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> findAll() {
        List<Booking> bookingList = bookingRepository.findAll();
      return bookingList.stream()
          .map(this::toResponseDto)
          .toList();
    }

    @Override
    @Transactional
    public BookingResponseDto create(BookingRequestDto request) {
        if(roomService.isExistingPeriod(request.getRoomId(), request.getArrivalDate(), request.getDepartureDate()))
            throw new NotChangeDataException("В периоде с " + request.getArrivalDate() + " по " +
                    request.getDepartureDate() + " для комнаты с Id" + request.getRoomId() + " есть резервированные даты.");
       return toResponseDto(bookingRepository.save(toEntity(request)));
    }

   @Override
    @Transactional(readOnly = true)
    public Booking findById(Long id) {
      return bookingRepository.findById(id)
              .orElseThrow(() -> new NoFoundEntityException("Бронирование с ID: " + id + " найдено."));
    }

    @Override
    @Transactional
    public BookingResponseDto update(Long id, BookingRequestDto request) {
        Booking updatedBooking = findById(id);
        if(!updatedBooking.getUser().getId().equals(request.getUserId()))
            throw new NotChangeDataException("Изменить данные бронирования может только пользователь который зарезервировал бронь.");
        if(roomService.isExistingPeriod(request.getRoomId(), request.getArrivalDate(), request.getDepartureDate()))
            throw new NotChangeDataException("В периоде с " + request.getArrivalDate() + " по " +
                    request.getDepartureDate() + " для комнаты с Id" + request.getRoomId() + " есть резервированные даты.");
        roomService.clearBookingDates(updatedBooking.getRoom().getId(),
                updatedBooking.getArrivalDate(),
                updatedBooking.getDepartureDate());
        Booking booking = toEntity(request);
        booking.setId(id);
        return toResponseDto(bookingRepository.save(booking));
    }

    @Override
    public void deleteById(Long id) {
        Booking booking = findById(id);
        roomService.clearBookingDates(booking.getRoom().getId(),
                booking.getArrivalDate(),
                booking.getDepartureDate());
        bookingRepository.delete(booking);
    }

    private Booking toEntity(BookingRequestDto request){
        Booking booking = new Booking();
        booking.setUser(userService.findById(request.getUserId()));
        booking.setRoom(roomService.bookingDates(request.getRoomId(),
                request.getArrivalDate(),
                request.getDepartureDate()));
        booking.setArrivalDate(request.getArrivalDate());
        booking.setDepartureDate(request.getDepartureDate());
        return booking;
    }

    private BookingResponseDto toResponseDto(Booking booking) {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(booking.getId());
        response.setUserResponseDto(userService.getUserById(booking.getUser().getId()));
        response.setRoomResponseDto(roomService.getRoomResponseById(booking.getRoom().getId()));
        response.setArrivalDate(booking.getArrivalDate());
        response.setDepartureDate(booking.getDepartureDate());
        return response;
    }
}
