import rtl.*;
import rtl.graph.FlowGraph;
import rtl.graph.RtlCFG;

class Test {

	public static void main(String[] args) {
		try {
			Program prog = rtl.Parser.run(System.in);
			// Pour chaque fonction...
			for (Function f: prog.functions) {
				System.out.println("TP5 ANALYSIS of "+f.headerToString());
				
				// Construction du graphe de flot de contrôle
				FlowGraph g = new RtlCFG(f);
				// Mise en place d'un débogueur pour l'analyse des définitions possibles.
				DataFlowDebug debug = new DataFlowDebug(f, g);

				System.out.println("Constant Inference Analysis");
				TP5ConstantInference ae = new TP5ConstantInference(f,g,debug);
				
				debug.show(System.out); // Affichage de l'analyse  
				System.out.println();
			}
		} catch (Throwable e) {
			System.out.println("TP5 failed: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

