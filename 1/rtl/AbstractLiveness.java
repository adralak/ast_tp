package rtl;


import java.util.Set;
import rtl.graph.DiGraph.Node;

public abstract class AbstractLiveness {

	public abstract void build(); 

	public abstract Set<Ident> liveIn(Node n); 

	public abstract Set<Ident> liveOut(Node n);

}
