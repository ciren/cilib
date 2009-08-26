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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.RandomNumber;
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

    private RandomNumber rand1;
    private RandomNumber rand2;
    private RandomNumber rand3;
    private ControlParameter cognitive;
    private ControlParameter social;
    private ControlParameter crossoverProbability;

    /**
     * Create a new instance of the {@linkplain BareBonesDEVelocityUpdate}.
     */
    public BareBonesDEVelocityUpdate() {
        rand1 = new RandomNumber();
        rand2 = new RandomNumber();
        rand3 = new RandomNumber();
        cognitive = new RandomizingControlParameter();
        social = new RandomizingControlParameter();
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
    public BareBonesDEVelocityUpdate getClone() {
        return new BareBonesDEVelocityUpdate(this);
    }

    /**
     * {@inheritDoc}
     */
    public void updateVelocity(Particle particle) {
        Vector personalBestPosition = (Vector) particle.getBestPosition();
        Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();
        Vector velocity = (Vector) particle.getVelocity();

        PSO pso = (PSO) AbstractAlgorithm.get();
        List<Entity> positions = getRandomParentEntities(pso.getTopology());

        //select three random individuals, all different and different from particle
        RandomNumber r1 = new RandomNumber();

        Vector position1 = (Vector) positions.get(0).getCandidateSolution();
        Vector position2 = (Vector) positions.get(1).getCandidateSolution();
//        Vector position3 = (Vector) positions.get(2).getContents();
        for (int i = 0; i < particle.getDimension(); ++i) {
            double r = r1.getUniform(0, 1);
            double attractor = r*personalBestPosition.getReal(i)+(1-r)*nBestPosition.getReal(i);
            double stepSize = rand3.getUniform(0, 1) * (position1.getReal(i)-position2.getReal(i));

            if (rand2.getUniform(0, 1) > crossoverProbability.getParameter())
                velocity.setReal(i, attractor + stepSize);
            else
                velocity.setReal(i, ((Vector) particle.getPosition()).getReal(i)); //position3.getReal(i));
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

        RandomNumber randomNumber = new RandomNumber();

        int count = 0;

        while (count < 3) {
            int random = randomNumber.getRandomGenerator().nextInt(topology.size());
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
    public void updateControlParameters(Particle particle) {
        // TODO Auto-generated method stub

    }

    /**
     * Get the first {@linkplain RandomNumber}.
     * @return The first {@linkplain RandomNumber}.
     */
    public RandomNumber getRand1() {
        return rand1;
    }

    /**
     * Set the first {@linkplain RandomNumber}.
     * @param rand1 The value to set.
     */
    public void setRand1(RandomNumber rand1) {
        this.rand1 = rand1;
    }

    /**
     * Get the second{@linkplain RandomNumber}.
     * @return The second {@linkplain RandomNumber}.
     */
    public RandomNumber getRand2() {
        return rand2;
    }

    /**
     * Set the second {@linkplain RandomNumber}.
     * @param rand2 The value to set.
     */
    public void setRand2(RandomNumber rand2) {
        this.rand2 = rand2;
    }


    /**
     * Get the third {@linkplain RandomNumber}.
     * @return The third {@linkplain RandomNumber}.
     */
    public RandomNumber getRand3() {
        return rand3;
    }


    /**
     * Set the third {@linkplain RandomNumber}.
     * @param rand3 The value to set.
     */
    public void setRand3(RandomNumber rand3) {
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

}
