/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec.iterationstrategies;

import java.util.List;

import com.google.common.collect.Lists;

import fj.F;
import fj.Ord;
import fj.Ordering;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.operators.mutation.GaussianMutationStrategy;
import net.sourceforge.cilib.entity.operators.mutation.MutationStrategy;
import net.sourceforge.cilib.util.functions.Entities;
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
    public void performIteration(final EC algorithm) {
        fj.data.List<Individual> topology = algorithm.getTopology();
        fj.data.List<Individual> offspring = topology.map(Entities.<Individual>clone_());

        // Apply the mutation
        this.mutationStrategy.mutate(Lists.newArrayList(offspring));

        for (Individual individual : offspring) {
            individual.calculateFitness();
        }

        final fj.data.List<Individual> intermediate = topology.append(offspring);
        final fj.data.List<IndividualScore> scores = topology.map(new F<Individual, IndividualScore>() {
                @Override
                public IndividualScore f(Individual current) {
                    int score = getScore(current, intermediate);
                    return new IndividualScore(current, score);
                }
            });

        final fj.data.List<IndividualScore> sortedScores = scores.sort(Ord.<IndividualScore>ord(new F<IndividualScore, F<IndividualScore, Ordering>>() {
                    @Override
                    public F<IndividualScore, Ordering> f(final IndividualScore o1) {
                        return new F<IndividualScore, Ordering>() {
                            @Override
                            public Ordering f(final IndividualScore o2) {
                                int o1Score = o1.getScore();
                                int o2Score = o2.getScore();
                                return o1Score < o2Score ? Ordering.LT : o1Score == o2Score ? Ordering.EQ : Ordering.GT;
                            }
                        };
                    }
                }));

        algorithm.setTopology(sortedScores.map(new F<IndividualScore, Individual>() {
                    @Override
                        public Individual f(IndividualScore a) {
                        return a.getEntity();
                    }
                }).take(algorithm.getInitialisationStrategy().getEntityNumber()));

    }

    private int getScore(Individual current, fj.data.List<Individual> topology) {
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
