package net.sourceforge.cilib.ec.iterationstrategies;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.OnePointCrossoverStrategy;
import net.sourceforge.cilib.entity.operators.mutation.GaussianMutationStrategy;
import net.sourceforge.cilib.entity.operators.mutation.MutationStrategy;
import net.sourceforge.cilib.math.random.RandomNumber;

public class GeneticAlgorithmIterationStrategy implements IterationStrategy {
	
	private CrossoverStrategy crossoverStrategy;
	private MutationStrategy mutationStrategy;
	private RandomNumber randomNumber;
	
	public GeneticAlgorithmIterationStrategy() {
		this.crossoverStrategy = new OnePointCrossoverStrategy();
		this.mutationStrategy = new GaussianMutationStrategy();
		this.randomNumber = new RandomNumber();
	}

	public void perfromIteration(EC ec) {
		// Cacluate the fitness
		for (Iterator<? extends Entity> i = ec.getTopology().iterator(); i.hasNext(); ) {
		//for (Individual indiv : ec.getTopology()) {
			Entity entity = i.next();
			//indiv.setFitness(problem.getFitness(indiv.get(), true));
			entity.setFitness(ec.getOptimisationProblem().getFitness(entity.get(), true));
		}
		
		// Perform crossover
		List<Entity> crossedOver = this.crossoverStrategy.crossover(ec.getTopology());
				
		// Perform mutation on offspring
		this.mutationStrategy.mutate(crossedOver);
		
		
		// Perform new population selection
		Topology<Entity> topology = (Topology<Entity>) ec.getTopology();
		for (Iterator<Entity> i = crossedOver.iterator(); i.hasNext(); ) {
			Entity entity = i.next();
			topology.add(entity);
		}
		
		Collections.sort(ec.getTopology(), new AscendingFitnessComparator());
		//Collections.shuffle(ec.getTopology());

		//System.out.println("\n\n\nSorted list");
		//for (Entity e : topology) {
		//	System.out.println(e.getFitness());
		//}
		/*System.out.println("topology size before: " + topology.size());
		System.out.println("number of generated individuals: " + crossedOver.size());
		System.out.println("pop size: " + ec.getPopulationSize());
		for (int i = ec.getPopulationSize(); i < crossedOver.size(); i++) {
			int number = Double.valueOf(this.randomNumber.getUniform(0, topology.size())).intValue();
			topology.remove(number);
		}
		System.out.println("size after: " + topology.size());*/
		for (ListIterator<? extends Entity> i = ec.getTopology().listIterator(ec.getPopulationSize()); i.hasNext(); ) {
			i.next();
			i.remove();
		}
		
		crossedOver = null;
	}

}
