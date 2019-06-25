package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private Graph<ArtObject,DefaultWeightedEdge> graph;
	private ArtsmiaDAO dao;
	private List<ArtObject> objects;
	private Map<Integer, ArtObject> idMap;
	public Model() {
		dao = new ArtsmiaDAO();
		idMap = new HashMap<>();
	}
	
	
	public void creaGrafo() {
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		dao.listObjects(idMap);
		Graphs.addAllVertices(graph, idMap.values());
		
		for(Adiacenza a1 : dao.getAdiacenze()) {
			Graphs.addEdge(graph, idMap.get(a1.getO1()), idMap.get(a1.getO2()), a1.getPeso());
			}
		
		System.out.println("VERTICI:\n"+graph.vertexSet().size());
		System.out.println("ARCHI:"+graph.edgeSet().size());
	}
/*
 * public void creaGrafo() {
		listaOggetti = dao.listObjects(idMap);
		Graphs.addAllVertices(grafo,idMap.values());
		
		listaCoppie = dao.listCoppie();
		
		for(Coppie c:listaCoppie) {
			ArtObject source = idMap.get(c.getO1());
			ArtObject fine = idMap.get(c.getO2());
			Graphs.addEdge(grafo, source, fine, c.getPeso());
		}
	}
 * */

	public int getVertexSize() {
		// TODO Auto-generated method stub
		return graph.vertexSet().size();
	}


	public int getEdgeSize() {
		// TODO Auto-generated method stub
		return graph.edgeSet().size();
	}


	public boolean objisValid(int n) {
		if(!this.graph.vertexSet().contains(n))
		return false;
		else return true;
	}

	
	public int calcolaDimensioneCC(int n) {
		ArtObject start = trovaVertice(n);

		// visita il grafo
		Set<ArtObject> visitati = new HashSet<>();
		DepthFirstIterator<ArtObject, DefaultWeightedEdge> dfv = new DepthFirstIterator<>(this.graph, start);
		while (dfv.hasNext())
			visitati.add(dfv.next());

		// conta gli elementi
		return visitati.size();
	}

	private ArtObject trovaVertice(int idOggetto) {
		// trova il vertice di partenza
				ArtObject start = null;
				for (ArtObject ao : idMap.values()) {
					if (ao.getId() == idOggetto) {
						start = ao;
						break;
					}
				}
				if (start == null)
					throw new IllegalArgumentException("Vertice " + idOggetto + " non esistente");
				return start;
	}
	
	}

