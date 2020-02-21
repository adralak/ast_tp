import rtl.*;
import rtl.Transform.TransformInstrResult;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

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
	//TODO

	TP4CSE(Function f) {
		cfg = new RtlCFG(f);
		genIdent = new FreshIdentGenerator("cse", f);
		// vous pourrez ainsi obtenir un identifiant "frais" avec
		// Ident tmp = genIdent.fresh();

		// TODO
	}	

	public TransformInstrResult transform(BuiltIn bi) {
		return new TransformInstrResult(bi); //TODO
	}

	public TransformInstrResult transform(MemRead mr) {		
		return new TransformInstrResult(mr); //TODO
	}

}
