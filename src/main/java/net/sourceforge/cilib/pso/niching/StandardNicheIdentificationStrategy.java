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
package net.sourceforge.cilib.pso.niching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.Stats;

/**
 * <p>
 * Identify if a niche has been located.
 * </p>
 * <p>
 * Niches are defined to be found if the fitness of an entity has a standard deviation
 * of less than a threshold value for a predefined number of iterations.
 * </p>
 * <p>
 * If the fitness of the entity is indeed less than the predefined threshold, it is
 * determined that a niche has been located and the entity is returned as a
 * result for a new niching location.
 * </p>
 */
public class StandardNicheIdentificationStrategy implements NicheIdentificationStrategy {

    private ControlParameter threshold;
    private ControlParameter stationaryCounter;
    private Map<Entity, List<Double>> entityFitness;

    public StandardNicheIdentificationStrategy() {
        this.threshold = ConstantControlParameter.of(1.0E-6);
        this.stationaryCounter = ConstantControlParameter.of(3.0);
        this.entityFitness = new HashMap<Entity, List<Double>>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Entity> identify(Topology<? extends Entity> topology) {
        Map<Entity, Double> stdev = new HashMap<Entity, Double>();

        // Need to clean out old entity + fitness entries.
        List<Entity> removalList = new ArrayList<Entity>();
        for (Map.Entry<Entity, List<Double>> entry : this.entityFitness.entrySet()) {
            if (!topology.contains(entry.getKey())) {
                removalList.add(entry.getKey());
            }
        }

        for (Entity element : removalList)
            this.entityFitness.remove(element);

        // Need to update the list of fitnesses
        for (Entity entity : topology) {
            if (this.entityFitness.keySet().contains(entity)) {
                List<Double> fitnessList = this.entityFitness.get(entity);

                if (fitnessList.size() >= this.stationaryCounter.getParameter())
                    fitnessList.remove(0);

                fitnessList.add(entity.getFitness().getValue());
                continue;
            }

            List<Double> list = new ArrayList<Double>();
            list.add(entity.getFitness().getValue());
            this.entityFitness.put(entity, list);
        }

        // Finally do the calculations of the stdev:
        for (Map.Entry<Entity, List<Double>> entry : this.entityFitness.entrySet()) {
            if (entry.getValue().size() <= 2)
                continue;

            stdev.put(entry.getKey(), Stats.stdDeviation(entry.getValue().toArray(new Double []{})));
        }

        // Identify the niches
        List<Entity> niches = new ArrayList<Entity>();
        for (Map.Entry<Entity, Double> entry : stdev.entrySet()) {
            if (entry.getValue() < this.threshold.getParameter()) {
                niches.add(entry.getKey());
            }
        }

        return niches;
    }

    /**
     * Get the defined threshold value.
     * @return The threshold value.
     */
    public ControlParameter getThreshold() {
        return threshold;
    }

    /**
     * Set the threshold value
     * @param threshold The value to set.
     */
    public void setThreshold(ControlParameter threshold) {
        this.threshold = threshold;
    }

    /**
     * Obtain the stationary counter for the identification process.
     * @return The value of the stationary counter.
     */
    public ControlParameter getStationaryCounter() {
        return stationaryCounter;
    }

    /**
     * Set the stationary counter for the identification process.
     * @param stationaryCounter The counter value to set.
     */
    public void setStationaryCounter(ControlParameter stationaryCounter) {
        this.stationaryCounter = stationaryCounter;
    }

}
