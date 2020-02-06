package rtl;

import java.util.ArrayList;

import java.util.List;

/**
 * A substitution of a {@link Program} identifiers.
 * 
 * To implement a substitution, create a class that inherits from this one,
 * and redefine the method {@link #transform(Ident)}.
 */
public class SimpleTransform extends Transform {
	/**
	 * Apply the substitution on the entire program.
	 * @param p The program to transform.
	 */
	public static void transform(Program p) {
		for (Function f : p.functions) 
			new SimpleTransform().transform(f);// modifie (en place) chaque fonction
	}

	/**
	 * Identifier substitution.
	 * 
	 * Any references to the ident {@code id} in the program will be replaced by the {@link Ident} returned by this method.
	 * @param id The original identifier to replace. Can be null.
	 * @return An identifier to replace the original one. Can be the same as {@code id}.
	 * */
	public Ident transform(Ident id) {
		return id;
	}

	private class Visitor implements OperandVisitor<Operand> {

		public Operand visit(Ident id) {
			return transform(id);
		}

		public Operand visit(LitInt li) {
			return li;
		}

	}

	private Operand transform(Operand op) {
		if (op==null) return null;
		else return op.accept(new Visitor());
	}

	private List<Operand> transform(List<Operand> ops) {
		List<Operand> res = new ArrayList<>();
		for (Operand op: ops) res.add(transform(op));
		return res;	
	}

	final public TransformInstrResult transform(Assign a) {
		return new TransformInstrResult(new Assign(transform(a.ident), transform(a.operand)));
	}


	final public TransformInstrResult transform(BuiltIn bi) {
		return new TransformInstrResult(new BuiltIn(bi.operator, transform(bi.target), transform(bi.args)));
	}

	final public TransformInstrResult transform(Call c) {
		return new TransformInstrResult(new Call(transform(c.target), c.getCallee(), transform(c.args)));
	}

	final private MemRef transform(MemRef mr) {
		return new MemRef(transform(mr.ident), mr.offset);
	}

	final public TransformInstrResult transform(MemRead mr) {
		return new TransformInstrResult(new MemRead(transform(mr.ident), transform(mr.memRef)));
	}

	final public TransformInstrResult transform(MemWrite mw) {
		return new TransformInstrResult(new MemWrite(transform(mw.memRef), transform(mw.operand)));
	}

	final public TransformEndInstrResult transform(Goto g) {
		return new TransformEndInstrResult(g);
	}

	final public TransformEndInstrResult transform(Branch br) {
		return new TransformEndInstrResult(new Branch(transform(br.condition), br.thenTarget, br.elseTarget));
	}

	final public TransformEndInstrResult transform(Return ret) {
		return new TransformEndInstrResult(new Return(transform(ret.operand)));
	}

}
