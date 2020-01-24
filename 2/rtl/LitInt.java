package rtl;

/** A literal integer, that is, a constant */
public class LitInt implements Operand {
	private final int val;

	public LitInt(int val) {
		this.val = val;
	}

	/** Get the value represented by this LitInt */
	public int getVal() { return val; }
	
	@Override public String toString() {
		return Integer.toString(val);
	}
	
	@Override public boolean equals(Object o) {
		if (!(o instanceof LitInt)) return false;
		LitInt li = (LitInt) o;
		return val==li.val;
	}
	
	@Override public int hashCode() {
		return val;
	}
	
	public <V> V accept(OperandVisitor<V> v) {
		return v.visit(this);
	}

}

