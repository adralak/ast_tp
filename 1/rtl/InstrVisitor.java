package rtl;

public interface InstrVisitor<A> {
	
	public A visit(Assign a);
	public A visit(BuiltIn bi);
	public A visit(Call c);
	public A visit(MemRead mr);
	public A visit(MemWrite mw);

}
