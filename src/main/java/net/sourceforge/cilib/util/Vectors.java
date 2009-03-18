/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.util;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import net.sourceforge.cilib.type.types.Numeric;
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
     * Creates and returns a {@link net.sourceforge.cilib.type.types.container.Vector} with
     * each component's value set to the upper bound of that component in the given vector.
     *
     * @param vector The {@linkplain net.sourceforge.cilib.type.types.container.Vector} from
     *        which to create the upper bound vector.
     * @return a {@link net.sourceforge.cilib.type.types.container.Vector} with all the
     *         elements set to their respective upper bounds
     */
    public static Vector upperBoundVector(Vector vector) {
        Vector.Builder upper = Vector.newBuilder();

        for (Numeric element : vector) {
            upper.addWithin(element.getBounds().getUpperBound(), element.getBounds());
        }
        return upper.build();
    }

    /**
     * Creates and returns a {@link net.sourceforge.cilib.type.types.container.Vector} with
     * each component's value set to the lower bound of that component in the given vector.
     *
     * @param vector The {@linkplain net.sourceforge.cilib.type.types.container.Vector} from
     *        which to create the lower bound vector.
     * @return a {@link net.sourceforge.cilib.type.types.container.Vector} with all the
     *         elements set to their respective lower bounds
     */
    public static Vector lowerBoundVector(Vector vector) {
        Vector.Builder lower = Vector.newBuilder();

        for (Numeric element : vector) {
            lower.addWithin(element.getBounds().getLowerBound(), element.getBounds());
        }
        return lower.build();
    }

    /**
     * Calculate the furthest distance possible between two points within the given domain,
     * also known as the <code>zMax</code>.
     *
     * @param distanceMeasure the {@link net.sourceforge.cilib.util.DistanceMeasure} that
     *        should be used to calculate the <code>zMax</code>
     * @param domain the {@link net.sourceforge.cilib.type.types.container.Vector}
     *        representing the domain
     * @return the <code>zMax</code> for the given domain
     */
    public static double zMax(DistanceMeasure distanceMeasure, Vector domain) {
        Vector upperBoundVector = Vectors.upperBoundVector(domain);
        Vector lowerBoundVector = Vectors.lowerBoundVector(domain);

        return distanceMeasure.distance(upperBoundVector, lowerBoundVector);
    }

    /**
     * Determine the sum of a list of {@code Vector} instances.
     * @param vectors The {@code Vector} instances to sum.
     * @return The resultant {@code Vector}.
     */
    public static Vector sumOf(Vector... vectors) {
        Vector result = null;//vectors[0].getClone();

        for (Vector vector : vectors) {
            if (result == null) {
                result = vector;
                continue;
            }
            result = result.plus(vector);
        }

        return result;
    }

    public static <T extends Number> Vector transform(Vector vector, Function<Numeric, T> function) {
        Vector.Builder builder = Vector.newBuilder();
        for (Numeric n : vector) {
            builder.addWithin(function.apply(n).doubleValue(), n.getBounds()); //??
        }
        return builder.build();
    }

    /**
     * Return the string representation of the given {@code Vector} using the given characters as the first, last and
     * delimeter.
     * <p>
     * All returned strings will be in the format of:
     * <pre>
     * {@literal <first><item><delimeter><item><delimeter>...<delimiter><item><last>}
     * </pre>
     * For example: <code>Vectors.toString(vector, '\0', '\0', '\t')</code> will return the following representation:<br/>
     * <code>1\t2\t3</code>
     * @return The string representation of the given {@code Vector}.
     */
    public static String toString(Vector vector, char first, char last, char delimeter) {
        StringBuilder builder = new StringBuilder(first);
        Joiner.on(delimeter).appendTo(builder, vector);

        builder.append(last);
        return builder.toString();
    }
}
