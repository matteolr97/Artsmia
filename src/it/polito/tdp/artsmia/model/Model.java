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
	private Map<Integer, ArtObject> idMap;
	List<ArtObject> best = null;
	
	public Model() {
		idMap = new HashMap<Integer,ArtObject>();
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public void creaGrafo() {
		ArtsmiaDAO dao = new ArtsmiaDAO();
		dao.listObjects(idMap);
		
		//aggiungo i vertici
		Graphs.addAllVertices(graph, idMap.values());
		
		//aggiungo gli archi
		List<Adiacenza> adj = dao.listAdiacenze();
		
		for(Adiacenza a : adj) {
			ArtObject source = idMap.get(a.getO1());
			ArtObject dest = idMap.get(a.getO2());
			Graphs.addEdge(graph, source, dest, a.getPeso());
		}
		
		System.out.println("Grafo creato: " + graph.vertexSet().size() + 
				" vertici e " + graph.edgeSet().size() + " archi");
		
	}

	public int getVertexSize() {
		return graph.vertexSet().size();
	}
	
	public int getEdgeSize() {
		return graph.edgeSet().size();
	}

	public boolean isObjIdValid(int idOggetto) {
		if (this.idMap.values() == null)
			return false;

		for (ArtObject ao : idMap.values()) {
			if (ao.getId() == idOggetto)
				return true;
		}
		return false;
	}

	public int calcolaDimensioneCC(int idOggetto) {
		ArtObject start = trovaVertice(idOggetto);

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
	// SOLUZIONE PUNTO 2

		public List<ArtObject> camminoMassimo(int startId, int LUN) {
			// trova il vertice di partenza
			ArtObject start = trovaVertice(startId);

			List<ArtObject> parziale = new ArrayList<>();
			parziale.add(start);

			this.best = new ArrayList<>();
			best.add(start);

			cerca(parziale, 1, LUN);

			return best;

		}

		private void cerca(List<ArtObject> parziale, int livello, int LUN) {
			if (livello == LUN) {
				// caso terminale
				if (peso(parziale) > peso(best)) {
					best = new ArrayList<>(parziale);
					System.out.println(parziale);
				}
				return;
			}

			// trova vertici adiacenti all'ultimo
			ArtObject ultimo = parziale.get(parziale.size() - 1);

			List<ArtObject> adiacenti = Graphs.neighborListOf(this.graph, ultimo);

			for (ArtObject prova : adiacenti) {
				if (!parziale.contains(prova) && prova.getClassification() != null
						&& prova.getClassification().equals(parziale.get(0).getClassification())) {
					parziale.add(prova);
					cerca(parziale, livello + 1, LUN);
					parziale.remove(parziale.size() - 1);
				}
			}

		}

		private int peso(List<ArtObject> parziale) {
			int peso = 0;
			for (int i = 0; i < parziale.size() - 1; i++) {
				DefaultWeightedEdge e = graph.getEdge(parziale.get(i), parziale.get(i + 1));
				int pesoarco = (int) graph.getEdgeWeight(e);
				peso += pesoarco;
			}
			return peso;
		}
}
