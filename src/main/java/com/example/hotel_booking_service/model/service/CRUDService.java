package com.example.hotel_booking_service.model.service;

import java.util.List;

public interface CRUDService <E, R>{
    List<E> findAll();
    E findById (Long id);
    E create (R request);
    E update (Long id, R request);
    void deleteById(Long id);
}
