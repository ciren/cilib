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
package net.sourceforge.cilib.math;

import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * Some simple methods for determining some statistics.
 *
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
            sum += vector.getReal(i);
        }

        double xbar = sum / vector.size();
        double correction = 0.0;
        for (int i = 0; i < vector.size(); i++) {
            correction += (vector.getReal(i) - xbar);
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
        if (set.isEmpty())
            throw new IllegalArgumentException("Cannot calculate the mean for an empty set");

        Vector mean = null;

        for (Pattern pattern : set) {
            if (mean == null) {
                mean = pattern.data.getClone();
                mean.reset();        // initialize the mean to be all zeroes
            }
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
            summation += (vector.getReal(i) - mean) * (vector.getReal(i) - mean);
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
        if (set.isEmpty())
            throw new IllegalArgumentException("Cannot calculate the variance for an empty set");

        Vector variance = center.getClone();

        variance.reset();        // initialize the variance to be all zeroes
        for (Pattern pattern : set) {
            Vector diffSquare = pattern.data.subtract(center);
            for (Numeric numeric : diffSquare)
                numeric.setReal(numeric.getReal()*numeric.getReal());
            variance = variance.plus(diffSquare);
        }
        return variance.norm() / set.size();
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
        return Math.sqrt(variance(Vectors.create(values)));
    }

}
