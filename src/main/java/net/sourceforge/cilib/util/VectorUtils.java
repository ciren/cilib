package net.sourceforge.cilib.util;

import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Utility methods for Vectors
 */
public class VectorUtils {
	
	/**
	 * Constructs a {@link Vector} from <code>vector</code> Vector with each component's value
	 * set to the upper bound of that component. 
	 * @throws {@link UnsupportedOperationException} when an element in the {@link Vector}
	 *         is not a {@link Numeric}
	 * @return a {@link Vector} with all the elements set to their respective upper bounds
	 */
	public static Vector createUpperBoundVector(Vector vector) {
		Vector upper = vector.getClone();

		for(Type element : upper) {
			try {
				Numeric numeric = (Numeric) element;
				numeric.set(numeric.getUpperBound());
			}
			catch (ClassCastException cce) {
				throw new UnsupportedOperationException("Upper Bounds are only applicable to 'Numeric' types and not '" + element.getClass().getSimpleName() + "' types");
			}
		}
		return upper;
	}
	
	/**
	 * Constructs a {@link Vector} from <code>vector</code> Vector with each component's value
	 * set to the lower bound of that component. 
	 * @throws {@link UnsupportedOperationException} when an element in the {@link Vector}
	 *         is not a {@link Numeric}
	 * @return a {@link Vector} with all the elements set to their respective lower bounds
	 */
	public static Vector createLowerBoundVector(Vector vector) {
		Vector lower = vector.getClone();

		for(Type element : lower) {
			try {
				Numeric numeric = (Numeric) element;
				numeric.set(numeric.getLowerBound());
			}
			catch (ClassCastException cce) {
				throw new UnsupportedOperationException("Lower Bounds are only applicable to 'Numeric' types and not '" + element.getClass().getSimpleName() + "' types");
			}
		}
		return lower;
	}

}
