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
					System.out.println("TP2 ANALYSIS of "+f.headerToString());
					
					// Construction du graphe de flot de contrôle
					FlowGraph g = new RtlCFG(f);
					// Mise en place d'un débogueur pour l'analyse liveness.
					DataFlowDebug debug = new DataFlowDebug(f, g);
					
					System.out.println("Liveness analysis");
					// Initialisation de l'analyse liveness
					AbstractLiveness live = new TP2Liveness(g,debug);
					live.build(); // Résolution de l'analyse liveness
					
					// Affichage de l'analyse liveness à l'aide du débogueur.
					debug.show(System.out);
					System.out.println();

					
					System.out.println("Interference graph");
					// Construction du graphe d'interference.
					AbstractInterferenceGraph igraph = new TP2InterferenceGraph(g,live);
					
					// Affichage du graphe d'interference.
					igraph.show(System.out);
					System.out.println();

					System.out.println("Coloration");
					// Coloration du graphe d'interference.
					AbstractColorGraph cg = new ColorGraph(igraph);
					for (Node n: igraph.nodes()) {
						// On affiche la couleur de chaque variable (chaque noeud du graphe d'interference).
						System.out.println(" "+igraph.ident(n).toString()+" --> "+cg.color(n));
					}
					System.out.println();
				}
				
				// Execution du programme en stockant le comportement produit dans une liste.
				List<Integer> execBeforeTransformation = new Eval().runOnList(prog);
				
				// Transformation du programme pour fusionner des variables
				TP2MergeIdent.transform(prog);
				prog.print(); //pour afficher le nouveau programme
				
				// Exécution du programme après transformation.
				List<Integer> execAfterTransformation = new Eval().runOnList(prog);			
				
				// On compare les deux exécutions.
				System.out.println("Run before transformation: "+execBeforeTransformation);
				System.out.println("Run after transformation:  "+execAfterTransformation);
			} else {
				System.out.println("TP2 failed: Please provide a RTL file to analyze.");
			}
		} catch (Throwable e) {
			System.out.println("TP2 failed: " + e.getMessage());
		}
	}
}
