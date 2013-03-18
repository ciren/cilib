/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ff.firefly;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.InferiorFitness;

/**
 * THe standard firefly uses its position update strategy to move according to other fireflies.
 */
public class StandardFirefly extends AbstractFirefly {

    /**
     * Create a new instance
     */
    public StandardFirefly() {

    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public StandardFirefly(StandardFirefly copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardFirefly getClone() {
        return new StandardFirefly(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePosition(Firefly other) {
        setPosition(this.positionUpdateStrategy.updatePosition(this, other));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(Problem problem) {
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, problem.getDomain().getBuiltRepresentation().getClone());
        this.positionInitialisationStrategy.initialise(EntityType.CANDIDATE_SOLUTION, this);

        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinitialise() {
        this.positionInitialisationStrategy.initialise(EntityType.CANDIDATE_SOLUTION, this);
    }
}
