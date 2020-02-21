package rtl;

/** The interface of a visitor for operands.
 * Has a visit method for each cases of operands: Ident and LitInt.
 * The typical use of this interface is to make a class that implements it: {@code class MyInstrVisitor implement InstrVisitor<ReturnType>}
 * You need to implement each {@code visit} method, for each type of Operand.
 * Then you can use it on an operand, typically with {@code op.accept(new MyOperandVisitor());}.
 * The {@code accept} method of the operand will call the {@code visit} method of your visitor and return the result.
 * @param <A> the type returned by your visitor when it is accepted by an Operand
 * @see Operand
 * @see Ident
 * @see LitInt
 * **/
public interface OperandVisitor<V> {

	public V visit(Ident id);
	public V visit(LitInt li);
	
}
