/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sourceforge.cilib.algorithm.Algorithm;

/**
 *
 */
public class MeasurementStateManager {
    private ConcurrentHashMap<Algorithm, ConcurrentHashMap<Measurement, MeasurementMemento>> algorithmData;

    public MeasurementStateManager() {
        this.algorithmData = new ConcurrentHashMap<Algorithm, ConcurrentHashMap<Measurement, MeasurementMemento>>();
    }

    /**
     * Restore the current state of the measurement which is assigned to the
     * provided algorithm instance.
     * @param algorithm The algorithm for which this measurement's state needs
     *        to be applied.
     * @param measurement The measurement to which it's state needs to be
     *        restored.
     */
    public void setState(Algorithm algorithm, StateAwareMeasurement measurement) {
        if (!measurement.isStateAware()) return;

        try {
            ConcurrentHashMap<Measurement, MeasurementMemento> measurementData = this.algorithmData.get(algorithm);
            if (measurementData == null) {
                measurementData = new ConcurrentHashMap<Measurement, MeasurementMemento>();
                algorithmData.put(algorithm, measurementData);
            }

            MeasurementMemento memento = measurementData.get(measurement);
            if (memento == null) {// The state has not been saved yet.
                memento = new MeasurementMemento();
                measurementData.put(measurement, memento);
            }

            measurement.setState(memento);
        }
        catch (IOException io) {
//            logger.error("Error restoring state of measurement [{}] for algorithm [{}]", measurement, algorithm);
//            logger.error("Execption: ", io);
        }
        catch (ClassNotFoundException c) {
//            logger.error("Class cannot be found!", c);
        }
    }

    /**
     * Extract the state of the provided {@code measurement} based on the current
     * provided {@code algorithm}.
     * @param algorithm The algorithm to which the measurement state is associated.
     * @param measurement The measurement which needs to have it's state extracted.
     */
    public MeasurementMemento getState(Algorithm algorithm, StateAwareMeasurement measurement) {
        if (!measurement.isStateAware()) return null;

        if (!algorithmData.containsKey(algorithm))
            this.algorithmData.putIfAbsent(algorithm, new ConcurrentHashMap<Measurement, MeasurementMemento>());

        Map<Measurement, MeasurementMemento> data = this.algorithmData.get(algorithm);

        try {
            MeasurementMemento memento = measurement.getState();
            data.put(measurement, memento);
            return memento;
        }
        catch (IOException io) {
//           logger.error("Error persisting state of measurement {} for algorithm {}", measurement, algorithm);
//           logger.error("Execption: ", io);
        }

        return null;
    }

}
