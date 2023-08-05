package com.driver.service;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AirportService {


    @Autowired
    AirportRepository airportRepository;

    public boolean addAirport(Airport airport){
       if(airportRepository.addAirport(airport)) return true;
       return false;
    }

    public String getLargestAirportName(){
        return airportRepository.getLargestAirportName();
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity){
       return  airportRepository.getShortestDurationOfPossibleBetweenTwoCities(fromCity,toCity);

    }

    public int getNumberOfPeopleOn(Date date, String airportName){
       return airportRepository.getNumberOfPeopleOn(date,airportName);
    }

    public int calculateFlightFare(Integer flightId){
        return airportRepository.calculateFlightFare(flightId);
    }

    public String bookATicket(Integer flightId,Integer passengerId){
        return airportRepository.bookATicket(flightId,passengerId);
    }

    public String cancelATicket(Integer flightId,Integer passengerId){
       return airportRepository.cancelATicket(flightId,passengerId);
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){
        return airportRepository.countOfBookingsDoneByPassengerAllCombined(passengerId);
    }

    public String addFlight(Flight flight){
        return airportRepository.addFlight(flight);
    }

    public String getAirportNameFromFlightId(Integer flightId){
        return airportRepository.getAirportNameFromFlightId(flightId);
    }

    public int calculateRevenueOfAFlight(Integer flightId){
        return airportRepository.calculateRevenueOfAFlight(flightId);
    }

    public String addPassenger(Passenger passenger){
        return airportRepository.addPassenger(passenger);
    }
}
