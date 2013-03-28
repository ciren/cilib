/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.hpso;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.HeterogeneousPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.hpso.detectionstrategies.BehaviorChangeTriggerDetectionStrategy;
import net.sourceforge.cilib.pso.hpso.detectionstrategies.PersonalBestStagnationDetectionStrategy;
import net.sourceforge.cilib.pso.hpso.pheromoneupdate.ConstantPheromoneUpdateStrategy;
import net.sourceforge.cilib.pso.hpso.pheromoneupdate.PheromoneUpdateStrategy;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.util.selection.recipes.RouletteWheelSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;
import net.sourceforge.cilib.util.selection.weighting.ParticleBehaviorWeighting;
import net.sourceforge.cilib.util.selection.weighting.SpecialisedRatio;

/**
 * Iteration strategy for adaptive dynamic heterogeneous particle swarms (HPSO).
 *
 * Each behavior has an associated pheromoneConcentration level which gets updated using
 * {@linkplain PheromoneUpdateStrategy}. A behavior is then chosen in proportion
 * to the pheromoneConcentration level for that behavior.
 */
public class PheromoneIterationStrategy implements IterationStrategy<PSO>, HeterogeneousIterationStrategy {
    private List<Double> pheromoneConcentration;
    private PheromoneUpdateStrategy pheromoneUpdateStrategy;

    private List<ParticleBehavior> behaviorPool;
    private Selector<ParticleBehavior> behaviorSelectionRecipe;
    private IterationStrategy<PSO> iterationStrategy;
    private BehaviorChangeTriggerDetectionStrategy detectionStrategy;
    private ControlParameter minPeromone;

    /**
     * Create a new instance of {@linkplain PheromoneIterationStrategy}.
     */
    public PheromoneIterationStrategy() {
        this.minPeromone = ConstantControlParameter.of(0.01);
        this.behaviorPool = new ArrayList<ParticleBehavior>();
        this.pheromoneConcentration = new ArrayList<Double>();
        this.pheromoneUpdateStrategy = new ConstantPheromoneUpdateStrategy();

        this.iterationStrategy = new SynchronousIterationStrategy();
        this.detectionStrategy = new PersonalBestStagnationDetectionStrategy();

        SpecialisedRatio weighting = new SpecialisedRatio();
        weighting.setBehaviors(behaviorPool);
        weighting.setWeights(pheromoneConcentration);

        this.behaviorSelectionRecipe = new RouletteWheelSelector<ParticleBehavior>(new ParticleBehaviorWeighting(weighting));
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public PheromoneIterationStrategy(PheromoneIterationStrategy copy) {
        this.minPeromone = copy.minPeromone.getClone();
        this.pheromoneConcentration = new ArrayList<Double>(copy.pheromoneConcentration);
        this.pheromoneUpdateStrategy = copy.pheromoneUpdateStrategy;

        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.detectionStrategy = copy.detectionStrategy.getClone();
        this.behaviorSelectionRecipe = copy.behaviorSelectionRecipe;
        this.behaviorPool = new ArrayList<ParticleBehavior>(copy.behaviorPool);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PheromoneIterationStrategy getClone() {
        return new PheromoneIterationStrategy(this);
    }

    /**
     * Structure of Dynamic Heterogeneous iteration strategy:
     *
     * <ol>
     *   <li>For each particle:</li>
     *   <li>Check if particle must change its behavior</li>
     *   <li>If particle must change its behavior:</li>
     *   <ol>
     *     <li>Select a new behavior to the particle from the behavior pool</li>
     *   </ol>
     *   <li>Perform normal iteration</li>
     *   <li>Update and evaporate pheromoneConcentration levels</li>
     * </ol>
     */
    @Override
    public void performIteration(PSO algorithm) {
        ParticleBehavior behavior;
        for(Particle p : algorithm.getTopology()) {
            if (detectionStrategy.detect(p)) {
                behavior = behaviorSelectionRecipe.on(behaviorPool).select();
                behavior.incrementSelectedCounter();
                p.setParticleBehavior(behavior);
            }
        }

        iterationStrategy.performIteration(algorithm);

        //update the pheromoneConcentration levels
        for(Entity e : algorithm.getTopology()) {
            Particle p = (Particle)e;
            int index = behaviorPool.indexOf(p.getParticleBehavior());

            //in case behavior is in init strategy still, assumes same ordering of behaviors
            if(index == -1) {
                index = ((HeterogeneousPopulationInitialisationStrategy)
                            algorithm.getInitialisationStrategy())
                            .getBehaviorPool().indexOf(p.getParticleBehavior());
            }

            double deltaP = pheromoneUpdateStrategy.updatePheromone(p);
            pheromoneConcentration.set(index, Math.max(pheromoneConcentration.get(index) + deltaP, minPeromone.getParameter()));
        }

        //evaporate the pheromoneConcentration levels
        double sumPheromone = 0;
        for(Double d : pheromoneConcentration) {
            sumPheromone += d;
        }

        for(ParticleBehavior pb : behaviorPool) {
            int index = behaviorPool.indexOf(pb);
            pheromoneConcentration.set(index, (sumPheromone - pheromoneConcentration.get(index)) * pheromoneConcentration.get(index) / sumPheromone);
        }
    }

    /**
     * Sets the PheromoneUpdateStrategy
     *
     * @param strat The strategy to change to.
     */
    public void setPheromoneUpdateStrategy(PheromoneUpdateStrategy strat) {
        this.pheromoneUpdateStrategy = strat;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addBehavior(ParticleBehavior behavior) {
        behaviorPool.add(behavior);
        setBehaviorPool(behaviorPool);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBehaviorPool(List<ParticleBehavior> pool) {
        pheromoneConcentration.clear();
        behaviorPool = pool;

        for(ParticleBehavior pb : behaviorPool) {
            pheromoneConcentration.add(new Double(1.0 / behaviorPool.size()));
        }
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

    public void setPheromoneConcentration(List<Double> pheromoneConcentration) {
        this.pheromoneConcentration = pheromoneConcentration;
    }

    public void setIterationStrategy(IterationStrategy<PSO> iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    public void setDetectionStrategy(BehaviorChangeTriggerDetectionStrategy detectionStrategy) {
        this.detectionStrategy = detectionStrategy;
    }

    public void setBehaviorSelectionRecipe(Selector<ParticleBehavior> behaviorSelectionRecipe) {
        this.behaviorSelectionRecipe = behaviorSelectionRecipe;
    }

    public PheromoneUpdateStrategy getPheromoneUpdateStrategy() {
        return pheromoneUpdateStrategy;
    }

    public List<Double> getPheromoneConcentration() {
        return pheromoneConcentration;
    }

    public IterationStrategy<PSO> getIterationStrategy() {
        return iterationStrategy;
    }

    public BehaviorChangeTriggerDetectionStrategy getDetectionStrategy() {
        return detectionStrategy;
    }

    public Selector<ParticleBehavior> getBehaviorSelectionRecipe() {
        return behaviorSelectionRecipe;
    }

    public void setMinPheromone(ControlParameter minPeromone) {
        this.minPeromone = minPeromone;
    }

    public ControlParameter getMinPheromone() {
        return minPeromone;
    }
}
