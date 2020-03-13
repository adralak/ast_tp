package rtl.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Directed graphs.
 *
 * A mutable data structure to manipulate directed graphs composed of nodes and
 * edges.
 *
 * Nodes are identified by an integer key, unique to a given graph.
 * Nodes are owned by a graph, and cannot be used across different graphs.
 */
public class Graph {

	private int nodecount=0;
	private Set<Node> nodes = new HashSet<Node>();
	public Set<Node> nodes() { return nodes;}

	private void check(Node n) {
		if (n.mygraph != this)
			throw new Error("Graph.addEdge using nodes from the wrong graph");
	}

	/**
	 * Add an edge between two nodes.
	 * The two nodes must be owned by this graph.
	 * @param from The starting node of the edge.
	 * @param to   The ending node of the edge.
	 */
	public void addEdge(Node from, Node to) {
		check(from); check(to);
		if (from.goesTo(to)) return;
		to.preds.add(from);
		from.succs.add(to);
	}

	/**
	 * Remove an edge.
	 * Nothing happens is the edge does not exist.
	 * @param from The starting node of the edge.
	 * @param to   The ending node of the edge.
	 */
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
	 * @param out The target output of the dump.
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

	/**
	 * Make a string representation of a node.
	 * @param  n The target node.
	 * @return   The string "nX" where "X" is the key of the node.
	 */
	public String toStringNode(Node n) {
		return "n"+n.mykey;
	}

	/**
	 * A graph's node.
	 *
	 * Nodes are identified by a key, unique to its graph.
	 */
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
