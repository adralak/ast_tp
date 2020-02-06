import java.util.Map;
import java.util.Set;

import rtl.*;
import rtl.graph.RtlCFG;
import rtl.graph.DiGraph.Node;

/**
 * Simplification de fonction.
 * 
 * Cette transformation combine la propagation de constantes {@link TP3ConstPopReachDef}
 * et la simplification d'expressions {@link TP3ConstExprSimpl} pour
 * simplifier au maximum le programme.
 * 
 * À l'issue de la transformation, certaines definitions peuvent devenir inutiles
 * et peuvent être éliminées avec la transformation {@link TP3DeadDefElim}.
 */
public class TP3ConstSimplification extends Transform {
	/**
	 * Transformation d'un programme entier.
	 * @param p Le programme à transformer.
	 */
	public static void transform(Program p) {
		// On fait la transformation pour chaque fonction.
		for (Function f : p.functions) {
			new TP3ConstSimplification(f).transform(f);
		}
	}
	
	/**
	 * Construit et effectue la transformation sur une 
	 * @param f
	 */
	TP3ConstSimplification(Function f) {
		// Calcul du graphe de flot de contrôle de la fonction.
		RtlCFG cfg = new RtlCFG(f);
		
		// Analyse des définitions possible en chaque point du programme.
		TP3ReachableDef reachDef = new TP3ReachableDef(cfg);
		Map<Node,Map<Ident,Set<Node>>> useDef = reachDef.useDef(); // On récupère la relation entre les usages et leurs définitions.
        
        TP3ConstPropReachDef optProp = new TP3ConstPropReachDef(cfg, useDef);
        boolean hasChanged = false;
        
        do {
        	optProp.transform(f);
        	TP3ConstExprSimpl optExp = new TP3ConstExprSimpl(cfg);
            optExp.transform(f);
            hasChanged = optExp.hasChanged();
        } while (hasChanged);
	}
}
