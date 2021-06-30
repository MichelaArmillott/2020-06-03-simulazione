package it.polito.tdp.PremierLeague.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private PremierLeagueDAO dao;
	private SimpleDirectedWeightedGraph<Player,DefaultWeightedEdge>grafo;
	private Map<Integer,Player>idMap;
	private List<Player>dreamTeam;
	public int titMaggiore;
	
	public Model() {
		dao=new PremierLeagueDAO();
		idMap=new HashMap<>();
		dao.listAllPlayers(idMap);
	}
	public void creaGrafo(double media) {
		grafo=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		//vertici
		Graphs.addAllVertices(grafo, dao.getVertici(idMap,media));
		//ARCHI
		for(Adiacenza a: dao.getArchi(idMap,media)) {
			if(grafo.containsVertex(a.getP1())&&grafo.containsVertex(a.getP2())) {
				DefaultWeightedEdge e=grafo.getEdge(a.getP1(), a.getP2());
				if(e==null) {
					if(a.getPeso()>0) {
						Graphs.addEdgeWithVertices(grafo, a.getP1(), a.getP2(), a.getPeso());
					}
					else {
						Graphs.addEdgeWithVertices(grafo, a.getP2(), a.getP1(), a.getPeso()*(-1));}
				}
			}
		}
		
	}
	
	
	public int getNVertici() {
		if(grafo != null)
		return grafo.vertexSet().size();
		return 0;}
					
		public int getNArchi() {
		if(grafo != null)
		return grafo.edgeSet().size();
		return 0;}
		
		public SimpleDirectedWeightedGraph<Player,DefaultWeightedEdge>getGrafo(){
			return grafo;}
		      
		//provo ricorsione
		   //metodo pubblico
		public List<Player>trovaDt(int k){
			dreamTeam=null;
			List<Player>parziale=new ArrayList<>();
			cerca(parziale,new ArrayList<Player>(grafo.vertexSet()),k);
			return dreamTeam;
			
		}
          //metodo privato
		private void cerca(List<Player> parziale, List<Player> giocatoriGrafo, int k) {
			//caso terminale
			if(parziale.size()==k) {
				int titParziale=this.getTitol(parziale);
				if(dreamTeam==null) {
					dreamTeam=new ArrayList<>(parziale);
					this.titMaggiore=titParziale;
					return;
				}
				else if(titParziale>this.titMaggiore) {
					dreamTeam=new ArrayList<>(parziale);
					this.titMaggiore=titParziale;
					return;
				}
				else return;}
				//genero parziale
				
			
			
			for(Player p : giocatoriGrafo) {
				if(!parziale.contains(p)) {
					parziale.add(p);
					//i "battuti" di p non possono più essere considerati
					List<Player> remainingPlayers = new ArrayList<>(giocatoriGrafo);
					remainingPlayers.removeAll(Graphs.successorListOf(grafo, p));
					cerca(parziale, remainingPlayers, k);
					parziale.remove(p);
					
				}
			}
		}
		
		public int getTitol(List<Player>squadra){
			int gradoTit=0;
			for(Player p:squadra) {
				double uscente=0.0;
				double entrante=0.0;
				for(DefaultWeightedEdge e:grafo.outgoingEdgesOf(p))
					uscente=uscente+grafo.getEdgeWeight(e);
				for(DefaultWeightedEdge e:grafo.incomingEdgesOf(p))
					entrante=entrante+grafo.getEdgeWeight(e);
				gradoTit+=(int)(uscente-entrante);}
				return gradoTit;
			
		}
		public Player best() {
		Player best=null;
		int sconfitti=0;
		for(Player p:grafo.vertexSet()) {
			if(grafo.outDegreeOf(p)>sconfitti) {
				best=p;
				sconfitti=grafo.outDegreeOf(p);
			}
		}return best;
		}
		
		public List<Battuti>battuti(Player best){
			List<Battuti>stampa=new ArrayList<>();
			for(DefaultWeightedEdge e:grafo.outgoingEdgesOf(best)) {
				Battuti b=new Battuti(grafo.getEdgeTarget(e),(int)grafo.getEdgeWeight(e));
				stampa.add(b);
			}Collections.sort(stampa);
			return stampa;
		}
}
