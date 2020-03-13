package rtl.graph;

import java.util.Set;

import rtl.EndInstr;
import rtl.Ident;
import rtl.Instr;
import rtl.graph.DiGraph.Node;

public abstract class FlowGraph extends DiGraph {

	public abstract Set<Ident> def(Node node);
	public abstract Set<Ident> use(Node node);

	public abstract Object instr(Node n);

	public abstract Node entry();
	public abstract Node node(Instr i);
	public abstract Node node(EndInstr i);

	public void show(java.io.PrintStream out) {
		for (Node n1 : nodes()) {
			out.print("  "+n1.toString());
			out.print(": "+def(n1).toString()+" ");
			out.print(use(n1).toString()+"; goto ");
			out.print(n1.succ().toString());	
			out.println();
		}
	}

}
