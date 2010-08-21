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
import net.sourceforge.cilib.io.DataReader;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.DataTableBuilder;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.io.transform.DataOperator;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.fitnessfactory.FitnessFactory;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * @author Theuns Cloete
 */
public interface ClusteringProblem extends OptimisationProblem {

    void setClusteringFunction(ClusteringFunction clusteringFunction);

    ClusteringFunction getClusteringFunction();

    /**
     * Retrieve the domain of the problem's data set.
     * @return the domain of this clustering problem
     */
    DomainRegistry getDomainRegistry();

    /**
     * Specify the domain of the problem's data set.
     * @param domainRegistry the domain of this clustering problem
     */
    void setDomainRegistry(DomainRegistry domainRegistry);

    /**
     * Specify the {@link DistanceMeasure distance measure} that should be used for all distance calculations throughout
     * the clustering.
     * @param distanceMeasure the {@link DistanceMeasure} that should be used
     */
    void setDistanceMeasure(DistanceMeasure distanceMeasure);

    /**
     * Retrieve the configured {@link DistanceMeasure distance measure} that should be used for all similarity
     * measurements during the clustering.
     * @return the configured {@link DistanceMeasure distance measure}
     */
    DistanceMeasure getDistanceMeasure();

    /**
     * Specify the {@link FitnessFactory} that should be used to create a {@link Fitness} object whenever the fitness of
     * the problem is calculated.
     * @param the {@link FitnessFactory} that should be used
     */
    void setFitnessFactory(FitnessFactory fitnessFactory);

    /**
     * The <em>expert</em> uses this method to set the number of clusters that should be used to optimise this
     * clustering.
     * @param numberOfClusters the user-specified <em>number of clusters</em> that should be used to optimise the
     * clustering
     */
    void setNumberOfClusters(int numberOfClusters);

    /**
     * Retrieve the number of clusters used throughout this clustering problem.
     * @return the configured number of clusters
     */
    int getNumberOfClusters();

    /**
     * Retrieve the maximum value possible in the configured domain. Whenever the domain is changed, the zMax is also
     * recalculated.
     * @return the maximum value possible in the domain
     */
    double getZMax();

    /**
     * Specify the {@link DataTableBuilder} that should be used to load a data set into memory. When this method is
     * called the {@link DataTableBuilder} should already be completely configured, i.e. its {@link DataReader} as well
     * as its operator pipeline (with {@link DataOperator} operators) should be configured.
     * @param dataTableBuilder the {@link DataTableBuilder} used to load a data set into memory.
     */
    void setDataTableBuilder(DataTableBuilder dataTableBuilder);

    /**
     * Retrieve the {@link DataTable} that represents the data set that should be clustered.
     * @return the data set that should be clustered.
     */
    DataTable<StandardPattern, TypeList> getDataTable();
}
