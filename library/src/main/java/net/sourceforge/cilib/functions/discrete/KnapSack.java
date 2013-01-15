/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.discrete;

import java.util.ArrayList;
import net.sourceforge.cilib.functions.DiscreteFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Perform the knapsack problem optimisation.
 *
 */
public class KnapSack implements DiscreteFunction {

    private static final long serialVersionUID = 79098409450300605L;
    private int capacity;
    private int numberOfObjects;
    private ArrayList<Double> weights;
    private ArrayList<Double> values;

    public KnapSack() {
        weights = new ArrayList<Double>();
        values = new ArrayList<Double>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer apply(Vector x) {
        double weight = 0.0;

        for (int i = 0; i < x.size(); i++) {
            int bitValue = x.booleanValueOf(i) ? 1 : 0;
            weight += bitValue * this.weights.get(i);
        }

        if (weight > capacity) {
            return Integer.MIN_VALUE; // This needs to be checked.
        }
        int profit = 0;

        for (int i = 0; i < x.size(); i++) {
            int bitValue = x.booleanValueOf(i) ? 1 : 0;
            profit += bitValue * this.values.get(i);
        }

        return profit;
        /*if (weights.size() == 0 && values.size() == 0) {
        randomInitialise();
        }*/
    }


    /*    private void randomInitialise() {
    Random random = new MersenneTwister();

    for (int i = 0; i < this.numberOfObjects; i++) {
    int number = 0;

    while (number == 0)
    number = random.nextInt(this.numberOfObjects);

    weights.add(i, Integer.valueOf(number).doubleValue());

    number = 0;
    while (number == 0)
    number = random.nextInt(this.numberOfObjects);

    values.add(i, Integer.valueOf(number).doubleValue());
    }
    }*/
    /**
     * @return Returns the capacity.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * @param capacity The capacity to set.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * @return Returns the numberOfObjects.
     */
    public int getNumberOfObjects() {
        return numberOfObjects;
    }

    /**
     * @param numberOfObjects The numberOfObjects to set.
     */
    public void setNumberOfObjects(int numberOfObjects) {
        this.numberOfObjects = numberOfObjects;
        this.weights.ensureCapacity(numberOfObjects);
        this.values.ensureCapacity(numberOfObjects);
    }

    /**
     * @return Returns the value.
     */
    public ArrayList<Double> getValue() {
        return values;
    }

    /**
     * @param value The value to set.
     */
    public void setValue(ArrayList<Double> value) {
        this.values = value;
    }

    /**
     * Helper method. This method enables in input of a comma-separated string,
     * containing the values for the different object types.
     *
     * @param valueString A comma separated string defining the values for
     *                    each selection bucket.
     */
    public void setValue(String valueString) {
        String[] parts = valueString.split(",");
        this.values.clear();

        for (int i = 0; i < parts.length; i++) {
            this.values.add(Double.valueOf(parts[i]));
        }
    }

    /**
     * @return Returns the weight.
     */
    public ArrayList<Double> getWeight() {
        return weights;
    }

    /**
     * @param weight The weight to set.
     */
    public void setWeight(ArrayList<Double> weight) {
        this.weights = weight;
    }

    /**
     * Helper method. This method enables in input of a comma-separated string,
     * containing the weight values for the different object types.
     *
     * @param weightString A comma separated string defining the weight value for
     *                     each selection bucket.
     */
    public void setWeight(String weightString) {
        String[] parts = weightString.split(",");
        this.weights.clear();

        for (int i = 0; i < parts.length; i++) {
            this.weights.add(Double.valueOf(parts[i]));
        }
    }
}
