package rtl.graph;

import rtl.Ident;

public abstract class AbstractInterferenceGraph extends Graph {

    // chaque noeud correspond Ã  une (unique) variable RTL
	public abstract Ident ident(Node n);
	public abstract Node node(Ident id);

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
