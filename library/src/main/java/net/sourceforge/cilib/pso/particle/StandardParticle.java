/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.entity.EntityType;
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
        this.getProperties().put(EntityType.Particle.BEST_POSITION, Vector.of());
        this.getProperties().put(EntityType.Particle.VELOCITY, Vector.of());
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if ((object == null) || (this.getClass() != object.getClass())) {
            return false;
        }

        StandardParticle other = (StandardParticle) object;
        return super.equals(object) &&
            (this.neighbourhoodBest == null ? true : this.neighbourhoodBest.equals(other.neighbourhoodBest));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getBestFitness() {
        return (Fitness) this.getProperties().get(EntityType.Particle.BEST_FITNESS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getBestPosition() {
        return (Vector) this.getProperties().get(EntityType.Particle.BEST_POSITION);
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
    public Vector getPosition() {
        return (Vector) getCandidateSolution();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getVelocity() {
        return (Vector) this.getProperties().get(EntityType.Particle.VELOCITY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise(Problem problem) {
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, problem.getDomain().getBuiltRepresentation().getClone());
        this.getProperties().put(EntityType.Particle.BEST_POSITION, Vector.copyOf(getPosition()));
        this.getProperties().put(EntityType.Particle.VELOCITY, Vector.copyOf(getPosition()));

        this.positionInitialisationStrategy.initialise(EntityType.CANDIDATE_SOLUTION, this);
        this.personalBestInitialisationStrategy.initialise(EntityType.Particle.BEST_POSITION, this);
        this.velocityInitialisationStrategy.initialise(EntityType.Particle.VELOCITY, this);

        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        this.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());
        this.getProperties().put(EntityType.PREVIOUS_FITNESS, InferiorFitness.instance());
        this.neighbourhoodBest = this;

        this.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
        this.getProperties().put(EntityType.PREVIOUS_SOLUTION, getCandidateSolution());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePosition() {
        getProperties().put(EntityType.PREVIOUS_SOLUTION, getCandidateSolution());
        getProperties().put(EntityType.CANDIDATE_SOLUTION, this.behavior.getPositionProvider().get(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateFitness() {
        super.calculateFitness();
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
    public void updateVelocity() {
        getProperties().put(EntityType.Particle.VELOCITY, this.behavior.getVelocityProvider().get(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinitialise() {
        this.positionInitialisationStrategy.initialise(EntityType.CANDIDATE_SOLUTION, this);
        this.velocityInitialisationStrategy.initialise(EntityType.Particle.VELOCITY, this);
    }
}
