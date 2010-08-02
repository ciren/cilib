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
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Edwin Peer
 * @author Gary Pampara
 */
public class StandardParticle extends AbstractParticle {
    private static final long serialVersionUID = 2610843008637279845L;

    protected Particle neighbourhoodBest;

    /** Creates a new instance of StandardParticle. */
    public StandardParticle() {
        super();

        this.getProperties().put(EntityType.Particle.BEST_POSITION, new Vector());
        this.getProperties().put(EntityType.Particle.VELOCITY, new Vector());
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public StandardParticle(StandardParticle copy) {
        super(copy);
        this.neighbourhoodBestUpdateStrategy = copy.neighbourhoodBestUpdateStrategy;
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
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

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
    public int getDimension() {
        return getPosition().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Particle getNeighbourhoodBest() {
        return neighbourhoodBest;
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
    public void initialise(OptimisationProblem problem) {
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, problem.getDomain().getBuiltRepresenation().getClone());

        this.getPositionInitialisationStrategy().initialize(EntityType.CANDIDATE_SOLUTION, this);
        this.getProperties().put(EntityType.Particle.BEST_POSITION, getPosition().getClone());

        // Create the velocity vector by cloning the position and setting all the values
        // within the velocity to 0
        this.getProperties().put(EntityType.Particle.VELOCITY, getPosition().getClone());
        this.velocityInitializationStrategy.initialize(EntityType.Particle.VELOCITY, this);

        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        this.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());
        this.neighbourhoodBest = this;

        this.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePosition() {
        behavior.getPositionUpdateStrategy().updatePosition(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calculateFitness() {
        Fitness fitness = getFitnessCalculator().getFitness(this);
        this.getProperties().put(EntityType.FITNESS, fitness);

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
        behavior.getVelocityUpdateStrategy().updateVelocity(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateControlParameters() {
        behavior.getVelocityUpdateStrategy().updateControlParameters(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reinitialise() {
        this.positionInitialisationStrategy.initialize(EntityType.CANDIDATE_SOLUTION, this);
        this.velocityInitializationStrategy.initialize(EntityType.Particle.VELOCITY, this);
    }
}
