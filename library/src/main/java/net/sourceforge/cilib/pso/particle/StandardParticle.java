/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public class StandardParticle extends AbstractParticle {
    private static final long serialVersionUID = 2610843008637279845L;

    protected Particle neighbourhoodBest;

    /** Creates a new instance of StandardParticle. */
    public StandardParticle() {
        super();
        put(Property.BEST_POSITION, Vector.of());
        put(Property.VELOCITY, Vector.of());
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public StandardParticle(StandardParticle copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StandardParticle getClone() {
        return new StandardParticle(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getBestFitness() {
        return (Fitness) get(Property.BEST_FITNESS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getBestPosition() {
        return (Vector) get(Property.BEST_POSITION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Particle getNeighbourhoodBest() {
        return this.neighbourhoodBest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getVelocity() {
        return (Vector) get(Property.VELOCITY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(Problem problem) {
        put(Property.CANDIDATE_SOLUTION, problem.getDomain().getBuiltRepresentation().getClone());
        put(Property.BEST_POSITION, Vector.copyOf((Vector) getPosition()));
        put(Property.VELOCITY, Vector.copyOf((Vector) getPosition()));

        put(Property.FITNESS, InferiorFitness.instance());
        put(Property.BEST_FITNESS, InferiorFitness.instance());
        put(Property.PREVIOUS_FITNESS, InferiorFitness.instance());
        this.neighbourhoodBest = this;

        this.positionInitialisationStrategy.initialise(Property.CANDIDATE_SOLUTION, this);
        this.personalBestInitialisationStrategy.initialise(Property.BEST_POSITION, this);
        this.velocityInitialisationStrategy.initialise(Property.VELOCITY, this);

        put(Property.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
        put(Property.POSITION_UPDATE_COUNTER, Int.valueOf(0));
        put(Property.PREVIOUS_SOLUTION, getPosition());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFitness(Fitness newFitness) {
        super.updateFitness(newFitness);
        this.personalBestUpdateStrategy.updatePersonalBest(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNeighbourhoodBest(Particle particle) {
        neighbourhoodBest = particle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinitialise() {
        this.positionInitialisationStrategy.initialise(Property.CANDIDATE_SOLUTION, this);
        this.velocityInitialisationStrategy.initialise(Property.VELOCITY, this);
    }
}
