/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.iterationstrategies;

import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.ec.SaDEIndividual;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 * This is a standard DE which uses an SaSEIndividual and updates its parameters at
 * the end of each iteration.
 *
 */
public class AdaptiveIterationStrategy extends AbstractIterationStrategy<EC> {

    protected Selector<SaDEIndividual> targetVectorSelectionStrategy;

    /**
     * Create an instance of the {@linkplain AdaptiveIterationStrategy}.
     */
    public AdaptiveIterationStrategy() {
        this.targetVectorSelectionStrategy = new RandomSelector();
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public AdaptiveIterationStrategy(AdaptiveIterationStrategy copy) {
        this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdaptiveIterationStrategy getClone() {
        return new AdaptiveIterationStrategy(this);
    }

    /**
     * Perform an iteration of the DE algorithm defined as the DE/x/y/z implementation but
     * including a parameter adaptation step.
     * @param ec The {@linkplain EC} on which to perform this iteration.
     */
    @Override
    public void performIteration(EC ec) {
        Topology<SaDEIndividual> topology = (Topology<SaDEIndividual>) ec.getTopology();

        for (int i = 0; i < topology.size(); i++) {
            SaDEIndividual current = topology.get(i);

            // Create the trial vector by applying mutation
            SaDEIndividual targetEntity = targetVectorSelectionStrategy.on(topology).exclude(current).select();

            // Create the trial vector / entity
            SaDEIndividual trialEntity = current.getTrialVectorCreationStrategy().create(targetEntity, current, topology);

            // Create the offspring by applying cross-over
            List<SaDEIndividual> offspring = current.getCrossoverStrategy().crossover(Arrays.asList(current, trialEntity)); // Order is VERY important here!!

            // Replace the parent (current) if the offspring is better
            SaDEIndividual offspringEntity = offspring.get(0);
            boundaryConstraint.enforce(offspringEntity);
            offspringEntity.calculateFitness();

            if (offspringEntity.getFitness().compareTo(current.getFitness()) > 0) { // the trial vector is better than the parent
                topology.set(i, offspringEntity); // Replace the parent with the offspring individual
            }

            topology.get(i).updateParameters();
        }
    }

    /**
     * Obtain the {@linkplain Selector} used to select the target vector.
     * @return The {@linkplain Selector} of the target vector.
     */
    public Selector getTargetVectorSelectionStrategy() {
        return targetVectorSelectionStrategy;
    }

    /**
     * Set the {@linkplain Selector} used to select the target vector within the DE.
     * @param targetVectorSelectionStrategy The {@linkplain Selector} to use for the
     *        selection of the target vector.
     */
    public void setTargetVectorSelectionStrategy(Selector targetVectorSelectionStrategy) {
        this.targetVectorSelectionStrategy = targetVectorSelectionStrategy;
    }

}
