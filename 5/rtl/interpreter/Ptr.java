package rtl.interpreter;

class Ptr extends Value {

	final Value[] block;
	final int offset;

	@Override
	public String toString() {
		return "Ptr[" + block.toString() + "," + offset + "]";
	}

	public Ptr(Value[] block, int offset) {
		this.block = block;
		this.offset = offset;
	}
		
	public Value add(int i) {
		return new Ptr(block,offset+i);
	}
}
