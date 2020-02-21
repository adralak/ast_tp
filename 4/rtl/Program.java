package rtl;

import java.util.ArrayList;
import java.util.List;

/** Represents a RTL program as a list of functions.
 * @see Function
 */
public class Program {

	/** The functions composing this program. */
	public final List<Function> functions;
	private Function main; 
	
	public Program() {
		functions = new ArrayList<Function>();
	}

	public void addFunction(Function f) {
		functions.add(f);
	}

	/** Get the main function of this program, where the execution starts.
	 * @return The main function of this program
	 */
	public Function getMain() {
		return main;
	}

	public void setMain(Function main) {
		this.main = main;
	}

	public void print() {
		for (Function f : functions) {
			f.print();
			System.out.println();
		}
	}

}
