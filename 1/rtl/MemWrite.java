package rtl;

public class MemWrite implements Instr {
	public final MemRef memRef;
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


