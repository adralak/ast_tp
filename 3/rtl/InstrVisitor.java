package rtl;

/** The interface of a visitor for instructions from the RTL program.
 * Has a visit method for each cases of instructions: Assign, BuiltIn (built-in operation), Call, MemRead (memory read) and MemWrite (memory write).
 * The typical use of this interface is to make a class that implements it: {@code class MyInstrVisitor implement InstrVisitor<ReturnType>}
 * You need to implement each {@code visit} method, for each type of Instr.
 * Then you can use it on a instruction, typically with {@code instr.accept(new MyInstrVisitor());}.
 * The {@code accept} method of the instruction will call the {@code visit} method of your visitor and return the result.
 * @param <A> the type returned by your visitor when it is accepted by an Instr
 * @see Instr
 * @see Assign
 * @see BuiltIn
 * @see Call
 * @see MemRead
 * @see MemWrite
 * **/
public interface InstrVisitor<A> {
	
	public A visit(Assign a);
	public A visit(BuiltIn bi);
	public A visit(Call c);
	public A visit(MemRead mr);
	public A visit(MemWrite mw);

}
