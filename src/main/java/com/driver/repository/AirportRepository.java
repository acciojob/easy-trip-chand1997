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

    public boolean addAirport(Airport airport){
        if(airport.getAirportName()==null || airport.getCity()==null || airport.getNoOfTerminals()==0) return false;
        airportDb.put(airport.getAirportName(),airport);
        return true;
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
        if(airportNames.size()==0) return null;
        return airportNames.get(0);
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity,City toCity){
        if(fromCity==null || toCity==null) return 0;
        double shortestDuration=Double.MAX_VALUE;

        if(!flightDb.isEmpty()){
            for(Flight f: flightDb.values()){
                if(f.getToCity()!=null && f.getFromCity()!=null){
                    if(f.getFromCity().equals(fromCity) && f.getToCity().equals(toCity)){
                       if(f.getDuration()!=0) shortestDuration=Math.min(shortestDuration,f.getDuration());
                    }
                }
            }
        }

        if(shortestDuration!=Double.MAX_VALUE) return shortestDuration;
        return -1;

    }

    public int getNumberOfPeopleOn( Date date,String airportName){
        if(!airportDb.containsKey(airportName) || airportDb.get(airportName).getCity()==null) return 0;
        if(date==null) return 0;
        City city=airportDb.get(airportName).getCity();


        int totalPeople=0;

        if(!flightDb.isEmpty()){
            for(Flight f: flightDb.values()){
                if(f.getFlightDate()!=null && f.getFlightDate().equals(date)){
                    if((f.getFromCity()!=null && city.equals(f.getFromCity())) ||
                            (f.getToCity()!=null && city.equals(f.getToCity()))) totalPeople++;
                }
            }
        }



        return totalPeople;
    }

    public int calculateFlightFare(Integer flightId){
        if(!flightDb.containsKey(flightId) || !flightBookingDb.containsKey(flightId)) return 0;

        return 3000+flightBookingDb.get(flightId).size()*50;

    }

    public String bookATicket(Integer flightId,Integer passengerId){
        if(!flightDb.containsKey(flightId) || !passengerDb.containsKey(passengerId)) return "FAILURE";

        if(!flightBookingDb.containsKey(flightId)) flightBookingDb.put(flightId,new ArrayList<>());

        if(flightBookingDb.get(flightId).size()>=flightDb.get(flightId).getMaxCapacity()) return "FAILURE";


        if(flightBookingDb.get(flightId).contains(passengerId))  return "FAILURE";

        flightBookingDb.get(flightId).add(passengerId);



        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId,Integer passengerId){
        if(!flightDb.containsKey(flightId) || !passengerDb.containsKey(passengerId)) return null;
        if(!flightBookingDb.containsKey(flightId)) return null;
        if(!flightBookingDb.get(flightId).contains(passengerId)) return "FAILURE";


        flightBookingDb.get(flightId).remove(passengerId);



        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){
        if(!passengerDb.containsKey(passengerId)) return 0;
        int totalBookings=0;

        if(!flightBookingDb.isEmpty()){
            for(List<Integer> list:flightBookingDb.values()){
                for(Integer id:list){
                    if(passengerId==id) totalBookings++;
                }
            }
        }
        return totalBookings;
    }

    public String addFlight(Flight flight){
       if(flight.getFlightId()==0) return "FAILURE";
       if(flightDb.containsKey(flight.getFlightId())) return null;
       flightDb.put(flight.getFlightId(),flight);
       return "SUCCESS";
    }

    public String getAirportNameFromFlightId(Integer flightId){
        if(!flightDb.containsKey(flightId)) return null;

        if(!airportDb.isEmpty()){
            for(Airport airport:airportDb.values()){
                City c=airport.getCity();
                City fromCity=flightDb.get(flightId).getFromCity();
                if(c!=null && fromCity!=null && c.equals(fromCity)) return airport.getAirportName();
            }
        }

        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId){
        if(!flightBookingDb.containsKey(flightId) || flightBookingDb.get(flightId).size()==0) return 0;

        int  n=flightBookingDb.get(flightId).size();


        int totalRevenue=0;
        for(int i=0;i<n;i++){
            totalRevenue+=(3000+i*50);
        }
        return totalRevenue;
    }

    public String addPassenger(Passenger passenger){
        if(passenger.getPassengerId()==0) return "FAILURE";
        if(passengerDb.containsKey(passenger.getPassengerId())) return null;
        passengerDb.put(passenger.getPassengerId(),passenger);
        return "SUCCESS";
    }

}

