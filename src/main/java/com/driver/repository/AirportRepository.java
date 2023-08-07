package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;
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
        if(airport==null) return false;
        if(airport.getAirportName()==null || airport.getCity()==null || airport.getNoOfTerminals()==0) return false;
        if(airportDb.containsKey(airport.getAirportName())) return false;
        airportDb.put(airport.getAirportName(),airport);
        return true;
    }

    public String getLargestAirportName(){
        int maxTerminals=0;

        if(!airportDb.isEmpty()){
            for(Airport a:airportDb.values()){
                if(a.getNoOfTerminals()!=0) maxTerminals=Math.max(maxTerminals,a.getNoOfTerminals());
            }
        }

        if(maxTerminals==0) return "NOT FOUND";
        List<String> airportNames=new ArrayList<>();

        if(!airportDb.isEmpty()){
            for(String name:airportDb.keySet()){
                if(airportDb.get(name).getNoOfTerminals()==maxTerminals) airportNames.add(name);
            }
        }

        if(airportNames.size()>1) Collections.sort(airportNames);
        if(airportNames.isEmpty()) return "NOT FOUND";
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

   public int getNumberOfPeopleOn(Date date,String airportName){
        System.out.println(date+" "+airportName);
        if(date==null || airportName==null) return 0;
        if(!airportDb.containsKey(airportName) || airportDb.get(airportName).getCity()==null) return 0;

        City city=airportDb.get(airportName).getCity();

        String d1=date.toString().substring(0,10) + date.toString().substring(23,date.toString().length());


        int totalPeople=0;

        if(flightDb.isEmpty()) return 0;
            for(Flight f: flightDb.values()){
                if(f.getFlightDate()!=null){
                    Date datee=f.getFlightDate();
                     String d2=datee.toString().substring(0,10) + datee.toString().substring(23,datee.toString().length());
                    if(d1.equals(d2)){
                        if((f.getFromCity()!=null && city.equals(f.getFromCity())) ||
                                (f.getToCity()!=null && city.equals(f.getToCity())))
                            totalPeople+=flightBookingDb.get(f.getFlightId()).size();
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
        if(flightId==0 || passengerId==0)  return "FAILURE";
        if(flightDb.isEmpty() || flightBookingDb.isEmpty())  return "FAILURE";
        if(!flightDb.containsKey(flightId) || !passengerDb.containsKey(passengerId)) return "FAILURE";
        if(!flightBookingDb.containsKey(flightId)) return "FAILURE";
        if(!flightBookingDb.get(flightId).contains(passengerId)) return "FAILURE";


        flightBookingDb.get(flightId).remove(passengerId);



        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId){
        if(passengerId==0) return 0;
        if(!passengerDb.isEmpty() && !passengerDb.containsKey(passengerId)) return 0;
        int totalBookings=0;

        if(!flightBookingDb.isEmpty()){
            for(List<Integer> list:flightBookingDb.values()){
                if(!list.isEmpty()){
                    for(Integer id:list){
                        if(passengerId==id) totalBookings++;
                    }
                }
            }
        }
        return totalBookings;
    }

    public String addFlight(Flight flight){
        if(flight==null) return "FAILURE";
       if(flight.getFlightId()==0) return "FAILURE";
       if(flightDb.containsKey(flight.getFlightId())) return "FAILURE";
       flightDb.put(flight.getFlightId(),flight);
       return "SUCCESS";
    }

    public String getAirportNameFromFlightId(Integer flightId){
        if(!flightDb.containsKey(flightId)) return "NOT FOUND";

        if(!airportDb.isEmpty()){
            for(Airport airport:airportDb.values()){
                City c=airport.getCity();
                City fromCity=flightDb.get(flightId).getFromCity();
                if(c!=null && fromCity!=null && c.equals(fromCity)) return airport.getAirportName();
            }
        }

        return "NOT FOUND";
    }

    public int calculateRevenueOfAFlight(Integer flightId){
        if(flightId==0) return 0;
        if(!flightBookingDb.containsKey(flightId) || flightBookingDb.get(flightId).isEmpty()) return 0;

        int  n=flightBookingDb.get(flightId).size();


        int totalRevenue=0;
        for(int i=0;i<n;i++){
            totalRevenue+=(3000+i*50);
        }
        return totalRevenue;
    }

    public String addPassenger(Passenger passenger){
        if(passenger==null) return "FAILURE";
        if(passenger.getPassengerId()==0) return "FAILURE";
        if(passengerDb.containsKey(passenger.getPassengerId())) return "FAILURE";
        passengerDb.put(passenger.getPassengerId(),passenger);
        return "SUCCESS";
    }

}

