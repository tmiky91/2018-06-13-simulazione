package it.polito.tdp.flightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Airport;
import it.polito.tdp.flightdelays.model.Flight;
import it.polito.tdp.flightdelays.model.Rotta;

public class FlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT id, airline from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getString("ID"), rs.getString("airline")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirports(Map<String, Airport> idMap) {
		String sql = "SELECT id, airport, city, state, country, latitude, longitude FROM airports";
		List<Airport> result = new ArrayList<Airport>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(!idMap.containsKey(rs.getString("id"))) {
					Airport airport = new Airport(rs.getString("id"), rs.getString("airport"), rs.getString("city"),
							rs.getString("state"), rs.getString("country"), rs.getDouble("latitude"), rs.getDouble("longitude"));
					result.add(airport);
					idMap.put(rs.getString("id"), airport);
				}else {
					result.add(idMap.get(rs.getString("id")));
				}
				
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights() {
		String sql = "SELECT id, airline, flight_number, origin_airport_id, destination_airport_id, scheduled_dep_date, "
				+ "arrival_date, departure_delay, arrival_delay, air_time, distance FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
						rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
						rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
						rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Rotta> getRotte(Map<String, Airport> idMap, Airline a) {
		final String sql=	"select f.ORIGIN_AIRPORT_ID as id1, a1.LATITUDE as lat1, a1.LONGITUDE as long1, f.DESTINATION_AIRPORT_ID as id2, a2.LATITUDE as lat2, a2.LONGITUDE as long2, avg(f.ARRIVAL_DELAY) as media " + 
							"from flights as f, airports as a1, airports as a2 " + 
							"where f.AIRLINE=? " + 
							"and f.ORIGIN_AIRPORT_ID > f.DESTINATION_AIRPORT_ID " + 
							"and a1.ID=f.ORIGIN_AIRPORT_ID " + 
							"and a2.ID=f.DESTINATION_AIRPORT_ID " + 
							"group by id1, id2";
		List<Rotta> rotte = new LinkedList<>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, a.getId());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport a1 = idMap.get(rs.getString("id1"));
				Airport a2 = idMap.get(rs.getString("id2"));
				if(a1!=null && a2!=null) {
					Rotta rotta = new Rotta(a1, new LatLng(rs.getDouble("lat1"), rs.getDouble("long1")), a2, new LatLng(rs.getDouble("lat2"), rs.getDouble("long2")), rs.getDouble("media"));
					rotte.add(rotta);
				}
			}

			conn.close();
			return rotte;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
}
