/**
@authors Paul Bastide et Yan Garito
 */

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
	  /**
	   * reference mémoire de l'expression
	   */
	  public final MemRef memRef;

	  ReadExpr(MemRef m) {
	       this.memRef = m;
	  }

	  /**
	   * Comparaison logique de deux expressions
	   */
	  @Override public boolean equals(Object o) {
	       if (!(o instanceof ReadExpr)) return false;
	       ReadExpr e = (ReadExpr) o;
	       return (memRef.offset == e.memRef.offset && memRef.ident.equals(e.memRef.ident));
	  }

	  @Override public int hashCode() {
	       return (memRef.hashCode());
	  }

	  @Override public String toString() {
	       return (memRef.toString());
	  }

	  public boolean containsIdent(Ident id) {
	       return (memRef.ident.equals(id));
	  }
     }

     /**
      * L'ensemble des expression disponibles en entrée de chaque noeud.
      */
     private Map<Node,Set<Expr>> aeIn = new Hashtable<>();

     private Map<Node,Set<Expr>> oaeIn = new Hashtable<>();

     /**
      * L'ensemble des expression disponibles en sortie de chaque noeud.
      */
     private Map<Node,Set<Expr>> aeOut = new Hashtable<>();

     private Map<Node,Set<Expr>> oaeOut = new Hashtable<>();

     public Map<Node, Set<Node>> useDef_expr = new Hashtable<Node, Set<Node>>();

     public Map<Node, Set<Node>> useDef_mr = new Hashtable<Node, Set<Node>>();

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
	  // On ajoute TOUTES les expressions calculées dans le programme
	  Set<Expr> init = new HashSet<>();
	  for (Node n : cfg.nodes()) {
	       Object ob = cfg.instr(n);
	       if (ob instanceof Instr) {
		    Instr instr = (Instr) ob;
		    TP4InstrVisitor iv = new TP4InstrVisitor();
		    Expr tp_expr = instr.accept(iv);
		    if (tp_expr != null) {
			 init.add(tp_expr);
		    }
	       }

	  }
	  for (Node n : cfg.nodes()) {
	       aeIn.put(n, init);
	       aeOut.put(n, init);
	       oaeIn.put(n, init);
	       oaeOut.put(n, init);
	  }

	  onePass();
	  while (!isFixedPoint()) {
	       onePass();
	  }

	  make_useDef();
     }

     private void onePass() {
	  for (Node n : cfg.nodes()) {
	       /**
		* On sauvegarde d'abord les ancienne valeur de aeIn et oaeOut
		* la recherche de point fixe se fait entierement avec les ANCIENNES
		* valeur de aeIn et aeOut.
		*/
	       oaeIn.put(n, aeIn.get(n));
	       oaeOut.put(n, aeOut.get(n));

	       Set<Expr> tempOut = new HashSet<Expr>(oaeIn.get(n));

	       /**
		* on retire les expressions utilisant des variables redefinis dans le noeud
		*/
	       for (Ident id : cfg.def(n)) {
		    for (Expr e : oaeIn.get(n)) {
			 if (e.containsIdent(id)) {
			      tempOut.remove(e);
			 }
		    }
	       }
           /* Si on fait un call ou un memwrite (tous ce qui pourrait affecter la memoire)
           on efface toutes les expressions de lecture mémoire */
           if (cfg.instr(n) instanceof MemWrite || cfg.instr(n) instanceof Call ) {
               Set<Expr> to_remove_out = new HashSet<Expr>();
               for (Expr e : tempOut) {
                   if (e instanceof ReadExpr) {
                       to_remove_out.add(e);
                   }
               }
               tempOut.removeAll(to_remove_out);
           }

	       /**
		*
		*/
	       Object ob = cfg.instr(n);
	       if (ob instanceof Instr) {
		    Instr instr = (Instr) ob;
		    TP4InstrVisitor iv = new TP4InstrVisitor();
		    Expr tp_expr = instr.accept(iv);
		    if (tp_expr != null) {
			 tempOut.add(tp_expr);
		    }
	       }
	       Set<Expr> tempIn = new HashSet<Expr>(oaeIn.get(n));
	       if (n.pred().size() == 0) {
		    tempIn = new HashSet<Expr>();
	       }
	       for (Node n_n : n.pred()) {
		    tempIn.retainAll(oaeOut.get(n_n));
	       }

	       aeIn.put(n, tempIn);
	       aeOut.put(n, tempOut);
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

     // Makes the two "useDef" maps which link uses to definitions
     private void make_useDef()
	  {
	       for(Node n : cfg.nodes())
	       {
		    // First, put empty sets inside
		    useDef_expr.put(n, new HashSet<Node>());
		    useDef_mr.put(n, new HashSet<Node>());

		    Object o = cfg.instr(n);
		    // If it's a BuiltIn instruction
		    if(o instanceof BuiltIn)
		    {
			 BuiltIn bi = (BuiltIn) o;
			 BuiltInExpr bie = new BuiltInExpr(bi.operator, bi.args);

			 // If the expression it uses is unavailable, we skip it
			 if(!aeIn.get(n).contains(bie))
			      continue;

			 /* Otherwise, we recursively explore the graph to find its 
			    points of definitions */
			 Set<Node> use = new HashSet<Node>();
			 for(Node p : n.pred())
			 {
			      Set<Node> explored = explorer(p, bie);
			      use.addAll(explored);
			 }

			 useDef_expr.put(n, use);
		    }
		    // Same thing for MemReads
		    else if(o instanceof MemRead)
		    {
			 MemRead mr = (MemRead) o;
			 ReadExpr mre = new ReadExpr(mr.memRef);

			 if(!aeIn.get(n).contains(mre))
			      continue;

			 Set<Node> use = new HashSet<Node>();
			 for(Node p : n.pred())
			 {
			      Set<Node> explored = explorer(p, mre);
			      use.addAll(explored);
			 }

			 useDef_mr.put(n, use);
		    }

	       }
	  }

     // Explores the graph recursively, looking for expression e
	       private Set<Node> explorer(Node n, BuiltInExpr e)
	       {
		    Object o = cfg.instr(n);
		    Set<Node> union = new HashSet<Node>();

		    // This is for safety's sake, but we should never get here
		    if(n.equals(cfg.entry()))
			 return union;

		    // If it's a BuiltIn
		    if(o instanceof BuiltIn)
		    {
			 BuiltIn bi = (BuiltIn) o;
			 BuiltInExpr bie = new BuiltInExpr(bi.operator, bi.args);

			 // And it defines the expression we've been looking for, we return it
			 if(e.equals(bie))
			 {
			      union.add(n);
			      return union;
			 }
		    }

		    // Otherwise, explore the predecessors and merge their sets
		    for(Node p : n.pred())
		    {
			 Set<Node> prev_expl = explorer(p, e);

			 union.addAll(prev_expl);
		    }

		    return union;
	       }

     // Same for MemReads
	       private Set<Node> explorer(Node n, ReadExpr e)
	       {
		    Object o = cfg.instr(n);
		    Set<Node> union = new HashSet<Node>();

		    if(n.equals(cfg.entry()))
			 return union;

		    if(o instanceof MemRead)
		    {
			 MemRead mr = (MemRead) o;
			 ReadExpr mre = new ReadExpr(mr.memRef);

			 if(e.equals(mre))
			 {
			      union.add(n);
			      return union;
			 }
		    }

		    for(Node p : n.pred())
		    {
			 Set<Node> prev_expl = explorer(p, e);

			 union.addAll(prev_expl);
		    }

		    return union;
	       }

     // Return the correct useDef set
	       public Set<Node> useDef(Node n)
	       {
		    if(cfg.instr(n) instanceof BuiltIn)
			 return useDef_expr.get(n);
		    else 
			 return useDef_mr.get(n);
	       }

	       private	class TP4InstrVisitor implements InstrVisitor<Expr> {
		    /**
		     * retourne l'expression pour les builtIn et les memRead
		     */

		    public Expr visit(Assign a) {
			 return null;
		    }

		    public Expr visit(BuiltIn bi) {
			 BuiltInExpr bie = new BuiltInExpr(bi.operator,bi.args);
			 if (bie.operator.equals("Alloc") || bie.operator.equals("PrintInt")) {
			      return null;
			 }
			 return bie;
		    }

		    public Expr visit(Call c) {
			 return null;
		    }

		    public Expr visit(MemRead mr) {
			 ReadExpr re = new ReadExpr(mr.memRef);
			 return re;
		    }

		    public Expr visit(MemWrite mw) {
			 return null;
		    }
	       }
	  }
