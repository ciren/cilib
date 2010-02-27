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

import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
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
        return Vectors.transform(vector, new Function<Numeric, Double>() {
            @Override
            public Double apply(Numeric from) {
                return from.getBounds().getUpperBound();
            }
        });
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
        return Vectors.transform(vector, new Function<Numeric, Double>() {
            @Override
            public Double apply(Numeric from) {
                return from.getBounds().getLowerBound();
            }
        });
    }

    /**
     * Calculate the furthest distance possible between two points within the given domain,
     * also known as the <code>zMax</code>.
     *
     * @param distanceMeasure the {@link net.sourceforge.cilib.util.DistanceMeasure} that
     *        should be used to calculate the <code>zMax</code>
     * @param domain the {@link net.sourceforge.cilib.type.types.container.Vector} whose upper and lower bounds represent
     *			 the domain
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
    public static String toString(Vector vector, String first, String last, String delimeter) {
        StringBuilder stringRepresentation = new StringBuilder(first);
        Joiner.on(delimeter).appendTo(stringRepresentation, vector);

        return stringRepresentation.append(last).toString();
    }

    /**
     * Add an amount of noise (jitter) to the given {@link Vector}. Noise is calculated for each element of
     * the vector and then added to that element. The random amount of noise is a function of the given ratio of the
     * range (bounds) of the given vector. An example for a single element in the vector follows:
     *
     * Assume value := 0.3, ratio := 0.1, range := [-1, 1], random := [0, 1), then:
     * value + ((random - 0.5) * ratio * ((rangeMax - rangeMin) / 2) = 0.26 is the new value for the element.
     * If the ratio is 0.0, then no noise is added, i.e. the value remains the same.
     * If the ratio is 1.0, then the noise can be any value within the range [-range/2, range/2).
     * The random number determines what value within this range is used. Note how the random value is scaled (by
     * subtracting 0.5) so that negative numbers can also be generated.
     *
     * @param vector the {@Vector} to which noise should be added.
     * @param ratio the amount of noise that should be added.
     * @throws {@link UnsupportedOperationException} when an element in the is not a {@link Numeric}
     * @return a new {@link Vector} with added noise.
     */
    public static Vector jitter(Vector vector, final double ratio) {
        final RandomProvider random = new MersenneTwister();

        return Vectors.transform(vector, new Function<Numeric, Double>() {
            @Override
            public Double apply(Numeric from) {
                return from.doubleValue() + (random.nextDouble() - 0.5) * ratio * ((from.getBounds().getUpperBound() - from.getBounds().getLowerBound()) / 2.0);
            }
        });
    }

    /**
     * Some {@link Numeric} elements within a {@link Vector} might not always have a defined range. This method sets the
     * upper and lower bounds of the given vector accordingly.
     * @param vector The {@link Vector} whose bounds should be modified.
     * @param lowerBounds A {@link Vector} containing the lower bounds as its values.
     * @param upperBounds A {@link Vector} containing the upper bounds as its values.
     */
    public static Vector setBounds(Vector vector, Vector lowerBounds, Vector upperBounds) {
        Vector.Builder bounded = Vector.newBuilder();

        for (int i = 0, n = vector.size(); i < n; ++i) {
            bounded.add(Real.valueOf(vector.doubleValueOf(i), new Bounds(lowerBounds.doubleValueOf(i), upperBounds.doubleValueOf(i))));
        }
        return bounded.build();
    }

    public static Vector zeroVector(Vector vector) {
        return Vectors.transform(vector, new Function<Numeric, Double>() {
            @Override
            public Double apply(Numeric from) {
                return 0.0;
            }
        });
    }
}
