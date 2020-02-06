package rtl;

/** An instruction for memory write.
 * @see InstrVisitor
 * @see Instr
 * @see MemRead
 * @see MemRef
 */
public class MemWrite implements Instr {
	/** The location in memory that this instruction reads.
	 * @see BuiltIn */
	public final MemRef memRef;
	/** The value of this operand will be written by this instruction at the memory space pointed by MemRef. */
	public final Operand operand;

	public MemWrite(MemRef mr, Operand o) {
		memRef = mr;
		operand = o;
	}

	public String toString() {
		return memRef.toString()+" = "+operand.toString();
	}

	public <A> A accept(InstrVisitor<A> v) {
		return v.visit(this);
	}

	public <A> A accept(DefKindVisitor<A> v) {
		throw new rtl.interpreter.ErrorException("Invalid visitor DefKindVisitor on MemWrite instance");
	}

}


