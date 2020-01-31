import java.util.Hashtable;
import java.util.Map;

import rtl.AbstractLiveness;
import rtl.Ident;
import rtl.graph.AbstractInterferenceGraph;
import rtl.graph.FlowGraph;
import rtl.graph.Graph.Node;
import rtl.graph.DiGraph;

/**
 * Graphe d'interference entre les variables du programme.
 */
public class TP2InterferenceGraph extends AbstractInterferenceGraph {

     	private Map<Ident,Node> id_to_node = new Hashtable<Ident,Node>();
     	private Map<Node,Ident> node_to_id = new Hashtable<Node,Ident>();

	/**
	 * Construit un graphe d'interference à partir d'un {@link rtl.graph.FlowGraph}
	 * et d'une analyse des temps de vie des variables.
	 * @param g Le graphe de flot de contrôle du programme.
	 * @param live Information du temps de vie des variables du programme.
	 */
	public TP2InterferenceGraph(FlowGraph g, AbstractLiveness live) {
	     Node x, y;
	     
	     for(DiGraph.Node n : g.nodes())
	     {
		  for(Ident id : g.def(n))
		  {
		       x = get_node(id);

		       for(Ident other_id : live.liveOut(n))
		       {
			    if(!id.equals(other_id))
			    {
				 y = get_node(other_id);

				 if(!x.adj(y))
				 {
				      addEdge(x, y);
				      addEdge(y, x);
				 }
			    }
		       }
		  }
	     }
	}

     private Node get_node(Ident id)
	  {
	       if(id_to_node.containsKey(id))
		    return id_to_node.get(id);
	       else
	       {
		    Node n = new Node();
		    id_to_node.put(id, n);
		    node_to_id.put(n, id);

		    return n;
	       }
	  }
     
	/**
	 * @param n Un noeud du graphe d'interference.
	 * @return L'identifiant de la variable associée à ce noeud.
	 */
	public Ident ident(Node n) { 
	     return node_to_id.get(n);
	}
	
	/**
	 * @param n Un identifiant de variable.
	 * @return Le noeud associé à cette variable.
	 */
	public Node node(Ident id) {
	     return id_to_node.get(id);
	}
		
}
