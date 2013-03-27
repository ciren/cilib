/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.hpso.detectionstrategies.BehaviorChangeTriggerDetectionStrategy;
import net.sourceforge.cilib.pso.hpso.detectionstrategies.PersonalBestStagnationDetectionStrategy;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;

/**
 * Iteration strategy for adaptive dynamic heterogeneous particle swarms using
 * Spanvello and Montes de Oca's behavior selection strategy.
 *
 * <p>
 * References:
 * </p>
 * <ul><li>Spanevello, Paolo, and Montes De Oca, Marco A. "Experiments on Adaptive Heterogeneous PSO Algorithms."
 * Proceedings of SLSDS 2009 Doctoral Symposium on Engineering Stochastic Local Search Algorithms.
 * IRIDIA, Université Libre de Bruxelles, 2009. 36-40.</li></ul>
 */
public class DifferenceProportionalProbabilityIterationStrategy implements IterationStrategy<PSO>, HeterogeneousIterationStrategy {
    private IterationStrategy<PSO> iterationStrategy;
    private BehaviorChangeTriggerDetectionStrategy detectionStrategy;
    private List<ParticleBehavior> behaviorPool;
    private Map<ParticleBehavior, List<Particle>> rigidParticles;
    private ControlParameter rigidCountPerBehavior;
    private ControlParameter beta;
    private ProbabilityDistributionFunction random;

    /**
     * Create a new instance of {@linkplain DifferenceProportionalProbabilityIterationStrategy}.
     */
    public DifferenceProportionalProbabilityIterationStrategy() {
        this.iterationStrategy = new SynchronousIterationStrategy();
        this.detectionStrategy = new PersonalBestStagnationDetectionStrategy();
        this.behaviorPool = new ArrayList<ParticleBehavior>();
        this.rigidParticles = new HashMap<ParticleBehavior, List<Particle>>();
        this.beta = ConstantControlParameter.of(5.0);
        this.random = new UniformDistribution();
        this.rigidCountPerBehavior = ConstantControlParameter.of(1);
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
        this.rigidCountPerBehavior = copy.rigidCountPerBehavior.getClone();
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
                double diff = fitnessDifference(other.getBestFitness(), p.getBestFitness());

                if(random.getRandomNumber() < 1.0 / (1 + Math.exp(-beta.getParameter() * (diff / Math.abs(other.getBestFitness().getValue()))))) {
                    behavior = other.getParticleBehavior();
                    behavior.incrementSelectedCounter();
                    p.setParticleBehavior(behavior);
                }
            }
        }

        iterationStrategy.performIteration(algorithm);
    }

    private double fitnessDifference(Fitness newF, Fitness oldF) {
        return newF.compareTo(oldF) *
                Math.abs((newF.getValue().isNaN() ? 0 : newF.getValue()) - (oldF.getValue().isNaN() ? 0 : oldF.getValue()));
    }

    /**
     * Sets the rigid particles (particles that do not change their behavior)
     * @param algorithm
     */
    private void setRigidParticles(PSO algorithm) {
        if(algorithm.getIterations() == 0) {
            checkState(algorithm.getTopology().size() >= behaviorPool.size() * rigidCountPerBehavior.getParameter(), "There are not enough particles for your chosen rigid particle count and behavior count.");

            //assuming the behaviors in the intialisationstrategy are the same as the behaviors in behaviorpool
            setBehaviorPool(((HeterogeneousPopulationInitialisationStrategy) algorithm.getInitialisationStrategy()).getBehaviorPool());
            List<Particle> top = algorithm.getTopology();

            for(int j = 0; j < behaviorPool.size(); j++) {
                List<Particle> rigidParticleList = new ArrayList<Particle>();

                for(int i = 0; i < rigidCountPerBehavior.getParameter(); i++) {
                    top.get(i + (int) rigidCountPerBehavior.getParameter()*j).setParticleBehavior(behaviorPool.get(j));
                    rigidParticleList.add(top.get(i + (int) rigidCountPerBehavior.getParameter() * j));
                }

                rigidParticles.put(behaviorPool.get(j), rigidParticleList);
            }
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
     * Get the currently defined
     * {@linkplain BehaviorChangeTriggerDetectionStrategy stagnation detection strategy}.
     *
     * @return  The current
     *          {@linkplain BehaviorChangeTriggerDetectionStrategy stagnation detection strategy}.
     */
    public BehaviorChangeTriggerDetectionStrategy getDetectionStrategy() {
        return detectionStrategy;
    }

    /**
     * Set the {@linkplain BehaviorChangeTriggerDetectionStrategy stagnation detection strategy}
     * to be used.
     *
     * @param strategy  The {@linkplain BehaviorChangeTriggerDetectionStrategy stagnation detection strategy}
     *                  to set.
     */
    public void setDetectionStrategy(BehaviorChangeTriggerDetectionStrategy strategy) {
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

    public void setBeta(ControlParameter beta) {
        this.beta = beta;
    }

    public ControlParameter getBeta() {
        return beta;
    }

    public void setRigidCountPerBehavior(ControlParameter rigidCountPerBehavior) {
        this.rigidCountPerBehavior = rigidCountPerBehavior;
    }

    public ControlParameter getRigidCountPerBehavior() {
        return rigidCountPerBehavior;
    }
}
