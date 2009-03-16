/**
 * Copyright (C) 2003 - 2008
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

package net.sourceforge.cilib.pso.niching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.StatUtils;

/**
 *
 * @author gpampara
 */
public class StandardNicheIdentificationStrategy implements NicheIdentificationStrategy {

    private double threshold;
    private int stationaryCounter;
    private Map<Entity, List<Double>> entityFitness;

    public StandardNicheIdentificationStrategy() {
        this.threshold = -Double.MAX_VALUE;
        this.stationaryCounter = 3;
        this.entityFitness = new HashMap<Entity, List<Double>>();
    }

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

                if (fitnessList.size() >= this.stationaryCounter)
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

            stdev.put(entry.getKey(), StatUtils.stdDeviation(entry.getValue().toArray(new Double []{})));
        }

        // Identify the niches
        List<Entity> niches = new ArrayList<Entity>();
        for (Map.Entry<Entity, Double> entry : stdev.entrySet()) {
            if (entry.getValue() < this.threshold) {
                niches.add(entry.getKey());
                System.out.println("Found a niche!");
                System.out.println("niche fitness value: " + entry.getValue());
            }
        }

        return niches;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public int getStationaryCounter() {
        return stationaryCounter;
    }

    public void setStationaryCounter(int stationaryCounter) {
        this.stationaryCounter = stationaryCounter;
    }

}
