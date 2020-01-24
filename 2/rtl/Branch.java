package rtl;

/**
 * A terminal instruction for branching.
 * @see EndInstrVisitor
 * @see EndInstr
 */
public class Branch implements EndInstr {

	/** The operand that determines whether to jump to the block {@code thenTarget} if its value is other than 0, or to the {@code elseTarget}.
	 * @see Operand 
	 * @see OperandVisitor
	 * @see Block
	 * **/ 
	public final Operand condition;
	public final Block thenTarget;
	public final Block elseTarget;
	
	public Branch(Operand condition, Block thenTarget, Block elseTarget) {
		this.condition = condition;
		this.thenTarget = thenTarget;
		this.elseTarget = elseTarget;
	}

	public String toString() {
		return "if "+condition.toString()+
				" goto "+thenTarget.label.toString()+
				" else "+elseTarget.label.toString();
	}
	
	public <V> V accept(EndInstrVisitor<V> v) {
		return v.visit(this);
	}
	
}
