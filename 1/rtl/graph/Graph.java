package rtl.graph;

import java.util.HashSet;
import java.util.Set;

public class Graph {

	private int nodecount=0;
	private Set<Node> nodes = new HashSet<Node>();
	public Set<Node> nodes() { return nodes;} 

	private void check(Node n) {
		if (n.mygraph != this)
			throw new Error("Graph.addEdge using nodes from the wrong graph");
	}

	public void addEdge(Node from, Node to) {
		check(from); check(to);
		if (from.goesTo(to)) return;
		to.preds.add(from);
		from.succs.add(to);
	}

	public void rmEdge(Node from, Node to) {
		to.preds.remove(from);
		from.succs.remove(to);
	}

//	public void rmUndirectedEdge(Node from, Node to) {
//		rmEdge(from,to);
//		rmEdge(to,from);
//	}
//
	/**
	 * Print a human-readable dump for debugging.
	 */
	public void show(java.io.PrintStream out) {
		for (Node n1 : nodes) {
			out.print(n1.toString());
			out.print(": ");
			for (Node n2 : n1.succ()) {
				out.print(n2.toString());
				out.print(" ");
			}
			out.println();
		}
	}
	
	public String toStringNode(Node n) {
		return "n"+n.mykey;
	}

	public class Node {

		int mykey;
		Graph mygraph;
		public Node() {
			mykey = nodecount++;
			mygraph = Graph.this; 
			Graph.this.nodes.add(this);
		}

		Set<Node> succs = new HashSet<Node>();
		Set<Node> preds = new HashSet<Node>();

		private Set<Node> succ() {return succs;}
		private Set<Node> pred() {return preds;}

		public Set<Node> adj() {
			Set<Node> res = new HashSet<Node>(succs);
			res.addAll(preds);
			return res;
		}

		private int inDegree() {return pred().size();}
		private int outDegree() {return succ().size();}
		public int degree() {return inDegree()+outDegree();} 

		private boolean goesTo(Node n) {
			return succs.contains(n);
		}

		private boolean comesFrom(Node n) {
			return preds.contains(n);
		}

		public boolean adj(Node n) {
			return goesTo(n) || comesFrom(n);
		}

		@Override
		public int hashCode() { return mykey; }

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Node)) return false;
			Node n = (Node) obj;
			return mygraph==n.mygraph && mykey==n.mykey;
		}

		@Override
		public String toString() {return mygraph.toStringNode(this);}

	}

}

