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
package net.sourceforge.cilib.pso.hpso;

import static com.google.common.base.Preconditions.checkState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.hpso.detectionstrategies.PersonalBestStagnationDetectionStrategy;
import net.sourceforge.cilib.pso.hpso.detectionstrategies.BehaviorChangeTriggerDetectionStrategy;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.util.selection.recipes.Selector;
import net.sourceforge.cilib.util.selection.recipes.TournamentSelector;

public class DOLIterationStrategy implements IterationStrategy<PSO>, HeterogeneousIterationStrategy {
    private IterationStrategy<PSO> iterationStrategy;
    private BehaviorChangeTriggerDetectionStrategy<Particle> detectionStrategy;
    private Selector<ParticleBehavior> behaviorSelectionRecipe;
    private List<ParticleBehavior> behaviorPool;
    private Map<ParticleBehavior, List<Integer>> successCounters;
    private int windowSize;

    public DOLIterationStrategy() {
        this.iterationStrategy = new SynchronousIterationStrategy();
        this.detectionStrategy = new PersonalBestStagnationDetectionStrategy();
        this.behaviorSelectionRecipe = new TournamentSelector<ParticleBehavior>();
        this.behaviorPool = new ArrayList<ParticleBehavior>();
        this.windowSize = 10;
        this.successCounters = new HashMap<ParticleBehavior, List<Integer>>();

        ((TournamentSelector<ParticleBehavior>) this.behaviorSelectionRecipe).setTournamentSize(new ConstantControlParameter(0.4));
    }

    public DOLIterationStrategy(DOLIterationStrategy copy) {
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.detectionStrategy = copy.detectionStrategy.getClone();
        this.behaviorSelectionRecipe = copy.behaviorSelectionRecipe;
        this.behaviorPool = new ArrayList<ParticleBehavior>(copy.behaviorPool);
        this.successCounters = new HashMap<ParticleBehavior, List<Integer>>(copy.successCounters);
        this.windowSize = copy.windowSize;
    }

    @Override
    public DOLIterationStrategy getClone() {
        return new DOLIterationStrategy(this);
    }

    @Override
    public void performIteration(PSO algorithm) {
        checkState(behaviorPool.size() > 0, "You must add particle behaviors to the behavior pool first.");
        checkState(windowSize > 0, "N must be bigger than 0.");

        for(ParticleBehavior pb : behaviorPool) {
            int sum = 0;
            
            for(int i = 0; i < windowSize; i++)
                sum += successCounters.get(pb).get(i);

            pb.setSuccessCounter(sum);
        }
        
        ParticleBehavior behavior;
        for(Entity e : algorithm.getTopology()) {
            Particle p = (Particle)e;

            if (detectionStrategy.detect(p)) {
                behavior = behaviorSelectionRecipe.on(behaviorPool).select();
                behavior.incrementSelectedCounter();
                p.setParticleBehavior(behavior);
            }
        }
        
        for(ParticleBehavior pb : behaviorPool) {
            pb.resetSuccessCounter();
        }

        iterationStrategy.performIteration(algorithm);
        
        for(ParticleBehavior pb : behaviorPool) {
            successCounters.get(pb).set(AbstractAlgorithm.get().getIterations()%windowSize, pb.getSuccessCounter());
        }
    }

    /**
     * Get the current {@linkplain IterationStrategy}.
     * @return The current {@linkplain IterationStrategy}.
     */
    public IterationStrategy<PSO> getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Set the {@linkplain IterationStrategy} to be used.
     * @param iterationStrategy The value to set.
     */
    public void setIterationStrategy(IterationStrategy<PSO> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    /**
     * Get the currently defined {@linkplain StagnationDetectionStrategy}.
     * @return The current {@linkplain StagnationDetectionStrategy}.
     */
    public BehaviorChangeTriggerDetectionStrategy<Particle> getDetectionStrategy() {
        return detectionStrategy;
    }

    /**
     * Set the {@linkplain StagnationDetectionStrategy} to be used.
     * @param strategy The {@linkplain StagnationDetectionStrategy} to set.
     */
    public void setDetectionStrategy(BehaviorChangeTriggerDetectionStrategy<Particle> strategy) {
        this.detectionStrategy = strategy;
    }

    /**
     * Get the currently defined {@linkplain Selector},
     * @return The current {@linkplain Selector}.
     */
    public Selector<ParticleBehavior> getSelectionRecipe() {
        return behaviorSelectionRecipe;
    }

    /**
     * Set the current {@linkplain Selector} to use.
     * @param strategy The {@linkplain Selector} to set.
     */
    public void setSelectionRecipe(Selector<ParticleBehavior> recipe) {
        this.behaviorSelectionRecipe = recipe;
    }
    
    private void addToSuccessCounters(ParticleBehavior behavior) {
        ArrayList<Integer> zeroList = new ArrayList<Integer>(windowSize);
        for(int i = 0; i < windowSize; i++)
            zeroList.add(0);
        
        successCounters.put(behavior, zeroList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addBehavior(ParticleBehavior behavior) {
        behaviorPool.add(behavior);
        
        addToSuccessCounters(behavior);
    }
    
    /**
     * Sets the number of iterations for which to keep success counters.
     * @param windowSize The number of iterations
     */
    public void setWindowSize(int n) {
        this.windowSize = n;
        
        for(ParticleBehavior pb : behaviorPool) {
            addToSuccessCounters(pb);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBehaviorPool(List<ParticleBehavior> pool) {
        behaviorPool = pool;
        
        setWindowSize(windowSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParticleBehavior> getBehaviorPool() {
        return behaviorPool;
    }

    @Override
    public BoundaryConstraint getBoundaryConstraint() {
        return this.iterationStrategy.getBoundaryConstraint();
    }

    @Override
    public void setBoundaryConstraint(BoundaryConstraint boundaryConstraint) {
        this.iterationStrategy.setBoundaryConstraint(boundaryConstraint);
    }
}
