import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.LinkedList;
import java.util.List;

import rtl.*;
import rtl.constant.*;
import rtl.graph.FlowGraph;
import rtl.graph.DiGraph.Node;

/**
 * Inférence de constantes.
 */
public class TP5ConstantInference {
	/**
	 * Information associée à chaque entrée de noeud.
	 */
	private Map<Node,ConstMap> ctIn = new Hashtable<>();

	/**
	 * Information associée à chaque sortie de noeud.
	 */
	private Map<Node,ConstMap> ctOut = new Hashtable<>();

	/**
	 * Détermine si les informations calculées ont changées entre les deux
	 * dernières itérations. Si non, on est sur un point fixe.
	 */
	private boolean hasChanged = true;

	/**
	 * Pour le débogage.
	 */
	private final DataFlowDebug debug;

	/**
	 * Le graphe de flot de contrôle de la fonction analysée.
	 */
	private FlowGraph g;

	/**
	 * Tous les identifiants de la fonction.
	 */
	private Set<Ident> allVars;

	/**
	 * Effectue l'inférence des constantes à partir d'une fonction, son graphe
	 * de flot de contrôle et un outil de débogage.
	 * @param f     La fonction analysée.
	 * @param g     Le graphe de flot de contrôle de {@code f}.
	 * @param debug Pour le débogage.
	 */
	public TP5ConstantInference(Function f, FlowGraph g, DataFlowDebug debug) {
		this.g = g;
		allVars = AllIdents.compute(f);	// Collecte de tous les identifiants.
		this.debug = debug;
		build();
	}

	/**
	 * Effectue l'inférence des constantes à partir d'une fonction et de son
	 * graphe de flot de contrôle.
	 * @param f     La fonction analysée.
	 * @param g     Le graphe de flot de contrôle de {@code f}.
	 */
	public TP5ConstantInference(Function f, FlowGraph g) {
		this(f, g, null);
	}

	/**
	 * Calcule l'état initial d'un noeud.
	 * @param  n Un nœud.
	 * @return   La propriété vraie à l'entrée de {@code n} au début de
	 *           l'analyse.
	 */
	private ConstMap initial(Node n) {
	     if(n == g.entry())
		  return ConstMap.top(allVars);
	     else
		  return ConstMap.bottom();
	}

	/**
	 * Fonction de transfert.
	 * @param  n  Un nœud.
	 * @param  cm Une propriété vraie en entrée de {@code n}.
	 * @return    Une propriété vraie après l'exécution de l'instruction de
	 *            {@code n}.
	 */
	private ConstMap transfer(Node n, ConstMap cm) {
		// Si la propriété d'entrée est Bottom,
		// alors il n'y a aucune propriété que l'on peut donner en sortie
		if (cm.isBottom()) return cm;

		// On récupère l'instruction
		Object o = this.g.instr(n);
		if (o instanceof Instr) {
			Instr i = (Instr) o;
			Set<Ident> def = g.def(n);
			
			if(def.isEmpty())
			     return cm;

			for(Ident id : def)
			{
			     IntOrTop new_value = i.accept(new TP5InstrVisitor());
			     cm.set(id, new_value);
			}
			
			return cm;
		}
		else return cm; // dans tous les autres cas (EndInstr), pas de changement
	}

	/**
	 * Effectue l'analyse.
	 *
	 * S'arrête lorsqu'un point fixe a été trouvé.
	 */
	private void build() {
		for (Node n : this.g.nodes()) {
			ctIn.put(n, initial(n));
			ctOut.put(n, ConstMap.bottom());
		}
		onePass();
		while (!isFixedPoint())
			onePass();
	}

	/**
	 * Effectue une seule passe de l'analyse.
	 */
	public void onePass() {
		//
		Map<Node,ConstMap> ctInPrevious = new Hashtable<>();
		Map<Node,ConstMap> ctOutPrevious = new Hashtable<>();

		for (Node n : this.g.nodes()) {
			// Copie de l'état précédent
			ctInPrevious.put(n, ctIn.get(n)); // copie avec partage car ConstMap immutable
			ctOutPrevious.put(n, ctOut.get(n)); // copie avec partage car ConstMap immutable

			// Information In du nœud courant
			// X_in(n) = initiale(n) U U_(n' -> n) X_ou(n')
			ConstMap s = initial(n);
			for (Node pred : n.pred())
				s = s.join(ctOut.get(pred));
			ctIn.put(n, s);

			// Information Out du nœud courant
			// X_out(n) = transfer(n)(X_in(n))
			ctOut.put(n, transfer(n,ctIn.get(n)));
		}

		this.hasChanged = !ctIn.equals(ctInPrevious) || !ctOut.equals(ctOutPrevious);

		if (debug!=null) debug.recordCurrentMaps(ctIn, ctOut);
	}

	/**
	 * Détermine si un point fixe a été atteint.
	 * @return {@code true} si l'information calculée n'a pas changée entre les
	 *                      deux derniers appels à {@link #onePass}.
	 */
	public boolean isFixedPoint() {
		return !this.hasChanged;
	}


	private	class TP5InstrVisitor implements InstrVisitor<IntOrTop> {
		    /**
		     * retourne l'expression pour les builtIn et les memRead
		     */

		    public IntOrTop visit(Assign a) {
				Node n = g.node(a);
				if (a.operand instanceof Ident) {
					Ident id = (Ident) a.operand;
					return ctIn.get(n).get(id);
				}
				LitInt li = (LitInt) a.operand;
				return new IntOrTop(li.getVal());
		    }

		    public IntOrTop visit(BuiltIn bi) {
			 	Node n = g.node(bi);
				List<IntOrTop> iot_args = new LinkedList();
				for (Operand op : bi.args) {
					if (op instanceof Ident) {
						Ident id = (Ident) op;
						iot_args.add(ctIn.get(n).get(id));
					}
					else {
						LitInt li = (LitInt) op;
						iot_args.add(new IntOrTop(li.getVal()));
					}
				}
				if (bi.operator == "Add") {
					IntOrTop arg1 = iot_args.get(0);
					IntOrTop arg2 = iot_args.get(1);
					if ( !arg1.isTop() && !arg2.isTop()) {
						return new IntOrTop(arg1.getInt() + arg2.getInt());
					}
					else {
						return IntOrTop.top();
					}
				}
				if (bi.operator == "Sub") {
					IntOrTop arg1 = iot_args.get(0);
					IntOrTop arg2 = iot_args.get(1);
					if (bi.args.get(0).equals(bi.args.get(1))) {
						return new IntOrTop(0);
					}
					else if ( !arg1.isTop() && !arg2.isTop()) {
						return new IntOrTop(arg1.getInt() - arg2.getInt());
					}
					else {
						return IntOrTop.top();
					}
				}
				if (bi.operator == "Mult") {
					IntOrTop arg1 = iot_args.get(0);
					IntOrTop arg2 = iot_args.get(1);
					if (arg1.getInt() == 0 || arg2.getInt() == 0) {
						return new IntOrTop(0);
					}
					if ( !arg1.isTop() && !arg2.isTop()) {
						return new IntOrTop(arg1.getInt() + arg2.getInt());
					}
					else {
						return IntOrTop.top();
					}
				}
		    }

		    public IntOrTop visit(Call c) {
			 return IntOrTop.top();
		    }

		    public IntOrTop visit(MemRead mr) {
			 return IntOrTop.top();
		    }

		    public IntOrTop visit(MemWrite mw) {
			 return null;
		    }
	       }

}
