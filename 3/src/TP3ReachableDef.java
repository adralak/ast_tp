import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import rtl.graph.FlowGraph;
import rtl.*;
import rtl.graph.DiGraph.Node;

/**
 * Analyse des définitions possibles (Reachable Definitions) en chaque point d'un programme.
 */
public class TP3ReachableDef {
	/**
	 * Définition Possible (Reachable Definition).
	 * Une définition possible est un couple formé d'un identifiant de variable,
	 * ainsi qu'un noeud contenant une définition pour cette variable.
	 * 
	 * Le noeud peut prendre la valeur {@code null} lorsque la variable n'est pas initialisée.
	 */
	private class RDPair {
		/**
		 * Identifiant de la variable.
		 */
		final Ident ident;
		
		/**
		 * Noeud associé dans le CFG contenant une définition pour la variable.
		 */
		final Node node;
		
		/**
		 * Construit une définition possible.
		 * @param ident L'identifiant de la variable définie.
		 * @param node Un noeud du CFG contenant une définition pour la variable.
		 */
		RDPair(Ident ident, Node node) {
			this.ident = ident;
			this.node = node;
		}
		
		/**
		 * Méthode de comparaison pour pouvoir utiliser {@link RDpair} dans un {@code Set<RDPair>}.
		 */
		@Override public boolean equals(Object o) {
			if (!(o instanceof RDPair)) return false;
			RDPair p = (RDPair) o;
			return this.ident.equals(p.ident) && this.node.equals(p.node);
		}
		
		/**
		 * Méthode de Hash pour pouvoir utiliser {@link RDpair} dans un {@code Set<RDPair>}.
		 */
		@Override public int hashCode() {
			return this.ident.hashCode() + 31 * this.node.hashCode();
		}
		
		/**
		 * Formatage en chaîne de caractères.
		 */
		@Override public String toString() {
			return "("+this.ident+", "+this.node+")";
		}		
	}
	
	/**
	 * Met en relation un noeud à son ensemble de définitions possibles en entrée.
	 * Correspond à DPin dans le cours.
	 */
	private Map<Node,Set<RDPair>> rdIn = new Hashtable<>();
	
	/**
	 * Met en relation un noeud à son ensemble de définitions sortantes.
	 * Correspond à DPout dans le cours.
	 */
	private Map<Node,Set<RDPair>> rdOut = new Hashtable<>();
	
	/**
	 * Utilisé pour afficher la progression de l'algorithme (à la fin).
	 */
	private final DataFlowDebug debug;
	
	/**
	 * Le graphe de flot de contrôle du programme analysé.
	 */
	private FlowGraph g;
	
	/**
	 * Met en relation les noeuds utilisant une variable avec les noeuds définissant cette variable.
	 * Cet ensemble est calculé "sur demande" lorsque la méthode {@link #useDef()} est appelée.
	 */
	private Map<Node,Map<Ident,Set<Node>>> useDef;
	
	/**
	 * Met en relation les noeuds définissant une variable avec les noeuds utilisant cette définition.
	 * Cet ensemble est calculé "sur demande" lorsque la méthode {@link #defUse()} est appelée.
	 */
	private Map<Node,Map<Ident,Set<Node>>> defUse;

	/**
	 * Construit l'analyse des définitions possibles en chaque point d'un programme.
	 * @param g Le graphe de flot de contrôle du programme.
	 * @param debug Un débogueur pour suivre l'exécution de l'algorithme a posteriori.
	 */
	public TP3ReachableDef(FlowGraph g, DataFlowDebug debug) {
		this.g = g;
		this.debug = debug;
		build();
	}

	/**
	 * Construit l'analyse des définitions possibles en chaque point d'un programme.
	 * @param g Le graphe de flot de contrôle du programme.
	 */
	public TP3ReachableDef(FlowGraph g) {
		this(g, null);
	}

	/**
	 * Analyse du programme.
	 */
	private void build() {
		// Initialisation de rdIn et rdOut avec des ensembles vides.
		for (Node n : g.nodes()) { 
			this.rdIn.put(n, new HashSet<>());
			this.rdOut.put(n, new HashSet<>()); 
		}
		
		// Production du point fixe.
		onePass();
		while (!isFixedPoint()) 
			onePass();
	}

	/**
	 * Effectue une passe de l'algorithme.
	 */
	public void onePass() {
		// TODO
		
		// On enregistre l'état courrant de l'algorithme avec le debogueur
		// pour afficher toute l'exécution à la fin.
		if (this.debug!=null) this.debug.recordCurrentMaps(this.rdIn, this.rdOut);
	}

	/**
	 * Détermine si un point fixe a été atteint.
	 * @return true si l'état de l'analyse n'a pas changé entre les deux derniers appels à {@link #onePass()}.
	 */
	public boolean isFixedPoint() {
		return true; // TODO
	}

	/**
	 * Calcule la relation entre les noeuds utilisant une variable avec les noeuds définissant cette variable.
	 */
	public Map<Node,Map<Ident,Set<Node>>> useDef() {
		if (this.useDef != null) return this.useDef; // On ne fait le calcul qu'une seule fois.
		
		// TODO
		
		return this.useDef;
	}

	/**
	 * Calcule la relation entre les noeuds définissant une variable avec les noeuds utilisant cette définition.
	 */
	public Map<Node,Map<Ident,Set<Node>>> defUse() {
		if (this.defUse != null) return this.defUse; // On ne fait le calcul qu'une seule fois.
		
		// TODO
		
		return this.defUse;
	}
}
