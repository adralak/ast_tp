/**
@authors Paul Bastide et Yan Garito
 */

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

	  for(Node n : cfg.nodes())
	       temps_to_make.put(n, new HashSet<Ident>());
	  
	  for(Node n : cfg.nodes())
	  {
	       Set<Node> available_defs = exprs.useDef(n);

	       // If the expression is available
	       if(!available_defs.isEmpty())
	       {
		    // We'll have to make a new tmp
		    Ident tmp = genIdent.fresh();
		    used_temps.put(n, tmp);

		    // And the ancestors will have to define it
		    for(Node ancestor : available_defs)
			 temps_to_make.get(ancestor).add(tmp);
	       }
	  }
     }	
     
     public TransformInstrResult transform(BuiltIn bi) {
	  Instr new_instr;
	  Node n = cfg.node(bi);	  

	  // If the expression here is already defined, we can make the instruction simpler
	  if(used_temps.containsKey(n))
	  {
	       Ident id = used_temps.get(n);
	       new_instr = new Assign(bi.target, id);
	  }
	  // Otherwise, it stays the same
	  else
	       new_instr = bi;

	  TransformInstrResult new_instrs = new TransformInstrResult(new_instr);

	  // Then we add the tmps we have to make for later use
	  for(Ident id : temps_to_make.get(n))
	       new_instrs.addAfter.add(new Assign(id, bi.target));

	  return new_instrs;
     }

     // Same for MemRead
     public TransformInstrResult transform(MemRead mr) {
	  Instr new_instr;
	  Node n = cfg.node(mr);	  

	  if(used_temps.containsKey(n))
	  {
	       Ident id = used_temps.get(n);
	       new_instr = new Assign(mr.ident, id);
	  }
	  else
	       new_instr = mr;

	  TransformInstrResult new_instrs = new TransformInstrResult(new_instr);

	  for(Ident id : temps_to_make.get(n))
	       new_instrs.addAfter.add(new Assign(id, mr.ident));

	  return new_instrs;

     }

}
