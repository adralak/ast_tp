/*
Authors: Paul Bastide and Yan Garito
*/

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import rtl.*;
import rtl.graph.DiGraph.Node;
import rtl.graph.RtlCFG;

/**
 * Propagation de constantes.
 * 
 * Cette transformation utilise le résultat de l'analyse des définitions possibles pour
 * simplifier le programme et remplacer certains usages de variables par des constantes.
 * 
 * Par exemple, dans le code RTL suivant :
 * 
 * <pre>{@code
 * x = 41
 * y = Add(x 1)
 * }</pre>
 * 
 * la seule définition possible pour la variable {@code x} est une constante.
 * Il est alors possible de remplacer les usages associés à cette définition
 * par cette constante.
 * 
 * <pre>{@code
 * x = 41
 * y = Add(41 1)
 * }</pre>
 * 
 * Notez que cette transformation ne s'occupe pas de supprimer la définition de {@code x}
 * devenue inutile. Cela sera réalisé plus tard par la transformation {@link TP3DeadDefElim}.
 */
public class TP3ConstPropReachDef extends Transform {
     /**
      * Transformation d'un programme entier.
      * @param p Le programme à transformer.
      */
     public static void transform(Program p) {
	  // On fait la transformation pour chaque fonction.
	  for (Function f : p.functions) {
	       RtlCFG cfg = new RtlCFG(f);
	       TP3ReachableDef reachDef = new TP3ReachableDef(cfg);
	       new TP3ConstPropReachDef(cfg, reachDef.useDef()).transform(f);
	  }
     }

     /**
      * Le graphe de flot de contrôle de la fonction à transformer.
      */
     private RtlCFG cfg;
	
     /**
      * La relation "UseDef" entre les usages en chaque noeud et leur définitions possibles associées.
      */
     private Map<Node,Map<Ident,Set<Node>>> useDef;

     /**
      * Création d'une transformation de propagation de constantes.
      * @param cfg Le graphe de flot de contrôle de la fonction à transformer.
      * @param useDef La relation usage-definitions de la fonction.
      */
     TP3ConstPropReachDef(RtlCFG cfg, Map<Node,Map<Ident,Set<Node>>> useDef) {
	  this.cfg = cfg;
	  this.useDef = useDef;
     }
	
     /**
      * Transformation d'une instruction d'affectation.
      */
     public TransformInstrResult transform(Assign a) {
	  Node n = cfg.node(a);
	  Instr newInstr = a.accept(new TP3_InstrVisitor());
		
	  cfg.updateInstr(n, newInstr);
		
	  return new TransformInstrResult(newInstr);
     }


     public TransformInstrResult transform(BuiltIn bi) {
	  Node n = cfg.node(bi);
	  Instr newInstr = bi.accept(new TP3_InstrVisitor());
	  
	  cfg.updateInstr(n, newInstr);
	  
	  return new TransformInstrResult(newInstr);
     }
     public TransformInstrResult transform(Call c) {
	  Node n = cfg.node(c);
	  Instr newInstr = c.accept(new TP3_InstrVisitor());

	  cfg.updateInstr(n, newInstr);
		
	  return new TransformInstrResult(newInstr);
     }
     public TransformInstrResult transform(MemRead mr) {
	  Node n = cfg.node(mr);
	  Instr newInstr = mr.accept(new TP3_InstrVisitor());
		
	  cfg.updateInstr(n, newInstr);
		
	  return new TransformInstrResult(newInstr);
     }
     public TransformInstrResult transform(MemWrite mw) {
	  Node n = cfg.node(mw);
	  Instr newInstr = mw.accept(new TP3_InstrVisitor());
	  
	  cfg.updateInstr(n, newInstr);
		
	  return new TransformInstrResult(newInstr);
     }

     public TransformEndInstrResult transform(Goto g) {
	  Node n = cfg.node(g);
	  EndInstr newInstr = g.accept(new TP3_EndInstrVisitor());
	  
	  cfg.updateInstr(n, newInstr);
		
	  return new TransformEndInstrResult(newInstr);
     }
     public TransformEndInstrResult transform(Branch br) {
	  Node n = cfg.node(br);
	  EndInstr newInstr = br.accept(new TP3_EndInstrVisitor());
	  
	  cfg.updateInstr(n, newInstr);
		
	  return new TransformEndInstrResult(newInstr);
     }
     public TransformEndInstrResult transform(Return r) {
	  Node n = cfg.node(r);
	  EndInstr newInstr = r.accept(new TP3_EndInstrVisitor());
	  
	  cfg.updateInstr(n, newInstr);
		
	  return new TransformEndInstrResult(newInstr);
     }

     // TODO : une rédéfinition de chaque méthode transform de la classe Transform

     private class TP3_EndInstrVisitor implements EndInstrVisitor<EndInstr>
     {
	  Node n;
	  
	  public EndInstr visit(Goto g)
	       {
		    return g;
	       }

