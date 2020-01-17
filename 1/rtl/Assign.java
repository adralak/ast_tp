package rtl;

public class Assign implements Instr {
	public final Ident ident;
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

