package com.example.hotel_booking_service.service.impl;

import com.example.hotel_booking_service.aop.LogExecution;
import com.example.hotel_booking_service.exception.NoFoundEntityException;
import com.example.hotel_booking_service.exception.NotChangeDataException;
import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.model.entity.UnavailableDate;
import com.example.hotel_booking_service.model.filter.RoomFilter;
import com.example.hotel_booking_service.repository.RoomRepository;
import com.example.hotel_booking_service.repository.UnavailableDateRepository;
import com.example.hotel_booking_service.service.HotelService;
import com.example.hotel_booking_service.service.RoomService;
import com.example.hotel_booking_service.repository.specification.RoomSpecification;
import com.example.hotel_booking_service.web.dto.request.RoomRequestDto;
import com.example.hotel_booking_service.web.dto.response.RoomListResponseDto;
import com.example.hotel_booking_service.web.dto.response.RoomResponseDto;
import com.example.hotel_booking_service.web.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @LogExecution
    @Transactional(readOnly = true)
    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(() ->
                new NoFoundEntityException("Комната с Id: " + id + "не найдена"));
    }

    @Override
    @LogExecution
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
    @LogExecution
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
        return roomMapper.toResponseDto(updatedRoom);
    }

    @Override
    @LogExecution
    @Transactional
    public void deleteById(Long id) {
        Room room = findById(id);
        roomRepository.delete(room);
    }

    @Override
    @LogExecution
    public Long getCount() {
        return roomRepository.count();
    }

    @Override
    @LogExecution
    @Transactional(readOnly = true)
    public RoomResponseDto getRoomResponseById(Long id) {
        Room room = findById(id);
        return roomMapper.toResponseDto(room);
    }

    @Override
    @LogExecution
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
    @LogExecution
    public void clearBookingDates(Long roomId, LocalDate arrival, LocalDate departure) {
        List<LocalDate> bookingDates = getDateListBetweenArrivalAndDepartureDates(arrival, departure);
        unavailableDateRepository.deleteByRoomIdAndDates(roomId, bookingDates);
    }

    @Override
    @LogExecution
    public boolean isExistingPeriod(Long roomId, LocalDate arrival, LocalDate departure) {
        List<LocalDate> bookingDates = getDateListBetweenArrivalAndDepartureDates(arrival, departure);
        Room room = findById(roomId);
        return bookingDates.stream()
                .anyMatch(d -> unavailableDateRepository.existsByRoomAndUnavailableDate(room, d));
    }

    @Override
    @LogExecution
    public RoomListResponseDto findRoomsByFilter(RoomFilter filter) {
        Page<Room> page = roomRepository.findAll(RoomSpecification.withFilter(filter),
                PageRequest.of(filter.getPageNumber(), filter.getPageSize()));
        if(page.getSize() == 0){
            throw new NoFoundEntityException("Список комнат пуст");
        }
        return new RoomListResponseDto(
                page.getContent().stream()
                        .map(roomMapper::toResponseDto)
                        .toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }
}
