/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.iterationstrategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.mutation.GaussianMutationStrategy;
import net.sourceforge.cilib.entity.operators.mutation.MutationStrategy;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.RandomArrangement;

public class EvolutionaryProgrammingIterationStrategy extends AbstractIterationStrategy<EC> {

    private static final long serialVersionUID = 4966470754016818350L;

    private MutationStrategy mutationStrategy;

    public EvolutionaryProgrammingIterationStrategy() {
        this.mutationStrategy = new GaussianMutationStrategy();
    }

    private EvolutionaryProgrammingIterationStrategy(EvolutionaryProgrammingIterationStrategy copy) {
        this.mutationStrategy = copy.mutationStrategy.getClone();
    }

    @Override
    public EvolutionaryProgrammingIterationStrategy getClone() {
        return new EvolutionaryProgrammingIterationStrategy(this);
    }

    @Override
    public void performIteration(EC algorithm) {
        Topology<Individual> topology = algorithm.getTopology();
        List<Individual> offspring = new ArrayList();

        for (Individual individual : topology) {
            offspring.add(individual.getClone()); // Create an offspring by cloning the parent.
        }

        // Apply the mutation
        this.mutationStrategy.mutate(offspring);

        for (Individual individual : offspring) {
            individual.calculateFitness();
        }

        topology.addAll(offspring);

        List<Individual> newPopulation = new ArrayList(algorithm.getInitialisationStrategy().getEntityNumber());

        List<IndividualScore> scores = new ArrayList();
        for (int i = 0; i < topology.size(); i++) {
            Individual current = topology.get(i);
            int score = getScore(current, topology);
            scores.add(new IndividualScore(current, score));
        }

        Collections.sort(scores, new Comparator<IndividualScore>() {
            @Override
            public int compare(IndividualScore o1, IndividualScore o2) {
                int thisVal = o1.getScore();
                int anotherVal = o2.getScore();
                return (thisVal<anotherVal ? -1 : (thisVal==anotherVal ? 0 : 1));
            }
        });

        for (int i = 0; i < algorithm.getInitialisationStrategy().getEntityNumber(); i++) {
            newPopulation.add(scores.get(i).getEntity());
        }

        topology.clear();
        topology.addAll(newPopulation);
    }

    private int getScore(Individual current, Topology<Individual> topology) {
        int score = 0;
        List<Individual> selection = Selection.copyOf(topology)
                .orderBy(new RandomArrangement())
                .select(Samples.first(10).unique());

        for (Individual i : selection) {
            if (current.getFitness().compareTo(i.getFitness()) < 0) {
                score++;
            }
        }

        return score;
    }

    public MutationStrategy getMutationStrategy() {
        return mutationStrategy;
    }

    public void setMutationStrategy(MutationStrategy mutationStrategy) {
        this.mutationStrategy = mutationStrategy;
    }

    private static final class IndividualScore {
        private final Individual entity;
        private final int score;

        IndividualScore(Individual entity, int score) {
            this.entity = entity;
            this.score = score;
        }

        int getScore() {
            return score;
        }

        Individual getEntity() {
            return entity;
        }
    }
}
