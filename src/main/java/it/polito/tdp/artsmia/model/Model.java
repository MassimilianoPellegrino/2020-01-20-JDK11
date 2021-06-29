package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<Author, DefaultWeightedEdge> grafo;
	private Map<Integer, Author> idMap;
	ArtsmiaDAO dao = new ArtsmiaDAO();
	List<Arco> listaArchi;
	List<Author> bestSol;
	double bestW;
	
	public void creaGrafo(String role) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
				
		Graphs.addAllVertices(grafo, dao.getVertici(role, idMap));
		listaArchi = dao.getArchi(role, idMap);
		
		for(Arco a: listaArchi) {
			//if(grafo.vertexSet().contains(a.getA1()) && grafo.vertexSet().contains(a.getA1()))
				Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), a.getW());
		}
		
		//System.out.println(grafo.vertexSet().size()+"\n"+grafo.edgeSet().size());
	}
	
	public List<Arco> getCoppie(){
		
		listaArchi.sort(new Comparator<Arco>() {

			@Override
			public int compare(Arco o1, Arco o2) {
				// TODO Auto-generated method stub
				return o2.getW()-o1.getW();
			}
		});
		
		return listaArchi;
	}
	
	public List<String> getRuoli(){
		return dao.getRuoli();
	}

	public Graph<Author, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}


	public int getBestW() {
		return (int) bestW;
	}
	
	public List<Author> getCammino(int idAutore) {
		
		Author a = idMap.get(idAutore);
		
		bestSol = new ArrayList<>();
		List<Author> partSol = new ArrayList<>();
		partSol.add(a);
		double partW = 0;
		
		itera(partSol, partW, a, a);
		
		return bestSol;
		
		
	}
	
	
	private void itera(List<Author> partSol, double partW, Author first, Author last) {
		
		last = partSol.get(partSol.size()-1);
				
		int size = Graphs.neighborListOf(grafo, last).size();
		int x = 0;
		
		for(Author a: Graphs.neighborListOf(grafo, last)) {
			
			if(!partSol.contains(a)) {
				if(first.equals(last))
					partW = grafo.getEdgeWeight(grafo.getEdge(last, a));
				
				if(grafo.getEdgeWeight(grafo.getEdge(last, a)) == partW) {
					partSol.add(a);
					itera(partSol, partW, first, last);
					partSol.remove(a);
				}else
					x++;
			}else
				x++;
		}
		
		if(x==size) {
			//System.out.println(x);
			if(partSol.size()>bestSol.size()) {
				bestSol = new ArrayList<>(partSol);
				bestW = partW;
			}
			return;
		}
		
	}
	
	
	
	

}
