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
package net.sourceforge.cilib.entity.operators.creation;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.ControlParameters;
import net.sourceforge.cilib.util.Sequence;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.RandomArrangement;

/**
 * A creation strategy for DE where the difference vector is computed by
 * selecting random entities from the population for each dimension.
 * 
 */
public class RandPerDimensionCreationStrategy implements CreationStrategy {

    private static final long serialVersionUID = 930740770470361009L;
    protected ControlParameter scaleParameter;
    protected ControlParameter numberOfDifferenceVectors;

    /**
     * Create a new instance of {@code CurrentToRandCreationStrategy}.
     */
    public RandPerDimensionCreationStrategy() {
        this.scaleParameter = ConstantControlParameter.of(0.5);
        this.numberOfDifferenceVectors = ConstantControlParameter.of(2);
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public RandPerDimensionCreationStrategy(RandPerDimensionCreationStrategy copy) {
        this.scaleParameter = copy.scaleParameter.getClone();
        this.numberOfDifferenceVectors = copy.numberOfDifferenceVectors.getClone();
    }

    /**
     * {@inheritDoc}
     * @return A copy of the current instance.
     */
    @Override
    public RandPerDimensionCreationStrategy getClone() {
        return new RandPerDimensionCreationStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity create(Entity targetEntity, Entity current, Topology<? extends Entity> topology) {
        RandomProvider random = new MersenneTwister();
        
        List<Entity> participants = (List<Entity>) Selection.copyOf(topology.asList())
                .exclude(targetEntity, current)
                .select(Samples.all());
        Vector differenceVector = determineDistanceVector(participants);

        Vector targetVector = (Vector) targetEntity.getCandidateSolution();
        Vector trialVector = targetVector.plus(differenceVector.multiply(ControlParameters.supplierOf(scaleParameter)));

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
        RandomProvider random = new MersenneTwister();
        Vector distanceVector = Vector.copyOf(Sequence.repeat(0.0, participants.get(0).getCandidateSolution().size()));
        Iterator<Entity> iterator;
        int number = Double.valueOf(this.numberOfDifferenceVectors.getParameter()).intValue();
        List<Entity> currentParticipants;

        Vector first, second;
        double difference;

        for (int d = 0; d < distanceVector.size(); d++) {
            //get random participants for this dimension
            currentParticipants = (List<Entity>) Selection.copyOf(participants)
                    .orderBy(new RandomArrangement(random))
                    .select(Samples.first(number));
            iterator = currentParticipants.iterator();

            while (iterator.hasNext()) {
                first = (Vector) iterator.next().getCandidateSolution();
                second = (Vector) iterator.next().getCandidateSolution();

                difference = first.doubleValueOf(d) - second.doubleValueOf(d);

                distanceVector.setReal(d, distanceVector.get(d).doubleValue() + difference);
            }
        }

        return distanceVector;
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
