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
 * the target algorithm completes a certain interval of iteration. A measurement
 * always taken after the last iteration, regardless of resolution. The resulting
 * measurement can be retrieved afterwards.
 */
public class MeasuringListener implements AlgorithmListener {

    private Measurement measurement;
    private Type lastMeasurement;
    private int resolution;

    public MeasuringListener() {
        resolution = 1;
    }

    public MeasuringListener(MeasuringListener rhs) {
        resolution = rhs.resolution;
        
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
     * {@inheritDoc}
     */
    public void algorithmFinished(AlgorithmEvent e) {
        if (e.getSource().getIterations() % resolution != 0)
            lastMeasurement = measurement.getValue(e.getSource());
    }

    /**
     * {@inheritDoc}
     */
    public void iterationCompleted(AlgorithmEvent e) {
        if (e.getSource().getIterations() % resolution == 0)
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

    /**
     * Set the interval at which measurements should be taken.
     * Default value is 1.
     * @param resolution The resolution to set.
     */
    public void setResolution(int resolution) {
        this.resolution = resolution;
    }
}
