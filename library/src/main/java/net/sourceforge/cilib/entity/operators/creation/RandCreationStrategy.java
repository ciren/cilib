/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.creation;

import fj.P1;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.SettableControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.Selection;
import net.sourceforge.cilib.util.selection.arrangement.RandomArrangement;

public class RandCreationStrategy implements CreationStrategy {

    private static final long serialVersionUID = 930740770470361009L;
    protected SettableControlParameter scaleParameter;
    protected SettableControlParameter numberOfDifferenceVectors;

    /**
     * Create a new instance of {@code CurrentToRandCreationStrategy}.
     */
    public RandCreationStrategy() {
        this.scaleParameter = ConstantControlParameter.of(0.5);
        this.numberOfDifferenceVectors = ConstantControlParameter.of(2);
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
    public <T extends Entity> T create(T targetEntity, T current, Topology<T> topology) {
        int number = Double.valueOf(this.numberOfDifferenceVectors.getParameter()).intValue();
        List<T> participants = Selection.copyOf(topology)
                .exclude(targetEntity, current)
                .orderBy(new RandomArrangement())
                .select(Samples.first(number).unique());
        Vector differenceVector = determineDistanceVector(participants);

        Vector targetVector = (Vector) targetEntity.getCandidateSolution();
        Vector trialVector = targetVector.plus(differenceVector.multiply(new P1<Number>() {
            @Override
            public Number _1() {
                return scaleParameter.getParameter();
            }
        }));

        T trialEntity = (T) current.getClone();
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
    protected <T extends Entity> Vector determineDistanceVector(List<T> participants) {
        Vector distanceVector = Vector.fill(0.0, participants.get(0).getCandidateSolution().size());
        Iterator<T> iterator = participants.iterator();

        while (iterator.hasNext()) {
            Vector first = (Vector) iterator.next().getCandidateSolution();
            Vector second = (Vector) iterator.next().getCandidateSolution();

            Vector difference = first.subtract(second);
            distanceVector = distanceVector.plus(difference);
        }

        return distanceVector;
    }

    /**
     * Get the number of difference vectors to create.
     * @return The {@code ControlParameter} describing the number of difference vectors.
     */
    public SettableControlParameter getNumberOfDifferenceVectors() {
        return numberOfDifferenceVectors;
    }

    /**
     * Set the number of difference vectors to create.
     * @param numberOfDifferenceVectors The value to set.
     */
    public void setNumberOfDifferenceVectors(SettableControlParameter numberOfDifferenceVectors) {
        this.numberOfDifferenceVectors = numberOfDifferenceVectors;
    }

    /**
     * Get the current scale parameter, used within the creation.
     * @return The {@code ControlParameter} representing the scale parameter.
     */
    public SettableControlParameter getScaleParameter() {
        return scaleParameter;
    }

    /**
     * Set the scale parameter for the creation strategy.
     * @param scaleParameter The value to set.
     */
    public void setScaleParameter(SettableControlParameter scaleParameter) {
        this.scaleParameter = scaleParameter;
    }
    
    public void setScaleParameter(double scaleParameterValue) {
        this.scaleParameter.setParameter(scaleParameterValue);
    }

}
