package rtl;

public class MemRead implements Instr {
	public final Ident ident;
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

