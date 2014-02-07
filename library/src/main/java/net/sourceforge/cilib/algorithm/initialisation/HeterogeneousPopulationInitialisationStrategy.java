/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.initialisation;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.entity.behaviour.Behaviour;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 * Create a collection of {@linkplain Entity entities} by cloning the given
 * prototype {@link Entity}.
 * <p>
 * The prototype Entity must be a {@link Particle}. All particles cloned from
 * the given {@link Particle} are assigned different behaviors from a behavior
 * pool. The manner in which behaviors are selected from the behavior pool is
 * governed by the {@link Selector} (random by default).
 */
public class HeterogeneousPopulationInitialisationStrategy implements PopulationInitialisationStrategy {

    private List<Behaviour> behaviorPool;
    private Selector<Behaviour> selectionRecipe;
    private PopulationInitialisationStrategy delegate;

    /**
     * Create an instance of the {@code ChargedPopulationInitialisationStrategy}.
     */
    public HeterogeneousPopulationInitialisationStrategy() {
        behaviorPool = new ArrayList<Behaviour>();
        selectionRecipe = new RandomSelector<Behaviour>();
        delegate = new ClonedPopulationInitialisationStrategy();
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public HeterogeneousPopulationInitialisationStrategy(HeterogeneousPopulationInitialisationStrategy copy) {
        this.behaviorPool = new ArrayList<Behaviour>(copy.behaviorPool);
        this.selectionRecipe = copy.selectionRecipe;
        this.delegate = copy.delegate.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HeterogeneousPopulationInitialisationStrategy getClone() {
        return new HeterogeneousPopulationInitialisationStrategy(this);
    }

    /**
     * Perform the required initialisation, using the provided <tt>Topology</tt> and
     * <tt>Problem</tt>.
     * @param problem The <tt>Problem</tt> to use in the initialisation of the topology.
     * @return An {@code Iterable<E>} of cloned instances.
     * @throws InitialisationException if the initialisation cannot take place.
     */
    @Override
    public <E extends Entity> Iterable<E> initialise(Problem problem) {
        Preconditions.checkNotNull(problem, "No problem has been specified");
        Preconditions.checkState(behaviorPool.size() > 0, "No particle behaviors have been added to the behavior pool.");

        Iterable<Entity> clones = delegate.initialise(problem);

        for (Entity p : clones) {
            p.setBehaviour(selectionRecipe.on(behaviorPool).select());
        }

        return (Iterable<E>) clones;
    }

    /**
     * Add a {@link ParticleBehavior} to the behavior pool.
     * @param behavior The {@link ParticleBehavior} to add to the behavior pool.
     */
    public void addBehavior(Behaviour behavior) {
        behaviorPool.add(behavior);
    }

    /**
     * Set the {@link ParticleBehavior} pool.
     * @param pool A {@link List} of {@link ParticleBehavior} objects.
     */
    public void setBehaviorPool(List<Behaviour> pool) {
        behaviorPool = pool;
    }

    /**
     * Get the current behavior pool.
     * @return The current {@link List} of {@link ParticleBehavior} objects.
     */
    public List<Behaviour> getBehaviorPool() {
        return behaviorPool;
    }

    /**
     * Set the prototype {@link Entity} for the copy process.
     * @param entityType The {@link Entity} to use for the cloning process. This must be a {@link Particle}.
     */
    @Override
    public void setEntityType(Entity entityType) {
        delegate.setEntityType(entityType);
    }

    /**
     * Get the {@link Entity} that has been defined as the prototype to copy.
     *
     * @return The prototype {@linkplain Entity}.
     */
    @Override
    public Entity getEntityType() {
        return delegate.getEntityType();
    }

    /**
     * Get the defined number of {@code Entity} instances to create.
     * @return The number of {@code Entity} instances.
     */
    @Override
    public int getEntityNumber() {
        return delegate.getEntityNumber();
    }

    /**
     * Set the number of {@code Entity} instances to clone.
     * @param entityNumber The number to clone.
     */
    @Override
    public void setEntityNumber(int entityNumber) {
        delegate.setEntityNumber(entityNumber);
    }

    public void setDelegate(PopulationInitialisationStrategy delegate) {
        this.delegate = delegate;
    }

    public PopulationInitialisationStrategy getDelegate() {
        return delegate;
    }

    public void setSelectionRecipe(Selector<Behaviour> selectionRecipe) {
        this.selectionRecipe = selectionRecipe;
    }

    public Selector<Behaviour> getSelectionRecipe() {
        return selectionRecipe;
    }
}
