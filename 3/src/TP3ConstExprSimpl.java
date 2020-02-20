import rtl.*;
import rtl.graph.RtlCFG;
import java.util.List;
import java.util.ArrayList;
import java.lang.Integer;
import rtl.graph.DiGraph.Node;
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
		hasChanged = false;
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
		TP3InstrVisitor iv = new TP3InstrVisitor();
		Instr new_instr = instr.accept(iv);
		Node n = cfg.node(instr);
		hasChanged = hasChanged || !(new_instr.equals(instr));

		if(new_instr != null)
		     cfg.updateInstr(n, new_instr);
		
		return new TransformInstrResult(new_instr);
	}


	private class TP3OpVisitor implements OperandVisitor<LitInt> {
		public LitInt visit(Ident id) {
			return null;
		}
		public LitInt visit(LitInt li) {
			return li;
		}
	}

	private class TP3InstrVisitor implements InstrVisitor<Instr> {
		public Instr visit(Assign a) {
			return a;
		}
		public Instr visit(BuiltIn bi) {
			Integer new_val = null;
			Ident _ident = bi.target;
			String _op = bi.operator;
			List<Operand> args = bi.args;
			List<Integer> intl = new ArrayList<Integer>();
			for (Operand opi : args) {
				TP3OpVisitor ov = new TP3OpVisitor();
				LitInt i = opi.accept(ov);
				if (i == null) {return bi;}
				intl.add((Integer) i.getVal());
			}

			if (new_val == null) {
				if (_op.equals("Add")) {
					new_val =  (intl.get(0) + intl.get(1));
				}
				else if (_op.equals("Sub")) {
					new_val =  (intl.get(0) - intl.get(1));
				}
				else if (_op.equals("Mul")) {
					new_val =  (intl.get(0) * intl.get(1));
				}
				else if (_op.equals("And")) {
					if (intl.get(0).equals(1) && intl.get(0).equals(1)) {
						new_val =  1;
					}
					else {
						new_val =  0;
					}
				}
				else if (_op.equals("Lt")) {
					if (intl.get(0) <= intl.get(1)) {
						new_val =  1;
					}
					else {
						new_val =  0;
					}
				}
				else {
					new_val = intl.get(0);
				}
			}

			if (new_val == null) {
				return bi;
			}
			else if(_ident == null)
			{
			     List<Operand> new_args = new ArrayList<Operand>();
			     new_args.add(new LitInt(new_val));
			     BuiltIn new_bi = new BuiltIn(_op, null, new_args);
			     return bi;
			}
			Assign a = new Assign(_ident, new LitInt(new_val.intValue()));
			return a;
		}
		public Instr visit(Call c) {
			return c;
		}
		public Instr visit(MemRead mr) {
			return mr;
		}
		public Instr visit(MemWrite mw) {
			return mw;
		}


	}
}
