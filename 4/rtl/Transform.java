package rtl;

import java.util.ArrayList;

import java.util.List;

/**
 * A transformation of a {@link Program} by substitution.
 * 
 * How each instruction is substitued can be defined by extending this class.
 * The default implementation preserves the instructions.
 * 
 * Note that this transformation cannot substitute blocks,
 * but only works at the instruction level.
 * It is also impossible to change the identifiers of a function's parameters.
 * 
 * The transformation is applied in place.
 **/
public class Transform {
	/**
	 * Apply the transformation on an entire program.
	 * @param p The program to transform.
	 */
	public static void transform(Program p) {
		for (Function f : p.functions) 
			new Transform().transform(f);
	}

	/**
	 * Transform a function.
	 * @param f The function to transform.
	 */
	public final void transform(Function f) {
		// on ne change pas les parametres
		for (Block b : f.blocks) {
			List<Instr> newInstrs = new ArrayList<>();
			for (Instr i : b.instrs) {
				TransformInstrResult transf = i.accept(new TransformInstr());
				newInstrs.addAll(transf.addBefore);
				if (transf.newInstr!=null) newInstrs.add(transf.newInstr);
				newInstrs.addAll(transf.addAfter);
			}

			TransformEndInstr visitor = new TransformEndInstr();
			TransformEndInstrResult res = b.getEnd().accept(visitor);
			newInstrs.addAll(res.addBefore);
			
			b.instrs.clear();
			b.instrs.addAll(newInstrs);
			b.setEnd(res.newInstr);
		}
	}

	/**
	 * The result of an {@link Instr} transformation.
	 */
	public class TransformInstrResult {
		/**
		 * Instructions to add before this instruction.
		 */
		public final List<Instr> addBefore = new ArrayList<>();
		
		/**
		 * The new instruction.
		 */
		final Instr newInstr;
		
		/**
		 * Instructions to add after this instruction.
		 */
		public final List<Instr> addAfter = new ArrayList<>();
		
		/**
		 * Build a new instruction transformation result.
		 * @param newInstr The new instruction.
		 */
		public TransformInstrResult(Instr newInstr) {
			this.newInstr = newInstr;
		}
	}

	/**
	 * The result of an {@link EndInstr} transformation.
	 */
	public class TransformEndInstrResult {
		/**
		 * Instructions to add before the {@link EndInstr} of the block.
		 */
		final List<Instr> addBefore = new ArrayList<>();
		
		/**
		 * New {@link EndInstr} of the block.
		 */
		final EndInstr newInstr;
		
		/**
		 * Build a new end instruction transformation result.
		 * @param newInstr The new end instruction.
		 */
		public TransformEndInstrResult(EndInstr newInstr) {
			this.newInstr = newInstr;
		}
	}

	public TransformInstrResult transform(Assign a) {
		return new TransformInstrResult(a);
	}

	public TransformInstrResult transform(BuiltIn bi) {
		return new TransformInstrResult(bi);
	}

	public TransformInstrResult transform(Call c) {
		return new TransformInstrResult(c);
	}

	public TransformInstrResult transform(MemRead mr) {
		return new TransformInstrResult(mr);
	}

	public TransformInstrResult transform(MemWrite mw) {
		return new TransformInstrResult(mw);
	}

	public TransformEndInstrResult transform(Goto g) {
		return new TransformEndInstrResult(g);
	}

	public TransformEndInstrResult transform(Branch br) {
		return new TransformEndInstrResult(br);
	}

	public TransformEndInstrResult transform(Return ret) {
		return new TransformEndInstrResult(ret);
	}

	private class TransformInstr implements InstrVisitor<TransformInstrResult> {

		public TransformInstrResult visit(Assign a) {
			return transform(a);
		}

		public TransformInstrResult visit(BuiltIn bi) {
			return transform(bi);
		}

		public TransformInstrResult visit(Call c) {
			return transform(c);
		}

		public TransformInstrResult visit(MemRead mr) {
			return transform(mr);
		}

		public TransformInstrResult visit(MemWrite mw) {
			return transform(mw);
		}						
	}

	private class TransformEndInstr implements EndInstrVisitor<TransformEndInstrResult> {

		public TransformEndInstrResult visit(Goto g) {
			return transform(g);
		}

		public TransformEndInstrResult visit(Branch br) {
			return transform(br);
		}

		public TransformEndInstrResult visit(Return r) {
			return transform(r);
		}
	}


}
