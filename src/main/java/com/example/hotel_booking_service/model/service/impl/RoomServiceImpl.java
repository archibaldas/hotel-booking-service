package com.example.hotel_booking_service.model.service.impl;

import com.example.hotel_booking_service.exception.NoFoundEntityException;
import com.example.hotel_booking_service.exception.NotChangeDataException;
import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.model.entity.UnavailableDate;
import com.example.hotel_booking_service.model.repository.RoomRepository;
import com.example.hotel_booking_service.model.repository.UnavailableDateRepository;
import com.example.hotel_booking_service.model.service.HotelService;
import com.example.hotel_booking_service.model.service.RoomService;
import com.example.hotel_booking_service.web.dto.request.RoomRequestDto;
import com.example.hotel_booking_service.web.dto.response.RoomResponseDto;
import com.example.hotel_booking_service.web.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static com.example.hotel_booking_service.utils.DateUtils.getDateListBetweenArrivalAndDepartureDates;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final HotelService hotelService;
    private final UnavailableDateRepository unavailableDateRepository;

    @Override
    public List<RoomResponseDto> findAll() {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(() ->
                new NoFoundEntityException("Комната с Id: " + id + "не найдена"));
    }

    @Override
    @Transactional
    public RoomResponseDto create(RoomRequestDto request) {
        Room room = roomMapper.toEntity(request);
        room.setHotel(hotelService.findById(request.getHotelId()));
        try {
            room = roomRepository.save(room);
        } catch (Exception e){
            throw new NotChangeDataException("Не возможно сохранить комнату так как комната с такими данными уже сохранена");

        }
        return roomMapper.toResponseDto(room);
    }

    @Override
    @Transactional
    public RoomResponseDto update(Long id, RoomRequestDto request) {
        Room updatedRoom = findById(id);
        if(!updatedRoom.getHotel().getId().equals(request.getHotelId())){
            throw new NotChangeDataException("Нельзя изменять данные отеля для комнаты");
        }
        try{
            updatedRoom = roomRepository.save(roomMapper.updateEntity(request, updatedRoom));
        } catch (Exception e){
            throw new NotChangeDataException("Не возможно обновить комнату так как комната с такими данными уже сохранена");
        }

        RoomResponseDto responseDto = roomMapper.toResponseDto(updatedRoom);
        responseDto.setUnavailableDates(updatedRoom.getUnavailableDates().stream()
                .map(UnavailableDate::getUnavailableDate). toList());
        return responseDto;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Room room = findById(id);
        roomRepository.delete(room);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomResponseDto getRoomResponseById(Long id) {
        Room room = findById(id);
        RoomResponseDto responseDto = roomMapper.toResponseDto(room);
        responseDto.setUnavailableDates(room.getUnavailableDates().stream()
                .map(UnavailableDate::getUnavailableDate). toList());
        return responseDto;
    }

    @Override
    public Room bookingDates(Long roomId, LocalDate arrival, LocalDate departure) {
        Room updatedRoom = findById(roomId);
        List<LocalDate> dateListForRoom = getDateListBetweenArrivalAndDepartureDates(arrival, departure);
        List<UnavailableDate> unavailableDates = dateListForRoom.stream()
                .map(d -> {
                    UnavailableDate unavailableDate = new UnavailableDate();
                    unavailableDate.setRoom(updatedRoom);
                    unavailableDate.setUnavailableDate(d);
                    return unavailableDate;
                })
                .toList();
        unavailableDateRepository.saveAll(unavailableDates);
        return findById(roomId);
    }

    @Override
    public void clearBookingDates(Long roomId, LocalDate arrival, LocalDate departure) {
        List<LocalDate> bookingDates = getDateListBetweenArrivalAndDepartureDates(arrival, departure);
        Room room = findById(roomId);
        List<UnavailableDate> unavailableDates = room.getUnavailableDates();
        for(LocalDate date : bookingDates){
            for(UnavailableDate unavailableDate : unavailableDates){
                if (unavailableDate.getUnavailableDate().equals(date)) {
                    unavailableDateRepository.delete(unavailableDate);
                }
            }
        }
    }

    @Override
    public boolean isExistingPeriod(Long roomId, LocalDate arrival, LocalDate departure) {
        List<LocalDate> bookingDates = getDateListBetweenArrivalAndDepartureDates(arrival, departure);
        Room room = findById(roomId);
        return bookingDates.stream()
                .anyMatch(d -> unavailableDateRepository.existsByRoomAndUnavailableDate(room, d));
    }
}
