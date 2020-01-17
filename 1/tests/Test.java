import rtl.Function;
import rtl.Program;

class Test {

	public static void main(String[] args) {
		try {
			Program prog = rtl.Parser.run(System.in);
			for (Function f: prog.functions) {
				System.out.println("TP1 ANALYSIS of "+f.headerToString());
				TP1RtlFlowGraph cfg = new TP1RtlFlowGraph(f);
				cfg.show(System.out);
				System.out.println("");
			}
		} catch (Throwable e) {
			System.out.println("TP1 failed: " + e.getMessage());
		}

	}
}
