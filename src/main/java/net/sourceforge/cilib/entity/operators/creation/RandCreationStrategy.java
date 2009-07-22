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
package net.sourceforge.cilib.entity.operators.creation;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.TopologyHolder;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.Selection;

public class RandCreationStrategy implements CreationStrategy {
    private static final long serialVersionUID = 930740770470361009L;

    protected ControlParameter scaleParameter;
    protected ControlParameter numberOfDifferenceVectors;

    /**
     * Create a new instance of {@code CurrentToRandCreationStrategy}.
     */
    public RandCreationStrategy() {
        this.scaleParameter = new ConstantControlParameter(0.5);
        this.numberOfDifferenceVectors = new ConstantControlParameter(2);
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public RandCreationStrategy(RandCreationStrategy copy) {
        this.scaleParameter = copy.scaleParameter.getClone();
        this.numberOfDifferenceVectors = copy.numberOfDifferenceVectors.getClone();
    }

    /**
     * {@inheritDoc}
     * @return A copy of the current instance.
     */
    @Override
    public RandCreationStrategy getClone() {
        return new RandCreationStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity create(Entity targetEntity, Entity current, Topology<? extends Entity> topology) {
        Random random = new MersenneTwister();
        List<Entity> participants = Selection.from(topology.asList()).exclude(targetEntity, current).unique().random(random, (int)numberOfDifferenceVectors.getParameter()).select();
        Vector differenceVector = determineDistanceVector(participants);

        Vector targetVector = (Vector) targetEntity.getCandidateSolution();
        Vector trialVector = targetVector.plus(differenceVector.multiply(scaleParameter.getParameter()));

        Entity trialEntity = current.getClone();
        trialEntity.setCandidateSolution(trialVector);

        return trialEntity;
    }

    /**
     * Calculate the {@linkplain Vector} that is the resultant of several difference vectors.
     * @param participants The {@linkplain Entity} list to create the difference vectors from. It
     *        is very important to note that the {@linkplain Entity} objects within this list
     *        should not contain duplicates. If duplicates are contained, this will severely
     *        reduce the diversity of the population as not all entities will be considered.
     * @return A {@linkplain Vector} representing the resultant of all calculated difference vectors.
     */
    protected Vector determineDistanceVector(List<Entity> participants) {
        Vector distanceVector = new Vector(participants.get(0).getCandidateSolution().size(), new Real(0.0));
        Iterator<Entity> iterator = participants.iterator();

        while (iterator.hasNext()) {
            Vector first = (Vector) iterator.next().getCandidateSolution();
            Vector second = (Vector) iterator.next().getCandidateSolution();

            Vector difference = first.subtract(second);
            distanceVector = distanceVector.plus(difference);
        }

        return distanceVector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performOperation(TopologyHolder holder) {
        throw new UnsupportedOperationException("Not supported yet. This may need some more refactoring. May require looping operator?");
    }

    /**
     * Get the number of difference vectors to create.
     * @return The {@code ControlParameter} describing the numberof difference vectors.
     */
    public ControlParameter getNumberOfDifferenceVectors() {
        return numberOfDifferenceVectors;
    }

    /**
     * Set the number of difference vectors to create.
     * @param numberOfDifferenceVectors The value to set.
     */
    public void setNumberOfDifferenceVectors(ControlParameter numberOfDifferenceVectors) {
        this.numberOfDifferenceVectors = numberOfDifferenceVectors;
    }

    /**
     * Get the current scale parameter, used within the creation.
     * @return The {@code ControlParameter} representing the scale parameter.
     */
    public ControlParameter getScaleParameter() {
        return scaleParameter;
    }

    /**
     * Set the scale parameter for the creation strategy.
     * @param scaleParameter The value to set.
     */
    public void setScaleParameter(ControlParameter scaleParameter) {
        this.scaleParameter = scaleParameter;
    }

}
