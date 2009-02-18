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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.velocityupdatestrategies.GCVelocityUpdateStrategy;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 *
 * @author gpampara
 */
public class StandardSwarmCreationStrategy implements NicheCreationStrategy {

	private Map<Entity, List<Fitness>> mainSwarmFitnesses;
	private int fitnessTraceLength;
	private double threshold;

	public StandardSwarmCreationStrategy() {
		this.mainSwarmFitnesses = new HashMap<Entity, List<Fitness>>();
		this.fitnessTraceLength = 3;
		this.threshold = 0.001;
	}

	@Override
	public void create(Niche algorithm) {
		// Remove any particles that are no longer contained within the main swarm
		PopulationBasedAlgorithm mainSwarm = algorithm.getMainSwarm();
		for (Entity entity : mainSwarmFitnesses.keySet()) {
			if (!mainSwarm.getTopology().contains(entity))
				this.mainSwarmFitnesses.remove(entity);
		}

		// Update the current fitness trace for each entity in the main swarm
		for (Entity entity : mainSwarm.getTopology()) {
			if (!this.mainSwarmFitnesses.containsKey(entity)) {
				this.mainSwarmFitnesses.put(entity, Arrays.asList(entity.getFitness()));
				continue;
			}

			List<Fitness> fitnessTrace = this.mainSwarmFitnesses.get(entity);
			if (fitnessTrace.size() >= fitnessTraceLength)
				fitnessTrace.remove(0);

			fitnessTrace.add(entity.getFitness());
		}

		// Calculate the current deviation values.
		Map<Entity, Double> deviations = new HashMap<Entity, Double>(this.mainSwarmFitnesses.size());
		for (Map.Entry<Entity, List<Fitness>> entry : mainSwarmFitnesses.entrySet()) {
//			double value = StatUtils.stdDeviation(entry.getValue());
			throw new UnsupportedOperationException("this needs to be verified!");
//                     deviations.put(entry.getKey(), value);
		}

		// Determine if sub swarms should be created.
		for (Map.Entry<Entity, Double> deviation : deviations.entrySet()) {
			if (Double.compare(deviation.getValue().doubleValue(), threshold) < 0) {
				PopulationBasedAlgorithm subSwarm = createSubSwarm(deviation.getKey(), mainSwarm);
				algorithm.addPopulationBasedAlgorithm(subSwarm);
			}
		}
	}

	private PopulationBasedAlgorithm createSubSwarm(Entity entity, PopulationBasedAlgorithm mainSwarm) {
		Entity closestEntity = getClosestEntity(entity, mainSwarm);
		mainSwarm.getTopology().remove(entity);
		mainSwarm.getTopology().remove(closestEntity); // Remove the closet entity from the main swarm

		PSO subSwarm = new PSO();
		subSwarm.getTopology().add((Particle) entity);
		subSwarm.getTopology().add((Particle) closestEntity);

		for (Particle p : subSwarm.getTopology()) {
			p.setVelocityUpdateStrategy(new GCVelocityUpdateStrategy());
		}

		System.out.println("This implementation needs to be completed!!");

		return subSwarm;
	}

	private Entity getClosestEntity(Entity entity, PopulationBasedAlgorithm mainSwarm) {
		Entity closestEntity = null;
		double closest = Double.MAX_VALUE;
		DistanceMeasure measure = new EuclideanDistanceMeasure();

		for (Entity other : mainSwarm.getTopology()) {
			double distance = measure.distance((Vector) entity.getCandidateSolution(), (Vector) other.getCandidateSolution());
			if (distance < closest) {
				closest = distance;
				closestEntity = other;
			}
		}

		return closestEntity;
	}

}
