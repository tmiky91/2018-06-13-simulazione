package it.polito.tdp.flightdelays.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flightdelays.db.FlightDelaysDAO;

public class Model {
	
	private Map<String, Airport> idMap;
	private SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	
	public Model() {
		idMap = new HashMap<>();
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
	}

	public List<Airline> getAllAirlines() {
		FlightDelaysDAO dao = new FlightDelaysDAO();
		return dao.loadAllAirlines();
	}

	public String caricaVoli(Airline a) {
		String risultato="";
		FlightDelaysDAO dao = new FlightDelaysDAO();
		dao.loadAllAirports(idMap);
		List<Rotta> rotte = dao.getRotte(idMap, a);
		for(Rotta r: rotte) {
			if(!grafo.containsVertex(r.getA1())) {
				grafo.addVertex(r.getA1());
			}
			if(!grafo.containsVertex(r.getA2())) {
				grafo.addVertex(r.getA2());
			}
			DefaultWeightedEdge edge = grafo.getEdge(r.getA1(), r.getA2());
			if(edge==null) {
				Graphs.addEdgeWithVertices(grafo, r.getA1(), r.getA2(), (r.getMedia()/LatLngTool.distance(r.getCoorA1(), r.getCoorA2(), LengthUnit.KILOMETER)) );
			}
		}
		risultato+="Grafo Creato! Vertici: "+grafo.vertexSet().size()+" Archi: "+grafo.edgeSet().size()+"\n";
		List<DefaultWeightedEdge> lista = new LinkedList<>(grafo.edgeSet());
		Collections.sort(lista, new Comparator<DefaultWeightedEdge>() {

			@Override
			public int compare(DefaultWeightedEdge o1, DefaultWeightedEdge o2) {
				//Devo cambiare valori ai pesi perchè essendo valori molto piccoli castando ad int li porta tutti a 0
				double peso1 = grafo.getEdgeWeight(o1);
				double peso2 = grafo.getEdgeWeight(o2);
				if(peso2<peso1) {
					peso1=1;
					peso2=0;
				}
				else if(peso1<peso2) {
					peso2=1;
					peso1=0;
				}
				return (int) (peso2-peso1);
			}
		});
		int i=0;
		for(DefaultWeightedEdge edge: lista) {
			risultato+="Aeroporto 1: "+grafo.getEdgeSource(edge).getName()+" Aeroporto 2: "+grafo.getEdgeTarget(edge).getName()+" Peso: "+grafo.getEdgeWeight(edge)+"\n";
			i++;
			if(i==10) {
				break;
			}
		}
		return risultato;
	}

}
