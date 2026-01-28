package com.example.hotel_booking_service.service.impl;

import com.example.hotel_booking_service.aop.LogExecution;
import com.example.hotel_booking_service.exception.NoFoundEntityException;
import com.example.hotel_booking_service.model.entity.Hotel;
import com.example.hotel_booking_service.model.filter.HotelFilter;
import com.example.hotel_booking_service.repository.HotelRepository;
import com.example.hotel_booking_service.service.HotelService;
import com.example.hotel_booking_service.repository.specification.HotelSpecification;
import com.example.hotel_booking_service.web.dto.request.HotelRequestDto;
import com.example.hotel_booking_service.web.dto.response.HotelListResponseDto;
import com.example.hotel_booking_service.web.dto.response.HotelResponseDto;
import com.example.hotel_booking_service.web.mapper.HotelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.hotel_booking_service.utils.RatingCalculator.getNewRating;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    @LogExecution
    @Transactional
    public HotelResponseDto create(HotelRequestDto request) {
        if (isExistingHotel(request.getName(), request.getCity())) {
            throw new IllegalArgumentException("Отель с таким названием уже существует в этом городе");
        }
        Hotel hotel = hotelRepository.save(hotelMapper.toEntity(request));
        return hotelMapper.toResponseDto(hotel);
    }

    @Override
    @LogExecution
    @Transactional(readOnly = true)
    public Hotel findById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new NoFoundEntityException("Отель не найден с id: " + id));
    }

    @Override
    @LogExecution
    @Transactional
    public HotelResponseDto update(Long id, HotelRequestDto request) {
        Hotel hotel = findById(id);

        if (isUpdatableHotel(hotel, request.getName(), request.getCity())) {
                throw new IllegalArgumentException("Отель с таким названием уже существует в этом городе");
        }
        hotel = hotelRepository.save(hotelMapper.updateEntityFromDto(request, hotel));
        return hotelMapper.toResponseDto(hotel);
    }

    @Override
    @LogExecution
    public void deleteById(Long id) {
        hotelRepository.delete(findById(id));
    }

    @Override
    @LogExecution
    public Long getCount() {
        return hotelRepository.count();
    }

    @Override
    @LogExecution
    public HotelResponseDto getHotelResponseById(Long id) {
        Hotel hotel = findById(id);
        return hotelMapper.toResponseDto(hotel);
    }

    @Override
    @LogExecution
    public HotelResponseDto updateHotelRating(Long id, int newMark) {
        Hotel hotel = findById(id);
        int numberOfRating = hotel.getRatingCount();
        double newRating;
        if(numberOfRating == 0){
            newRating = newMark;
        }
        else {
            double rating = hotel.getRating();
            newRating = getNewRating(numberOfRating, rating, newMark);
        }
        numberOfRating += 1;
        hotel.setRatingCount(numberOfRating);
        hotel.setRating(newRating);
        return hotelMapper.toResponseDto(hotelRepository.save(hotel));
    }

    @Override
    @LogExecution
    @Transactional(readOnly = true)
    public HotelListResponseDto findAllByFilter(HotelFilter filter) {
        List<Hotel> filterList = hotelRepository.findAll(HotelSpecification.withFilter(filter),
                PageRequest.of(filter.getPageNumber(), filter.getPageSize())).getContent();
        if(filterList.isEmpty()){
            throw new NoFoundEntityException("Ничего не найдено");
        }
        HotelListResponseDto listResponseDto = new HotelListResponseDto();
        List<HotelResponseDto> responseDtos = filterList.stream().map(hotelMapper::toResponseDto)
        .toList();
        listResponseDto.setHotels(responseDtos);
        listResponseDto.setPageNumber(filter.getPageNumber());
        listResponseDto.setPageSize(filter.getPageSize());
        listResponseDto.setTotalCount(getCount());
        return listResponseDto;
    }

    @LogExecution
    private boolean isUpdatableHotel(Hotel hotel, String name, String city){
        return (!hotel.getName().equals(name) ||
                !hotel.getCity().equals(city)) && isExistingHotel(name, city);
    }

    @LogExecution
    private boolean isExistingHotel(String name, String city){
        return hotelRepository.existsByNameAndCity(name, city);
    }
}
