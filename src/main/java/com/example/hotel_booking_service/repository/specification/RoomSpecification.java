package com.example.hotel_booking_service.repository.specification;

import com.example.hotel_booking_service.model.entity.Room;
import com.example.hotel_booking_service.model.entity.UnavailableDate;
import com.example.hotel_booking_service.model.filter.RoomFilter;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RoomSpecification {

    static Specification<Room> withFilter(RoomFilter filter){
        return Specification.where(byId(filter.getId()))
                .and(byDescription(filter.getDescription()))
                .and(byPriceBetween(filter.getMinPrice(), filter.getMaxPrice()))
                .and(byPeopleCount(filter.getCountPeople()))
                .and(byHotelId(filter.getHotelId()))
                .and(byAvailableDates(
                        filter.getArrivalDate(),
                        filter.getDepartureDate()
                ));

    }

    static Specification<Room> byId(Long id){
        return (root, query, criteriaBuilder) -> {
            if(id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    static Specification<Room> byDescription(String description){
        return (root, query, criteriaBuilder) -> {
            if(description == null || description.isBlank()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.get("description")), "%" + description
                            .toLowerCase() + "%");
        };
    }

    static Specification<Room> byPriceBetween(BigDecimal minPrice, BigDecimal maxPrice){
        return (root, query, criteriaBuilder) -> {
            if(minPrice == null && maxPrice == null){
                return criteriaBuilder.conjunction();
            }
            if(minPrice != null && maxPrice != null){
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
        };
    }

    static Specification<Room> byPeopleCount(Integer count){
        return (root, query, criteriaBuilder) -> {
            if(count == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.greaterThanOrEqualTo(root.get("maxPeople"), count);
        };
    }

    static Specification<Room> byAvailableDates(
            LocalDate arrival,
            LocalDate departure
    ) {
        return (root, query, cb) -> {

            if (arrival == null || departure == null) {
                return cb.conjunction();
            }

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<UnavailableDate> ud = subquery.from(UnavailableDate.class);

            subquery.select(cb.literal(1L))
                    .where(
                            cb.equal(ud.get("room"), root),
                            cb.between(
                                    ud.get("unavailableDate"),
                                    arrival,
                                    departure.minusDays(1)
                            )
                    );

            return cb.not(cb.exists(subquery));
        };
    }

    static Specification<Room> byHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) return cb.conjunction();
            return cb.equal(root.get("hotel").get("id"), hotelId);
        };
    }
}
