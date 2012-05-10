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
package net.sourceforge.cilib.util.calculator;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.MinimisationFitness;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;

/**
 *
 * @author Kristina
 */
public class QuantizationErrorBasedFitnessCalculation implements FitnessCalculator<Entity> {
    
    public QuantizationErrorBasedFitnessCalculation() {
        
    }
    
    public QuantizationErrorBasedFitnessCalculation(QuantizationErrorBasedFitnessCalculation copy) {
        
    }

    @Override
    public FitnessCalculator<Entity> getClone() {
        return new QuantizationErrorBasedFitnessCalculation(this);
    }

    @Override
    public Fitness getFitness(Entity entity) {
        ClusterParticle particle = (ClusterParticle) entity;
        double quantizationError = 0;
        
        for(ClusterCentroid centroid : (CentroidHolder) particle.getCandidateSolution()) {
            for(double distance : centroid.getDataItemDistances()) {
                quantizationError += distance / (double) centroid.getDataItemDistances().length;
            }
        }
        
        quantizationError /= (double) particle.getDimension();
        
        return new MinimisationFitness(quantizationError);
    }

}
