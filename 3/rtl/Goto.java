package rtl;

/** A terminal instruction for an unconditional jump to another block.
 * @see EndInstrVisitor
 * @see EndInstr
 * @see Block
 */
public class Goto implements EndInstr{
	/** The target block of this jump */
	public final Block target;
	
	public Goto(Block target) {
		this.target = target;
	}

	public String toString() {
		return "goto "+target.label.toString();
	}
	
	public <V> V accept(EndInstrVisitor<V> v) {
		return v.visit(this);
	}

}
