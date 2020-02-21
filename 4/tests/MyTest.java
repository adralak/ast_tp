import java.io.FileInputStream;
import java.util.List;

import rtl.DataFlowDebug;
import rtl.Function;
import rtl.AbstractLiveness;
import rtl.Program;
import rtl.graph.AbstractInterferenceGraph;
import rtl.graph.ColorGraph;
import rtl.graph.AbstractColorGraph;
import rtl.graph.FlowGraph;
import rtl.graph.Graph.Node;
import rtl.graph.RtlCFG;
import rtl.interpreter.Eval;

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
					System.out.println("TP4 ANALYSIS of "+f.headerToString());
					
					// Construction du graphe de flot de contrôle
					FlowGraph g = new RtlCFG(f);
					// Mise en place d'un débogueur pour l'analyse des définitions possibles.
					DataFlowDebug debug = new DataFlowDebug(f, g);

					System.out.println("Available Expressions Analysis");
					TP4AvailableExpressions avail = new TP4AvailableExpressions(f,g,debug); // Initialisation de l'analyse 
					
					debug.show(System.out); // Affichage de l'analyse  
					System.out.println();
				}
				
				// Execution du programme en stockant le comportement produit dans une liste.
				List<Integer> execBeforeTransformation = new Eval().runOnList(prog);
				
				// Transformation du programme
				TP4CSE.transform(prog);
				System.out.println();
				prog.print(); //pour afficher le nouveau programme
				
				// Exécution du programme après transformation.
				List<Integer> execAfterTransformation = new Eval().runOnList(prog);			
				
				// On compare les deux exécutions.
				System.out.println("Run before transformation: "+execBeforeTransformation);
				System.out.println("Run after transformation:  "+execAfterTransformation);
			} else {
				System.out.println("TP4 failed: Please provide a RTL file to analyze.");
			}
		} catch (Throwable e) {
			System.out.println("TP4 failed: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
