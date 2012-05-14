/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.niching.iterationstrategies;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import fj.F;
import fj.data.List;
import java.util.Collections;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.niching.NichingAlgorithm;
import static net.sourceforge.cilib.niching.NichingFunctions.*;
import net.sourceforge.cilib.niching.NichingSwarms;
import static net.sourceforge.cilib.niching.NichingSwarms.*;
import net.sourceforge.cilib.problem.DeratingOptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.util.functions.Algorithms;
import net.sourceforge.cilib.util.functions.Solutions;

public class DeratingNichePSO extends AbstractIterationStrategy<NichingAlgorithm> {

    protected java.util.List<OptimisationSolution> solutions;
    
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
        
        List<PopulationBasedAlgorithm> subswarms = List.<PopulationBasedAlgorithm>iterableList(alg.getPopulations());
        subswarms = onMainSwarm(Algorithms.<PopulationBasedAlgorithm>initialise())
            .andThen(phase1(alg))
            .andThen(onSubswarms(clearDeratingSolutions(problem)))
            .andThen(phase2(alg))
            .andThen(joinAndMerge(alg, subswarms))
            .f(NichingSwarms.of(alg.getMainSwarm(), Collections.<PopulationBasedAlgorithm>emptyList()))._2();

        problem.clearSolutions();
        problem.addSolutions(subswarms.map(Solutions.getPosition().o(Algorithms.<PopulationBasedAlgorithm>getBestSolution())).toCollection());
        alg.setPopulations(Lists.newLinkedList(subswarms.toCollection()));
        alg.getMainSwarm().setOptimisationProblem(problem);
        // dont need to set the main swarm because it gets reinitialised
    }
    
    /**
     * Clear solutions so subswarms can optimize in original search space
     */
    public static F<PopulationBasedAlgorithm, PopulationBasedAlgorithm> 
            clearDeratingSolutions(final DeratingOptimisationProblem problem) {
        return new F<PopulationBasedAlgorithm, PopulationBasedAlgorithm>() {
            @Override
            public PopulationBasedAlgorithm f(PopulationBasedAlgorithm a) {
                problem.clearSolutions();
                a.setOptimisationProblem(problem);
                return a;
            }        
        };
    }

    /**
     * Add new swarms to subswarms list and merge swarms if possible
     */
    public static NichingFunction joinAndMerge(final NichingAlgorithm alg, final List<PopulationBasedAlgorithm> joiningList) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                return merge(alg.getMergeDetector(), alg.getMainSwarmMerger(), alg.getSubSwarmMerger())
                        .f(NichingSwarms.of(a.getMainSwarm(), joiningList.append(a.getSubswarms())));
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
                if (!a.getSubswarms().exists(Algorithms.<PopulationBasedAlgorithm>isFinished())) {
                    return a;
                }

                return this.f(alg.getSubSwarmIterator()
                        .andThen(absorb(alg.getAbsorptionDetector(), alg.getMainSwarmAbsorber(), alg.getSubSwarmAbsorber()))
                        .andThen(createNiches(alg.getNicheDetector(), alg.getNicheCreator(), alg.getMainSwarmCreationMerger()))
                        .andThen(onSubswarms(clearDeratingSolutions((DeratingOptimisationProblem) a.getMainSwarm().getOptimisationProblem()))).f(a));
            }
        };
    }
}
