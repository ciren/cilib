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
package net.sourceforge.cilib.measurement.entropy;

import java.util.Iterator;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Calculates the entropy in each dimension of the search space, with respect
 * to the positions of the entities in the population.
 *
 * @author Bennie Leonard
 */
public class DimensionalEntropyMeasurement extends EntropyMeasurement {

    public DimensionalEntropyMeasurement() {
        intervals = 5;
    }

    public DimensionalEntropyMeasurement(int i) {
        intervals = i;
    }

    public DimensionalEntropyMeasurement(DimensionalEntropyMeasurement copy) {
        this.intervals = copy.intervals;
    }

    @Override
    public Measurement<TypeList> getClone() {
        return new DimensionalEntropyMeasurement(this);
    }

    /**
     * Calculates an entropy value in each dimension of the search space.
     * For each dimension, the search space is divided into a number of
     * discrete <tt>intervals</tt>. Then, the probability that a given entity
     * will fall within a specific interval is calculated by counting the number
     * of entities in each interval and dividing this number by the size of
     * the population. The probabilities calculated for each interval are used
     * to determine the entropy in the specific dimension.
     *
     * @param algorithm The algorithm to perform the entropy measurement on.
     * @return An array of double values; the entropy measurements in each dimension of the search space.
     */
    @Override
    public TypeList getValue(Algorithm algorithm) {
        PopulationBasedAlgorithm populationBasedAlgorithm = (PopulationBasedAlgorithm) algorithm;

        int numberOfEntities = populationBasedAlgorithm.getTopology().size();
        Iterator<? extends Entity> populationIterator;

        int dimensions = populationBasedAlgorithm.getOptimisationProblem().getDomain().getDimension();

        Vector bounds = (Vector)populationBasedAlgorithm.getOptimisationProblem().getDomain().getBuiltRepresenation();
        Entity e;

        //keeps an entropy measurement for each dimension
        TypeList entropyMeasurements = new TypeList();

        for(int d = 0; d < dimensions; d++) {
            double dimensionLowerBound = bounds.get(d).getBounds().getLowerBound();
            double dimensionRange = bounds.get(d).getBounds().getRange();

            //keeps probabilities that a given particle will fall in each interval
            double[] probabilities = new double[intervals];

            for(int i = 0; i < intervals; i++) {
                int entityCount = 0; //number of entities in this interval

                double intervalLowerBound = dimensionLowerBound + i * (dimensionRange / intervals);
                double intervalUppderBound = intervalLowerBound + (dimensionRange / intervals);

                populationIterator = populationBasedAlgorithm.getTopology().iterator();

                while(populationIterator.hasNext()) {
                    e = populationIterator.next();
                    Vector entityPosition = (Vector) e.getCandidateSolution();
                    double position = entityPosition.get(d).doubleValue();

                    if(position <= intervalUppderBound && position >= intervalLowerBound) {
                        //The entity falls within the current interval in the current dimension
                        entityCount++;
                    }
                }

                //calculate probability that a particle falls within this interval
                double p = (double)entityCount / (double)numberOfEntities;
                probabilities[i] = p;
            }

            double entropy = 0.0;
            
            //calculate entropy for current dimension
            if(intervals == 1) {
                entropy = 0.0;
            } else {
                for(int i = 0; i < intervals; i++) {
                    double p = probabilities[i];
                    if(probabilities[i] > 0) { //equivalent to defining: 0 log 0 = 0
                        entropy += p * (Math.log(p) / Math.log(intervals));
                    }
                }
                entropy *= -1;
            }

            entropyMeasurements.append(Real.valueOf(entropy));
        }
        
        return entropyMeasurements;
    }
    
}
