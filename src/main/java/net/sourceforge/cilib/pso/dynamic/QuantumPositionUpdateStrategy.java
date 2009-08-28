/*
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
package net.sourceforge.cilib.pso.dynamic;

import java.util.Arrays;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.pso.positionupdatestrategies.PositionUpdateStrategy;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Position update strategy for QSO (Quantum PSO). Implemented according
 * to paper by Blackwell and Branke, "Multiswarms, Exclusion, and Anti-
 * Convergence in Dynamic Environments."
 *
 * @author Anna Rakitianskaia, Julien Duhain
 */
public class QuantumPositionUpdateStrategy implements PositionUpdateStrategy {

    private static final long serialVersionUID = -7844226788317206737L;
    private static final double EPSILON = 0.000000001;
    private double radius;
    private RandomNumber randomizer;
    Vector nucleus;

    public Vector getNucleus() {
        return nucleus;
    }

    public void setNucleus(Vector nucleus) {
        this.nucleus = nucleus;
    }

    public QuantumPositionUpdateStrategy() {
        radius = 5;
        randomizer = new RandomNumber();
    }

    public QuantumPositionUpdateStrategy(QuantumPositionUpdateStrategy copy) {
        this.radius = copy.radius;
        this.randomizer = copy.randomizer.getClone();
    }

    public QuantumPositionUpdateStrategy getClone() {
        return new QuantumPositionUpdateStrategy(this);
    }

    /**
     * Update particle position; do it in a standard way if the particle is neutral, and
     * in a quantum way if the particle is charged. The "quantum" way entails sampling the
     * position from a uniform distribution : a spherical cloud around gbest with a radius r.
     * @param particle the particle to update position of
     */
    public void updatePosition(Particle particle) {
        ChargedParticle checkChargeParticle = (ChargedParticle) particle;
        if(checkChargeParticle.getCharge() < EPSILON) { // the particle is neutral
            Vector position = (Vector) particle.getPosition();
            Vector velocity = (Vector) particle.getVelocity();

            for (int i = 0; i < position.getDimension(); ++i) {
                double value = position.getReal(i);
                value += velocity.getReal(i);
                position.setReal(i, value);
            }
        }
        else { // the particle is charged
            //based on the Pythagorean theorem,
            //the following code breaks the square of the radius distance into smaller
            //parts that are then "distributed" among the dimensions of the problem.
            //the position of the particle is determined in each dimension by a random number
            //between 0 and the part of the radius assigned to that dimension
            //This ensures that the quantum particles are placed randomly within the
            //multidimensional sphere determined by the quantum radius.

            Vector position = (Vector) particle.getPosition();
            nucleus = (Vector) AbstractAlgorithm.get().getBestSolution().getPosition();

            double distance = Math.pow(this.radius,2); //square of the radius
            int dimensions = position.getDimension();
            double[] pieces = new double[dimensions]; // break up of the distance
            pieces[dimensions-1] = distance;
            for(int i=0; i<dimensions-1; i++){
                pieces[i]=randomizer.getUniform(0, distance);
            }//for
            Arrays.sort(pieces);
            int sign = 1;
            if(randomizer.getUniform() <= 0.5){
                sign = -1;
            }//if
            //deals with first dimension
            position.setReal(0, nucleus.getReal(0) + sign*randomizer.getUniform(0,Math.sqrt(pieces[0])));
            //deals with the other dimensions
            for(int i=1; i<dimensions; i++){
                sign=1;
                if(randomizer.getUniform() <= 0.5){
                    sign = -1;
                }//if
                double rad = Math.sqrt(pieces[i]-pieces[i-1]);
                double dis = randomizer.getUniform(0, rad);
                double newpos = nucleus.getReal(i) + sign*dis;
                position.setReal(i, newpos);
            }//for
        }//else
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
        if(radius < 0) throw new IllegalArgumentException("Radius of the electron cloud can not be negative");
        this.radius = radius;
    }
}
