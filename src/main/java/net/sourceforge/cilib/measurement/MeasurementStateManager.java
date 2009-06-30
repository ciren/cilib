/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.measurement;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.sourceforge.cilib.algorithm.Algorithm;

/**
 *
 * @author gpampara
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
