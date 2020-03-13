package rtl.interpreter;

class Int extends Value {

	final int val;

	@Override
	public String toString() {
		return "Int[" + val + "]";
	}

	public Int(int val) {
		this.val = val;
	}
	
	public int get() { return val; }

	public Value add(int i) {
		return new Int(val+i);
	}

}
