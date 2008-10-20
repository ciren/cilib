/*
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
package net.sourceforge.cilib.entity.operators.selection;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.TopologyHolder;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.MinimisationFitness;

/**
 * This class implements Roulette Wheel selection, also known as proportionate
 * selection. The probability of selecting an {@linkplain Entity} is equal to
 * the ratio of its fitness to the sum total of the fitness of all the
 * {@linkplain Entity}s in the population.
 * 
 * <p>
 * This method of selection is not particularly useful if a single solution has
 * very high fitness as compared to the rest. This results in selecting the
 * fittest solution every time we make a selection.
 * 
 * @author Vikash Ranjan Parida
 */
public class RouletteWheelSelectionStrategy extends SelectionStrategy {
	private static final long serialVersionUID = 6827649649373047787L;
	private Random random;

	/**
	 * Create an instance of the {@linkplain RouletteWheelSelectionStrategy}.
	 */
	public RouletteWheelSelectionStrategy() {
		this.random = new MersenneTwister();
	}

	/**
	 * Create a copy of the provided instance.
	 * @param copy The instance to copy.
	 */
	public RouletteWheelSelectionStrategy(final RouletteWheelSelectionStrategy copy) {
		this.random = copy.random.getClone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SelectionStrategy getClone() {
		return new RouletteWheelSelectionStrategy(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends Entity> T select(final Topology<T> population) {
		double minimumFitness = getMinimumFitness(population);
		double maximumFitness = getMaximumFitness(population);

		double totalFitness = getTotalFitness(population, minimumFitness, maximumFitness);
		double cumulativeProb = 0.0;
		double valueToPick = random.nextDouble();

		// If the fitness' have not been calculated return a random entity. This should NEVER happen.
		if (Double.compare(totalFitness, InferiorFitness.instance().getValue()) == 0)
			throw new UnsupportedOperationException("Cannot perform selection operator on Topology of Entity. Each Entity is incorrectly defined to have an InferiorFitness. Initial Fitness' need to be determined before selection");
		
		// If the fitness of all the Entities is zero, we randomly select one. This prevents the case
		// where it is possible to divide by zero resulting in an ArithmeticException.
		if (Double.compare(totalFitness, 0.0) == 0)
			return population.get(random.nextInt(population.size()));

		for (T entity : population) {
			double probability = getScaledFitness(entity, minimumFitness, maximumFitness) / totalFitness;
			if (valueToPick < cumulativeProb + probability) {
				return entity;
			}

			cumulativeProb += probability;
		}

		return population.get(population.size() - 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
//	public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
	public void performOperation(TopologyHolder holder) {
//		Topology<Entity> offspring = (Topology<Entity>) holder.getOffpsring();
		Topology<? extends Entity> topology = holder.getTopology();
//		offspring.add(select(topology));
		holder.add(select(topology));
	}

	/**
	 * It calculates the sum total of the fitness of all the {@linkplain Entity}s
	 * in the {@linkplain Topology}.
	 * 
	 * @param topology The {@linkplain Topology} to use.
	 * @return The sum total of the fitness values.
	 */
	private double getTotalFitness(final Topology<? extends Entity> topology, double minimum, double maximum) {
		double totalFitness = 0;
		for (Entity entity : topology) {
			totalFitness += getScaledFitness(entity, minimum, maximum);
		}

		return totalFitness;
	}

	/**
	 * Get the minimum fitness value of the entire population.
	 * @param topology The topology to determine the fitness of.
	 * @return The minimum fitness value of the entire topology / population.
	 */
	private double getMinimumFitness(final Topology<? extends Entity> topology) {
		double minimum = Double.MAX_VALUE;

		for (Entity entity : topology)
			minimum = Math.min(minimum, entity.getFitness().getValue().doubleValue());

		return minimum;
	}

	/**
	 * Get the maximum current fitness within the topology.
	 * @param topology The topology to determine the maximum fitness of.
	 * @return The maximum fitness within the entire topology / population.
	 */
	private double getMaximumFitness(final Topology<? extends Entity> topology) {
		double maximum = -Double.MAX_VALUE;

		for (Entity entity : topology)
			maximum = Math.max(maximum, entity.getFitness().getValue().doubleValue());
		
		return maximum;
	}

	/**
	 * Determine the scaled fitness of the provided entity. This fitness value is
	 * a fitness value that is scaled to <code>(0, 1]</code>.
	 * @param entity The entity for which the scaled fitness value is to be calculated.
	 * @param minimum The minimum fitness value in the current topology.
	 * @param maximum The maximum fitness value in the current topology.
	 * @return The scaled fitness value of the entity.
	 */
	private double getScaledFitness(Entity entity, double minimum, double maximum) {
		double result = 0.0;
		if (entity.getFitness() instanceof MinimisationFitness)
			result = 1.0 / (1.0 + entity.getFitness().getValue().doubleValue() - minimum);
		else
			result = 1.0 / (1.0 + maximum - entity.getFitness().getValue().doubleValue()); // Maximisation case

		if (Double.compare(result, Double.NaN) == 0) return result;
		if (result <= 0.0 & result > 1.0)
			throw new ArithmeticException("Scaling should prevent this. The value returned should be: 0.0 < x <= 1.0");

		return result;
	}

	/**
	 * Get the current {@linkplain Random} number generator. 
	 * @return The current {@linkplain Random}.
	 */
	public Random getRandom() {
		return random;
	}

	/**
	 * Set the {@linkplain Random} number generator.
	 * @param random the value to set.
	 */
	public void setRandom(Random random) {
		this.random = random;
	}

}
