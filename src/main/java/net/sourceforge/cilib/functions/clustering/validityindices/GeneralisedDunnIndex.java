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
package net.sourceforge.cilib.functions.clustering.validityindices;

/**
 * This is the Generalised Dunn Index.
 *
 *  Illustrated in:<br/>
 * @Article{ 5928407, title = "Some New Indexes of Cluster Validity", author = "James C. Bezdek and
 *           Nikhil R. Pal", journal = "IEEE Transactions on Systems, Man, and Cybernetics, Part B:
 *           Cybernetics", pages = "301--315", volume = "28", number = "3", month = jun, year =
 *           "1998", issn = "1083-4419"}.
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
        return getMinimum();
    }
}
