import rtl.graph.FlowGraph;
import rtl.*;

import java.util.Set;
import java.util.HashMap;

public class TP1RtlFlowGraph extends FlowGraph {

     private HashMap<Node,Data> node_data;
     private HashMap<Instr,Node> instr_node;
     private HashMap<EndInstr,Node> end_instr_node;
     private Node first;
     private Block e;

     public Node entry() {
	  return first;
     }

     public Object instr(Node n) {
	  Data d = node_data[n];

	  if(d.end)
	       return d.e;
	  else if(n == first)
	       return e;
	  else 
	       return d.i;
     }

     public TP1RtlFlowGraph(Function f) {
	  node_data = new HashMap<Node,Data>();
	  instr_node = new HashMap<Instr,Node>();
	  end_instr_node = new HashMap<EndInstr,Node>();

	  e = f.getEntry();
	  first = new Node();
	  Data d = new Data(f.params);
	  node_data.put(first, d);
     }

     public List<Ident> def(Node node) {
	  return node_data[node].def;
     }

     public List<Ident> use(Node node) {
	  return node_data[node].use;
     }

     public Node node(Instr i) {
	  return instr_node[i];
     }

     public Node node(EndInstr i) {
	  return end_instr_node[i];
     }

     public class Data {
	  public List<Ident> use;
	  public List<Ident> def;
	  public boolean end;
	  public Instr i;
	  public EndInstr e;

	  public Data(Instr _i) {
	       use = new List<Ident>();
	       def = new List<Ident>();
	       i = _i;
	       end = false;
	  }

	  public Data(EndInstr _e)
	       {
		    use = new List<Ident>();
		    def = new List<Ident>();
		    e = _e;
		    end = true;
	       }

	  Public Data(List<Ident> def)
	       {
		    use = new List<Ident>();
		    this.def = def;
	       }

     }

     public class TP1Visitor implements InstrVisitor<Data> {

	  public Data visit(Assign a) {
	       Ident _ident = a.ident;
	       Operand _operand = a.operand;
			
	  }


     }


}
