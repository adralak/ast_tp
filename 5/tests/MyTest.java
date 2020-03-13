import java.io.FileInputStream;

import rtl.DataFlowDebug;
import rtl.Function;
import rtl.Program;
import rtl.graph.FlowGraph;
import rtl.graph.RtlCFG;

class MyTest {

	/**
	 * Point d'entrée du programme de test.
	 * @param args Les paramètres du programme.
	 */
	public static void main(String[] args) {
		try {
			if (args.length >= 1) {
				// On ouvre le fichier donné en paramètre.
				java.io.InputStream in = new FileInputStream(args[0]);
				
				// On parse le programme RTL.
				Program prog = rtl.Parser.run(in);
				
				// Pour chaque fonction...
				for (Function f: prog.functions) {
					System.out.println("TP5 ANALYSIS of "+f.headerToString());
					
					// Construction du graphe de flot de contrôle
					FlowGraph g = new RtlCFG(f);
					// Mise en place d'un débogueur pour l'analyse
					DataFlowDebug debug = new DataFlowDebug(f, g);

					System.out.println("Constant Inference Analysis");
					TP5ConstantInference ae = new TP5ConstantInference(f,g,debug);
					
					debug.show(System.out); // Affichage de l'analyse  
					System.out.println();
				}
			} else {
				System.out.println("TP5 failed: Please provide a RTL file to analyze.");
			}
		} catch (Throwable e) {
			System.out.println("TP5 failed: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
