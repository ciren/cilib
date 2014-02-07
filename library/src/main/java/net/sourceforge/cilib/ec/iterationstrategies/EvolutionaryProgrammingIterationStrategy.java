/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.iterationstrategies;

import com.google.common.collect.Lists;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.operators.mutation.GaussianMutationStrategy;
import net.sourceforge.cilib.entity.operators.mutation.MutationStrategy;
import net.sourceforge.cilib.util.functions.Entities;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.ScoredSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

public class EvolutionaryProgrammingIterationStrategy extends AbstractIterationStrategy<EC> {

    private static final long serialVersionUID = 4966470754016818350L;

    private MutationStrategy mutationStrategy;
    private Selector<Individual> populationSelector;

    public EvolutionaryProgrammingIterationStrategy() {
        this.mutationStrategy = new GaussianMutationStrategy();
        this.populationSelector = new ScoredSelector<>();
    }

    private EvolutionaryProgrammingIterationStrategy(EvolutionaryProgrammingIterationStrategy copy) {
        this.mutationStrategy = copy.mutationStrategy.getClone();
        this.populationSelector = copy.populationSelector;
    }

    @Override
    public EvolutionaryProgrammingIterationStrategy getClone() {
        return new EvolutionaryProgrammingIterationStrategy(this);
    }

    @Override
    public void performIteration(final EC algorithm) {
        fj.data.List<Individual> topology = algorithm.getTopology();
        fj.data.List<Individual> offspring = topology.map(Entities.<Individual>clone_());

        // Apply the mutation
        this.mutationStrategy.mutate(Lists.newArrayList(offspring));

        for (Individual individual : offspring) {
            boundaryConstraint.enforce(individual);
            individual.updateFitness(individual.getBehaviour().getFitnessCalculator().getFitness(individual));
        }
        
        // Perform new population selection
        algorithm.setTopology(fj.data.List.iterableList(populationSelector
            .on(topology.append(fj.data.List.iterableList(offspring)))
            .select(Samples.first(topology.length()))));
    }

    public MutationStrategy getMutationStrategy() {
        return mutationStrategy;
    }

    public void setMutationStrategy(MutationStrategy mutationStrategy) {
        this.mutationStrategy = mutationStrategy;
    }

    public Selector<Individual> getPopulationSelector() {
        return populationSelector;
    }

    public void setPopulationSelector(Selector<Individual> populationSelector) {
        this.populationSelector = populationSelector;
    }
    
}
