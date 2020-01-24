import java.util.Hashtable;
import java.util.Map;

import rtl.AbstractLiveness;
import rtl.Ident;
import rtl.graph.AbstractInterferenceGraph;
import rtl.graph.FlowGraph;
import rtl.graph.Graph.Node;

/**
 * Graphe d'interference entre les variables du programme.
 */
public class TP2InterferenceGraph extends AbstractInterferenceGraph {

	//TODO

	/**
	 * Construit un graphe d'interference à partir d'un {@link rtl.graph.FlowGraph}
	 * et d'une analyse des temps de vie des variables.
	 * @param g Le graphe de flot de contrôle du programme.
	 * @param live Information du temps de vie des variables du programme.
	 */
	public TP2InterferenceGraph(FlowGraph g, AbstractLiveness live) {
		//TODO
	}

	/**
	 * @param n Un noeud du graphe d'interference.
	 * @return L'identifiant de la variable associée à ce noeud.
	 */
	public Ident ident(Node n) { 
		return null; //TODO
	}
	
	/**
	 * @param n Un identifiant de variable.
	 * @return Le noeud associé à cette variable.
	 */
	public Node node(Ident id) { 
		return null; //TODO
	}
		
}
