/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import java.util.ArrayList;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @param <E> The entity type.
 */
public class RandomBoundedInitialisationStrategy<E extends Entity> implements
        InitialisationStrategy<E> {

    private static final long serialVersionUID = -7926839076670354209L;
    protected ControlParameter lowerBound;
    protected ControlParameter upperBound;
    private ProbabilityDistributionFunction random;
    protected ArrayList<ControlParameter[]> boundsPerDimension;

    public RandomBoundedInitialisationStrategy() {
        this.lowerBound = ConstantControlParameter.of(0.1);
        this.upperBound = ConstantControlParameter.of(0.1);
        this.random = new UniformDistribution();
        boundsPerDimension = new ArrayList<ControlParameter[]>();
    }

    public RandomBoundedInitialisationStrategy(RandomBoundedInitialisationStrategy copy) {
        this.lowerBound = copy.lowerBound;
        this.upperBound = copy.upperBound;
        this.random = copy.random;
        boundsPerDimension = copy.boundsPerDimension;
    }

    @Override
    public RandomBoundedInitialisationStrategy getClone() {
        return new RandomBoundedInitialisationStrategy(this);
    }

    @Override
    public void initialise(Enum<?> key, E entity) {
        Type type = entity.getProperties().get(key);
        Vector velocity = (Vector) type;

        for (int i = 0; i < velocity.size(); i++) {
            changeBoundsForNextDimension(i);
            velocity.setReal(i, random.getRandomNumber(lowerBound.getParameter(), upperBound.getParameter()));
        }
    }

    /*
     * Sets the upper and lower bounds being used to the appropriate ones for the current dimension
     * @param index The dimension
     */
    private void changeBoundsForNextDimension(int index) {
        if(boundsPerDimension.size() > 0) {
            lowerBound = boundsPerDimension.get(index)[0];
            upperBound = boundsPerDimension.get(index)[1];
        }

        //do nothing otherwise
    }

    /*
     * Set the list containing the upper and lower bounds to be used fro each dimension
     * @param bounds The arraylist containing the bounds
     */
    public void setBoundsPerDimension(ArrayList<ControlParameter[]> bounds) {
        boundsPerDimension = bounds;
    }

    public ControlParameter getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(ControlParameter lowerBound) {
        this.lowerBound = lowerBound;
    }

    public ControlParameter getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(ControlParameter upperBound) {
        this.upperBound = upperBound;
    }

}
