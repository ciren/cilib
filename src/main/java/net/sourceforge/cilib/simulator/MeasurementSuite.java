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
package net.sourceforge.cilib.simulator;

import net.sourceforge.cilib.measurement.MeasurementStateManager;
import java.io.Serializable;
import java.util.ArrayList;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.measurement.StateAwareMeasurement;
import net.sourceforge.cilib.type.types.Type;

/**
 * The <code>MeasurementSuite</code> is essentially a collection of measurements.
 *
 * @see net.sourceforge.Measurement.Measurement
 *
 * @author  Edwin Peer
 */
public class MeasurementSuite implements Serializable {
    private static final long serialVersionUID = 8021290553229945841L;

    private String file;
    private int samples;
    private int resolution;
    private ArrayList<Measurement> measurements;
    private SynchronizedOutputBuffer buffer;
    private MeasurementStateManager measurementStateManager;

    /** Creates a new instance of MeasurementSuite. */
    public MeasurementSuite() {
        measurements = new ArrayList<Measurement>();
        file = "results.txt";
        samples = 30;
        resolution = 1;
        measurementStateManager = new MeasurementStateManager();
    }

    /**
     * Initialise the require output buffers for the {@linkplain MeasurementSuite}.
     */
    public void initialise() {
        buffer = new SynchronizedOutputBuffer(file, measurements.size(), samples);
        buffer.write("# 0 - Iterations");
        for (Measurement measurement : measurements) {
            buffer.writeDescription(measurement);
        }
    }

    /**
     * Sets the output file to record the measurements in.
     *
     * @param file The name of the output file.
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * Get the current specified filename.
     * @return The current file name.
     */
    public String getFile() {
        return this.file;
    }

    /**
     * Sets the number of samples to take for each measurement. Each sample results
     * in the experiment being performed again.
     *
     * @param samples The number of samples.
     */
    public void setSamples(int samples) {
        this.samples = samples;
    }

    /**
     * Accessor for the number of samples to take for each measurement.
     *
     * @return The number of samples.
     */
    public int getSamples() {
        return samples;
    }

    /**
     * Sets the resolution of the results. The resolution determines how offen
     * results are logged to file. If the resolution is 10 then results are
     * logged every 10 iterations.
     *
     * @param resolution The result resolution.
     */
    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    /**
     * Accessor for the resolution of the results.
     *
     * @return The result resolution.
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * Get the current {@linkplain SynchronizedOutputBuffer}.
     * @return The current buffer.
     */
    public SynchronizedOutputBuffer getOutputBuffer() {
        return buffer;
    }

    public void setOutputBuffer(SynchronizedOutputBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * Adds a measurement to the suite.
     *
     * @param measurement The measurement to be added.
     */
    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
    }

    /**
     * Measure the provided {@linkplain Algorithm}. All the current measurements
     * that are defined for the {@linkplain MeasurementSuite} are applied to the
     * {@linkplain Algorithm}. Any measurements that are
     * {@linkplain StateAwareMeasurement state aware} instances will
     * automatically have their internal state saved and restored
     * as measurements are taken on the current {@linkplain Algorithm}.
     * @param algorithm The {@linkplain Algorithm} to measure.
     */
    public void measure(Algorithm algorithm) {
        for (Measurement measurement : measurements) {
            Type value = null;

            if (measurement instanceof StateAwareMeasurement) {
                StateAwareMeasurement stateAwareMeasurement = (StateAwareMeasurement) measurement;
                measurementStateManager.setState(algorithm, stateAwareMeasurement);
                value = measurement.getValue(algorithm);
                measurementStateManager.getState(algorithm, stateAwareMeasurement);
            }
            else
                value = measurement.getValue(algorithm);

            buffer.writeMeasuredValue(value, algorithm, measurement);
        }
    }

}
