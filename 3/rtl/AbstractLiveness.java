package rtl;


import java.util.Set;
import rtl.graph.DiGraph.Node;

/**
 * Interface for variable liveness analysis.
 * 
 * A liveness analysis is attached to a given {@link rtl.graph.FlowGraph} instance and gives,
 * for each node of the graph, information about the liveness of each variable.
 */
public interface AbstractLiveness {
	/**
	 * Called to do the analysis.
	 */
	public void build(); 

	/**
	 * @param n A node of the associated flow graph.
	 * @return The set of variables alive immediatly before the instruction of {@code n}.
	 */
	public Set<Ident> liveIn(Node n); 

	/**
	 * @param n A node of the associated flow graph.
	 * @return The set of variables alive immediatly after the instruction of {@code n}.
	 */
	public Set<Ident> liveOut(Node n);

}
