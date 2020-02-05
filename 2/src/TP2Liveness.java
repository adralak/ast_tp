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


     // Used to store the old liveIns and liveOuts
     Map<Node,Set<Ident>> oliveIn = new Hashtable<Node,Set<Ident>>();
     Map<Node,Set<Ident>> oliveOut = new Hashtable<Node,Set<Ident>>();

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
	  while (isNotFixedPoint())
	       onePass();
     }

     /**
      * Effectue une passe de l'analyse.
      */
     public void onePass() {
	  System.out.println("-----");
	  for (Node n : g.nodes()) {
	       // Remember the old values
	       oliveIn.put(n, new HashSet<Ident>(liveIn.get(n)));
	       oliveOut.put(n, new HashSet<Ident>(liveOut.get(n)));
	       // Prepare the new sets
	       Set<Ident> nOut = new HashSet<Ident>();
	       Set<Ident> nIn = new HashSet<Ident>();
	       // Build nOut
	       for (Node n_n : n.succ()) {
		    nOut.addAll(new HashSet<Ident>(liveIn.get(n_n)));
	       }
	       // Build nIn
	       nIn.addAll(new HashSet<Ident>(liveOut.get(n)));
	       nIn.removeAll(g.def(n));
	       nIn.addAll(g.use(n));

	       // Store nIn and nOut in the current Maps
	       liveOut.put(n,new HashSet<Ident>(nOut));
	       liveIn.put(n,new HashSet<Ident>(nIn));

	       //System.out.println("oldIn :" + oliveIn.get(n));
	       //System.out.println("newIn :" + liveIn.get(n));
	       //System.out.println("oldOut :" + oliveOut.get(n));
	       //System.out.println("newOut :" + liveOut.get(n));

	       //System.out.println(is_done);
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
     private Boolean isNotFixedPoint() {
	  for(Node n : g.nodes())
	  {
	       if(oliveIn.get(n).equals(liveIn.get(n)) && oliveOut.get(n).equals(liveOut.get(n)))
		    continue;
	       else
		    return true;
	  }

	  return false;
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
