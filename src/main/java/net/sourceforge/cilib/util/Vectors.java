/**
 * Copyright (C) 2003 - 2009
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

import java.util.Arrays;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Utility methods for {@linkplain Vector}s.
 */
public final class Vectors {

    /**
     * Default constructor. Specified constructor to be private so that an instance
     * of this utility class cannot be created.
     */
    private Vectors() {
    }

    /**
     * Constructs a {@link Vector} from <code>vector</code> Vector with each component's value
     * set to the upper bound of that component.
     * @param vector The {@linkplain Vector} to create the upper bound vector from.
     * @throws UnsupportedOperationException When an element in the {@link Vector}
     *         is not a {@link Numeric}
     * @return a {@link Vector} with all the elements set to their respective upper bounds
     */
    public static Vector upperBoundVector(Vector vector) {
        Vector upper = vector.getClone();

        for (Type element : upper) {
            Numeric numeric = (Numeric) element;
            numeric.set(numeric.getBounds().getUpperBound());
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
    public static Vector lowerBoundVector(Vector vector) {
        Vector lower = vector.getClone();

        for (Type element : lower) {
            Numeric numeric = (Numeric) element;
            numeric.set(numeric.getBounds().getLowerBound());
        }

        return lower;
    }

    /**
     * Utility method to create a {@linkplain Vector}, given any number of {@linkplain Number} instances.
     * @param <T> The type extending {@linkplain Number}.
     * @param elements The list of values to include within the created {@linkplain Vector}.
     * @return The created {@linkplain Vector} object, containing the provided list of items.
     */
    public static <T extends Number> Vector create(T... elements) {
        return create(Arrays.asList(elements));
    }

    /**
     * Create a {@code Vector} from the provided {@code Iterable}.
     * @param <T> The number type.
     * @param iterable The iterable of data elements.
     * @return A {@code Vector} of the provided objects.
     */
    public static <T extends Number> Vector create(Iterable<T> iterable) {
        Vector vector = new Vector();

        for (T element : iterable)
            vector.add(new Real(element.doubleValue()));

        return vector;
    }

    /**
     * Determine the sum of a list of {@code Vector} instances.
     * @param vectors The {@code Vector} instances to sum.
     * @return The resultant {@code Vector}.
     */
    public static Vector sumOf(Vector... vectors) {
        Vector result = vectors[0].getClone();
        result.reset();

        for (Vector vector : vectors)
            result = result.plus(vector);

        return result;
    }

}
