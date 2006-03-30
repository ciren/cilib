package net.sourceforge.cilib.entity.operators.crossover;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Vector;

/**
*
* @author  Andries Engelbrecht
*/

public class OnePointCrossoverStrategy extends CrossoverStrategy {

	@Override
	public void crossover(Entity parent1, Entity parent2) {
		
		//How do we handle variable sizes? Resizing the entities?
		Entity offspring1 = parent1.clone();
		Entity offspring2 = parent2.clone();
		
		if (this.getCrossoverProbability().getParameter() >= this.getRandomNumber().getUniform()) {
			
			Vector parentChromosome1 = (Vector) parent1.get();
			Vector parentChromosome2 = (Vector) parent2.get();
			Vector offspringChromosome1 = (Vector) offspring1.get();
			Vector offspringChromosome2 = (Vector) offspring2.get();
			
			int sizeParent1 = parentChromosome1.getDimension();
			int sizeParent2 = parentChromosome2.getDimension();
			int selectRange = sizeParent2;
			int maxDimension = sizeParent1;
		
			if (sizeParent1 <= sizeParent2) {
				selectRange = sizeParent1;
				maxDimension = sizeParent2;
			}
					
			int crossoverPoint = (int) this.getRandomNumber().getUniform(0,selectRange-1);
		
			for (int i = 0; i < crossoverPoint; i++) {
				offspringChromosome1.set(i,parentChromosome1.get(i));
				offspringChromosome2.set(i,parentChromosome2.get(i));
			}
			for (int i = crossoverPoint; i < maxDimension; i++) {
				offspringChromosome1.set(i,parentChromosome2.get(i));
				offspringChromosome2.set(i,parentChromosome1.get(i));
			}
		}
		parent1 = offspring1;
		parent2 = offspring2;
	}
}
