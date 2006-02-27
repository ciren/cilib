/*
 * SynchronizedOutputBuffer.java
 *
 * Created on February 6, 2003, 8:07 PM
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
import java.lang.*;
import java.lang.reflect.*;

import net.sourceforge.cilib.Algorithm.*;
import net.sourceforge.cilib.Measurement.*;

/**
 *
 * @author  espeer
 */
public class SynchronizedOutputBuffer {
    
    /** Creates a new instance of SynchronizedOutputBuffer */
    public SynchronizedOutputBuffer(String file, int measurements, int samples) {
        try {
            writer = new BufferedWriter(new FileWriter(file));
        }
        catch (IOException ex) {
            throw new SimulationException(ex.toString());
        }
        this.measurements = measurements;
        this.samples = samples;
        nextSampleId = 0;
        nextMeasurementId = 0;
        measurementMap = new HashMap();
        sampleMap = new HashMap();
        lineMap = new HashMap();
    }
    
    public synchronized void writeDescription(Measurement measurement) {
        int column = getMeasurementId(measurement) * samples + 1;
        String description = measurement.getClass().getName();
        description = description.substring(description.lastIndexOf(".") + 1);
        for (int i = 0; i < samples; ++i) {
            writeLine("# " + String.valueOf(column + i) + " - " + description + " (" + String.valueOf(i) + ")");
        }
    }
    
    public synchronized void writeMeasuredValue(Number value, Algorithm algorithm, Measurement measurement) {
        Integer key = new Integer(algorithm.getIterations());
        Line line;
        if (lineMap.containsKey(key)) {
            line = (Line) lineMap.get(key);
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
        
    
    public synchronized void write(String string) {
        writeLine(string);
    }
    
    public synchronized void flush() {
        try {
            writer.flush();
        }
        catch (IOException ex) {
            throw new SimulationException(ex.toString());
        }
    }
    
    public synchronized void close() {
        try {
            writer.close();
        }
        catch (IOException ex) {
            throw new SimulationException(ex.toString());
        }
    }
    
    private void writeLine(String string) {
        try {
            writer.write(string);
            writer.newLine();
        }
        catch (IOException ex) {
            throw new SimulationException(ex.toString());
        }        
    }
    
    private int getMeasurementId(Measurement measurement) {
        if (measurementMap.containsKey(measurement)) {
            return ((Integer) measurementMap.get(measurement)).intValue();
        }
        else {
            measurementMap.put(measurement, new Integer(nextMeasurementId));
            return nextMeasurementId++;
        }
    }
    
    private int getSampleId(Algorithm algorithm) {
        if (sampleMap.containsKey(algorithm)) {
            return ((Integer) sampleMap.get(algorithm)).intValue();
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
    private HashMap sampleMap;
    private HashMap measurementMap;
    private HashMap lineMap;
    private BufferedWriter writer;
}
