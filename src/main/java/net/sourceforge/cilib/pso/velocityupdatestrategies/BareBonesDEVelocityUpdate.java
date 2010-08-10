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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *  The <tt>VelocityUpdateStrategy</tt> strategy which uses a DE strategy
 *  where the trial vector is the bare bones attractor point.
 *
 *  TODO: To be published by Omran and Engelbrecht
 *
 *  @author Andries Engelbrecht
 */
public class BareBonesDEVelocityUpdate implements VelocityUpdateStrategy {

    private static final long serialVersionUID = -8781011210069055197L;
    private ProbabilityDistributionFuction rand1;
    private ProbabilityDistributionFuction rand2;
    private ProbabilityDistributionFuction rand3;
    private RandomProvider r1;
    private RandomProvider r2;
    private ControlParameter cognitive;
    private ControlParameter social;
    private ControlParameter crossoverProbability;

    /**
     * Create a new instance of the {@linkplain BareBonesDEVelocityUpdate}.
     */
    public BareBonesDEVelocityUpdate() {
        rand1 = new UniformDistribution();
        rand2 = new UniformDistribution();
        rand3 = new UniformDistribution();
        r1 = new MersenneTwister();
        r2 = new MersenneTwister();
        cognitive = new ConstantControlParameter();
        social = new ConstantControlParameter();
        crossoverProbability = new ConstantControlParameter(0.5);

        cognitive.setParameter(1);
        social.setParameter(1);
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public BareBonesDEVelocityUpdate(BareBonesDEVelocityUpdate copy) {
        this();

        cognitive.setParameter(copy.cognitive.getParameter());
        social.setParameter(copy.social.getParameter());
        crossoverProbability.setParameter(copy.crossoverProbability.getParameter());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BareBonesDEVelocityUpdate getClone() {
        return new BareBonesDEVelocityUpdate(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateVelocity(Particle particle) {
        Vector personalBestPosition = (Vector) particle.getBestPosition();
        Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();
        Vector velocity = (Vector) particle.getVelocity();

        PSO pso = (PSO) AbstractAlgorithm.get();
        List<Entity> positions = getRandomParentEntities(pso.getTopology());

        //select three random individuals, all different and different from particle
        ProbabilityDistributionFuction pdf = new UniformDistribution();

        Vector position1 = (Vector) positions.get(0).getCandidateSolution();
        Vector position2 = (Vector) positions.get(1).getCandidateSolution();
//        Vector position3 = (Vector) positions.get(2).getContents();
        for (int i = 0; i < particle.getDimension(); ++i) {
            double r = pdf.getRandomNumber(0, 1);
            double attractor = r * personalBestPosition.doubleValueOf(i) + (1 - r) * nBestPosition.doubleValueOf(i);
            double stepSize = rand3.getRandomNumber(0, 1) * (position1.doubleValueOf(i) - position2.doubleValueOf(i));

            if (rand2.getRandomNumber(0, 1) > crossoverProbability.getParameter()) {
                velocity.setReal(i, attractor + stepSize);
            } else {
                velocity.setReal(i, ((Vector) particle.getPosition()).doubleValueOf(i)); //position3.getReal(i));
            }
        }
    }

    /**
     * Get a list of individuals that are suitable to be used within
     * the recombination arithmetic operator.
     * @param topology The {@see net.sourceforge.cilib.entity.Topology Topology} containing the entites.
     * @return A list of unique entities.
     */
    public static List<Entity> getRandomParentEntities(Topology<? extends Entity> topology) {
        List<Entity> parents = new ArrayList<Entity>(3);

        ProbabilityDistributionFuction randomNumber = new UniformDistribution();

        int count = 0;

        while (count < 3) {
            int random = randomNumber.getRandomProvider().nextInt(topology.size());
            Entity parent = topology.get(random);
            if (!parents.contains(parent)) {
                parents.add(parent);
                count++;
            }
        }

        return parents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateControlParameters(Particle particle) {
        // TODO Auto-generated method stub
    }

    /**
     * Get the first {@linkplain RandomNumber}.
     * @return The first {@linkplain RandomNumber}.
     */
    public ProbabilityDistributionFuction getRand1() {
        return rand1;
    }

    /**
     * Set the first {@linkplain RandomNumber}.
     * @param rand1 The value to set.
     */
    public void setRand1(ProbabilityDistributionFuction rand1) {
        this.rand1 = rand1;
    }

    /**
     * Get the second{@linkplain RandomNumber}.
     * @return The second {@linkplain RandomNumber}.
     */
    public ProbabilityDistributionFuction getRand2() {
        return rand2;
    }

    /**
     * Set the second {@linkplain RandomNumber}.
     * @param rand2 The value to set.
     */
    public void setRand2(ProbabilityDistributionFuction rand2) {
        this.rand2 = rand2;
    }

    /**
     * Get the third {@linkplain RandomNumber}.
     * @return The third {@linkplain RandomNumber}.
     */
    public ProbabilityDistributionFuction getRand3() {
        return rand3;
    }

    /**
     * Set the third {@linkplain RandomNumber}.
     * @param rand3 The value to set.
     */
    public void setRand3(ProbabilityDistributionFuction rand3) {
        this.rand3 = rand3;
    }

    /**
     * Get the cognitive component.
     * @return The cognitive component.
     */
    public ControlParameter getCognitive() {
        return cognitive;
    }

    /**
     * Set the cognitive component.
     * @param cognitive The value to set.
     */
    public void setCognitive(ControlParameter cognitive) {
        this.cognitive = cognitive;
    }

    /**
     * Get the social component.
     * @return The social component.
     */
    public ControlParameter getSocial() {
        return social;
    }

    /**
     * Set the social component {@linkplain ControlParameter}.
     * @param social The value to set.
     */
    public void setSocial(ControlParameter social) {
        this.social = social;
    }

    /**
     * Get the cross-over probability.
     * @return The cross over probability {@linkplain ControlParameter}.
     */
    public ControlParameter getCrossoverProbability() {
        return crossoverProbability;
    }

    /**
     * Set the crossover probability.
     * @param crossoverProbability The value to set.
     */
    public void setCrossoverProbability(ControlParameter crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public RandomProvider getR1() {
        return r1;
    }

    public void setR1(RandomProvider r1) {
        this.r1 = r1;
    }

    public RandomProvider getR2() {
        return r2;
    }

    public void setR2(RandomProvider r2) {
        this.r2 = r2;
    }
}
