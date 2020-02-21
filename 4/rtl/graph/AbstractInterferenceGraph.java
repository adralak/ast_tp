package rtl.graph;

import rtl.Ident;

/**
 * Abstract class for interference graphs between the variables of a program.
 * 
 * An implementation of this abstract class provides an interference analysis,
 * where the result is a graph in which every node is a variable,
 * and every edge denotes an interference between the variables.
 */
public abstract class AbstractInterferenceGraph extends Graph {
	/**
	 * @param n A node of the interference graph.
	 * @return The identifier of the variable represented by this node.
	 */
	public abstract Ident ident(Node n);
	
	/**
	 * @param i A variable identifier.
	 * @return The node representing the variable of the given identifier.
	 */
	public abstract Node node(Ident id);

	/**
	 * Display the interference graph.
	 * @param out Output stream where to write the formatted graph.
	 */
	public void show(java.io.PrintStream out) {
		for (Node n1 : nodes()) {
			out.print(" "+ident(n1).toString());
			out.print(": ");
			for (Node n2 : n1.adj()) {
				out.print(ident(n2).toString());
				out.print(" ");
			}
			out.println();
		}
	}
}
