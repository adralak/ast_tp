import rtl.*;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rtl.graph.RtlCFG;
import rtl.graph.DiGraph.Node;
import rtl.graph.FlowGraph;

/**
 * Analyse des expressions disponibles.
 * 
 * Associe à chaque noeud l'ensemble des expression disponibles.
 */
public class TP4AvailableExpressions {

	/**
	 * Expression.
	 * 
	 * On utilise une interface particulière pour représenter une expression
	 * utilisée dans une instruction {@link rtl.Instr}.
	 * 
	 * Cette interface est implémentée par les deux types d'expressions considérées
	 * dans ce TP :
	 * 	- Les expression arithmetiques et logiques ({@link rtl.BuiltIn} sauf {@code Alloc} et {@code Print}).
	 *  - Les lectures en mémoire.
	 *  
	 * Dans les deux cas, on ne s'intéresse qu'à la partie droite de l'affectation : l'expression.
	 */
	private static interface Expr {
		/**
		 * Determine si une variable (un identifiant) est utilisée par l'expression.
		 */
		boolean containsIdent(Ident id);
	}

	/**
	/**
	 * Le graphe de flot de controle de la fonction analysée.
	 */
	private FlowGraph cfg;

	public TP4AvailableExpressions(Function f, FlowGraph cfg, DataFlowDebug debug) {
		this.f = f;
		this.cfg = cfg;
		this.debug = debug;
		build();
	}

	public TP4AvailableExpressions(Function f, FlowGraph cfg) {
		this(f,cfg,null);
	}

	private void build() {
		Set<Expr> init = new HashSet<>();//TODO
		//Attention cette initialisation est plus complexe que
		//dans les TP précédents
		
		for (Node n : cfg.nodes()) { 
			aeIn.put(n, init);
			aeOut.put(n, init);			
		}	
		onePass();
		while (!isFixedPoint()) {
			onePass();		
		}
	}

	private void onePass() {
		for (Node n : cfg.nodes()) {
			//TODO
			//Il vous faudra relire et compléter (pour gen et kill) le slide 33 du CM4
			//en vous aidant de l'exemple présenté slide 32
		}		
		if (debug!=null) debug.recordCurrentMaps(aeIn, aeOut);
	}

	private boolean isFixedPoint() {
	     for(Node n : cfg.nodes())
	     {
		  if(!oaeIn.get(n).equals(aeIn.get(n)) || !oaeOut.get(n).equals(aeOut.get(n)))
		       return false;
	     }
	     return true;
	}	
	
}
