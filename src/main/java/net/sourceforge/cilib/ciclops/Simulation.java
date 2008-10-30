/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.ciclops;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.AlgorithmEvent;
import net.sourceforge.cilib.algorithm.AlgorithmListener;
import net.sourceforge.cilib.measurement.Measurement;

/**
 * @author Edwin Peer
 */
public class Simulation implements AlgorithmListener {
	private static final long serialVersionUID = 3273290641707115790L;
	
	private int resolution;
	private Algorithm algorithm;
	private Collection<Measurement> measurements;
	private List<MeasurementCollector> sample;

	public Simulation() {
		resolution = 100;
		algorithm = null;
		measurements = new LinkedList<Measurement>();
	}

	public Simulation getClone() {
		return null;
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
	public Collection<Measurement> getMeasurements() {
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
			Iterator<Measurement> i = measurements.iterator();
			for (int index = 0; i.hasNext(); ++index) {
				Measurement measurement = i.next();
				sample.get(index).serialiseValue(measurement.getValue(e.getSource()));
			}
		}
	}

	public void algorithmStarted(AlgorithmEvent e) {
		sample = new ArrayList<MeasurementCollector>(measurements.size());
		Iterator<Measurement> i = measurements.iterator();
		while (i.hasNext()) {
			Measurement measurement = i.next();
			if (measurement instanceof AlgorithmListener) {
				algorithm.addAlgorithmListener((AlgorithmListener) measurement);
			}
			MeasurementCollector mc = new MeasurementCollector(measurement);
			mc.serialiseValue(measurement.getValue(e.getSource()));
			sample.add(mc);
		}
	}

	public void algorithmFinished(AlgorithmEvent e) {
	}

	public void algorithmTerminated(AlgorithmEvent e) {
	}

	public MeasurementCollector[] getSampleData() {
		return sample.toArray(new MeasurementCollector[sample.size()]);
	}
}