	  public EndInstr visit(Branch br)
	       {
		    n = cfg.node(br);
		    LitInt cst = br.condition.accept(new Const_OperandVisitor());

		    if(cst == null)
			 return br;
		    else
			 return new Branch(cst, br.thenTarget, br.elseTarget);
	       }

	  public EndInstr visit(Return r)
	       {
		    n = cfg.node(r);

		    if(r.operand == null)
			 return r;
		    
		    LitInt cst = r.operand.accept(new Const_OperandVisitor());

		    if(cst == null)
			 return r;
		    else
			 return new Return(cst);
	       }

	  private class Const_OperandVisitor implements OperandVisitor<LitInt>
	  {
	       public LitInt visit(Ident id)
		    {
			 Set<Node> defs = useDef.get(n).get(id);
			 LitInt cst = null;
			 for(Node n : defs)
			 {
			      Instr def = (Instr) cfg.instr(n);
			      
			      LitInt def_cst = def.accept(new Const_InstrVisitor());
			      if(def_cst == null)
				   return null;
			      else if(cst == null)
				   cst = def_cst;
			      else
			      {
				   if(!cst.equals(def_cst))
					return null;
			      }
			 }

			 return cst;
		    }

	       public LitInt visit(LitInt cst)
		    {
			 return cst;
		    }
	  }

	  
	  private class Const_InstrVisitor implements InstrVisitor<LitInt>
	  {
	       public LitInt visit(Assign a)
		    {
			 return a.operand.accept(new Const_OperandVisitor());
		    }
	       public LitInt visit(BuiltIn bi)
		    {
			 return null;
		    }
	       public LitInt visit(Call c)
		    {
			 return null;
		    }
	       public LitInt visit(MemRead mr)
		    {
			 return null;
		    }
	       public LitInt visit(MemWrite mw)
		    {
			 return null;
		    }
	  }

     }
     
     private class TP3_InstrVisitor implements InstrVisitor<Instr>
     {
	  Node n;
	  
	  public Instr visit(Assign a)
	       {
		    n = cfg.node(a);
		    LitInt cst = a.operand.accept(new Const_OperandVisitor());

		    if(cst == null)
			 return a;
		    else
			 return new Assign(a.ident, cst);
	       }

	  public Instr visit(BuiltIn bi)
	       {
		    n = cfg.node(bi);
		    List<Operand> args = bi.args;
		    List<Operand> new_args = new ArrayList();
		    Const_OperandVisitor v = new Const_OperandVisitor();

		    for(Operand arg : args)
		    {
			 LitInt cst = arg.accept(v);
			 if(cst == null)
			      new_args.add(arg);
			 else
			      new_args.add(cst);
		    }

		    return new BuiltIn(bi.operator, bi.target, new_args);
	       }

	  public Instr visit(Call c)
	       {
		    n = cfg.node(c);
		    List<Operand> args = c.args;
		    List<Operand> new_args = new ArrayList();
		    Const_OperandVisitor v = new Const_OperandVisitor();

		    for(Operand arg : args)
		    {
			 LitInt cst = arg.accept(v);

			 if(cst == null)
			      new_args.add(arg);
			 else
			      new_args.add(cst);
		    }

		    return new Call(c.target, c.getCallee(), new_args);
	       }

	  public Instr visit(MemRead mr)
	       {
		    return mr;
	       }

	  public Instr visit(MemWrite mw)
	       {
		    n = cfg.node(mw);
		    LitInt cst = mw.operand.accept(new Const_OperandVisitor());

		    if(cst == null)
			 return mw;
		    else
			 return new MemWrite(mw.memRef, cst);
	       }
	  
	  private class Const_OperandVisitor implements OperandVisitor<LitInt>
	  {
	       public LitInt visit(Ident id)
		    {
			 Set<Node> defs = useDef.get(n).get(id);
			 LitInt cst = null;

			 for(Node n : defs)
			 {
			      Instr def = (Instr) cfg.instr(n);

			      if(def == null)
				   continue;
			      
			      LitInt def_cst = def.accept(new Const_InstrVisitor()); 
			      
			      if(def_cst == null)
				   return null;
			      else if(cst == null)
				   cst = def_cst;
			      else
			      {
				   if(!cst.equals(def_cst))
					return null;
			      }
			 }

			 return cst;
		    }

	       public LitInt visit(LitInt cst)
		    {
			 return cst;
		    }
	  }

	  
	  private class Const_InstrVisitor implements InstrVisitor<LitInt>
	  {
	       public LitInt visit(Assign a)
		    {
			 return a.operand.accept(new Const_OperandVisitor());
		    }
	       public LitInt visit(BuiltIn bi)
		    {
			 return null;
		    }
	       public LitInt visit(Call c)
		    {
			 return null;
		    }
	       public LitInt visit(MemRead mr)
		    {
			 return null;
		    }
	       public LitInt visit(MemWrite mw)
		    {
			 return null;
		    }
	  }

     }
     
}

