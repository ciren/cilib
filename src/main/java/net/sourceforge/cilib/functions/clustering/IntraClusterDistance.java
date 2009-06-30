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
package net.sourceforge.cilib.functions.clustering;

/**
 * This <i>clustering fitness function</i> will probably never be used to train on directly. The
 * main reason why it has been implemented is to be able to take measurements of the
 * <i>intra-cluster distance</i> via the {@linkplain GenericFunctionMeasurement} class.
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCenterStrategy}.
 * @author Theuns Cloete
 */
public class IntraClusterDistance extends ClusteringFitnessFunction {
    private static final long serialVersionUID = -4185205766188040942L;

    @Override
    public double calculateFitness() {
        return calculateAverageIntraClusterDistance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IntraClusterDistance getClone() {
        return new IntraClusterDistance();
    }
}
