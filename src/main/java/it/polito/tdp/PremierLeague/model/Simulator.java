package it.polito.tdp.PremierLeague.model;

import java.security.PublicKey;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.PremierLeague.model.Event.EventType;

public class Simulator {
	
//	Dati in ingresso
	private Graph<Player, DefaultWeightedEdge> graph;
	private Map<Integer, Player> idMap;
	private Match m;
	private int N;
	
	
//	Dati in uscita
	private int goal1;
	private int goal2;
	private int espulsi;
	
	
//	Modello del mondo
	private int nGiocatori1;
	private int nGiocatori2;
	private int azioniSalienti;
	
	
//	Coda degli eventi
	private PriorityQueue<Event> queue;
	
	public Simulator(Graph<Player, DefaultWeightedEdge> graph, Map<Integer, Player> idMap) {
		this.graph = graph;
		this.idMap = idMap;
	}
	
	public void init(Match m, int N) {
		this.m = m;
		this.N = N;
		
//		Inizializzo gli output
		this.goal1 = 0;
		this.goal2 = 0;
		this.espulsi = 0;
		
//		Inizializzo il mondo
		this.nGiocatori1 = 11;
		this.nGiocatori2 = 11;
		this.azioniSalienti = 0;
		
		this.queue = new PriorityQueue<>();
		while(this.azioniSalienti < N) {
			double prob = Math.random();
			if(prob < 0.2) 
				this.queue.add(new Event(0, EventType.INFORTUNIO, null));
			else if(prob >= 0.2 && prob < 0.5)
				this.queue.add(new Event(0, EventType.ESPULSIONE, null));
			else if(prob >= 0.5 && prob < 1)
				this.queue.add(new Event(0, EventType.GOAL, null));
		}
	}
	
	public void run() {
		while(!queue.isEmpty()) {
			Event event = this.queue.poll();
			processEvent(event);
		}
	}

	private void processEvent(Event event) {
		EventType type = event.getType();
		
		switch (type) {
		case GOAL:
			if(nGiocatori1 > nGiocatori2)
				this.goal1++;
			else if(nGiocatori1 < nGiocatori2)
				this.goal2++;
			else if(nGiocatori1 == nGiocatori2) {
				
			}
				
			break;
		case ESPULSIONE:
			break;
		case INFORTUNIO:
			break;

		default:
			break;
		}
		
	}
}
