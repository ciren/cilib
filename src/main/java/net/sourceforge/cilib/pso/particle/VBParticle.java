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
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public class VBParticle extends AbstractParticle{

    private Vector vp;
    private Vector vg;
    private double dotProduct;
    private double radius;
    private int nicheID;

    protected Particle neighbourhoodBest;

    public VBParticle(){
        super();
        this.getProperties().put(EntityType.Particle.BEST_POSITION, new Vector());
        this.getProperties().put(EntityType.Particle.VELOCITY, new Vector());
        this.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance().getClone());
        this.neighbourhoodBest = this.getClone();
        nicheID = 0;
    }

    public void calculateVP(){
        vp = this.getBestPosition().subtract(this.getPosition());
    }

    public void calculateVG(){
        Vector tmp = (Vector) this.neighbourhoodBest.getBestPosition();
        vg = tmp.subtract(this.getPosition());
    }

    public void calculateDotProduct(){
       dotProduct = vp.dot(vg);
    }

    /**
     * @return the vp
     */
    public Vector getVp() {
        return vp;
    }

    /**
     * @param vp the vp to set
     */
    public void setVp(Vector vp) {
        this.vp = vp;
    }

    /**
     * @return the vg
     */
    public Vector getVg() {
        return vg;
    }

    /**
     * @param vg the vg to set
     */
    public void setVg(Vector vg) {
        this.vg = vg;
    }

    /**
     * @return the dotProduct
     */
    public double getDotProduct() {
        return dotProduct;
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setPersonalBest(Vector pBest){
        this.getProperties().put(EntityType.Particle.BEST_POSITION, pBest);
    }

    public void setPosition(Vector vec){
        Vector position = this.getPosition();
        for(int i=0; i<vec.size(); i++){
            position.setReal(i, vec.doubleValueOf(i));
        }
    }

    /**
     * @return the nicheID
     */
    public int getNicheID() {
        return nicheID;
    }

    /**
     * @param nicheID the nicheID to set
     */
    public void setNicheID(int nicheID) {
        this.nicheID = nicheID;
    }

     /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public VBParticle(VBParticle copy) {
        super(copy);
        this.vp = copy.vp;
        this.vg = copy.vg;
        this.dotProduct = copy.dotProduct;
        this.radius = copy.radius;
        this.nicheID = copy.nicheID;
        this.neighbourhoodBestUpdateStrategy = copy.neighbourhoodBestUpdateStrategy;
        this.neighbourhoodBest = copy.neighbourhoodBest;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VBParticle getClone() {
           return new VBParticle(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

        VBParticle other = (VBParticle) object;
        return super.equals(object) &&
            (this.neighbourhoodBest == null ? true : this.neighbourhoodBest.equals(other.neighbourhoodBest));
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePosition() {
        getProperties().put(EntityType.CANDIDATE_SOLUTION, this.behavior.getPositionProvider().get(this));
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
        getProperties().put(EntityType.Particle.VELOCITY, this.behavior.getVelocityProvider().get(this));
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