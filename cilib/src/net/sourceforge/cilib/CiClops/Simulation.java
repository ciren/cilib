/*
 * Simulation.java
 * 
 * Created on Mar 1, 2004
 * 
 * Copyright (C) 2004 - CIRG@UP 
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
 * 
 */

package net.sourceforge.cilib.CiClops;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.Algorithm.AlgorithmEvent;
import net.sourceforge.cilib.Algorithm.AlgorithmListener;
import net.sourceforge.cilib.Domain.Component;
import net.sourceforge.cilib.Domain.Composite;
import net.sourceforge.cilib.Domain.Compound;
import net.sourceforge.cilib.Domain.Unknown;
import net.sourceforge.cilib.Measurement.Measurement;

/**
 * @author espeer
 */
public class Simulation implements AlgorithmListener {

    public Simulation() {
        resolution = 100;
        algorithm = null;
        measurements = new LinkedList();
        domain = new Unknown();
    }
    
    /**
     * @return Returns the algorithm.
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    /**
     * @param algorithm The algorithm to set.
     */
    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * @return Returns the resolution.
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * @param resolution The resolution to set.
     */
    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    /**
     * @return Returns the measurements.
     */
    public Collection getMeasurements() {
        return measurements;
    }
    
    public void addMeasurement(Measurement measurement) {
        measurements.add(measurement);
    }    
    

    public void initialise() {
        algorithm.initialise();
    }
    
    public void run() {
        algorithm.addAlgorithmListener(this);
    	algorithm.run();
    	algorithm.removeAlgorithmListener(this);
    }
    
    public void iterationCompleted(AlgorithmEvent e) {
        if (algorithm.isFinished() || algorithm.getIterations() % resolution == 0) {
        	Iterator i = measurements.iterator();
        	for (int index = 0; i.hasNext(); ++index) {
                Measurement measurement = (Measurement) i.next();
                ((List) sample.get(index)).add(measurement.getValue());
            }    
        }
    }
    
	public void algorithmStarted(AlgorithmEvent e) {
        sample = new ArrayList(measurements.size());
        Iterator i = measurements.iterator();
        while (i.hasNext()) {
            Measurement measurement = (Measurement) i.next();
            if (measurement instanceof AlgorithmListener) {
            	algorithm.addAlgorithmListener((AlgorithmListener) measurement);
            }
            List list = new LinkedList();
            list.add(measurement.getValue());
            sample.add(list);
        }
	}

	public void algorithmFinished(AlgorithmEvent e) {
		Iterator i = measurements.iterator();
		ArrayList components = new ArrayList(measurements.size());
		for (int index = 0; i.hasNext(); ++index) {
			Measurement measurement = (Measurement) i.next();
			components.add(new Compound( ((List) sample.get(index)).size(), new net.sourceforge.cilib.Domain.Measurement(measurement)));
		}
		domain = new Composite(components);
	}

	public void algorithmTerminated(AlgorithmEvent e) {
	}

	public String getDomain() {
		return domain.getRepresentation();
	}
	
	public byte[] getSample() {
		Object[] tmp = sample.toArray();
		for (int i = 0; i < tmp.length; ++i) {
			tmp[i] = ((List) tmp[i]).toArray();
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			domain.serialise(oos, tmp);
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return bos.toByteArray();
	}
	
    private int resolution;
    private Algorithm algorithm;
    private Collection measurements;
    private ArrayList sample;
	private Component domain;
	
}
