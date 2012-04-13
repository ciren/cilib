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
package net.sourceforge.cilib.niching.derating;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import fj.P;
import fj.data.List;
import java.util.Collections;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.niching.NicheAlgorithm;
import net.sourceforge.cilib.niching.NichingSwarms;
import net.sourceforge.cilib.niching.creation.NicheCreationStrategy;
import net.sourceforge.cilib.niching.creation.NicheDetection;
import net.sourceforge.cilib.niching.merging.detection.MergeDetection;
import net.sourceforge.cilib.niching.merging.MergeStrategy;
import net.sourceforge.cilib.niching.Niching.NichingFunction;
import static net.sourceforge.cilib.niching.Niching.*;
import net.sourceforge.cilib.problem.DeratingOptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.util.functions.Algorithms;
import net.sourceforge.cilib.util.functions.Solutions;

/**
 *
 */
public class DeratingNichePSO extends AbstractIterationStrategy<NicheAlgorithm> {

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
    public void performIteration(NicheAlgorithm alg) {
        Preconditions.checkState(alg.getOptimisationProblem() instanceof DeratingOptimisationProblem,
                "DeratingNichePSOIterationStrategy can only be used with DeratingOptimisationProblem.");
        DeratingOptimisationProblem problem = (DeratingOptimisationProblem) alg.getOptimisationProblem();
        
        List<PopulationBasedAlgorithm> subswarms = List.<PopulationBasedAlgorithm>iterableList(alg.getPopulations());
        subswarms = combineSwarms
            .andThen(onMainSwarm(Algorithms.<PopulationBasedAlgorithm>initialise()))
            .andThen(phase1(alg.getNicheDetection(), 
                alg.getNicheCreationStrategy(),
                alg.getMainSwarmPostCreation()))
            .andThen(setSubswarmProblem(alg.getOptimisationProblem()))
            .andThen(phase2(alg.getNicheDetection(), 
                alg.getNicheCreationStrategy(),
                alg.getMainSwarmPostCreation(),
                alg.getAbsorptionDetection(),
                alg.getMainSwarmAbsorptionStrategy(),
                alg.getSubSwarmsAbsorptionStrategy()))
            .andThen(joinAndMerge(alg.getMergeDetection(), 
                alg.getMainSwarmMergeStrategy(),
                alg.getSubSwarmsMergeStrategy(),
                subswarms))
            .f(P.p(alg.getMainSwarm(), Collections.<PopulationBasedAlgorithm>emptyList()))._2();

        problem.clearSolutions();
        problem.addSolutions(subswarms.map(Solutions.getPosition().o(Algorithms.<PopulationBasedAlgorithm>getBestSolution())).toCollection());
        alg.setPopulations(Lists.newLinkedList(subswarms.toCollection()));
        // dont need to set the main swarm because it gets reinitialised
    }
    
    public static NichingFunction setSubswarmProblem(final OptimisationProblem problem) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                ((DeratingOptimisationProblem) problem).clearSolutions();
                return onSubswarms(Algorithms.<PopulationBasedAlgorithm>setOptimisationProblem().f(problem)).f(a);
            }        
        };
    }
    
    public static NichingFunction joinAndMerge(final MergeDetection mergeDetection, final MergeStrategy mainSwarmMergeStrategy, final MergeStrategy subSwarmsMergeStrategy, final List<PopulationBasedAlgorithm> joiningList) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                return merge(mergeDetection, mainSwarmMergeStrategy, subSwarmsMergeStrategy)
                        .f(NichingSwarms.of(a._1(), joiningList.append(a._2())));
            }        
        };
    }
    
    public static NichingFunction phase1(final NicheDetection nicheDetection, final NicheCreationStrategy creationStrategy, final MergeStrategy mainSwarmCreationMergingStrategy) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                if (Algorithms.isFinished().f(a._1()) || a._1().getTopology().isEmpty()) {
                    return a;
                }
                
                return this.f(onMainSwarm(Algorithms.<PopulationBasedAlgorithm>iterateUnlessDone())
                        .andThen(createNiches(nicheDetection, creationStrategy, mainSwarmCreationMergingStrategy))
                        .f(a));
            }        
        };
    }
    
    public static NichingFunction phase2(final NicheDetection nicheDetection, final NicheCreationStrategy creationStrategy, final MergeStrategy mainSwarmCreationMergingStrategy, final MergeDetection absorptionDetection, final MergeStrategy mainSwarmAbsorptionStrategy, final MergeStrategy subSwarmsAbsorptionStrategy) {
        return new NichingFunction() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                if (!a._2().exists(Algorithms.<PopulationBasedAlgorithm>isFinished())) {
                    return a;
                }
                
                return this.f(onSubswarms(Algorithms.<PopulationBasedAlgorithm>iterateUnlessDone())
                        .andThen(absorb(absorptionDetection, mainSwarmAbsorptionStrategy, subSwarmsAbsorptionStrategy))
                        .andThen(createNiches(nicheDetection, creationStrategy, mainSwarmCreationMergingStrategy))
                        .andThen(setSubswarmProblem(a._1().getOptimisationProblem())).f(a));
            }        
        };
    }
}
