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
package net.sourceforge.cilib.functions.clustering.validityindices;

/**
 * This is the Generalised Dunn Index.
 *
 * Illustrated in:<br/>
 * <pre>
 * {@literal @}Article{ 5928407, title = "Some New Indexes of Cluster Validity", author = "James C. Bezdek and
 *           Nikhil R. Pal", journal = "IEEE Transactions on Systems, Man, and Cybernetics, Part B:
 *           Cybernetics", pages = "301--315", volume = "28", number = "3", month = jun, year =
 *           "1998", issn = "1083-4419"}.
 * </pre>
 * Sub-classes should implement the <tt>calculateWithinClusterScatter</tt> and
 * <tt>calculateBetweenClusterSeperation</tt> methods defined in {@linkplain ScatterSeperationRatio}.
 * @author Theuns Cloete
 */
public abstract class GeneralisedDunnIndex extends ScatterSeperationRatio {
    private static final long serialVersionUID = -2232760005736588472L;

    public GeneralisedDunnIndex() {
        super();
    }

    /**
     * This is a template method. It calls the <tt>calculateWithinClusterScatter</tt> and
     * <tt>calculateBetweenClusterSeperation</tt> methods defined in {@linkplain ScatterSeperationRatio}
     * that should be implemented by sub-classes.
     */
    @Override
    public double calculateFitness() {
        double withinScatter = -Double.MAX_VALUE, betweenSeperation = Double.MAX_VALUE;

        cacheWithinClusterScatter();
        cacheBetweenClusterSeperation();

        for (int i = 0; i < clustersFormed; i++) {
            withinScatter = Math.max(withinScatter, getWithinClusterScatter(i));
        }

        for (int i = 0; i < clustersFormed - 1; i++) {
            for (int j = i + 1; j < clustersFormed; j++) {
                betweenSeperation = Math.min(betweenSeperation, getBetweenClusterSeperation(i, j));
            }
        }

        return betweenSeperation / withinScatter;
    }

    @Override
    protected Double worstFitness() {
        return 0.0;
    }
}
