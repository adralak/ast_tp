package rtl;

public interface Instr extends DefKind {	
	
	public <A> A accept(InstrVisitor<A> v);

	public <A> A accept(DefKindVisitor<A> v);

}

