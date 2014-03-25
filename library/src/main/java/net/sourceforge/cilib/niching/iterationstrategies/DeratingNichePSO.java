/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.iterationstrategies;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import fj.F;
import fj.data.List;
import java.util.Collections;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.niching.NichingAlgorithm;
import static net.sourceforge.cilib.niching.NichingFunctions.*;
import net.sourceforge.cilib.niching.NichingFunctions.NichingFunction;
import net.sourceforge.cilib.niching.NichingSwarms;
import static net.sourceforge.cilib.niching.NichingSwarms.*;
import net.sourceforge.cilib.problem.DeratingOptimisationProblem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.functions.Algorithms;
import net.sourceforge.cilib.util.functions.Entities;
import net.sourceforge.cilib.util.functions.Solutions;

public class DeratingNichePSO extends AbstractIterationStrategy<NichingAlgorithm> {

    protected java.util.List<OptimisationSolution> solutions;
    protected boolean mergingVersion = true;

    public DeratingNichePSO() {
        this.solutions = Lists.<OptimisationSolution>newLinkedList();
    }

    public DeratingNichePSO(DeratingNichePSO copy) {
        this.solutions = Lists.<OptimisationSolution>newLinkedList(copy.solutions);
    }

    @Override
    public DeratingNichePSO getClone() {
        return new DeratingNichePSO(this);
    }

    @Override
    public void performIteration(NichingAlgorithm alg) {
        Preconditions.checkState(alg.getOptimisationProblem() instanceof DeratingOptimisationProblem,
                "DeratingNichePSO can only be used with DeratingOptimisationProblem.");
        DeratingOptimisationProblem problem = (DeratingOptimisationProblem) alg.getOptimisationProblem();

        List<SinglePopulationBasedAlgorithm> subswarms = List.iterableList(alg.getPopulations());
	NichingSwarms swarms = phase1(alg)
            .f(NichingSwarms.of(alg.getMainSwarm(), Collections.<SinglePopulationBasedAlgorithm>emptyList()));
        swarms = onSubswarms(clearDeratingSolutions(problem)).f(swarms);
        swarms = phase2(alg).f(swarms);
        swarms = joinAndMerge(alg, subswarms, mergingVersion).f(swarms);

        problem.clearSolutions();
	problem.addSolutions(swarms._2().map(Solutions.getPosition().o(Algorithms.<SinglePopulationBasedAlgorithm>getBestSolution())).toCollection());
        alg.setPopulations(Lists.newLinkedList(swarms._2().toCollection()));
        alg.getMainSwarm().setOptimisationProblem(problem);
        // don't need to set the main swarm because it gets reinitialised
	alg.getMainSwarm().performInitialisation();
    }

    /**
     * Clear solutions so subswarms can optimize in original search space
     */
    public static F<SinglePopulationBasedAlgorithm, SinglePopulationBasedAlgorithm>
            clearDeratingSolutions(final DeratingOptimisationProblem problem) {
        return new F<SinglePopulationBasedAlgorithm, SinglePopulationBasedAlgorithm>() {
            @Override
            public SinglePopulationBasedAlgorithm f(SinglePopulationBasedAlgorithm a) {
                problem.clearSolutions();
                a.setOptimisationProblem(problem);
		a.setTopology(a.getTopology().map(new F<Entity,Entity>() {
                    @Override
                    public Entity f(Entity a) {
                        Particle dp = (Particle) a.getClone();
                        dp.put(Property.CANDIDATE_SOLUTION, dp.getBestPosition());
                        a.put(Property.BEST_FITNESS, a.getBehaviour().getFitnessCalculator().getFitness(dp));
                        return a;
                    }
                }.andThen(Entities.evaluate())));
                return a;
            }
        };
    }

    /**
     * Add new swarms to subswarms list and merge swarms if possible
     */
    public static NichingFunction joinAndMerge(final NichingAlgorithm alg, final List<SinglePopulationBasedAlgorithm> joiningList, final boolean mergingVersion) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
		if (mergingVersion) {
                    return merge(alg.getMergeDetector(), alg.getMainSwarmMerger(), alg.getSubSwarmMerger())
                        .f(NichingSwarms.of(a.getMainSwarm(), a.getSubswarms().append(joiningList)));
                }
                
                return NichingSwarms.of(a.getMainSwarm(), a.getSubswarms().append(joiningList));

            }
        };
    }

    /**
     * Recursive function which iterates the main swarm and creates niches until
     * the main swarm's stopping conditions are met
     */
    public static NichingFunction phase1(final NichingAlgorithm alg) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                if (a.getMainSwarm().isFinished() || a.getMainSwarm().getTopology().isEmpty()) {
                    return a;
                }

                return this.f(onMainSwarm(alg.getMainSwarmIterator())
                        .andThen(createNiches(alg.getNicheDetector(), alg.getNicheCreator(), alg.getMainSwarmCreationMerger()))
                        .f(a));
            }
        };
    }

    /**
     * Recursive function iterates each new subswarm individually, absorbs
     * particles from the main swarm and creates niches until all the subswarm's
     * stopping conditions are met.
     */
    public static NichingFunction phase2(final NichingAlgorithm alg) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
		if (a.getSubswarms().forall(Algorithms.<SinglePopulationBasedAlgorithm>isFinished())) {
                    return a;
                }

                return this.f(alg.getSubSwarmIterator()
                        .andThen(absorb(alg.getAbsorptionDetector(), alg.getMainSwarmAbsorber(), alg.getSubSwarmAbsorber()))
                        .andThen(createNiches(alg.getNicheDetector(), alg.getNicheCreator(), alg.getMainSwarmCreationMerger()))
                        .andThen(onSubswarms(clearDeratingSolutions((DeratingOptimisationProblem) a.getMainSwarm().getOptimisationProblem()))).f(a));
            }
        };
    }

    public void setMergingVersion(boolean mergingVersion) {
        this.mergingVersion = mergingVersion;
    }
}
