package rtl;

/** 
 * An instruction of assignment.
 * @see InstrVisitor
 * @see Instr
 */
public class Assign implements Instr {
	/** The identifier that receives the value of the operand. Left-hand of the assignment.
	 * @see Ident **/
	public final Ident ident;
	/** The operand whose value is copied into the left-hand of the assignment.
	 * Can be literal integer (LitInt) or another variable (Ident)
	 * @see Operand
	 * @see OperandVisitor
	 * @see Ident
	 * @see LitInt **/
	public final Operand operand;

	public Assign(Ident x, Operand o) {
		ident = x;
		operand = o;
	}

	public String toString() {
		return ident.toString()+" = "+operand.toString();
	}   
	
	public <A> A accept(InstrVisitor<A> v) {
		return v.visit(this);
	}
	
	public <A> A accept(DefKindVisitor<A> v) {
		return v.visit(this);
	}

}

