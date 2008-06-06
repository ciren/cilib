/*
 * Copyright (C) 2003 - 2008
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.util;

import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Utility methods for {@linkplain Vector}s.
 */
public final class VectorUtils {
	
	/**
	 * Default constructor. Specified constructor to be private so that an instance
	 * of this utility class cannot be created.
	 */
	private VectorUtils() {
	}
	
	/**
	 * Constructs a {@link Vector} from <code>vector</code> Vector with each component's value
	 * set to the upper bound of that component.
	 * @param vector The {@linkplain Vector} to create the upper bound vector from.
	 * @throws UnsupportedOperationException When an element in the {@link Vector}
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
	 * @param vector The {@linkplain Vector} from which to create the lower bound vector.
	 * @throws UnsupportedOperationException when an element in the {@link Vector}
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

	/**
	 * Utility method to create a {@linkplain Vector}, given any number of {@linkplain Number} instances.
	 * @param <T> The type extending {@linkplain Number}.
	 * @param result The list of values to include within the created {@linkplain Vector}.
	 * @return The created {@linkplain Vector} object, containing the provided list of items.
	 */
	public static <T extends Number> Vector create(T... result) {
		Vector vector = new Vector();
		
		for (T element : result)
			vector.add(new Real(element.doubleValue()));
		
		return vector;
	}

}
