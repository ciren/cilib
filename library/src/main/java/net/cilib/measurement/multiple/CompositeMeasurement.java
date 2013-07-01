/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.measurement.multiple;

import java.util.ArrayList;
import java.util.List;
import net.cilib.algorithm.Algorithm;
import net.cilib.algorithm.population.MultiPopulationBasedAlgorithm;
import net.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.cilib.measurement.Measurement;
import net.cilib.type.types.Type;
import net.cilib.type.types.container.TypeList;

/**
 * Measurement to perform measurements on a set of contained {@code Algorithm}
 * instances. This type of measurement is generally only defined for
 * {@link net.cilib.algorithm.population.MultiPopulationBasedAlgorithm}.
 */
public class CompositeMeasurement implements Measurement<TypeList> {

    private static final long serialVersionUID = -7109719897119621328L;
    private List<Measurement<? extends Type>> measurements;

    /**
     * Create a new instance with zero measurements.
     */
    public CompositeMeasurement() {
        this.measurements = new ArrayList<Measurement<? extends Type>>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompositeMeasurement getClone() {
        CompositeMeasurement newCM = new CompositeMeasurement();

        for(Measurement<? extends Type> m : this.measurements) {
            newCM.addMeasurement(m.getClone());
        }

        return newCM;
    }

    /**
     * Get the measurement values for all sub-algorithms.
     * @param algorithm The top level algorithm
     * @return The values of measurements applied to all contained algorithms.
     */
    @Override
    public TypeList getValue(Algorithm algorithm) {
        TypeList vector = new TypeList();

        MultiPopulationBasedAlgorithm multi = (MultiPopulationBasedAlgorithm) algorithm;

        for (SinglePopulationBasedAlgorithm single : multi.getPopulations()) {
            for (Measurement<? extends Type> measurement : measurements) {
                vector.add(measurement.getValue(single));
            }
        }

        return vector;
    }

    /**
     * Add a measurement to the composite for evaluation on the sub-algorithms.
     * @param measurement The measurement to add.
     */
    public void addMeasurement(Measurement<? extends Type> measurement) {
        this.measurements.add(measurement);
    }
}
