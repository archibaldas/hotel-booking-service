package com.example.hotel_booking_service.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RatingCalculator {

    private static double getTotalRating(double rating, int numberOfRating){
        return rating * numberOfRating;
    }

    private static double getNewTotalRating(double totalRating, double rating, int newMark){
        return  totalRating - rating + newMark;
    }

    public static double getNewRating(int numberOfRating, double rating, int newMark){
        double totalRating = getTotalRating(rating, numberOfRating);
        double newTotalRating = getNewTotalRating(totalRating, rating, newMark);
        double newRating = Math.round((newTotalRating * 10)/numberOfRating);
        return newRating/10.0;
    }


}
