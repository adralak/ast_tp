package rtl;

import java.util.ArrayList;
import java.util.List;

/** A function in the RTL program.
 * Has a name, parameters and a list of blocs.
 * One of this bloc is the entry, where the executions starts when the function is called.
 * @see Block
 * @see Program
 */
public class Function {

	/** The identifier corresponding to the name of the function. Used to call it. */
	public final Ident name;
	/** The identifier corresponding to the parameters of the function. */
	public final List<Ident> params;
	/** The blocs composing the functions. Includes the entry bloc. */
	public final List<Block> blocks;
	private Block entry;
	private List<String> comments = new ArrayList<String>(); // only used in print()

	public Function(Ident name) {
		this.name = name;
		this.params = new ArrayList<Ident>();
		this.blocks = new ArrayList<Block>();
	}

	public void addBlock(Block b) {
		blocks.add(b);
	}

	public void addParam(Ident id) {
		params.add(id);
	}

	/** Gets the entry bloc, where the execution of the function starts. This block is included in the list of blocks. */
	public Block getEntry() {
		return entry;
	}

	public void setEntry(Block entry) {
		this.entry = entry;
	}

	public void addComments(String msg) {
		this.comments.add(msg);
	}

	public void print() {
		System.out.println(headerToString());
		for (String s : comments)
			System.out.println("  // "+s);
		for (Block b : blocks)
			b.print();
	}

	public String headerToString() {
		return "func "+this.name.toString()+"("+stringOfList(this.params)+")";
	}

	static String stringOfList(List<Ident> l) {
		String res = "";
		if (l.size()==0) return res;
		if (l.size()==1) return l.get(0).toString();
		res = l.get(0).toString();
		for (int i=1;i<l.size();i++)
			res = res + " " + l.get(i).toString();
		return res;
	}

}
