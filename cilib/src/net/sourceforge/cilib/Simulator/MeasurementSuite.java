/*
 * MeasurementSuite.java
 *
 * Created on February 5, 2003, 12:56 PM
 *
 * 
 * Copyright (C) 2003 - Edwin S. Peer 
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
 *   
 */

package net.sourceforge.cilib.Simulator;

import java.io.*;
import java.util.*;

import net.sourceforge.cilib.Algorithm.*;
import net.sourceforge.cilib.Measurement.*;

/**
 *
 * @author  espeer
 */
public class MeasurementSuite implements Serializable {
    
    /** Creates a new instance of MeasurementSuite */
    public MeasurementSuite() {
        measurements = new ArrayList();
        file = "results.txt";
        samples = 100;
        resolution = 1;
    }
    
    public void initialise() {
        buffer = new SynchronizedOutputBuffer(file, measurements.size(), samples);
        buffer.write("# 0 - Iterations");
        for (Iterator i = measurements.iterator(); i.hasNext(); ) {
            Measurement measurement = (Measurement) i.next();
            buffer.writeDescription(measurement);
        }
    }
    
    public void setFile(String file) {
        this.file = file;
    }
    
    public void setSamples(int samples) {
        this.samples = samples;
    }
    
    public int getSamples() {
        return samples;
    }
    
    public void setResolution(int resolution) {
        this.resolution = resolution;
    }
    
    public int getResolution() {
        return resolution;
    }
    
    public SynchronizedOutputBuffer getOutputBuffer() {
        return buffer;
    }
    
    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
    }
    
    public void measure(Algorithm algorithm) {
        for (Iterator i = measurements.iterator(); i.hasNext(); ) {
            Measurement measurement = (Measurement) i.next();
            Number value = measurement.getValue(algorithm);
            buffer.writeMeasuredValue(value, algorithm, measurement);
        }
    }
    
    private String file;
    private int samples;
    private int resolution;
    private ArrayList measurements;
    private SynchronizedOutputBuffer buffer;
}
