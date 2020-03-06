import rtl.*;
import rtl.Transform.TransformInstrResult;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

import rtl.graph.DiGraph.Node;
import rtl.graph.FlowGraph;
import rtl.graph.RtlCFG;

public class TP4CSE extends Transform {

	
     public static void transform(Program p) {
	  for (Function f : p.functions) 
	       new TP4CSE(f).transform(f);
     }

     private FlowGraph cfg;
     private FreshIdentGenerator genIdent;
     private TP4AvailableExpressions exprs;
     private Map<Node, Ident> used_temps;
     private Map<Node, Set<Ident>> temps_to_make;

     TP4CSE(Function f) {
	  cfg = new RtlCFG(f);
	  genIdent = new FreshIdentGenerator("cse", f);
	  // vous pourrez ainsi obtenir un identifiant "frais" avec
	  // Ident tmp = genIdent.fresh();
	  exprs = new TP4AvailableExpressions(f, cfg);
	  this.used_temps = new Hashtable<Node, Ident>();
	  this.temps_to_make = new Hashtable<Node, Set<Ident>>();

	  System.out.println(exprs.useDef_expr);
	  
	  for(Node n : cfg.nodes())
	       temps_to_make.put(n, new HashSet<Ident>());
	  
	  for(Node n : cfg.nodes())
	  {
	       Set<Node> available_defs = exprs.useDef(n);

	       if(!available_defs.isEmpty())
	       {
		    Ident tmp = genIdent.fresh();
		    used_temps.put(n, tmp);
			 
		    for(Node ancestor : available_defs)
			 temps_to_make.get(ancestor).add(tmp);
	       }
	  }

	  System.out.println(used_temps);
	  System.out.println(temps_to_make);
     }	
     
     public TransformInstrResult transform(BuiltIn bi) {
	  TransformInstrResult new_instrs;
	  Instr new_instr;
	  Node n = cfg.node(bi);	  

	  if(used_temps.containsKey(n))
	  {
	       Ident id = used_temps.get(n);
	       new_instr = new Assign(bi.target, id);
	  }
	  else
	       new_instr = bi;

	  new_instrs = new TransformInstrResult(new_instr);

	  for(Ident id : temps_to_make.get(n))
	       new_instrs.addAfter.add(new Assign(id, bi.target));

	  return new_instrs;
     }

     public TransformInstrResult transform(MemRead mr) {		
	  return new TransformInstrResult(mr); //TODO
     }

}
