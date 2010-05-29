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
package net.sourceforge.cilib.problem.clustering;

import net.sourceforge.cilib.functions.clustering.ClusteringFunction;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.fitnessfactory.FitnessFactory;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * @author Theuns Cloete
 */
public interface ClusteringProblem extends OptimisationProblem {

    void setClusteringFunction(ClusteringFunction clusteringFunction);

    ClusteringFunction getClusteringFunction();

    DomainRegistry getDomainRegistry();

    void setDomainRegistry(DomainRegistry domainRegistry);

    void setDistanceMeasure(DistanceMeasure distanceMeasure);

    DistanceMeasure getDistanceMeasure();

    void setFitnessFactory(FitnessFactory fitnessFactory);

    void setNumberOfClusters(int numberOfClusters);

    int getNumberOfClusters();

    double getZMax();
}
