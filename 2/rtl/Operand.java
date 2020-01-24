package rtl;

/** An operand is either an Ident or a LitInt
 * @see Ident
 * @see LitInt
 * @see OperandVisitor
 */
public interface Operand {

	/** Apply a visitor on the operand.
	 * This method calls the {@code visit} method of the visitor that corresponds to its type (ident or literal integer).
	 * It then returns the results of this visitor.
	 * Typical use: {@code op.accept(new MyOperandVisitor()) }
	 * @see OperandVisitor
	 * @param v The visitor
	 * @param <A> The type of objects returned by the visitor
	 * @return The result from the visitor **/
	public <V> V accept(OperandVisitor<V> v);
	
}
