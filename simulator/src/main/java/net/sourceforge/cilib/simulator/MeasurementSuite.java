/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.simulator;

import com.google.common.collect.Lists;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.sourceforge.cilib.measurement.MeasurementStateManager;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.measurement.StateAwareMeasurement;
import net.sourceforge.cilib.type.types.Type;

/**
 * The <code>MeasurementSuite</code> is essentially a collection of measurements.
 *
 * @see net.sourceforge.Measurement.Measurement
 *
 */
public class MeasurementSuite {

    private static final long serialVersionUID = 8021290553229945841L;
    private File file;
    private BufferedWriter writer;
    private int resolution;
    private List<Measurement<?>> measurements;
    private MeasurementStateManager measurementStateManager;

    /** Creates a new instance of MeasurementSuite. */
    public MeasurementSuite() {
        measurements = new ArrayList<Measurement<?>>();
        resolution = 1;
        measurementStateManager = new MeasurementStateManager();
    }

    /**
     * Initialise the require output buffers for the {@linkplain MeasurementSuite}.
     */
    public void initialise() {
        try {
            file = File.createTempFile("cilib_data", ".tmp");
            writer = new BufferedWriter(new FileWriter(file));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    /**
     * Sets the resolution of the results. The resolution determines how often
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
     * Adds a measurement to the suite.
     *
     * @param measurement The measurement to be added.
     */
    public void addMeasurement(Measurement<?> measurement) {
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
        Type[] tmp = new Type[measurements.size()];
        int index = 0;
        for (Measurement<?> measurement : measurements) {
            Type value = null;

            if (measurement instanceof StateAwareMeasurement<?>) {
                StateAwareMeasurement<?> stateAwareMeasurement = (StateAwareMeasurement<?>) measurement;
                measurementStateManager.setState(algorithm, stateAwareMeasurement);
                value = measurement.getValue(algorithm);
                measurementStateManager.getState(algorithm, stateAwareMeasurement);
            } else {
                value = measurement.getValue(algorithm);
            }

            tmp[index++] = value;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(algorithm.getIterations());
        for (Type t : tmp) {
            builder.append(" ").append(t);
        }

        try {
            writer.write(builder.toString());
            writer.newLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void add(Measurement<?> measurement) {
        measurements.add(measurement);
    }

    public void close() throws IOException {
        this.writer.flush();
        this.writer.close();
    }

    public List<String> getDescriptions() {
        List<String> result = Lists.newArrayList();
        for (Measurement<?> measurement : measurements) {
            result.add(measurement.getClass().getSimpleName());
        }
        return result;
    }
}
