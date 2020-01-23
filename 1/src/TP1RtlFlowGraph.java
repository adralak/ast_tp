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
	  TP1eVisitor v_end = new TP1eVisitor();

	  for(Block b : f.blocks)
	       make_graph_block(b, v, v_end);

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
		    n = node(j.instrs.get(0));
		    addEdge(end, n);
	       }
	  }
     }

     private void make_graph_block(Block b, TP1Visitor v, TP1eVisitor v_end)
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
	       d = end.accept(v_end);
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

	  public Data(List<Ident> def)
	       {
		    use = new List<Ident>();
		    this.def = def;
	       }

     }

     public class OpVisitor implements OperandVisitor<Ident> {
         public Ident visit(Ident id) {
             return id;
         }
         public Ident visit(LitInt li) {
             return null;
         }
     }

     public class JumpVisitor implements EndInstrVisitor<List<Block>> {
         public List<Block> visit(Goto g) {
             List<Block> lb = new List<Block>();
             lb.add(g.target);
             return lb;
         }

         public List<Block> visit(Branch br) {
             List<Block> lb = new List<Block>();
             lb.add(br.thenTarget);
             lb.add(br.elseTarget);
             return lb;
         }

         public List<Block> visit(Return) {
             List<Block> lb = new List<Block>();
             return lb;
         }
     }

     public class TP1eVisitor implements EndInstrVisitor<Data> {
         public Data visit(Goto g) {
             Data d = new Data(g);
             return d;
         }

         public Data visit(Branch br) {
             Data d = new Data(br);
             Operand _operand = br.condition;
             OpVisitor ov = new OpVisitor();
             Ident i_op = _operand.accept(ov);
             if (i_op != null) {
                 d.use.add(i_op);
             }
             return d;
         }

         public Data visit(Return r) {
             Data d = new Data(r);
             return d;
         }
     }


     public class TP1Visitor implements InstrVisitor<Data> {



        public Data visit(Assign a) {
           Data d = new Data(a);
           Ident _ident = a.ident;
           Operand _operand = a.operand;
           d.def.add(_ident);
           OpVisitor ov = new OpVisitor();
           Ident i_op = _operand.accept(ov);
           if (i_op != null) {
               d.use.add(i_op);
           }

           return d;

        }

        public Data visit(BuiltIn bi) {
          Data d = new Data(bi);
          Ident _ident = bi.target;
          if (_ident != null) {
              d.def.add(_ident);
          }
          for(Operand op : bi.args) {
              OpVisitor ov = new OpVisitor();
              Ident i_op = op.accept(ov);
              if (i_op != null) {
                  d.use.add(i_op);
              }
          }
          return d;
        }

        public Data visit(Call c) {
          Data d = new Data(c);
          Ident _ident = c.target;
          if (_ident != null) {
              d.def.add(_ident);
          }
          for(Operand op : c.args) {
              OpVisitor ov = new OpVisitor();
              Ident i_op = op.accept(ov);
              if (i_op != null) {
                  d.use.add(i_op);
              }
          }
          return d;
        }

          public Data visit(MemRead mr) {
          Data d = new Data(mr);
          Ident _ident = mr.ident;
          d.def.add(_ident);
          return d;
        }

          public Data visit(MemWrite mw){
          Data d = new Data(mw);
          OpVisitor ov = new OpVisitor();
          Operand op = mw.operand;
          Ident i_op = op.accept(ov);
          if (i_op != null) {
              d.use.add(i_op);
          }
          return d;
        }
    }

}
