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
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.initialisation.HeterogeneousPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.boundaryconstraint.BoundaryConstraint;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.hpso.detectionstrategies.BehaviorChangeTriggerDetectionStrategy;
import net.sourceforge.cilib.pso.hpso.detectionstrategies.PersonalBestStagnationDetectionStrategy;
import net.sourceforge.cilib.pso.iterationstrategies.SynchronousIterationStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.util.selection.recipes.Selector;
import net.sourceforge.cilib.util.selection.recipes.TournamentSelector;

/**
 * Iteration strategy for heterogeneous particle swarms (HPSO).<br>
 *
 * An HPSO is defined as a PSO in which each particle may follow a different
 * behavior from the other particles. This class is an implementation of a number
 * of different strategies. The default for this class is to use a dynamic
 * strategy. The following strategies are implemented:<br><br>
 *
 * <i>Dynamic</i> HPSO (dHPSO): Particles can change their behaviors through the course
 * of the algorithm when certain conditions are met, such as when a particle stagnates.
 * Parameters to use:
 * <ul>
 *  <li>windowSize = maximum number of iterations</li>
 *  <li>behaviorSelectionRecipe = RandomSelector</li>
 * </ul><br>
 *
 * <i>Frequency based</i> HPSO (fk-HPSO-1000): Similar to dHPSO but particles probabilistically
 * choose new behaviors from the behavior pool, rather than randomly. In this way,
 * behaviors that perform well in early iterations are favoured over the other behaviors
 * in subsequent iterations.
 * Parameters to use:
 * <ul>
 *  <li>windowSize = maximum number of iterations</li>
 *  <li>behaviorSelectionRecipe = TournamentSelector/RouletteWheelSelector
 * with a ParticleBehaviorWeighting</li>
 * </ul><br>
 *
 * <i>Iteration best</i> HPSO (fk-HPSO-1): Similar to fk-HPSO but only the previous iterations'
 * successes contribute to behavior selection.
 * Parameters to use:
 * <ul>
 *  <li>windowSize = maximum number of iterations</li>
 *  <li>behaviorSelectionRecipe = TournamentSelector/RouletteWheelSelector
 * with a ParticleBehaviorWeighting</li>
 * </ul><br>
 *
 * <i>Moving window</i> HPSO (fk-HPSO-10): This is a generalisation of aHPSO and aHPSO-IB.
 * It uses the previous windowSize iterations' successes to choose a behavior.
 * Parameters to use:
 * <ul>
 *  <li>0 &lt; windowSize &lt;= max iterations</li>
 *  <li>behaviorSelectionRecipe = TournamentSelector/RouletteWheelSelector
 * with a ParticleBehaviorWeighting</li>
 * </ul><br>
 *
 * The default parameters are
 * <ul>
 *  <li>windowSize = 10</li>
 *  <li>behaviorSelectionRecipe = TournamentSelector with k = 20%</li>
 *  <li>BehaviorChangeTrigger = PersonalBestStagnation with windowSize = 10 iterations</li>
 *  <li>IterationStrategy = SynchronousIterationStrategy</li>
 * </ul>
 *
 * For <i>static</i> HPSO (sHPSO) only use {@link HeterogeneousPopulationInitialisationStrategy}
 * with a regular iteration strategy.<br>
 *
 * TODO: add reference
 */
public class HPSOIterationStrategy implements IterationStrategy<PSO>, HeterogeneousIterationStrategy {
    private IterationStrategy<PSO> iterationStrategy;
    private BehaviorChangeTriggerDetectionStrategy detectionStrategy;
    private Selector<ParticleBehavior> behaviorSelectionRecipe;
    private List<ParticleBehavior> behaviorPool;
    private Map<ParticleBehavior, List<Integer>> successCounters;
    private ControlParameter windowSize;

    /**
     * Default constructor.
     */
    public HPSOIterationStrategy() {
        this.iterationStrategy = new SynchronousIterationStrategy();
        this.detectionStrategy = new PersonalBestStagnationDetectionStrategy();
        this.behaviorSelectionRecipe = new TournamentSelector<ParticleBehavior>();
        this.behaviorPool = new ArrayList<ParticleBehavior>();
        this.windowSize = ConstantControlParameter.of(10);
        this.successCounters = new HashMap<ParticleBehavior, List<Integer>>();

        ((TournamentSelector<ParticleBehavior>) this.behaviorSelectionRecipe).setTournamentSize(ConstantControlParameter.of(0.4));
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public HPSOIterationStrategy(HPSOIterationStrategy copy) {
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.detectionStrategy = copy.detectionStrategy.getClone();
        this.behaviorSelectionRecipe = copy.behaviorSelectionRecipe;
        this.behaviorPool = new ArrayList<ParticleBehavior>(copy.behaviorPool);
        this.successCounters = new HashMap<ParticleBehavior, List<Integer>>(copy.successCounters);
        this.windowSize = copy.windowSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HPSOIterationStrategy getClone() {
        return new HPSOIterationStrategy(this);
    }

    /**
     * Structure of Dynamic Heterogeneous iteration strategy:
     *
     * <ol>
     *   <li>For each particle:</li>
     *   <li>Check if particle must change its behavior</li>
     *   <li>If particle must change its behavior:</li>
     *   <ol>
     *     <li>Assign a new behavior to the particle from the behavior pool</li>
     *   </ol>
     *   <li>Perform normal iteration</li>
     *   <li>Update success counters</li>
     * </ol>
     */
    @Override
    public void performIteration(PSO algorithm) {
        checkState(behaviorPool.size() > 0, "You must add particle behaviors to the behavior pool first.");
        checkState((int) windowSize.getParameter() > 0, "N must be bigger than 0.");

        //reset for algorithm
        if (AbstractAlgorithm.get().getIterations() == 0) {
            for(ParticleBehavior pb : behaviorPool) {
                addToSuccessCounters(pb);
            }
        }

        for(ParticleBehavior pb : behaviorPool) {
            int sum = 0;

            for(int i = 0; i < (int) windowSize.getParameter(); i++) {
                sum += successCounters.get(pb).get(i);
            }

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
            successCounters.get(pb).set(AbstractAlgorithm.get().getIterations()%(int)windowSize.getParameter(), pb.getSuccessCounter());
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
     * Get the currently defined {@linkplain Selector},
     * @return The current {@linkplain Selector}.
     */
    public Selector<ParticleBehavior> getSelectionRecipe() {
        return behaviorSelectionRecipe;
    }

    /**
     * Set the current {@linkplain Selector} to use.
     * @param recipe The {@linkplain Selector} to set.
     */
    public void setSelectionRecipe(Selector<ParticleBehavior> recipe) {
        this.behaviorSelectionRecipe = recipe;
    }

    private void addToSuccessCounters(ParticleBehavior behavior) {
        ArrayList<Integer> zeroList = new ArrayList<Integer>((int)windowSize.getParameter());
        for(int i = 0; i < (int) windowSize.getParameter(); i++) {
            zeroList.add(0);
        }

        successCounters.put(behavior, zeroList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addBehavior(ParticleBehavior behavior) {
        behaviorPool.add(behavior);
    }

    /**
     * Sets the number of iterations for which to keep success counters.
     * @param n The number of iterations
     */
    public void setWindowSize(ControlParameter n) {
        this.windowSize = n;
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
}
