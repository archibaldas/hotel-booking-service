package com.example.hotel_booking_service.model.service.impl;

import com.example.hotel_booking_service.exception.NoFoundEntityException;
import com.example.hotel_booking_service.model.entity.Hotel;
import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.model.repository.HotelRepository;
import com.example.hotel_booking_service.model.service.HotelService;
import com.example.hotel_booking_service.web.dto.request.HotelRequestDto;
import com.example.hotel_booking_service.web.dto.response.HotelResponseDto;
import com.example.hotel_booking_service.web.dto.response.RoomResponseDto;
import com.example.hotel_booking_service.web.mapper.HotelMapper;
import com.example.hotel_booking_service.web.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final RoomMapper roomMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HotelResponseDto> findAll() {
        log.info("Получение полного списка отелей");
        return hotelRepository.findAll().stream()
                .map(h -> {
                    HotelResponseDto response = hotelMapper.toResponseDto(h);
                    response.setRooms(roomsToRoomResponseList(h.getRooms()));
                    return response;
                })
                .toList();
    }

    @Override
    @Transactional
    public HotelResponseDto create(HotelRequestDto request) {
        log.info("Создание и занесение данных отеля c именем: {} в базу данных",request.getName());
        if (isExistingHotel(request.getName(), request.getCity())) {
            throw new IllegalArgumentException("Отель с таким названием уже существует в этом городе");
        }
        Hotel hotel = hotelRepository.save(hotelMapper.toEntity(request));
        log.info("Отель внесен в базу данных с Id: {}", hotel.getId());
        return hotelMapper.toResponseDto(hotel);
    }

    @Override
    @Transactional(readOnly = true)
    public Hotel findById(Long id) {
        log.info("Поиск отеля по Id: {}", id);
        return hotelRepository.findById(id)
                .orElseThrow(() -> new NoFoundEntityException("Отель не найден с id: " + id));
    }

    @Override
    @Transactional
    public HotelResponseDto update(Long id, HotelRequestDto request) {
        log.info(" Обновление данных отеля с Id: {}", id);

        Hotel hotel = findById(id);

        if (isUpdatableHotel(hotel, request.getName(), request.getCity())) {
                throw new IllegalArgumentException("Отель с таким названием уже существует в этом городе");
        }
        hotel = hotelRepository.save(hotelMapper.updateEntityFromDto(request, hotel));
        log.info("Данные отеля с Id: {} обновлены", id);
        HotelResponseDto responseDto = hotelMapper.toResponseDto(hotel);
        responseDto.setRooms(roomsToRoomResponseList(hotel.getRooms()));
        return responseDto;
    }

    @Override
    public void deleteById(Long id) {
        log.info("Удаление отеля c Id: {}", id);

        hotelRepository.delete(findById(id));
        log.info("Отель с Id: {} удален", id);
    }

    @Override
    public HotelResponseDto getHotelResponseById(Long id) {
        Hotel hotel = findById(id);
        HotelResponseDto hotelResponseDto = hotelMapper.toResponseDto(hotel);
        hotelResponseDto.setRooms(roomsToRoomResponseList(hotel.getRooms()));
        return hotelResponseDto;
    }

    private boolean isUpdatableHotel(Hotel hotel, String name, String city){
        return (!hotel.getName().equals(name) ||
                !hotel.getCity().equals(city)) && isExistingHotel(name, city);
    }

    private boolean isExistingHotel(String name, String city){
        return hotelRepository.existsByNameAndCity(name, city);
    }

    private List<RoomResponseDto> roomsToRoomResponseList(List<Room>rooms){
        return rooms.stream().map(roomMapper::toResponseDto).toList();
    }



}
