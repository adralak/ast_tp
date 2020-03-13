package rtl;

/** A terminal instruction that ends the function and potentially returns a value.
 * @see EndInstrVisitor
 * @see EndInstr
 * @see Block
 */
public class Return implements EndInstr {
	/** The value returned, if any */
	public final Operand operand;

	public Return(Operand operand) {
		this.operand = operand;
	}

	public String toString() {
		return "ret "+((operand==null)?"":operand.toString());
	}    
	
	public <V> V accept(EndInstrVisitor<V> v) {
		return v.visit(this);
	}
	
}

