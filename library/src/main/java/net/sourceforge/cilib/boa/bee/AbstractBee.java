/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.boa.bee;

import net.sourceforge.cilib.boa.positionupdatestrategies.BeePositionUpdateStrategy;
import net.sourceforge.cilib.boa.positionupdatestrategies.VisualPositionUpdateStategy;
import net.sourceforge.cilib.entity.AbstractEntity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 * The entity class for the ABC algorithm that represents the bees.
 */
public abstract class AbstractBee extends AbstractEntity implements HoneyBee {

    private static final long serialVersionUID = 7005546673802814268L;
    protected BeePositionUpdateStrategy positionUpdateStrategy;
    protected Selector<HoneyBee> targetSelectionStrategy;
    protected int dimension;

    /**
     * Default constructor. Defines reasonable defaults for common members.
     */
    public AbstractBee() {
        this.positionUpdateStrategy = new VisualPositionUpdateStategy();
        this.targetSelectionStrategy = new RandomSelector();
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy the reference of the bee that is deep copied.
     */
    public AbstractBee(AbstractBee copy) {
        super(copy);
        this.positionUpdateStrategy = copy.positionUpdateStrategy;
        this.targetSelectionStrategy = copy.targetSelectionStrategy;
        this.dimension = copy.dimension;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract AbstractBee getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    public BeePositionUpdateStrategy getPositionUpdateStrategy() {
        return this.positionUpdateStrategy;
    }

    /**
     * Sets the position update strategy of the bee.
     * @param positionUpdateStrategy the new position update strategy.
     */
    public void setPositionUpdateStrategy(BeePositionUpdateStrategy positionUpdateStrategy) {
        this.positionUpdateStrategy = positionUpdateStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void updatePosition();

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDimension() {
        return this.dimension;
    }

    /**
     * Sets the dimension of the solution used by the bee.
     * @param dimension the new dimension of the solution.
     */
    public void setDimension(int dimension) {
        this.dimension = dimension;
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
     * {@inheritDoc}
     */
    @Override
    public void initialise(Problem problem) {
        Vector candidate = Vector.newBuilder().copyOf(problem.getDomain().getBuiltRepresentation()).buildRandom();
        this.setCandidateSolution(candidate);
        this.dimension = candidate.size();
        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinitialise() {
        throw new UnsupportedOperationException("Reinitialise not implemented for AbstractBee");
    }

    /**
     * Gets the target selection strategy, for selecting bees to follow in position updates.
     * @return the target selection strategy.
     */
    public Selector getTargetSelectionStrategy() {
        return targetSelectionStrategy;
    }

    /**
     * Sets the target selection strategy, for selecting bees to follow in position updates.
     * @param targetSelectionStrategy  the new target selection strategy.
     */
    public void setTargetSelectionStrategy(Selector targetSelectionStrategy) {
        this.targetSelectionStrategy = targetSelectionStrategy;
    }
}
