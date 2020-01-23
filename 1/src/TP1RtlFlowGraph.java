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

	  TP1Visitor v = new TP1Visitor();
	  
	  for(Block b : f.blocks)
	       make_graph_block(b, v);

	  JumpVisitor jv = new JumpVisitor();
	  List<Block> jumps_to = null;
	  EndInstr end;
	  Node end_node, n;
	  
	  for(Block b : f.blocks)
	  {
	       end = b.getEnd();
	       end_node = end_instr_node[end];
	       jumps_to = end.accept(jv);

	       for(Block j : jumps_to)
	       {
		    n = instr_node[j.instrs.get(0)];
		    addEdge(end, n);
	       }
	  }
     }

     private void make_graph_block(Block b, TP1Visitor v)
	  {
	       Node curr = new Node();
	       Node pred = first;
	       Data d;
	       
	       for(Instr i : b.instrs)
	       {
		    d = i.accept(v);
		    node_data.put(curr, d);
		    instr_node.put(i, curr);
		    addEdge(pred, curr);
		    pred = curr;
		    curr = new Node();
	       }

	       EndInstr end = b.getEnd();
	       d = end.accept(v);
	       node_data.put(curr, d);
	       end_instr_node.put(end, curr);
	       addEdge(pred, curr);
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

     public class JumpVisitor implements InstrVisitor<List<Block>> {
	  
     }
     
     public class TP1Visitor implements InstrVisitor<Data> {

	  public Data visit(Assign a) {
	       Ident _ident = a.ident;
	       Operand _operand = a.operand;
			
	  }


     }


}
