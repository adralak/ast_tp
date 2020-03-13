package rtl;

import java.util.ArrayList;
import java.util.List;

/** A block in a function from a RTL program.
 * A block is a sequence of instructions (Instr) ended by a terminal instruction (EndInstr).
 * It has a label (an Ident) used to jump to it.
 * @see Instr
 * @see EndInstr
 * @see Function
 */
public class Block {

	/** The name of the bloc, used to jump to it (via a Goto or a Branch)
	 * @see Branch
	 * @see Goto
	 */
	public final Ident label;
	public final List<Phi> phis;
	/** Its list of instructions. The instructions are executed in order.
	 * It does not include the terminal instruction.
	 */
	public final List<Instr> instrs;
	private EndInstr end;

	public Block(Ident label, EndInstr end) {
		this.label = label;
		this.phis = new ArrayList<>();
		this.instrs = new ArrayList<>();
		this.end = end;
	}

	/** Gets the terminal instruction. It can be a jump (Goto or Branch) or a Return (which ends the function).
	 * It is not included in the list of instructions.
	 * @see Goto
	 * @see Branch
	 * @see Return
	 */
	public EndInstr getEnd() {
		return end;
	}

	public void setEnd(EndInstr end) {
		this.end = end;
	}
	
	public void addPhi(Phi phi) {
		phis.add(phi);
	}

	public void addInstr(Instr i) {
		instrs.add(i);
	}

	public void print() {
		System.out.println("  "+this.label.toString()+":");
		for (Phi phi : this.phis)
			System.out.println("    "+phi);		
		for (Instr instr : this.instrs)
			System.out.println("    "+instr);
		System.out.println("    "+this.end);		
	}
}
