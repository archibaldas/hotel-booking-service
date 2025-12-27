package com.example.hotel_booking_service.model.service;

import java.util.List;

public interface CRUDService <D, E, R>{
    List<D> findAll();
    E findById (Long id);
    D create (R request);
    D update (Long id, R request);
    void deleteById(Long id);
}
