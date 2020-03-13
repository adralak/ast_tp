package rtl.constant;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import rtl.*;
import rtl.interpreter.ErrorException;

/**
 * Constants inference map.
 *
 * Associate to each variable (identifier) of the function its inferred value.
 * The inferred value is of type {@link IntOrTop}, that is either an actual
 * integer, or ⊤ (top) when the value can be anything.
 *
 * Uninitialized variables have no associated value.
 * Any attempt to retreive or modify the associated value of such variable will
 * raise an error. The only way to associate a value to a previously
 * uninitialized variable is using the {@link #join} method.
 *
 * We name ⊥ (bottom) the inference map in which every variable is
 * uninitialized.
 * We name ⊤ (top) the inference map in which every variable is mapped to ⊤.
 */
public class ConstMap {
	/**
	 * The actual map associating each variable to its known value.
	 */
	private final Map<Ident,IntOrTop> map;

	/**
	 * Return the ⊥ map.
	 */
	private ConstMap() {
		this.map = null;
	}

	/**
	 * Build a new inference map associating the same value to the given set of
	 * variables.
	 *
	 * Note that is your intent is to create the ⊤
	 *
	 * @param v A constant value (or ⊤)
	 * @param s The set of variables to associate the {@code v} with.
	 */
	private ConstMap(IntOrTop v, Set<Ident> s) {
		this.map = new Hashtable<>();
		for (Ident id : s) this.map.put(id, v);
	}

	/**
	 * Deep copy constructor.
	 * @param cm The constant inference map to copy.
	 */
	private ConstMap(ConstMap cm) {
		this.map = new Hashtable<>(cm.map);
	}

	/**
	 * ⊥.
	 * @return Return the ⊥ map.
	 */
	static public ConstMap bottom() {
		return new ConstMap();
	}

	/**
	 * Check if this map is ⊥.
	 * @return {@code true} if this map is the bottom map, {@code false} otherwise.
	 */
	public boolean isBottom() {
		return this.map==null;
	}

	/**
	 * Copy.
	 * @return A deep copy of this map.
	 */
	private ConstMap copy() {
		return new ConstMap(this);
	}

	/**
	 * ⊤ for a given domain.
	 * @param  dom The domain of variables.
	 * @return     The ⊤ map for the given domain of variables.
	 */
	static public ConstMap top(Set<Ident> dom) {
		return new ConstMap(IntOrTop.top(),dom);
	}

	/**
	 * Union of two inference maps.
	 * @param  mp The constant inference map to be united with.
	 * @return    The union to this map and the given map.
	 */
	public ConstMap join(ConstMap mp) {
		if (isBottom()) return mp;
		if (mp.isBottom()) return this;
		ConstMap res = copy();
		for (Ident id : map.keySet())
			res.map.put(id, map.get(id).join(mp.get(id)));
		return res;
	}

	/**
	 * Get the inferred value for the given variable.
	 *
	 * Throws an exception if the variable is uninitialized.
	 *
	 * @param  id Identifier of the variable.
	 * @return    The inferred value of {@code id}.
	 */
	public IntOrTop get(Ident id) {
		if (isBottom()) throw new ErrorException("ConstMap: get is forbidden on BOT");
		if (!this.map.containsKey(id)) throw new ErrorException("ConstMap: get on uninitialized ident "+id);
		return this.map.get(id);
	}

	/**
	 * Set the inferred value of a variable.
	 *
	 * For pedagogical reasons, this will throw an exception if the variable is
	 * uninitialized in the map. The only way to assign a value for which no
	 * value is already assigned is through the {@link #join} method.
	 *
	 * @param  id Identifier of the variable.
	 * @param  v  New inferred value for the variable {@code id}.
	 * @return    A new constant inference map in which {@code id} is associated
	 * to the inferred value {@code v}.
	 */
	public ConstMap set(Ident id, IntOrTop v) {
		if (isBottom()) throw new ErrorException("ConstMap: put is forbidden on BOT");
		if (!this.map.containsKey(id)) throw new ErrorException("ConstMap: put on uninitialized ident "+id);
		ConstMap res = this.copy();
		res.map.put(id, v);
		return res;
	}

	@Override public boolean equals(Object o) {
		if (!(o instanceof ConstMap)) return false;
		ConstMap cm = (ConstMap) o;
		if (this.map==null) return cm.map==null;
		return this.map.equals(cm.map);
	}

	@Override public String toString() {
		if (this.isBottom()) return "BOT";
		else return this.map.toString();
	}
}
