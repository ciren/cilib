/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.pso.velocityupdatestrategies.VelocityUpdateStrategy;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * LFDecorator.
 * @author  barlad
 */
public class LFDecorator extends ParticleDecorator implements Cloneable {
    private static final long serialVersionUID = -7796859060873208936L;

    /** Creates a new instance of LFParticleDecorator. */
    public LFDecorator(Particle target) {
        super(target);

        defaultDeltaT = 0.5;

        deltaT = defaultDeltaT;
        delta = 1;
        m = 3;
        delta1 = 0.001;

        epsilon = 0.000000000000000000000000000000000000000001;

        i = 0;
        j = 2;
        s = 0;
        p = 1;

        wasNeighbourhoodBest = false;
    }

    public LFDecorator getClone() {
        return new LFDecorator(this.getTarget()); //super.clone();
    }

    @Override
    public void calculateFitness() {
        super.calculateFitness();
    }

    public void updateVelocity(VelocityUpdateStrategy vu) {
        this.getVelocityUpdateStrategy().updateVelocity(this);
    }

    /**
     *
     * @param particle
     * @return
     * @deprecated
     */
    @Deprecated
    public static LFDecorator extract(Particle particle) {
        throw new UnsupportedOperationException("This method has been removed from the CILib API");
    }

    public void initialise(OptimisationProblemAdapter problem) {
        //previousPosition = new double[problem.getDomain().getDimension()];
        //previousVelocity = new double[problem.getDomain().getDimension()];
        //previousPosition = new double[DomainParser.getInstance().getDimension()];
        //previousVelocity = new double[DomainParser.getInstance().getDimension()];


        //previousPosition = new MixedVector(DomainParser.getInstance().getDimension());
        //previousVelocity = new MixedVector(DomainParser.getInstance().getDimension());

        previousPosition = new Vector(problem.getDomain().getDimension());
        previousVelocity = new Vector(problem.getDomain().getDimension());

        //gradient = new double[DomainParser.getInstance().getDimension()];
        gradient = new double[problem.getDomain().getDimension()];
        //nextGradient = new double[problem.getDomain().getDimension()];
        //nextGradient = new double[DomainParser.getInstance().getDimension()];
        nextGradient = new double[problem.getDomain().getDimension()];

        super.initialise(problem);
    }

    /**
     * Returns the algorithm state variable indicating whether the particle was
     * the neighbourhood best in the previous iteration of the algorithm.
     */
    public boolean getWasNeighbourhoodBest() {
        return wasNeighbourhoodBest;
    }

    /**
     * Sets the algorithm state variable indicating whether the particle was
     * the neighbourhood best in the previous iteration of the algorithm.
     */
    public void setWasNeighbourhoodBest(boolean wasNeighbourhoodBest) {
        this.wasNeighbourhoodBest = wasNeighbourhoodBest;
    }

    /**
     * Returns the algorithm parameter variable delta.
     */
    public double getDelta() {
        return delta;
    }

    /**
     * Sets the algorithm state variable delta.
     */
    public void setDelta(double delta) {
        this.delta = delta;
    }

    /**
     * Returns the algorithm parameter variable deltaT.
     */
    public double getDeltaT() {
        return deltaT;
    }

    /**
    * Sets the algorithm parameter variable deltaT.
    */
    public void setDeltaT(double deltaT) {
        this.deltaT = deltaT;
    }

    /**
     * Returns the algorithm parameter variable deltaT.
     */
    public double getDefaultDeltaT() {
        return defaultDeltaT;
    }

    /**
    * Sets the algorithm parameter variable deltaT.
    */
    public void setDefaultDeltaT(double defaultDeltaT) {
        this.defaultDeltaT = defaultDeltaT;
    }

    /**
    * Returns the algorithm state variable p.
    */
    public double getP() {
        return p;
    }

    /**
    * Sets the algorithm state variable p.
    */
    public void setP(double p) {
        this.p = p;
    }

    /**
    * Returns the algorithm parameter variable delta1.
    */
    public double getDelta1() {
        return delta1;
    }

    /**
    * Sets the algorithm parameter variable delta1.
    */
    public void setDelta1(double delta1) {
        this.delta1 = delta1;
    }

    /**
    * Returns the algorithm state variable s.
    */
    public int getS() {
        return s;
    }

    /**
    * Sets the algorithm state variable s.
    */
    public void setS(int s) {
        this.s = s;
    }

    /**
     * Returns the algorithm state variable m.
     */
    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    /**
     * Returns the algorithm state variable i.
     */
    public int getI() {
        return i;
    }

    /**
     * Sets the algorithm state variable i.
     */
    public void setI(int i) {
        this.i = i;
    }

    /**
     * Returns the algorithm state variable j.
     */
    public int getJ() {
        return j;
    }

    /**
     * Sets the algorithm state variable j.
     */
    public void setJ(int j) {
        this.j = j;
    }

    /**
     * Returns the algorithm parameter variable epsilon.
     */
    public double getEpsilon() {
        return epsilon;
    }

    /**
     * Sets the algorithm parameter variable epsilon.
     */
    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    /**
     * Returns the position of the particle in the previous step.
     */
    public Vector getPreviousPosition() {
        return previousPosition;
    }

    /**
     * Returns the velocity of the particle in the previous step.
     */
    public Vector getPreviousVelocity() {
        return previousVelocity;
    }

    /**
     * Returns the gradient of the particle.
     */
    public double [] getGradient() {
        return gradient;
    }

    /**
     * Returns the array used to store the future gradient of the particle.
     */
    public double [] getNextGradient() {
        return nextGradient;
    }

    private double defaultDeltaT;
    private double deltaT;
    private double delta;
    private int m;
    private double delta1;
    private double epsilon;

    private int i;
    private int j;
    private int s;
    private double p;

    private double [] gradient;
    private double [] nextGradient;

    //private double previousVelocity [];
    //private double previousPosition [];
    private Vector previousVelocity;
    private Vector previousPosition;

    private boolean wasNeighbourhoodBest;
}

