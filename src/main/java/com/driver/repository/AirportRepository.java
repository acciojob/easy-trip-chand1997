package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.*;

@Repository
public class AirportRepository {
//    HashMap<airportName,Airport>
    HashMap<String, Airport> airportDb=new HashMap<>();

    //    HashMap<flightId,Flight>
    HashMap<Integer, Flight> flightDb=new HashMap<>();

    //    HashMap<flightId,List<passengerId>>
    HashMap<Integer, List<Integer>> flightBookingDb=new HashMap<>();

    //    HashMap<passengerId,Passenger>
    HashMap<Integer,Passenger> passengerDb=new HashMap<>();

    public void addAirport(Airport airport){
        airportDb.put(airport.getAirportName(),airport);
    }

    public String getLargestAirportName(){
        int maxTerminals=0;
        for(Airport a:airportDb.values()){
            maxTerminals=Math.max(maxTerminals,a.getNoOfTerminals());
        }
        List<String> airportNames=new ArrayList<>();
        for(String name:airportDb.keySet()){
            if(airportDb.get(name).getNoOfTerminals()==maxTerminals) airportNames.add(name);
        }
        if(airportNames.size()>1) Collections.sort(airportNames);
        return airportNames.get(0);
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity,City toCity){
        double shortestDuration=Double.MAX_VALUE;
        for(Flight f: flightDb.values()){
            if(f.getFromCity().equals(fromCity) && f.getToCity().equals(toCity)){
                shortestDuration=Math.min(shortestDuration,f.getDuration());
            }
        }
        if(shortestDuration!=Double.MAX_VALUE) return shortestDuration;
        return -1;

    }

    public int getNumberOfPeopleOn( Date date,String airportName){
        City city=airportDb.get(airportName).getCity();
        int totalPeople=0;
        for(Flight f: flightDb.values()){
            if(f.getFlightDate().equals(date)){
                if(city.equals(f.getFromCity()) || city.equals(f.getToCity())) totalPeople++;
            }
        }
        return totalPeople;
    }

    public int calculateFlightFare(Integer flightId){
        return 3000+flightBookingDb.get(flightId).size()*50;
    }

    public String bookATicket(Integer flightId,Integer passengerId){
        if(flightBookingDb.get(flightId).size()>=flightDb.get(flightId).getMaxCapacity()) return "FAILURE";
        if(flightBookingDb.get(flightId).contains(passengerId))  return "FAILURE";
        flightBookingDb.get(flightId).add(passengerId);
        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId,Integer passengerId){
        if(!flightBookingDb.get(flightId).contains(passengerId)) return "FAILURE";
        flightBookingDb.get(flightId).remove(passengerId);
        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){
        int totalBookings=0;
        for(List<Integer> list:flightBookingDb.values()){
            for(Integer id:list){
                if(passengerId==id) totalBookings++;
            }
        }
        return totalBookings;
    }

    public String addFlight(Flight flight){
        flightDb.put(flight.getFlightId(),flight);
        return "SUCCESS";
    }

    public String getAirportNameFromFlightId(Integer flightId){
        for(Airport airport:airportDb.values()){
            if(airport.getCity().equals(flightDb.get(flightId).getFromCity())) return airport.getAirportName();
        }
        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId){
        int n=flightBookingDb.get(flightId).size();
        int totalRevenue=0;
        for(int i=0;i<n;i++){
            totalRevenue+=(3000+i*50);
        }
        return totalRevenue;
    }

    public String addPassenger(Passenger passenger){
        passengerDb.put(passenger.getPassengerId(),passenger);
        return "SUCCESS";
    }

}

