import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import rtl.*;
import rtl.graph.FlowGraph;
import rtl.graph.DiGraph.Node;

/**
 * Analyse de la durée de vie des variables pour un {@link rtl.graph.FlowGraph} donné.
 */
public class TP2Liveness implements AbstractLiveness {
	/**
	 * Le {@link rtl.graph.FlowGraph} sur lequel est effectué l'analyse.
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

    private Map<Node,boolean> is_done = new Hashtable<Node,boolean>();
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
			is_done.put(n, false);
		}

		// Effectue une passe de l'analyse.
		onePass();
		// Continue l'analyse tant qu'un point fixe n'a pas été trouvé.
		while (isNotFixedPoint())
			onePass();
	}

	/**
	 * Effectue une passe de l'analyse.
	 */
	public void onePass() {

		Map<Node,Set<Ident>> oliveIn = new Hashtable<Node,Set<Ident>>();
		Map<Node,Set<Ident>> oliveOut = new Hashtable<Node,Set<Ident>>();

		for (Node n : g.nodes()) {
			oliveIn.put(n, liveIn.get(n).clone());
			oliveOut.put(n, liveOut.get(n).clone());
		}

		for (Node n : g.nodes()) {
			// ensemble des sommets deja visités
			List node_visited = new ArrayList<Node>();
			List node_to_visit = new ArrayList<Node>();
			node_visited.add(n);
			node_to_visit.addAll(n.succ());
			// boolean pour savoir si Lin et Lout ont été modifié
			boolean uLin = true;
			boolean uLout = true;

			liveOut.get(n) = new HashSet<Ident>();

			while(node_to_visit.size() != 0) {
				liveOut.get(n).addAll(liveIn.get(node_to_visit.get(0)));
				node_visited.add(node_to_visit.get(0));
				for (Node n_n : node_to_visit.get(0).succ()) {
					if(!node_visited.contains(n_n)) {
						node_to_visit.add(n_n);
					}
				}
				node_to_visit.remove(0);
			}
			liveIn.get(n) = liveOut.get(n).clone();
			liveIn.get(n).removeAll(g.def(n));
			liveIn.get(n).addAll(g.use(n));

			if (oliveIn.get(n) == liveIn.get(n) && oliveOut.get(n) == liveOut.get(n)) {
				is_done.get(n) = true;
			}

			is_done.get(n) = false;
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
	private boolean isNotFixedPoint() {
	     return is_done.contains(false);
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
