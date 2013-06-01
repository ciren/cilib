/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.creation;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.RandomArrangement;
import fj.P1;
import net.sourceforge.cilib.controlparameter.AdaptableControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;

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
    public <T extends Entity> T create(T targetEntity, T current, fj.data.List<T> topology) {
        List<T> participants = (List<T>) Selection.copyOf(topology)
                .exclude(targetEntity, current)
                .select(Samples.all());
        Vector differenceVector = determineDistanceVector(participants);

        Vector targetVector = (Vector) targetEntity.getPosition();
        Vector trialVector = targetVector.plus(differenceVector.multiply(new P1<Number>() {
            @Override
            public Number _1() {
                return scaleParameter.getParameter();
            }
        }));

        T trialEntity = (T) current.getClone();
        trialEntity.setPosition(trialVector);

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
    protected <T extends Entity> Vector determineDistanceVector(List<T> participants) {
        Vector distanceVector = Vector.fill(0.0, participants.get(0).getPosition().size());
        Iterator<Entity> iterator;
        int number = Double.valueOf(this.numberOfDifferenceVectors.getParameter()).intValue();
        List<Entity> currentParticipants;

        Vector first, second;
        double difference;

        for (int d = 0; d < distanceVector.size(); d++) {
            //get random participants for this dimension
            currentParticipants = (List<Entity>) Selection.copyOf(participants)
                    .orderBy(new RandomArrangement())
                    .select(Samples.first(number));
            iterator = currentParticipants.iterator();

            while (iterator.hasNext()) {
                first = (Vector) iterator.next().getPosition();
                second = (Vector) iterator.next().getPosition();

                difference = first.doubleValueOf(d) - second.doubleValueOf(d);

                distanceVector.setReal(d, distanceVector.get(d).doubleValue() + difference);
            }
        }

        return distanceVector;
    }

    /**
     * Get the number of difference vectors to create.
     * @return The {@code ControlParameter} describing the number of difference vectors.
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
    @Override
    public ControlParameter getScaleParameter() {
        return scaleParameter;
    }

    /**
     * Set the scale parameter for the creation strategy.
     * @param scaleParameter The value to set.
     */
    public void setScaleControlParameter(ControlParameter scaleParameter) {
        this.scaleParameter = scaleParameter;
    }

    @Override
    public void setScaleParameter(double scaleParameterValue) {
        this.scaleParameter = new AdaptableControlParameter(scaleParameterValue);
    }
}
