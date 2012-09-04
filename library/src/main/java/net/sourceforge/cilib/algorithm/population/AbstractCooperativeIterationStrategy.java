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
package net.sourceforge.cilib.algorithm.population;

import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.StandardDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * This class holds the functionality that is common to a number of cooperative iteration strategies
 */
public abstract class AbstractCooperativeIterationStrategy<E extends PopulationBasedAlgorithm> extends AbstractIterationStrategy<E> {
    protected ClusterParticle contextParticle;
    protected boolean contextinitialized;
    protected DataTable table;

    /*
     * Default constructor for AbstractCooperativeIterationStrategy
     */
    public AbstractCooperativeIterationStrategy() {
        contextParticle = new ClusterParticle();
        contextinitialized = false;
        table = new StandardDataTable();
    }

    /*
     * Copy constructor for AbstractCooperativeIterationStrategy
     * @param copy The AbstractCooperativeIterationStrategy to be copied
     */
    public AbstractCooperativeIterationStrategy(AbstractCooperativeIterationStrategy copy) {
        contextParticle = copy.contextParticle;
        contextinitialized = copy.contextinitialized;
        table = copy.table;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract AbstractIterationStrategy<E> getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void performIteration(E algorithm);

    /*
     * Adds the data patterns closest to a centrid to its data pattern list
     * @param candidateSolution The solution holding all the centroids
     * @param dataset The dataset holding all the data patterns
     */
    public void assignDataPatternsToParticle(CentroidHolder candidateSolution, DataTable dataset) {
        double euclideanDistance;
        Vector addedPattern;
        DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();

        for(int i = 0; i < dataset.size(); i++) {
                euclideanDistance = Double.POSITIVE_INFINITY;
                addedPattern = Vector.of();
                Vector pattern = ((StandardPattern) dataset.getRow(i)).getVector();
                int centroidIndex = 0;
                int patternIndex = 0;
                for(ClusterCentroid centroid : candidateSolution) {
                    if(distanceMeasure.distance(centroid.toVector(), pattern) < euclideanDistance) {
                        euclideanDistance = distanceMeasure.distance(centroid.toVector(), pattern);
                        addedPattern = Vector.copyOf(pattern);
                        patternIndex = centroidIndex;
                    }
                    centroidIndex++;
                }

                candidateSolution.get(patternIndex).addDataItem(euclideanDistance, addedPattern);
            }
    }

    /*
     * Returns the context particle
     * @return contextParticle The context particle
     */
    public ClusterParticle getContextParticle() {
        clearDataPatterns(contextParticle);
        assignDataPatternsToParticle((CentroidHolder) contextParticle.getCandidateSolution(), table);
        return contextParticle;
    }

    /*
     * Initializes the context particle for the first time
     * @param algorithm The algorithm whose context particle needs to be initialized
     */
    public void initializeContextParticle(MultiPopulationBasedAlgorithm algorithm) {
        int populationIndex = 0;
        CentroidHolder solution = new CentroidHolder();
        CentroidHolder velocity = new CentroidHolder();
        CentroidHolder bestPosition = new CentroidHolder();

        for(PopulationBasedAlgorithm alg : algorithm.getPopulations()) {
            if(!((DataClusteringPSO) alg).isExplorer()) {
                solution.add(((CentroidHolder) alg.getTopology().get(0).getCandidateSolution()).get(populationIndex).getClone());
                velocity.add(((CentroidHolder) ((ClusterParticle) alg.getTopology().get(0)).getVelocity()).get(populationIndex).getClone());
                bestPosition.add(((CentroidHolder) ((ClusterParticle) alg.getTopology().get(0)).getBestPosition()).get(populationIndex).getClone());

                populationIndex++;
            }
        }

        contextParticle.setCandidateSolution(solution);
        contextParticle.getProperties().put(EntityType.Particle.VELOCITY, velocity);
        contextParticle.getProperties().put(EntityType.Particle.BEST_POSITION, bestPosition);
        contextParticle.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        contextParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());

        contextinitialized = true;
    }


    /*
     * Removes all data patterns held by cluster centroids held by the particle received
     * @param particle The particle whose centrids must be cleared
     */
    public void clearDataPatterns(ClusterParticle particle) {
        for(ClusterCentroid centroid : (CentroidHolder) particle.getCandidateSolution()) {
            centroid.clearDataItems();
        }
    }
}
