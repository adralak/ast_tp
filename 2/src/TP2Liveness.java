import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import rtl.*;
import rtl.graph.FlowGraph;
import rtl.graph.DiGraph.Node;

/**
 * Analyse de la durée de vie des variables pour un {@link rtl.graph.FlowGraph} donné.
 */
public class TP2Liveness implements AbstractLiveness {
	/**
	 * Le {@link rtl.graph.FlowGraph} sur lequel est éfféctué l'analyse.
	 */
	private FlowGraph g;
	
	/**
	 * Associe pour chaque noeud, l'ensemble des variables vivantes à son entrée.
	 */
	private Map<Node,Set<Ident>> liveIn = new Hashtable<Node,Set<Ident>>();
	
	/**
	 * Associe pour chaque noeud, l'ensemble des variables vivantes à sa sortie.
	 */
	private Map<Node,Set<Ident>> liveOut = new Hashtable<Node,Set<Ident>>();
	
	/**
	 * Utilisé pour l'affichage du calcul itératif.
	 */
	private final DataFlowDebug debug;

	/**
	 * Initialisation de l'analyse à partir d'un graphe.
	 * @param g Le graphe sur lequel effectuer l'analyse.
	 * @param debug Un débogueur pour visualiser le déroulement de l'analyse.
	 */
	public TP2Liveness(FlowGraph g, DataFlowDebug debug) {
		this.g = g;
		this.debug = debug;
	}

	/**
	 * Initialisation de l'analyse à partir d'un graphe.
	 * @param g Le graphe sur lequel effectuer l'analyse.
	 */
	public TP2Liveness(FlowGraph g) {
		this.g = g;
		this.debug = null;
	}

	/**
	 * Effectue l'analyse.
	 */
	public void build() {
		// Pour chaque noeud, on commence par lui allouer une place dans nos tables associatives.
		for (Node n : g.nodes()) { 
			liveIn.put(n, new HashSet<Ident>());
			liveOut.put(n, new HashSet<Ident>()); 
		}
		
		// Effectue une passe de l'analyse.
		onePass();
		
		// Continue l'analyse tant qu'un point fixe n'a pas été trouvé.
		while (!isFixedPoint()) 
			onePass();		
	}

	/**
	 * Effectue une passe de l'analyse.
	 */
	public void onePass() {
		for (Node n : g.nodes()) {
			//TODO
		}
		
		// On utilise le débogueur pour afficher (plus tard) ce qu'il s'est passé pendant cette passe.
		if (debug!=null) {
			debug.recordCurrentMaps(liveIn, liveOut); // ne pas modifier
		}
	}

	/**
	 * Determine si un point fixe a été trouvé.
	 * @return true si rien n'a changé entre les deux derniers appels à {@link #onePass()}.
	 */
	private boolean isFixedPoint() {
		return true; //TODO
	}

	/**
	 * @param n Un noeud du graphe.
	 * @return La liste des variables vivantes à l'entrée du noeud {@code n}.
	 */
	public Set<Ident> liveIn(Node n) {
		return liveIn.get(n);
	}

	/**
	 * @param n Un noeud du graphe.
	 * @return La liste des variables vivantes à la sortie du noeud {@code n}.
	 */
	public Set<Ident> liveOut(Node n) {
		return liveOut.get(n);
	}
}
