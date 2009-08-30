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

import java.util.HashMap;
import java.util.Vector;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.AlgorithmFactory;
import net.sourceforge.cilib.problem.ProblemFactory;

/**
 * <p>
 * This class represents a single simulation experiment. The experiment is repeated based on the
 * number of samples that the measurement suite requires. In this implementation each experiment is
 * execute in its own thread. Thus, each experiment is execute in parallel for a given simulation.
 * </p>
 * <p>
 * The simulation executes a given algorithm on the given problem. Factories are utilised so that
 * the simulation can create as many alogirthms and problems as it needs to execute many experiments.
 * </p>
 * <p>
 * The primary purpose of running simulations is to measure the performance of the given algorithm
 * on a given problem. For that reason, a simulation accepts a measurement suite which it uses to
 * record the performace.
 * </p>
 * @author Edwin Peer
 */
public class Simulator {
    private static final long serialVersionUID = 8987667794610802908L;

    private MeasurementSuite measurementSuite;
    private Simulation[] simulations;
    private Vector<ProgressListener> progressListeners;
    private HashMap<Simulation, Double> progress;

    private final AlgorithmFactory algorithmFactory;
    private final ProblemFactory problemFactory;

    /**
     * Creates a new instance of Simulator given an algorithm factory, a problem factory and a
     * measurement suite. {@see net.sourceforge.cilib.XMLObjectFactory}
     * @param algorithmFactory The algorithm factory.
     * @param problemFactory The problem factory.
     * @param measurementSuite The measurement suite.
     */
    public Simulator(AlgorithmFactory algorithmFactory, ProblemFactory problemFactory, MeasurementSuite measurementSuite) {
        measurementSuite.initialise();
        this.measurementSuite = measurementSuite;
        progressListeners = new Vector<ProgressListener>();
        progress = new HashMap<Simulation, Double>();

        simulations = new Simulation[measurementSuite.getSamples()];

        this.algorithmFactory = algorithmFactory;
        this.problemFactory = problemFactory;
    }

    /**
     * Executes all the experiments for this simulation.
     */
    public void execute() {
        for (int i = 0; i < measurementSuite.getSamples(); ++i) {
            simulations[i] = new Simulation(this, algorithmFactory.newAlgorithm(), problemFactory.newProblem());
            progress.put(simulations[i], 0.0);
        }

        for (int i = 0; i < measurementSuite.getSamples(); ++i) {
            simulations[i].start();
        }
        for (int i = 0; i < measurementSuite.getSamples(); ++i) {
            try {
                simulations[i].join();
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        measurementSuite.getOutputBuffer().close();
        measurementSuite = null;
        simulations = null;
        progress = null;
        progressListeners = null;
    }

    /**
     * Terminates all the experiments.
     */
    public void terminate() {
        for (int i = 0; i < measurementSuite.getSamples(); ++i) {
            simulations[i].terminate();
        }
    }

    /**
     * Adds a listener for progress events. A progress is fired periodically based on the resolution
     * of the measurements. {@see ProgressEvent} {@see ProgressListener}
     * @param listener The event listener
     */
    public void addProgressListener(ProgressListener listener) {
        progressListeners.add(listener);
    }

    /**
     * Removes a listener for progress events.
     * @param listener The event listener
     */
    public void removeProgressListener(ProgressListener listener) {
        progressListeners.remove(listener);
    }

    private synchronized void notifyProgress() {
        double ave = 0;
        for (Double tmp : progress.values()) {
            ave += tmp.doubleValue();
        }

        ave /= progress.size();

        for (ProgressListener listener : progressListeners) {
            listener.handleProgressEvent(new ProgressEvent(ave));
        }
    }

    /**
     * Indicate that the provided {@code Simulation} has completed.
     * @param simulation The {@code simulation} which has completed.
     */
    void simulationFinished(Simulation simulation) {
        measurementSuite.measure(simulation.getAlgorithm());
        progress.put(simulation, new Double(((AbstractAlgorithm) simulation.getAlgorithm()).getPercentageComplete()));
        notifyProgress();
    }

    /**
     * Indicate that the provided {@code Simulation} has completed an iteration.
     * @param simulation The {@code Simulation} that has completed an iteration.
     */
    void simulationIterationCompleted(Simulation simulation) {
        Algorithm algorithm = simulation.getAlgorithm();
        if (algorithm.getIterations() % measurementSuite.getResolution() == 0) {
            measurementSuite.measure(simulation.getAlgorithm());
            progress.put(simulation, new Double(((AbstractAlgorithm)algorithm).getPercentageComplete()));
            notifyProgress();
        }
    }
}
