import java.io.FileInputStream;

import rtl.Function;
import rtl.Program;

class MyTest {

	public static void main(String[] args) {
		try {
			if (args.length >= 1) {
				java.io.InputStream in = new FileInputStream(args[0]);
				Program prog = rtl.Parser.run(in);
				for (Function f: prog.functions) {
					System.out.println("TP1 ANALYSIS of "+f.headerToString());
					TP1RtlFlowGraph cfg = new TP1RtlFlowGraph(f);
					cfg.show(System.out);
					System.out.println("");
				}
			} else {
				System.out.println("TP1 failed: Please provide a RTL file to analyze.");
			}
		} catch (Throwable e) {
			System.out.println("TP1 failed: " + e.getMessage());
		}
	}
}
