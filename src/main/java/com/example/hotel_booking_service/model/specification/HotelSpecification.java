package com.example.hotel_booking_service.model.specification;

import com.example.hotel_booking_service.model.entity.Hotel;
import com.example.hotel_booking_service.model.filter.HotelFilter;
import org.springframework.data.jpa.domain.Specification;

public interface HotelSpecification {

    static Specification<Hotel> withFilter(HotelFilter hotelFilter){
        return Specification.where(byId(hotelFilter.getId()))
                .and(byName(hotelFilter.getName()))
                .and(byTitle(hotelFilter.getTitle()))
                .and(byCity(hotelFilter.getCity()))
                .and(byAddress(hotelFilter.getAddress()))
                .and(byDistanceBetween(hotelFilter.getMinDistanceFromCenter(), hotelFilter.getMaxDistanceFromCenter()))
                .and(byRatingBetween(hotelFilter.getMinRating(), hotelFilter.getMaxRating()))
                .and(byRatingCountBetween(hotelFilter.getMinRatingCount(), hotelFilter.getMaxRatingCount()));
    }

    static Specification<Hotel> byId(Long id){
        return (root, query, criteriaBuilder) -> {
            if(id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    static Specification<Hotel> byName(String name){
        return (root, query, criteriaBuilder) -> {
            if(name == null || name.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.get("name")), "%" + name
                            .toLowerCase() + "%");
        };
    }

    static Specification<Hotel> byTitle(String title) {
        return (root, query, criteriaBuilder) -> {
          if(title == null || title.isBlank()){
              return criteriaBuilder.conjunction();
          }
          return criteriaBuilder
                  .like(criteriaBuilder
                          .lower(root.get("title")), "%" + title
                          .toLowerCase() + "%");
        };
    }

    static Specification<Hotel> byCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if(city == null || city.isBlank()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.get("city")), "%" + city
                            .toLowerCase() + "%");
        };
    }

    static Specification<Hotel> byAddress(String address) {
        return (root, query, criteriaBuilder) -> {
            if(address == null || address.isBlank()){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.get("address")), "%" + address
                            .toLowerCase() + "%");
        };
    }

    static Specification<Hotel> byDistanceBetween(Double min, Double max){
        return (root, query, criteriaBuilder) -> {
            if(min == null && max == null){
                return criteriaBuilder.conjunction();
            }
            if(min != null && max != null){
                return criteriaBuilder.between(root.get("distanceFromCenter"), min, max);
            } else if (min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("distanceFromCenter"), min);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("distanceFromCenter"), max);
            }
        };
    }

    static Specification<Hotel> byRatingBetween(Double min, Double max){
        return (root, query, criteriaBuilder) -> {
            if(min == null && max == null){
                return criteriaBuilder.conjunction();
            }
            if(min != null && max != null){
                return criteriaBuilder.between(root.get("rating"), min, max);
            } else if (min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), min);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("rating"), max);
            }
        };
    }

    static Specification<Hotel> byRatingCountBetween(Integer min, Integer max){
        return (root, query, criteriaBuilder) -> {
            if(min == null && max == null){
                return criteriaBuilder.conjunction();
            }
            if(min != null && max != null){
                return criteriaBuilder.between(root.get("ratingCount"), min, max);
            } else if (min != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("ratingCount"), min);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("ratingCount"), max);
            }
        };
    }
}
