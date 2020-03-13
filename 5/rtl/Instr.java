package rtl;

/** An instruction from the RTL program 
 * Can be an Assign, a BuiltIn (built-in operation), a Call, a MemRead (memory read) or a MemWrite (memory write).
 * @see Block 
 * @see EndInstr
 * @see InstrVisitor
 * **/
public interface Instr extends DefKind {	
	
	/** Apply a visitor on the instruction.
	 * This method calls the {@code visit} method of the visitor that corresponds to its type (assign, builtin, etc).
	 * It then returns the results of this visitor.
	 * Typical use: {@code instr.accept(new MyInstrVisitor()) }
	 * @see InstrVisitor
	 * @param v The visitor
	 * @param <A> The type of objects returned by the visitor
	 * @return The result from the visitor **/
	public <A> A accept(InstrVisitor<A> v);

	public <A> A accept(DefKindVisitor<A> v);

}

