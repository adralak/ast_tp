package rtl;

import java.util.List;

/** An instruction for function call.
 * @see InstrVisitor
 * @see Instr
 * @see Return
 * @see Function
 */
public class Call implements Instr {

	/** The result of this call, if any, will be stored in the target Ident.*/
	public final Ident target;
	/** Function called **/
	private Function callee;
	/** List of the operands needed by the function called .*/
	public final List<Operand> args;
	
	public Call(Ident target, Function callee, List<Operand> args) {
		this.target = target;
		this.callee = callee;
		this.args = args;
	}

	/** Get the function called by this instruction **/
	public Function getCallee() {
		return callee;
	}

	public void setCallee(Function callee) {
		this.callee = callee;
	}

	public String toString() {
		String res = "";
		if (target!=null) res = target.toString()+" = ";
		res = res+"call "+callee.name.toString()+"("+stringOfList(args)+")";
		return res;
	}
	
	public static String stringOfList(List<Operand> l) {
		String res = "";
		if (l.size()==0) return res;
		if (l.size()==1) return l.get(0).toString();
		res = l.get(0).toString();
		for (int i=1;i<l.size();i++)
			res = res + " " + l.get(i).toString();
		return res;
	}

	public <A> A accept(InstrVisitor<A> v) {
		return v.visit(this);
	}

	public <A> A accept(DefKindVisitor<A> v) {
		return v.visit(this);
	}


}
