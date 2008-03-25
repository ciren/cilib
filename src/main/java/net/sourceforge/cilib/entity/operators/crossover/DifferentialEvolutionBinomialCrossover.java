package net.sourceforge.cilib.entity.operators.crossover;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * 
 * @author gpampara
 *
 */
public class DifferentialEvolutionBinomialCrossover extends CrossoverStrategy {

	/**
	 * {@inheritDoc}
	 */
	public DifferentialEvolutionBinomialCrossover getClone() {
		return new DifferentialEvolutionBinomialCrossover();
	}

	/**
	 * Perform the cross-over based on the binomial method for recombination. The given
	 * <code>parentCollection</code> should only contain two {@linkplain Entity} objects,
	 * as the crossover operator is only defined for two {@linkplain Entity}s.
	 * 
	 * <p>
	 * It is VERY important that the order in which the parents are presented is consistent.
	 * The first {@linkplain Entity} within the collection MUST be the <code>trialVector</code>
	 * {@linkplain Entity}, followed by the target parent {@linkplain Entity}.
	 * 
	 * @throws UnsupportedOperationException if the number of parents does not equal the size value of 2.
	 * @return A list consisting of the offspring. This operator only returns a single offspring {@linkplain Entity}.
	 */
	public List<Entity> crossover(List<Entity> parentCollection) {
		if (parentCollection.size() != 2)
			throw new UnsupportedOperationException("Cannot use exponential recomination on a parent entity grouping not consisting of 2 entities");
		
		Vector parentVector = (Vector) parentCollection.get(0).getContents();
		Vector trialVector = (Vector) parentCollection.get(1).getContents();
		Vector offspringVector = new Vector();
		
		int i = Double.valueOf(this.getRandomNumber().getUniform(0, parentVector.getDimension())).intValue();
		
		for (int j = 0; j < parentVector.getDimension(); j++) {
			if ((getRandomNumber().getUniform() < this.getCrossoverProbability().getParameter()) || (j == i))
				offspringVector.add(new Real(trialVector.getReal(j)));
			else 
				offspringVector.add(new Real(parentVector.getReal(j)));
		}
		
		Entity offspring = parentCollection.get(0).getClone();
		offspring.setContents(offspringVector);
		return Arrays.asList(offspring);
	}

	/**
	 * 
	 */
	public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
		throw new UnsupportedOperationException("performOperation() is currently not implemented.");
	}

}
