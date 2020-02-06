import java.util.Map;
import java.util.Set;

import rtl.*;
import rtl.graph.DiGraph.Node;
import rtl.graph.RtlCFG;

/**
 * Propagation de constantes.
 * 
 * Cette transformation utilise le résultat de l'analyse des définissions possibles pour
 * simplifier le programme et remplacer certains usages de variables par des constantes.
 * 
 * Par exemple, dans le code RTL suivant :
 * 
 * <pre>{@code
 * x = 41
 * y = Add(x 1)
 * }</pre>
 * 
 * la seule définition possible pour la variable {@code x} est une constante.
 * Il est alors possible de remplacer les usages associés à cette définition
 * par cette constante.
 * 
 * <pre>{@code
 * x = 41
 * y = Add(41 1)
 * }</pre>
 * 
 * Notez que cette transformation ne s'occupe pas de supprimer la définition de {@code x}
 * devenue inutile. Cela sera réalisé plus tard par la transformation {@link TP3DeadDefElim}.
 */
public class TP3ConstPropReachDef extends Transform {
	/**
	 * Transformation d'un programme entier.
	 * @param p Le programme à transformer.
	 */
	public static void transform(Program p) {
		// On fait la transformation pour chaque fonction.
		for (Function f : p.functions) {
			RtlCFG cfg = new RtlCFG(f);
			TP3ReachableDef reachDef = new TP3ReachableDef(cfg);
			new TP3ConstPropReachDef(cfg, reachDef.useDef()).transform(f);
		}
	}

	/**
	 * Le graphe de flot de contrôle de la fonction à transformer.
	 */
	private RtlCFG cfg;
	
	/**
	 * La relation "UseDef" entre les usages en chaque noeud et leur définitions possibles associées.
	 */
	private Map<Node,Map<Ident,Set<Node>>> useDef;

	/**
	 * Création d'une transformation de propagation de constantes.
	 * @param cfg Le graphe de flot de contrôle de la fonction à transformer.
	 * @param useDef La relation usage-definitions de la fonction.
	 */
	TP3ConstPropReachDef(RtlCFG cfg, Map<Node,Map<Ident,Set<Node>>> useDef) {
		this.cfg = cfg;
		this.useDef = useDef;
	}
	
	/**
	 * Transformation d'une instruction d'affectation.
	 */
	public TransformInstrResult transform(Assign a) {
		// TODO
		// Création de la nouvelle instruction (TODO)
		Instr newInstr = new Assign(a.ident, a.operand); // ceci est juste un exemple !

		// Mise à jour du cfg avec cette nouvelle instruction pour le nœud
		Node n = cfg.node(a);
		// La classe RtlCFG étend FlowGraph en ajoutant des méthodes
        //   public Node node(Instr i) 
        //   public Node node(EndInstr i) 
        // qui vous permettent de récupérer les noeuds associés à des instructions.
        // La recherche effectuée se base sur des tests d'égalité physique donc
        // deux instructions égales structurellement peuvent renvoyer deux 
        // noeuds différents.
		
		cfg.updateInstr(n, newInstr);
		// La classe RtlCFG ajoute aussi deux méthodes
        //   public void updateInstr(Node n, Instr newInstr) 
        //   public void updateInstr(Node n, EndInstr newInstr)
        // qui vous permettent de mettre à jour un cfg avec une nouvelle instruction.
		// Le noeud n doit déjà exister et la nouvelle instruction doit suivre le même
		// flot de controle que l'ancienne.
		
		return new TransformInstrResult(newInstr); // allez voir la déclaration de TransformInstrResult
	}

	// TODO : une rédéfinition de chaque méthode transform de la classe Transform
}
