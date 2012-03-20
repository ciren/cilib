/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.simulator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;

/**
 *
 */
class SynchronizedOutputBuffer {

    /** Creates a new instance of SynchronizedOutputBuffer. */
    SynchronizedOutputBuffer(String file, int measurements, int samples) {
        try {
            File pathedFile = new File(file);
            String parentPath = pathedFile.getParent();
            if (parentPath != null) {
                File parent = new File(parentPath);
                if (!parent.exists()) {
                    if (parent.mkdirs())
                        System.out.println("INFO: Created directory structure for output: " + parent.toString());
                    else
                        System.out.println("ERROR: Failed to create directory structure: " + parent.toString());
                }
            }
            writer = new BufferedWriter(new FileWriter(file));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex.toString());
        }
        this.measurements = measurements;
        this.samples = samples;
        nextSampleId = 0;
        nextMeasurementId = 0;
        measurementMap = new HashMap<Measurement, Integer>();
        sampleMap = new HashMap<Algorithm, Integer>();
        lineMap = new HashMap<Integer, Line>();
    }

    synchronized void writeDescription(Measurement measurement) {
        int column = getMeasurementId(measurement) * samples + 1;
        String description = measurement.getClass().getName();
        description = description.substring(description.lastIndexOf(".") + 1);
        for (int i = 0; i < samples; ++i) {
            writeLine("# " + String.valueOf(column + i) + " - " + description + " (" + String.valueOf(i) + ")");
        }
    }

    synchronized void writeMeasuredValue(Object value, Algorithm algorithm, Measurement measurement) {
        Integer key = new Integer(algorithm.getIterations());
        Line line;
        if (lineMap.containsKey(key)) {
            line = lineMap.get(key);
        }
        else {
            line = new Line(samples * measurements);
            lineMap.put(key, line);
        }
        int sampleId = getSampleId(algorithm);
        int measurementId = getMeasurementId(measurement);
        line.setElement(measurementId * samples + sampleId, value);
        if (line.isFull()) {
            writeLine(String.valueOf(key.intValue()) + " " + line.toString());
            lineMap.remove(key);
        }
    }


    synchronized void write(String string) {
        writeLine(string);
    }

    synchronized void flush() {
        try {
            writer.flush();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    synchronized void close() {
        try {
            writer.close();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    protected void writeLine(String string) {
        try {
            writer.write(string);
            writer.newLine();
            writer.flush();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    protected int getMeasurementId(Measurement measurement) {
        if (measurementMap.containsKey(measurement)) {
            return (measurementMap.get(measurement)).intValue();
        }
        else {
            measurementMap.put(measurement, new Integer(nextMeasurementId));
            return nextMeasurementId++;
        }
    }

    protected int getSampleId(Algorithm algorithm) {
        if (sampleMap.containsKey(algorithm)) {
            return (sampleMap.get(algorithm)).intValue();
        }
        else {
            sampleMap.put(algorithm, new Integer(nextSampleId));
            return nextSampleId++;
        }
    }

    private int samples;
    private int measurements;
    private int nextSampleId;
    private int nextMeasurementId;
    private HashMap<Algorithm, Integer> sampleMap;
    private HashMap<Measurement, Integer> measurementMap;
    private HashMap<Integer, Line> lineMap;
    private BufferedWriter writer;
}
