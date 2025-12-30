package com.example.hotel_booking_service.model.service.impl;

import com.example.hotel_booking_service.exception.NoFoundEntityException;
import com.example.hotel_booking_service.model.entity.Booking;
import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.model.entity.User;
import com.example.hotel_booking_service.model.repository.BookingRepository;
import com.example.hotel_booking_service.model.service.RoomService;
import com.example.hotel_booking_service.model.service.UserService;
import com.example.hotel_booking_service.web.dto.request.BookingRequestDto;
import com.example.hotel_booking_service.web.dto.response.BookingResponseDto;
import com.example.hotel_booking_service.web.dto.response.RoomResponseDto;
import com.example.hotel_booking_service.web.dto.response.UserResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
          .map(b -> toResponseDto(b))
          .toList();
    }

    @Override
    @Transactional
    public BookingResponseDto create(BookingRequestDto request) {
        
      return null;
    }

   @Override
    @Transactional(readOnly = true)
    public Booking findById(Long id) {
      return null;
    }

    @Override
    @Transactional
    public BookingResponseDto update(Long id, BookingRequestDto request) {
  
        return null;
    }

    @Override
    public void deleteById(Long id) {
    }

    private Booking toEntity(BookingRequestDto request){
        Booking booking = new Booking();
        booking.setUser(userService.findById(request.getUserId()));
        Room room = roomService.findById(request.getRoomId());
        room = roomService.update(room.getId(), new RoomRequestDto(
            room.getName,
            room.getDescription,
            room.getNumber,
            room.getPrice,
            room.getMaxPeople,
            createUnavailebleDateListForRoom(request.getArrivalDate(),
                                             request.getDepartureDate()),
            room.getHotel.getId();
        )
        booking.setRoom(room);
        booking.setArrivalDate(request.getArrivalDate());
        booking.setDepartureDate(request.getDepartureDate());
        return booking;
    }

    private BookingResponseDto toResponseDto(Booking booking) {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(booking.getId());
        response.setUser(userService.getUserById(booking.getUser.getId()));
        response.setRoom(roomService.getRoomById(booking.getRoom.getId()));
        response.setArrivalDate(booking.getArrivalDate());
        response.setDepartureDate(booking.getDepartureDate());
        return response;
    }

    private List<LocalDate> createUnavailebleDateListForRoom(LocalDate arrival, LocalDate departure, Room room){
        long daysBetween = ChronoUnit.DAYS.between(arrival,departure);
        List<LocalDate> unavailableDateList = new ArrayList();
        List<LocalDate> unavailableDataListFromRoom = getUnavailableDates(room.getUnavailableDates());
        for (int i = 0; i <= daysBetwen; i++){
            LocalDate date = arrival.plusDays(i);
            if(unavailableDataListFromRoom.contains(date){
                throw new NotChangeDataException("В периоде бронирования есть зарезервированные даты");
            }
            unavailableDateList.add(date);
        }
        return unavailableDateList;
    }

    private List<LocalDate> getUnavailableDates(List<UnavailableDate> dates){
        return dates.stream()
            .map(d -> d.getUnavailableDate())
            .toList();
    }

  
}
