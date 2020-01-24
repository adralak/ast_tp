import rtl.graph.FlowGraph;
import rtl.graph.RtlCFG;
import rtl.graph.AbstractColorGraph;
import rtl.graph.AbstractInterferenceGraph;
import rtl.graph.ColorGraph;
import rtl.*;

/**
 * Transformation du programme fusionnant les variables non interférantes.
 */
public class TP2MergeIdent extends SimpleTransform {
	/**
	 * La fonction à transformer.
	 */
	Function f;
	
	/**
	 * Le graphe d'interférance de la fonction.
	 */
	AbstractInterferenceGraph igraph;
	
	/**
	 * La coloration du graphe.
	 */
	AbstractColorGraph cg;

	/**
	 * Construction de la transformation à partir des information d'interférance des variables.
	 * @param f La fonction à transformer.
	 * @param igraph Le graphe d'interférance de la fonction {@code f}.
	 * @param cg La coloration du graphe {@code igraph}.
	 */
	public TP2MergeIdent(Function f, AbstractInterferenceGraph igraph, AbstractColorGraph cg) {
		this.f = f;
		this.igraph = igraph;
		this.cg = cg;
	}

	/**
	 * Transforme un programme entier.
	 * @param p Le programme à transformer.
	 */
	public static void transform(Program p) {
		for (Function f : p.functions) { 
			// Construction du graphe de flôt de controle de la fonction.
			FlowGraph g = new RtlCFG(f);
			
			// Analyse du temps de vie des variables.
			AbstractLiveness live = new TP2Liveness(g);
			live.build();
			
			// Construction du graphe d'interférance.
			AbstractInterferenceGraph igraph = new TP2InterferenceGraph(g,live);
			
			// Coloration du graphe d'interférance.
			AbstractColorGraph cg = new ColorGraph(igraph);
			
			// Application de la transformation de la fonction.
			new TP2MergeIdent(f,igraph,cg).transform(f); // chaque fonction est modifiée en place
		}
	}

	/**
	 * Transformation d'un identifiant.
	 * @param id L'identifiant d'une variable.
	 * @return Le nouvel identifiant de la variable après transformation.
	 */
	@Override
	public Ident transform(Ident id) {
		// Il vous suffit de redefinir le comportement de cette fonction pour transformer tous
		// les idents d'un programme
		return id;//TODO à modifier
	}

}
