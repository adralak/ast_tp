package rtl;

/** A terminal instruction from the RTL program 
 * Can be a Goto, a Branch or a Return
 * @see Block 
 * @see Instr
 * @see EndInstrVisitor
 * **/
public interface EndInstr {
	
	/** Apply a visitor on the terminal instruction.
	 * This method calls the {@code visit} method of the visitor that corresponds to its type (goto, branch or return).
	 * It then returns the results of this visitor.
	 * Typical use: {@code instr.accept(new MyEndInstrVisitor()) }
	 * @see EndInstrVisitor
	 * @param v The visitor
	 * @param <V> The type of objects returned by the visitor
	 * @return The result from the visitor **/
	public <V> V accept(EndInstrVisitor<V> v);
	
}

