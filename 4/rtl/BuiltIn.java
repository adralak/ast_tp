package rtl;

import java.util.List;
import java.util.Set;

import rtl.interpreter.ErrorException;

import java.util.Arrays;
import java.util.HashSet;

/** An instruction for built-in operations.
 * Built-in operations are the most basic operations that can be performed: addition, multiplication, etc.
 * <dl>
 * 	<dt>Alloc</dt><dd>Allocates some space in memory. The size is given in argument. </dd>
 * 	<dt>PrintInt</dt><dd>Prints the value given in argument into the console.</dd>
 * 	<dt>Add</dt><dd>Adds the two arguments and stores the result in the target Ident.</dd>
 * 	<dt>Sub</dt><dd>Same than Add, but for substraction</dd>
 * 	<dt>Mul</dt><dd>Same than Add, but for multiplication</dd>
 * 	<dt>And</dt><dd>Stores in the target Ident the result of the logical AND of the two arguments. A 0 represents false, and a 1 represents true. Any other value is not a valid boolean.</dd>
 * 	<dt>Lt</dt><dd>Stores 1 (meaning true) in the target Ident if the first argument is less than the second one. Stores 0 otherwise.</dd>
 * </dl>
 * 
 * @see InstrVisitor
 * @see Instr
 */
public class BuiltIn implements Instr {

	public final static String ALLOC = "Alloc";
	public final static String PRINT = "PrintInt";
	public final static String ADD = "Add";
	public final static String SUB = "Sub";
	public final static String MUL = "Mul";
	public final static String AND = "And";
	public final static String LT = "Lt";
	
	private static Set<String> operatorNames = 
			new HashSet<String>(Arrays.asList(new String[] { ALLOC, PRINT, ADD, SUB, MUL, AND, LT }));
	/** The name of the operator represented by this built-in instruction **/
	public final String operator; // must belongs to operatorNames
	/** The result of this built-in instruction, if any, will be stored in the target Ident.
	 * Alloc, Add, Sub, Mul, And and Lt have a target.
	 */
	public final Ident target; // null if no target
	/** List of the operands needed by this built-in instruction.
	 * Alloc and PrintInt have one argument.
	 * Add, Sub, Mul, And and Lt have two arguments.
	 */
	public final List<Operand> args;

	public BuiltIn(String operator, Ident target, List<Operand> args) {
		if (!operatorNames.contains(operator)) throw new ErrorException(operator+ "is not a valid builtin operator");
		this.operator = operator;
		this.target = target;
		this.args = args;
	}

	public String toString() {
		String res = "";
		if (target!=null) res = target.toString()+" = ";
		res = res+operator+"("+Call.stringOfList(args)+")";
		return res;
	}	

	public <A> A accept(InstrVisitor<A> v) {
		return v.visit(this);
	}

	public <A> A accept(DefKindVisitor<A> v) {
		return v.visit(this);
	}


}
