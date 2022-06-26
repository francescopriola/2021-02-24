package it.polito.tdp.PremierLeague.model;

import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Map<Integer, Player> idMap;
	private Graph<Player, DefaultWeightedEdge> graph;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = dao.listAllPlayers();
	}
	
	
	
	public void creaGrafo(int matchID) {
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.graph, dao.getVertex(matchID, idMap));
		
		for(Arco arco : this.dao.getEdge(matchID, idMap)) {
			if(arco.getP1().getEfficienza() >= arco.getP2().getEfficienza())
				Graphs.addEdgeWithVertices(this.graph, arco.getP1(), arco.getP2(), arco.getDeltaEff());
			else if(arco.getP1().getEfficienza() < arco.getP2().getEfficienza())
				Graphs.addEdgeWithVertices(this.graph, arco.getP2(), arco.getP1(), arco.getDeltaEff());
		}
		
		System.out.println("#Vertici: " + this.graph.vertexSet().size());
		System.out.println("#Archi: " + this.graph.edgeSet().size());
	}
	
	public int getVertici() {
		return this.graph.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.graph.edgeSet().size();
	}
	
	public boolean isGrafo() {
		if(this.graph == null)
			return false;
		else {
			return true;
		}
	}
	
	public BestPlayer giocatoreMigliore() {
		BestPlayer bp = null;
		double max = 0;
		
		for(Player player : this.graph.vertexSet()) {
			
			double pesiUscenti = 0.0;
			double pesiEntranti = 0.0;
			
			for(DefaultWeightedEdge edge : this.graph.outgoingEdgesOf(player))
				pesiUscenti += this.graph.getEdgeWeight(edge);
			
			for(DefaultWeightedEdge edge : this.graph.incomingEdgesOf(player))
				pesiEntranti += this.graph.getEdgeWeight(edge);
			
			double delta = pesiUscenti - pesiEntranti;
			if(delta > max) {
				max = pesiUscenti - pesiEntranti;
				bp = new BestPlayer(player, max);
			}
		}
		
		return bp;
		
	}
	
	public List<Match> getAllMatches(){
		return this.dao.listAllMatches();
	}
	
	
	
	
}
