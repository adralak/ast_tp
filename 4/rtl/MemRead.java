package rtl;

/** An instruction for memory read.
 * @see InstrVisitor
 * @see Instr
 * @see MemWrite
 * @see MemRef
 */
public class MemRead implements Instr {
	/** The value read in memory will be stored in this Ident */
	public final Ident ident;
	/** The location in memory that this instruction reads.
	 * @see BuiltIn */
	public final MemRef memRef;

	public MemRead(Ident ident, MemRef memRef) {
		this.ident = ident;
		this.memRef = memRef;
	}

	public String toString() {
		return ident.toString()+" = "+memRef.toString();
	}
	
	public <A> A accept(InstrVisitor<A> v) {
		return v.visit(this);
	}
	
	public <A> A accept(DefKindVisitor<A> v) {
		return v.visit(this);
	}

}

