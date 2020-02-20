/*
Authors: Paul Bastide and Yan Garito
*/

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

		public boolean idequals(Ident id) {
			return this.ident.equals(id);
		}

		public Ident getid() {
			return this.ident;
		}

		public Node getnode() {
			return this.node;
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
	 * Conserve les anciennes versions de rdOut et rdIn pour verifier que l'on a atteint le point isFixedPoint
	 */

 	private Map<Node,Set<RDPair>> ordIn = new Hashtable<Node,Set<RDPair>>();
 	private Map<Node,Set<RDPair>> ordOut = new Hashtable<Node,Set<RDPair>>();

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
			this.ordIn.put(n, new HashSet<>());
			this.ordOut.put(n, new HashSet<>());
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
		for (Node n : g.nodes()) {
			//System.out.println("----");
		  // Remember the old values
		  ordIn.put(n, new HashSet<RDPair>(rdIn.get(n)));
		  ordOut.put(n, new HashSet<RDPair>(rdOut.get(n)));
		  // Prepare the new sets
		  Set<RDPair> nOut = new HashSet<RDPair>();
		  Set<RDPair> nIn = new HashSet<RDPair>();
		  // Build nOut
		  for (Node n_n : n.pred()) {
		  	  nIn.addAll(new HashSet<RDPair>(rdOut.get(n_n)));
		  }
		  // Build nIn
		  nOut.addAll(nIn);
		  Set<RDPair> temp_on = new HashSet<RDPair>(nOut);
		  for (Ident id : g.def(n)) {
		  	  for (RDPair rd_n : temp_on) {
				  if (rd_n.idequals(id)) {
					  nOut.remove(rd_n);
				  }
			  }
		  }
		  for (Ident id : g.def(n)) {
			  RDPair p = new RDPair(id,n);
			  nOut.add(p);
		  }
		  // Store nIn and nOut in the current Maps
		  rdOut.put(n,new HashSet<RDPair>(nOut));
		  rdIn.put(n,new HashSet<RDPair>(nIn));
	   }


		// On enregistre l'état courrant de l'algorithme avec le debogueur
		// pour afficher toute l'exécution à la fin.
		if (this.debug!=null) this.debug.recordCurrentMaps(this.rdIn, this.rdOut);
	}

	/**
	 * Détermine si un point fixe a été atteint.
	 * @return true si l'état de l'analyse n'a pas changé entre les deux derniers appels à {@link #onePass()}.
	 */
	public boolean isFixedPoint() {
		for(Node n : g.nodes())
		{
		     if(!ordIn.get(n).equals(rdIn.get(n)) || !ordOut.get(n).equals(rdOut.get(n)))
			  return false;
		}
		return true;
	}

	/**
	 * Calcule la relation entre les noeuds utilisant une variable avec les noeuds définissant cette variable.
	 */
	public Map<Node,Map<Ident,Set<Node>>> useDef() {
		if (this.useDef != null) return this.useDef; // On ne fait le calcul qu'une seule fois.

		this.useDef = new Hashtable<Node,Map<Ident,Set<Node>>>();

		for (Node n : g.nodes()) {
			Map<Ident,Set<Node>> nhash = new Hashtable<>();
			for (Ident id : g.use(n)) {
				Set<Node> nidset = new HashSet<Node>();
				for(RDPair p: this.rdIn.get(n)) {
					if (p.idequals(id)) {
						nidset.add(p.getnode());
					}
				}
				nhash.put(id,nidset);
			}
			this.useDef.put(n,nhash);
		}
		return this.useDef;
	}

	/**
	 * Calcule la relation entre les noeuds définissant une variable avec les noeuds utilisant cette définition.
	 */
	public Map<Node,Map<Ident,Set<Node>>> defUse() {
		if (this.defUse != null) return this.defUse; // On ne fait le calcul qu'une seule fois.

		this.defUse = new Hashtable<Node,Map<Ident,Set<Node>>>();

		for (Node n : g.nodes()) {
			Map<Ident,Set<Node>> nhash = new Hashtable<Ident,Set<Node>>();
			defUse.put(n,nhash);
		}

		for (Node n : g.nodes()) {
			for(RDPair p: this.rdIn.get(n)) {
				Node defnode = p.getnode();
				Ident defid = p.getid();
				if (g.use(n).contains(defid)) {
					if (defUse.get(defnode).containsKey(defid)) {
						defUse.get(defnode).get(defid).add(n);
					}
					else {
						Set<Node> temp = new HashSet<Node>();
						temp.add(n);
						defUse.get(defnode).put(defid,temp);
					}
				}
			}
		}

		return this.defUse;
	}
}
