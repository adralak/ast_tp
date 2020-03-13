package rtl;

/** Represents a space in memory as a starting point and an offset to this point.
 * The starting point is given by an Ident, which must be initialized by Alloc.
 * The offset must be between 0 and the size of the allocation.
 * A MemRef can be read or write to.
 * @see MemRead
 * @see MemWrite
 * @see BuiltIn
 */
public class MemRef {
	/** The starting position for the read. It must have been allocated first.
	 * @see BuiltIn */
	public final Ident ident;
	/** An offset to the origin given by ident. Must be between 0 and the size of the allocation.
	 * @see BuiltIn */
	public final int offset;

	public MemRef(Ident ident, int offset) {
		this.ident = ident;
		this.offset = offset;
	}

	@Override public boolean equals(Object o) {
		if (!(o instanceof MemRef)) return false;
		MemRef mr = (MemRef) o;
		return ident.equals(mr.ident) && offset==mr.offset;
	}
	
	@Override public int hashCode() {
		return ident.hashCode() + 31 * offset;
	}
	
	@Override public String toString() {
		String offsetString;
		if (offset==0) offsetString = "";
		else if (offset>0) offsetString = "+"+offset;
		else offsetString = ""+offset;
		return "["+ident.toString()+offsetString+"]";
	}
}

