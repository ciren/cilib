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

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.velocityupdatestrategies.VelocityUpdateStrategy;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 *
 * @author  Edwin Peer
 */
public abstract class ParticleDecorator extends AbstractParticle {
    private static final long serialVersionUID = -1604818864075431177L;

    private AbstractParticle target;

    public ParticleDecorator() {
        this.neighbourhoodBestUpdateStrategy = null;
        this.behavior.setPositionUpdateStrategy(null);
        this.behavior.setVelocityUpdateStrategy(null);

        target = null;
    }

    public ParticleDecorator(Particle target) {
        this.target = (AbstractParticle) target;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

        ParticleDecorator other = (ParticleDecorator) object;
        return super.equals(other) && (this.target.equals(other.target));
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setTarget(Particle target) {
        this.target = (AbstractParticle) target;
    }

    public Particle getTarget() {
        return target;
    }

    public abstract ParticleDecorator getClone();

    public Fitness getBestFitness() {
        return target.getBestFitness();
    }

    public StructuredType getBestPosition() {
        return target.getBestPosition();
    }

    public int getDimension() {
        return target.getDimension();
    }

    public Fitness getFitness() {
        return target.getFitness();
    }

    public long getId() {
        return target.getId();
    }

    public Particle getNeighbourhoodBest() {
        return target.getNeighbourhoodBest();
    }

    public StructuredType getPosition() {
        return target.getPosition();
    }

    public StructuredType getVelocity() {
        return target.getVelocity();
    }


    public void initialise(OptimisationProblem problem) {
        target.initialise(problem);
    }

    public void updatePosition() {
        target.updatePosition();
    }

    @Override
    public void calculateFitness() {
        target.calculateFitness();
    }

    public void setNeighbourhoodBest(Particle particle) {
        target.setNeighbourhoodBest(particle);
    }


    public void updateVelocity() {
        target.updateVelocity();
    }

    public void updateControlParameters() {
        target.updateControlParameters();
    }

    public VelocityUpdateStrategy getVelocityUpdateStrategy() {
        return target.behavior.getVelocityUpdateStrategy();
    }

    public void setVelocityUpdateStrategy(VelocityUpdateStrategy velocityUpdateStrategy) {
        target.setVelocityUpdateStrategy(velocityUpdateStrategy);
    }


    public StructuredType getCandidateSolution() {
        return target.getCandidateSolution();
    }

    public void setCandidateSolution(StructuredType type) {
        this.target.setCandidateSolution(type);
    }

    public int compareTo(Entity o) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setDimension(int dim) {

    }

    public Type getBehaviouralParameters() {
        return null;
    }

    public void setBehaviouralParameters(Type type) {

    }

    public void reinitialise() {

    }
}
