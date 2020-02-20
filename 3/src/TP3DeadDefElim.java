import java.util.Map;
import java.util.Set;

import rtl.*;
import rtl.graph.RtlCFG;
import rtl.graph.DiGraph.Node;

/**
 * Élimination des définitions inutiles (Dead Definitions Elimination).
 * 
 * Transforme le programme pour enlever les définitions non utilisées.
 */
public class TP3DeadDefElim extends Transform {
     /**
      * Transformation d'un programme entier.
      * @param p Le programme à transformer.
      */
     public static void transform(Program p) {
	  // On fait la transformation pour chaque fonction.
	  for (Function f : p.functions) 
	       new TP3DeadDefElim(f).transform(f);
     }

     /**
      * Le graphe de flot de contrôle de la fonction à transformer.
      */
     private RtlCFG cfg;
	
     /**
      * La relation definition-usages de la fonction à transformer.
      */
     private Map<Node,Map<Ident,Set<Node>>> defUse;

     /**
      * Construit une transformation d'élimination des définitions inutiles.
      * @param f La fonction à transformer.
      */
     TP3DeadDefElim(Function f) {
	  // Construction du graphe de flot de contrôle de la fonction.
	  this.cfg = new RtlCFG(f);
		
	  // Construction de la relation definition-usages.
	  TP3ReachableDef reachDef = new TP3ReachableDef(cfg);
	  this.defUse = reachDef.defUse();
     }

     // TODO à vous d'implémenter la transformation en surchargant les méthodes de {@link rtl.Transform}.
     public TransformInstrResult transform(Assign a)
	  {
	       Node n = cfg.node(a);
	       Map<Ident, Set<Node>> uses = defUse.get(n);
	       Ident id = a.ident;
	       Set<Node> usage = uses.get(id);
	       
	       if(usage == null)
		    return new TransformInstrResult(null);
	       
	       if(usage.isEmpty())
		    return new TransformInstrResult(null);
	       else
		    return new TransformInstrResult(a);
	  }

     public TransformInstrResult transform(BuiltIn bi)
	  {
	       Ident id = bi.target;
	       if(id == null)
		    return new TransformInstrResult(bi);

	       Node n = cfg.node(bi);
	       Map<Ident, Set<Node>> uses = defUse.get(n);
	       Set<Node> usage = uses.get(id);

	       if(usage == null)
		    return new TransformInstrResult(null);
	       
	       if(usage.isEmpty())
		    return new TransformInstrResult(null);
	       else
		    return new TransformInstrResult(bi);
	  }

     public TransformInstrResult transform(Call c)
	  {
	       Ident id = c.target;
	       if(id == null)
		    return new TransformInstrResult(c);

	       Node n = cfg.node(c);
	       Map<Ident, Set<Node>> uses = defUse.get(n);
	       Set<Node> usage = uses.get(id);

	       if(usage == null)
		    return new TransformInstrResult(null);
	       
	       if(usage.isEmpty())
		    return new TransformInstrResult(null);
	       else
		    return new TransformInstrResult(c);
	  }

     public TransformInstrResult transform(MemRead mr)
	  {
	       Ident id = mr.ident;
	       if(id == null)
		    return new TransformInstrResult(mr);

	       Node n = cfg.node(mr);
	       Map<Ident, Set<Node>> uses = defUse.get(n);
	       Set<Node> usage = uses.get(id);

	       if(usage == null)
		    return new TransformInstrResult(null);
	       
	       if(usage.isEmpty())
		    return new TransformInstrResult(null);
	       else
		    return new TransformInstrResult(mr);
	  }    
}
