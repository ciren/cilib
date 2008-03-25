package net.sourceforge.cilib.entity.operators.selection;

import java.util.Random;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;

/**
 * 
 * @author gpampara
 *
 */
public class RandomSelectionStrategy extends SelectionStrategy {
	
	private Random random;
	
	public RandomSelectionStrategy() {
		this.random = new MersenneTwister();
	}

	@Override
	public SelectionStrategy getClone() {
		return null;
	}

	@Override
	public Entity select(Topology<? extends Entity> population) {
		return population.get(random.nextInt(population.size()));
	}

	@Override
	public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {

	}

}
