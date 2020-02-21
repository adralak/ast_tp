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
	 * Expression arithmetiques et logiques.
	 */
	private static class BuiltInExpr implements Expr {
		/**
		 * Opérateur arithmetique ou logique.
		 */
		final String operator; 
		
		/**
		 * Arguments de l'expression.
		 */
		final List<Operand> args;
		
		/**
		 * Construction d'une expression.
		 * @param operator L'opérateur arithmetique ou logique.
		 * @param args Les arguments de l'expression.
		 */
		BuiltInExpr(String operator, List<Operand> args) {
			this.operator = operator;
			this.args = args;
		}
		
		/**
		 * Comparaison *logique* de deux expressions.
		 */
		@Override public boolean equals(Object o) {
			if (!(o instanceof BuiltInExpr)) return false;
			BuiltInExpr e = (BuiltInExpr) o;
			return operator.equals(e.operator) && args.equals(e.args);
		}
		
		@Override public int hashCode() {
			return (operator.hashCode() + 31 * args.hashCode());
		}
		
		@Override public String toString() {	
			String s = args.toString();
			return operator+"("+s.substring(1, s.length()-1)+")";	
		}	
		
		public boolean containsIdent(Ident id) {
			return args.contains(id);
		}
	}	
	
	/**
	 * Lecture en mémoire.
	 */
	private static class ReadExpr implements Expr {
		//Il vous faudra déterminer les champs d'instance
		//Il vous faudra determiner les méthodes qui nécessitent une redéfinition
		//TODO
		
		public boolean containsIdent(Ident id) {
			return false; //TODO
		}
	}

	/**
	 * L'ensemble des expression disponibles en entrée de chaque noeud.
	 */
	private Map<Node,Set<Expr>> aeIn = new Hashtable<>();
	
	/**
	 * L'ensemble des expression disponibles en sortie de chaque noeud.
	 */
	private Map<Node,Set<Expr>> aeOut = new Hashtable<>();
	
	/**
	 * Permet d'afficher les itérations de l'algorithme de calcul de point fixe.
	 */
	private final DataFlowDebug debug;
	
	/**
	 * La fonction analysée.
	 */
	private Function f;
	
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
		  if(!oaeIn.get(n).equals(aeIn.get(n)) || !oaeOut.get(n).equals(oaeOut.get(n)))
		       return false;
	     }
	     return true;
	}	
	
}
