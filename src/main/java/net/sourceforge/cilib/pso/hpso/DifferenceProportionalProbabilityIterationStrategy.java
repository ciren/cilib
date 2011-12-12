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
import net.sourceforge.cilib.algorithm.initialisation.HeterogeneousPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.hpso.detectionstrategies.PersonalBestStagnationDetectionStrategy;
import net.sourceforge.cilib.pso.hpso.detectionstrategies.BehaviorChangeTriggerDetectionStrategy;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;

/**
 * Iteration strategy for adaptive dynamic heterogeneous particle swarms using 
 * Spanvello and Montes de Oca's behavior selection strategy.
 * 
 * <p>
 * References:
 * </p>
 * <ul><li>Spanevello, Paolo, and Montes De Oca, Marco A. “Experiments on Adaptive Heterogeneous PSO Algorithms.”
 * Proceedings of SLSDS 2009 Doctoral Symposium on Engineering Stochastic Local Search Algorithms.
 * IRIDIA, Université Libre de Bruxelles, 2009. 36-40.</li></ul>
 *
 * @author filipe
 */
public class DifferenceProportionalProbabilityIterationStrategy implements IterationStrategy<PSO>, HeterogeneousIterationStrategy {
    private IterationStrategy<PSO> iterationStrategy;
    private BehaviorChangeTriggerDetectionStrategy<Particle> detectionStrategy;
    private List<ParticleBehavior> behaviorPool;
    private Map<ParticleBehavior, List<Particle>> rigidParticles;
    private int rigidCountPerBehavior;
    private ControlParameter beta;
    private RandomProvider random;
    private boolean initialized;

    /**
     * Create a new instance of {@linkplain DifferenceProportionalProbabilityIterationStrategy}.
     */
    public DifferenceProportionalProbabilityIterationStrategy() {
        this.iterationStrategy = new SynchronousIterationStrategy();
        this.detectionStrategy = new PersonalBestStagnationDetectionStrategy();
        this.behaviorPool = new ArrayList<ParticleBehavior>();
        this.rigidParticles = new HashMap<ParticleBehavior, List<Particle>>();
        this.beta = new ConstantControlParameter(5.0);
        this.random = new MersenneTwister();
        this.rigidCountPerBehavior = 1;
        this.initialized = false;
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public DifferenceProportionalProbabilityIterationStrategy(DifferenceProportionalProbabilityIterationStrategy copy) {
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.detectionStrategy = copy.detectionStrategy.getClone();
        this.behaviorPool = new ArrayList<ParticleBehavior>(copy.behaviorPool);
        this.rigidParticles = new HashMap<ParticleBehavior, List<Particle>>(copy.rigidParticles);
        this.beta = copy.beta.getClone();
        this.random = copy.random;
        this.initialized = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DifferenceProportionalProbabilityIterationStrategy getClone() {
        return new DifferenceProportionalProbabilityIterationStrategy(this);
    }

    /**
     * 
     * @see net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy#performIteration()
     */
    @Override
    public void performIteration(PSO algorithm) {
        checkState(behaviorPool.size() > 0, "You must add particle behaviors to the behavior pool first.");

        setRigidParticles(algorithm);

        ParticleBehavior behavior;
        for(Entity e : algorithm.getTopology()) {
            Particle p = (Particle)e;

            if (detectionStrategy.detect(p) && !rigidParticles.get(p.getParticleBehavior()).contains(p)) {
                Particle other = p.getNeighbourhoodBest();
                double diff = p.getBestFitness().getValue() - other.getBestFitness().getValue();

                if(random.nextDouble() < 1 / (1 + Math.exp(-beta.getParameter() * (diff / Math.abs(other.getBestFitness().getValue()))))) {
                    behavior = other.getParticleBehavior();
                    behavior.incrementSelectedCounter();
                    p.setParticleBehavior(behavior);
                }
            }
        }

        iterationStrategy.performIteration(algorithm);
    }

    /**
     * Sets the rigid particles (particles that do not change their behavior)
     * @param algorithm 
     */
    private void setRigidParticles(PSO algorithm) {
        if(!initialized) {
            checkState(algorithm.getTopology().size() >= behaviorPool.size() * rigidCountPerBehavior, "There are not enough particles for your chosen rigid particle count and behavior count.");

            //assuming the behaviors in the intializationstrategy are the same as the behaviors in behaviorpool
            setBehaviorPool(((HeterogeneousPopulationInitialisationStrategy) algorithm.getInitialisationStrategy()).getBehaviorPool());
            List<Particle> top = algorithm.getTopology().asList();
            
            for(int j = 0; j < behaviorPool.size(); j++) {
                List<Particle> rigidParticleList = new ArrayList<Particle>();
                
                for(int i = 0; i < rigidCountPerBehavior; i++) {
                    top.get(i + rigidCountPerBehavior*j).setParticleBehavior(behaviorPool.get(j));
                    rigidParticleList.add(top.get(i + rigidCountPerBehavior*j));
                }

                rigidParticles.put(behaviorPool.get(j), rigidParticleList);
            }

            initialized = true;
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
     * {@inheritDoc}
     */
    @Override
    public void addBehavior(ParticleBehavior behavior) {
        behaviorPool.add(behavior);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBehaviorPool(List<ParticleBehavior> pool) {
        behaviorPool = pool;
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

    public void setRigidCountPerBehavior(int rigidCountPerBehavior) {
        this.rigidCountPerBehavior = rigidCountPerBehavior;
    }

    public int getRigidCountPerBehavior() {
        return rigidCountPerBehavior;
    }
}