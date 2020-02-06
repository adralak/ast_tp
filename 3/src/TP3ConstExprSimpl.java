import rtl.*;
import rtl.graph.RtlCFG;

/**
 * Simplification d'expressions.
 * 
 * RTL possède des opération prédéfinies {@link rtl.BuiltIn} dont la sémantique est connue.
 * Il est donc possible de calculer statiquement un appel à un de ces opérateurs
 * lorsque les opérandes sont des constantes.
 * 
 * Par exemple :
 * <pre>{@code
 * x = Mul(2 21)
 * y = Add(41 1)
 * }</pre>
 * 
 * peut être simplifié en
 * 
 * <pre>{@code
 * x = 42
 * y = 42
 * }</pre>
 */
public class TP3ConstExprSimpl extends Transform {
	/**
	 * Transformation d'un programme entier.
	 * @param p Le programme à transformer.
	 */
	public static void transform(Program p) {
		// On fait la transformation pour chaque fonction.
		for (Function f : p.functions) {
			RtlCFG cfg = new RtlCFG(f);
			new TP3ConstExprSimpl(cfg).transform(f);
		}
	}

	/**
	 * Le graphe de flot de contrôle de la fonction à transformer.
	 */
	private RtlCFG cfg;
	
	/**
	 * Determine si la transformation a changé une partie du programme ou non.
	 */
	private boolean hasChanged;

	/**
	 * Construit une transformation pour une fonction.
	 * @param cfg Le grphe de flot de contrôle de la fonction.
	 */
	TP3ConstExprSimpl(RtlCFG cfg) {
		this.cfg = cfg;
	}

	/**
	 * @return true si la transformation a effectivement changé le programme, false sinon.
	 */
	public boolean hasChanged() {
		return this.hasChanged;
	}

	/**
	 * Transforme une instruction prédéfinie.
	 * @param instr L'instruction prédéfinie à transformer.
	 */
	public TransformInstrResult transform(BuiltIn instr) {
		// TODO
		return new TransformInstrResult(instr);
	}
}
