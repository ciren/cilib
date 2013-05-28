/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm;

import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Type;

/**
 * This listener takes measurements whenever the target algorithm is started or
 * the target algorithm completes an iteration. The resulting measurement can be
 * retrieved afterwards.
 */
public class MeasuringListener implements AlgorithmListener {

    private Measurement measurement;
    private Type lastMeasurement;

    public MeasuringListener() {}

    public MeasuringListener(MeasuringListener rhs) {
        if (rhs.measurement != null)
            measurement = rhs.measurement.getClone();

        if (rhs.lastMeasurement != null)
            lastMeasurement = rhs.lastMeasurement.getClone();
    }
    
    /**
     * {@inheritDoc}
     */
    public MeasuringListener getClone() {
        return new MeasuringListener(this);
    }
    
    /**
     * {@inheritDoc}
     */
    public void algorithmStarted(AlgorithmEvent e) {
        lastMeasurement = measurement.getValue(e.getSource());
    }

    /**
     * This has no effect.
     */
    public void algorithmFinished(AlgorithmEvent e) {
        //do nothing
    }

    /**
     * {@inheritDoc}
     */
    public void iterationCompleted(AlgorithmEvent e) {
        lastMeasurement = measurement.getValue(e.getSource());
    }

    /**
     * Sets the Measurement that should be used for taking measurements.
     * @param measurement The measurement to set.
     */
    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    /**
     * Retrieves the most recent measurement taken.
     * @return The last measurement.
     */
    public Type getLastMeasurement() {
        return lastMeasurement;
    }
}
