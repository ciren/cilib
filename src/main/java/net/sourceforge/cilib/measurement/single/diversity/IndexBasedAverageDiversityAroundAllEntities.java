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
package net.sourceforge.cilib.measurement.single.diversity;

import java.util.Iterator;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.boa.bee.ParameterizedWorkerBee;
import net.sourceforge.cilib.ec.ParameterizedDEIndividual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class allows the user to determine the diversity of a single dimension of the entity.
 * It treats parameters as an extra 4 dimensions at the end of the particle.
 * 
 */
public class IndexBasedAverageDiversityAroundAllEntities extends Diversity {
    private int index;
    private Topology topology;
    
    public IndexBasedAverageDiversityAroundAllEntities() {
        index = 0;
        topology = new GBestTopology();
    }
    
    @Override
    public Real getValue(Algorithm algorithm) {
        int numberOfEntities = ((PopulationBasedAlgorithm) algorithm).getTopology().size();
        topology = ((PopulationBasedAlgorithm) algorithm).getTopology();
        
        Iterator<? extends Entity> populationCenterIterator = ((PopulationBasedAlgorithm) algorithm).getTopology().iterator();

        double totalDistanceSum = 0.0;
        int iterationIndex = 0;
        int innerIndex = 0;

        while (populationCenterIterator.hasNext()) {
            double currentCenter = get(index, populationCenterIterator.next());
            
            Iterator<? extends Entity> populationIterator = ((PopulationBasedAlgorithm) algorithm).getTopology().iterator();
            double currentDistanceSum = 0.0;

            while (populationIterator.hasNext()) {
                double currentEntityPosition = get(index, populationIterator.next());
                currentDistanceSum += currentCenter - currentEntityPosition;
                
                innerIndex++;
            }

            totalDistanceSum += currentDistanceSum / numberOfEntities;
            iterationIndex++;
        }

        totalDistanceSum /= numberOfEntities;

        totalDistanceSum /= normalisationParameter.getNormalisationParameter((PopulationBasedAlgorithm) algorithm);

        return Real.valueOf(totalDistanceSum);
    }
    
    /*
     * Get the value at the index ispecified. If it is a parameter return the
     * parameter value, if it is a position return the position value
     */
    private double get(int index, Entity entity) {
        final int ENTITY_DIMENSIONS = entity.getDimension();
        double result = 0;
        
        if(index < ENTITY_DIMENSIONS) {
            Vector candidateSolution = (Vector) entity.getCandidateSolution();
            result = candidateSolution.get(index).doubleValue();
        } else {
            if(entity instanceof ParameterizedParticle) {
                if ((index >= ENTITY_DIMENSIONS) && (index < ENTITY_DIMENSIONS + 4)) {
                    ParameterizedParticle parameterizeParticle = (ParameterizedParticle) entity;

                    if(index == ENTITY_DIMENSIONS + 3)
                        result = parameterizeParticle.getVmax().getParameter();
                    else if(index == ENTITY_DIMENSIONS + 2)
                        result = parameterizeParticle.getCognitiveAcceleration().getParameter();
                    else if(index == ENTITY_DIMENSIONS + 1)
                        result = parameterizeParticle.getSocialAcceleration().getParameter();
                    else if(index == ENTITY_DIMENSIONS)
                        result = parameterizeParticle.getInertia().getParameter();

                } else {
                    throw(new IndexOutOfBoundsException("The maximum index can only be DIMENSIONS -1 or DIMENSIONS + 3 if a ParameterizedParticle is used"));
                }
            } else if(entity instanceof ParameterizedDEIndividual) {
                if ((index >= ENTITY_DIMENSIONS) && (index < ENTITY_DIMENSIONS + 2)) {
                    ParameterizedDEIndividual parameterizedIndividual = (ParameterizedDEIndividual) entity;

                    if(index == ENTITY_DIMENSIONS) 
                        result = parameterizedIndividual.getScalingFactor().getParameter();
                    else if(index == ENTITY_DIMENSIONS + 1)
                        result = parameterizedIndividual.getScalingFactor().getParameter();

                }
            } else if(entity instanceof ParameterizedWorkerBee) {
                if ((index >= ENTITY_DIMENSIONS) && (index < ENTITY_DIMENSIONS + 2)) {
                    ParameterizedWorkerBee parameterizedIndividual = (ParameterizedWorkerBee) entity;
                    
                    if(index == ENTITY_DIMENSIONS) 
                        result = parameterizedIndividual.getForageLimit().getParameter();
                    else if(index == ENTITY_DIMENSIONS + 1)
                        result = parameterizedIndividual.getExplorerBeeUpdateLimit().getParameter();
                }
            }
        }
        
        return result;
    }
    
    public void setIndex(int selectedIndex) {
        index = selectedIndex;
    }
    
    public int getIndex() {
        return index ;
    }
}