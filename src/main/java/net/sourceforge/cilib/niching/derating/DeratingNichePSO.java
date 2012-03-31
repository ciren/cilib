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
package net.sourceforge.cilib.derating;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import fj.F;
import fj.P;
import fj.data.List;
import java.util.Collection;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.problem.DeratingOptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.pso.niching.NichePSO;
import net.sourceforge.cilib.pso.niching.Niching;
import static net.sourceforge.cilib.pso.niching.Niching.*;
import net.sourceforge.cilib.pso.niching.NichingSwarms;
import net.sourceforge.cilib.pso.niching.creation.ClosestNeighbourNicheCreationStrategy;
import net.sourceforge.cilib.pso.niching.creation.NicheCreationStrategy;
import net.sourceforge.cilib.pso.niching.creation.NicheDetection;
import net.sourceforge.cilib.pso.niching.merging.MergeDetection;
import net.sourceforge.cilib.pso.niching.merging.MergeStrategy;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public class DeratingNichePSO extends NichePSO {
    protected java.util.List<OptimisationSolution> solutions;
    protected List<PopulationBasedAlgorithm> pastSubswarms;
    
    public DeratingNichePSO() {
        super();
        this.solutions = Lists.<OptimisationSolution>newLinkedList();
        this.pastSubswarms = List.<PopulationBasedAlgorithm>nil();
        this.mainSwarm.addStoppingCondition(new MaximumIterations(500));
        ((ClosestNeighbourNicheCreationStrategy) this.swarmCreationStrategy).setBehavior(new ParticleBehavior());
    }
    
    public DeratingNichePSO(DeratingNichePSO copy) {
        super(copy);
        this.solutions = Lists.<OptimisationSolution>newLinkedList(copy.solutions);
        this.pastSubswarms = List.<PopulationBasedAlgorithm>iterableList(copy.pastSubswarms);
    }
    
    @Override
    public DeratingNichePSO getClone() {
        return new DeratingNichePSO(this);
    }
    
    @Override
    public void performInitialisation() {
        this.mainSwarm.setOptimisationProblem(optimisationProblem);
        this.mainSwarm.performInitialisation();
    }
    
    @Override
    public void algorithmIteration() {
        pastSubswarms = combineSwarms
            .andThen(initialiseMainSwarm)
            .andThen(phase1(nicheDetection, swarmCreationStrategy, mainSwarmPostCreation))
            .andThen(setSubswarmProblem(optimisationProblem))
            .andThen(phase2(nicheDetection, swarmCreationStrategy, mainSwarmPostCreation,
                absorptionDetection, mainSwarmAbsorptionStrategy, subSwarmsAbsorptionStrategy))
            .andThen(joinAndMerge(mergeDetection, mainSwarmMergeStrategy, subSwarmsMergeStrategy, pastSubswarms))
            .f(P.p(mainSwarm, subPopulationsAlgorithms))._2();
        
        Collection<Vector> nBests = pastSubswarms.map(new F<PopulationBasedAlgorithm, Vector>() {
            @Override
            public Vector f(PopulationBasedAlgorithm a) {
                return (Vector) a.getBestSolution().getPosition();
            }            
        }).toCollection();
        
        ((DeratingOptimisationProblem) optimisationProblem).clearSolutions();
        ((DeratingOptimisationProblem) optimisationProblem).addSolutions(nBests);
    }
    
    public static F<NichingSwarms, NichingSwarms>
            setSubswarmProblem(final OptimisationProblem problem) {
        return new F<NichingSwarms, NichingSwarms>() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                ((DeratingOptimisationProblem) problem).clearSolutions();
                a._2().foreach(Niching.setOptimisationProblem(problem));
                return a;
            }        
        };
    }
    
    public static F<NichingSwarms, NichingSwarms>
            joinAndMerge(final MergeDetection mergeDetection, final MergeStrategy mainSwarmMergeStrategy, final MergeStrategy subSwarmsMergeStrategy, final List<PopulationBasedAlgorithm> joiningList) {
        return new F<NichingSwarms, NichingSwarms>() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                return merge(mergeDetection, mainSwarmMergeStrategy, subSwarmsMergeStrategy).f(NichingSwarms.of(a._1(), joiningList.append(a._2())));
            }        
        };
    }
    
    public static F<NichingSwarms, NichingSwarms> 
            phase1(final NicheDetection nicheDetection, final NicheCreationStrategy creationStrategy, final MergeStrategy mainSwarmCreationMergingStrategy) {
        return new F<NichingSwarms, NichingSwarms>() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                System.out.println(a._1().getIterations() + " " + isFinished.f(a._1()));
                if (isFinished.f(a._1())) {
                    System.out.println ("done");
                    return a;
                }
                
                return this.f(iterateMainSwarm.andThen(createNiches(nicheDetection, creationStrategy, mainSwarmCreationMergingStrategy)).f(a));
            }        
        };
    }
    
    public static F<NichingSwarms, NichingSwarms> 
            phase2(final NicheDetection nicheDetection, final NicheCreationStrategy creationStrategy, final MergeStrategy mainSwarmCreationMergingStrategy, final MergeDetection absorptionDetection, final MergeStrategy mainSwarmAbsorptionStrategy, final MergeStrategy subSwarmsAbsorptionStrategy) {
        return new F<NichingSwarms, NichingSwarms>() {
            @Override
            public NichingSwarms f(NichingSwarms a) {
                if (a._2().forall(isFinished)) {
                    return a;
                }
                
                return this.f(iterateSubswarms
                        .andThen(absorb(absorptionDetection, mainSwarmAbsorptionStrategy, subSwarmsAbsorptionStrategy))
                        .andThen(createNiches(nicheDetection, creationStrategy, mainSwarmCreationMergingStrategy))
                        .andThen(setSubswarmProblem(a._1().getOptimisationProblem())).f(a));
            }        
        };
    }
    
    @Override
    public void setOptimisationProblem(OptimisationProblem problem) {
        Preconditions.checkArgument(problem instanceof DeratingOptimisationProblem, 
                "SequentialNiching can only be used with DeratingOptimisationProblem.");
        optimisationProblem = problem;
        mainSwarm.setOptimisationProblem(problem);
    }
}
