package rtl;

/** The interface of a visitor for terminal instructions from the RTL program.
 * Has a visit method for each cases of instructions: Goto, Branch and Return.
 * The typical use of this interface is to make a class that implements it: {@code class MyEndInstrVisitor implement EndInstrVisitor<ReturnType>}
 * You need to implement each {@code visit} method, for each type of EndInstr.
 * Then you can use it on a terminal instruction, typically with {@code instr.accept(new MyEndInstrVisitor());}.
 * The {@code accept} method of the instruction will call the {@code visit} method of your visitor and return the result.
 * @param <V> the type returned by your visitor when it is accepted by an EndInstr
 * @see EndInstr
 * @see Goto
 * @see Branch
 * @see Return
 * **/
public interface EndInstrVisitor<V> {

	public V visit(Goto g);
	public V visit(Branch br);
	public V visit(Return r);	
	
}
