package rtl.constant;

import rtl.interpreter.ErrorException;

/**
 * Inferred constant value.
 *
 * This can be an actual integer, or ⊤ (top) when the value is unknown.
 */
public class IntOrTop {
	/**
	 * The inferred value ({@code null} is ⊤).
	 */
	private final Integer val;

	/**
	 * The ⊤ value.
	 */
	private IntOrTop() {
		this.val = null;
	}

	/**
	 * Create an inferred constant value from an integer.
	 * @param i The inferred integer value.
	 */
	public IntOrTop(int i) {
		this.val = i;
	}

	/**
	 * The ⊤ value.
	 * @return ⊤.
	 */
	static public IntOrTop top() {
		return new IntOrTop();
	}

	/**
	 * Tests if this value is ⊤.
	 * @return {@code true} is this value if ⊤, {@code false} otherwise.
	 */
	public boolean isTop() {
		return this.val==null;
	}

	/**
	 * Get the known integer constant for this value.
	 * @return the known integer constant for this value. Throws an exception
	 * is this is ⊤.
	 */
	public int getInt() {
		if (isTop()) throw new ErrorException("IntOrOp: getInt is forbidden on TOP");
		return this.val;
	}

	/**
	 * Union of two values.
	 * @param  v The value to be united with.
	 * @return   The smallest upper bound between {@code this} and {@code v}.
	 */
	public IntOrTop join(IntOrTop v) {
		if (equals(v)) return this;
		else return top();
	}

	@Override public String toString() {
		if (this.val==null) return "T";
		else return this.val.toString();
	}

	@Override public boolean equals(Object o) {
		if (!(o instanceof IntOrTop)) return false;
		IntOrTop i = (IntOrTop) o;
		if (this.val==null) return i.val==null;
		return this.val.equals(i.val);
	}
}
