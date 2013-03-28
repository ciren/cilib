/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ff.firefly;

import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.initialisation.InitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.RandomInitialisationStrategy;
import net.sourceforge.cilib.ff.positionupdatestrategies.FireflyPositionUpdateStrategy;
import net.sourceforge.cilib.ff.positionupdatestrategies.StandardFireflyPositionUpdateStrategy;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Abstract entity class for the Firefly algorithm that represents a firefly.
 */
public abstract class AbstractFirefly extends AbstractEntity implements Firefly {

    protected FireflyPositionUpdateStrategy positionUpdateStrategy;
    protected InitialisationStrategy<Firefly> positionInitialisationStrategy;

    /**
     * Default constructor. Defines reasonable defaults for common members.
     */
    public AbstractFirefly() {
        this.positionUpdateStrategy = new StandardFireflyPositionUpdateStrategy();
        this.positionInitialisationStrategy = new RandomInitialisationStrategy<Firefly>();
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy the reference of the bee that is deep copied.
     */
    public AbstractFirefly(AbstractFirefly copy) {
        super(copy);
        this.positionUpdateStrategy = copy.positionUpdateStrategy.getClone();
        this.positionInitialisationStrategy = copy.positionInitialisationStrategy.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract AbstractFirefly getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    public FireflyPositionUpdateStrategy getPositionUpdateStrategy() {
        return this.positionUpdateStrategy;
    }

    /**
     * Sets the position update strategy of the firefly.
     * @param positionUpdateStrategy the new position update strategy.
     */
    public void setPositionUpdateStrategy(FireflyPositionUpdateStrategy positionUpdateStrategy) {
        this.positionUpdateStrategy = positionUpdateStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void updatePosition(Firefly other);

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareIntensity(Firefly other) {
        return compareTo(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBrighter(Firefly other) {
        return this.compareIntensity(other) > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getPosition() {
        return (Vector) this.getCandidateSolution();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(Vector position) {
        this.setCandidateSolution(position);
    }

    /**
     * Get the current position {@linkplain InitialisationStrategy}.
     * @return The current position {@linkplain InitialisationStrategy}.
     */
    public InitialisationStrategy<Firefly> getPositionInitialisationStrategy() {
        return this.positionInitialisationStrategy;
    }

    /**
     * Set the position {@link InitialisationStrategy} to be used.
     * @param positionInitialisationStrategy the {@link InitialisationStrategy} to use.
     */
    public void setPositionInitialisationStrategy(InitialisationStrategy positionInitialisationStrategy) {
        this.positionInitialisationStrategy = positionInitialisationStrategy;
    }
}
