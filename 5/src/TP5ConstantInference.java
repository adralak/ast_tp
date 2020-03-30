import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

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
		  return new ConstMap().top(allVars);
	     else
		  return new ConstMap().bottom();
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
			return null; //TODO 2: définir la propriété de sortie selon n et cm
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

}
