import rtl.graph.FlowGraph;
import rtl.*;

import java.util.Set;
import java.util.HashMap;

public class TP1RtlFlowGraph extends FlowGraph {

	public HashMap<Node,Data> node_data;
	public HashMap<Instr,Node> instr_node;


	public Node entry() {
		return null; //TODO
	}

	public Object instr(Node n) {
		return null; //TODO
	}

	public TP1RtlFlowGraph(Function f) {
		 node_data = new HashMap<Node,Data>();
		 instr_node = new HashMap<Instr,Node>();
	}

	public Set<Ident> def(Node node) {
		return null; //TODO
	}

	public Set<Ident> use(Node node) {
		return null; //TODO
	}

	public Node node(Instr i) {
		return null; //TODO
	}

	public Node node(EndInstr i) {
		return null; //TODO
	}

	public class Data {
		public Set<Ident> use;
		public Set<Ident> def;
		public Instr i;

		public Data(Instr _i) {
			use = new Set<Ident>();
			def = new Set<Ident>();
			Instr = _i; 
		}

	}

	public class TP1Visitor implements InstrVisitor<Data> {

		public Data visit(Assign a) {
			Ident _ident = a.ident;
			Operand _operand = a.operand;

		}


	}


}
