import rtl.*;
import rtl.graph.FlowGraph;
import rtl.graph.RtlCFG;

class Test {

	public static void main(String[] args) {
		try {
			Program prog = rtl.Parser.run(System.in);
			for (Function f: prog.functions) {
				FlowGraph g = new RtlCFG(f); 
				DataFlowDebug debug = new DataFlowDebug(f,g); 
				AbstractLiveness live = new TP2Liveness(g,debug); 
				live.build(); 
				System.out.println(" function "+f.name+" nb of iterations = "+debug.getHistorySize());
				debug.show(System.out,false);
			}
		} catch (Throwable e) {
			System.out.println("TP2 failed: " + e.getMessage());
			e.printStackTrace();
		}

	}
}
