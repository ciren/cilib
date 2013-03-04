/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;

/**
 * This measurement functions as a wrapper for Measurement<Real>. It collects
 * the wrapped measurement at each iteration of the algorithm. It then returns
 * the average of the measurements taken so far.
 *
 * This wrapper ensures that new measurements are only taken when the algorithm
 * has performed a new iteration. However it does not ensure that measurements
 * are taken at each iteration; it only takes new measurements when it is
 * called. This implies that the measurement resolution must be set to 1 for it
 * to function correctly.
 */
public class CollectiveMeanOfMeasurement implements Measurement<Real> {

    private Measurement<Real> measurement;
    private ArrayList<Real> results;
    private int iterations;

    public CollectiveMeanOfMeasurement() {
        measurement = new Fitness();
        results = new ArrayList<Real>();
        iterations = -1;
    }

    public CollectiveMeanOfMeasurement(CollectiveMeanOfMeasurement rhs) {
        measurement = rhs.measurement.getClone();
        iterations = rhs.iterations;
        results = new ArrayList<Real>();
        for (Real element : rhs.results) {
            results.add(element.getClone());
        }
    }

    @Override
    public CollectiveMeanOfMeasurement getClone() {
        return new CollectiveMeanOfMeasurement(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        //ensure that new measurements are only taken when new iterations occurred
        if (iterations < algorithm.getIterations()) {
            iterations = algorithm.getIterations();
            results.add(measurement.getValue(algorithm));
        }

        double total = 0;
        for (Real result : results) {
            total += result.doubleValue();
        }

        return Real.valueOf(total / results.size());
    }

    public Measurement<Real> getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement<Real> measurement) {
        this.measurement = measurement;
    }
}
