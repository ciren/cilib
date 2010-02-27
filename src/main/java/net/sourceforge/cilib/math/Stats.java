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
package net.sourceforge.cilib.math;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.cilib.problem.dataset.Pattern;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Sequence;
import net.sourceforge.cilib.util.Vectors;

/**
 * Some simple methods for determining some statistics.
 * @author Gary Pampara, Theuns Cloete
 */
public final class Stats {

    private Stats() {
    }

    /**
     *
     * @param vector
     * @return
     */
    public static double mean(Vector vector) {
        double sum = 0.0;
        for (int i = 0; i < vector.size(); i++) {
            sum += vector.doubleValueOf(i);
        }

        double xbar = sum / vector.size();
        double correction = 0.0;
        for (int i = 0; i < vector.size(); i++) {
            correction += (vector.doubleValueOf(i) - xbar);
        }

        return xbar + (correction / vector.size());
    }

    /**
     * Calculates the mean {@linkplain Vector} of the given set/cluster/collection of
     * {@link Pattern}s.
     *
     * This is illustrated in Equation 4.b of:<br/>
     * @InProceedings{ 657864, author = "Maria Halkidi and Michalis Vazirgiannis", title =
     *                 "Clustering Validity Assessment: Finding the Optimal Partitioning of a Data
     *                 Set", booktitle = "Proceedings of the IEEE International Conference on Data
     *                 Mining", year = "2001", isbn = "0-7695-1119-8", pages = "187--194", publisher =
     *                 "IEEE Computer Society", address = "Washington, DC, USA" }
     * @param set a set ({@link ArrayList}) of {@link Pattern}s
     * @return a {@link Vector} that represents the mean/center of the given set
     */
    public static Vector meanVector(Collection<Pattern> set) {
        if (set.isEmpty()) {
            throw new IllegalArgumentException("Cannot calculate the mean for an empty set");
        }

        Vector mean = Vectors.zeroVector(Iterables.get(set, 0).data);

        for (Pattern pattern : set) {
            mean = mean.plus(pattern.data);
        }
        return mean.divide(set.size());
    }

    /**
     *
     * @param vector
     * @return
     */
    public static double variance(final Vector vector) {
        double mean = mean(vector);
        double summation = 0.0;

        for (int i = 0; i < vector.size(); i++) {
            summation += (vector.doubleValueOf(i) - mean) * (vector.doubleValueOf(i) - mean);
        }

        return summation / vector.size();
    }

    /**
     * Calculates the variance of the given set/cluster/collection of @{link Pattern}s.
     *
     * This is illustrated in Equation 4.c of:<br/>
     * {@literal @}InProceedings{ 657864, author = "Maria Halkidi and Michalis Vazirgiannis", title =
     *                 "Clustering Validity Assessment: Finding the Optimal Partitioning of a Data
     *                 Set", booktitle = "Proceedings of the IEEE International Conference on Data
     *                 Mining", year = "2001", isbn = "0-7695-1119-8", pages = "187--194", publisher =
     *                 "IEEE Computer Society", address = "Washington, DC, USA" }
     * @param set a set ({@link ArrayList}) of {@link Pattern}s
     * @param center a {@link Vector} that represents the mean/center of the accompanied set
     * @return a double representing the variance of the given set with the given center
     */
    public static double variance(Collection<Pattern> set, Vector center) {
        return varianceVector(set, center).norm();
    }

    /**
     * Calculates the variance vector of the given set/cluster/collection of @{link Pattern}s.
     *
     * @param set a set ({@link ArrayList}) of {@link Pattern}s
     * @param center a {@link Vector} that represents the mean/center of the accompanied set
     * @return a {@link Vector} representing the variance vector of the given set with the given center. When the norm
     *         of this vector is taken, you will get the actual variance scalar of the given set with given center.
     */
    public static Vector varianceVector(Collection<Pattern> set, Vector center) {
        if (set.isEmpty()) {
            throw new IllegalArgumentException("Cannot calculate the variance for an empty set");
        }

        Vector variance = Vectors.zeroVector(center);

        for (Pattern pattern : set) {
            Vector diffSquare = Vectors.transform(pattern.data.subtract(center), new Function<Numeric, Double>() {
                @Override
                public Double apply(Numeric from) {
                    return from.doubleValue() * from.doubleValue();
                }
            });

            variance = variance.plus(diffSquare);
        }
        return variance.divide(set.size());
    }

    /**
     *
     * @param vector
     * @return
     */
    public static double stdDeviation(final Vector vector) {
        return Math.sqrt(variance(vector));
    }

    public static double stdDeviation(Number... values) {
        return Math.sqrt(variance(Vector.of(values)));
    }

    public static double stdDeviation(Collection<Pattern> set, Vector center) {
        return Math.sqrt(variance(set, center));
    }

    public static Vector stdDeviationVector(Collection<Pattern> set, Vector center) {
        Vector variance = varianceVector(set, center);

        return Vectors.transform(variance, new Function<Numeric, Double>() {
            @Override
            public Double apply(Numeric from) {
                return Math.sqrt(from.doubleValue());
            }
        });
    }
}
