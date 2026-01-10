package com.example.hotel_booking_service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class TestDatabaseUtils {

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${spring.jpa.properties.hibernate.default_schema:hotel_booking_schema}")
    private String defaultSchema;

    private final String[] TABLES = new String[]{"booking", "users", "hotels", "rooms", "unavailable_dates"};

    @PostConstruct
    @Transactional
    public void init() {
        try {
            entityManager.createNativeQuery("CREATE SCHEMA IF NOT EXISTS " + defaultSchema).executeUpdate();
        } catch (Exception e) {
            System.out.println("Схема: " + defaultSchema + " уже существует.");
        }
    }


    @Transactional
    public void resetDatabase() {

        for(String table : TABLES){
            try{
                entityManager.createNativeQuery("TRUNCATE TABLE " + defaultSchema + "." + table + " CASCADE")
                        .executeUpdate();

            } catch (Exception e) {
                System.err.println("Trancate не сработал для таблицы " + table + ". Ошибка: " + e.getLocalizedMessage());
                try {
                    entityManager.createNativeQuery("DELETE FROM " + defaultSchema + "." + table)
                            .executeUpdate();

                } catch (Exception ex) {
                    System.err.println("Удаление данных из таблицы: " + table + "  не удалось" + ". Ошибка: " + ex.getLocalizedMessage());
                }
            }
        }
    }
}
