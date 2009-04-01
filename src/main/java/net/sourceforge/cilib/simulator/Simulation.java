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
package net.sourceforge.cilib.simulator;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.AlgorithmEvent;
import net.sourceforge.cilib.algorithm.AlgorithmFactory;
import net.sourceforge.cilib.algorithm.AlgorithmListener;
import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.ProblemFactory;

/**
 * <p>
 * This class represents a single simulation experiment. The experiment is repeated based on the
 * number of samples that the measurement suite requires. In this implementation each experiment is
 * run in its own thread. Thus, each experiment is run in parallel for a given simulation.
 * </p>
 * <p>
 * The simulation executes a given algorithm on the given problem. Factories are utilised so that
 * the simulation can create as many alogirthms and problems as it needs to run many experiments.
 * </p>
 * <p>
 * The primary purpose of running simulations is to measure the performance of the given algorithm
 * on a given problem. For that reason, a simulation accepts a measurement suite which it uses to
 * record the performace.
 * </p>
 * @author Edwin Peer
 */
public class Simulation extends Thread implements AlgorithmListener {
    private static final long serialVersionUID = 8987667794610802908L;
    private MeasurementSuite measurementSuite;
    private Algorithm[] algorithms;
    private Thread[] threads;
    private Vector<ProgressListener> progressListeners;
    private Hashtable<Algorithm, Double> progress;

    public Simulation getClone() {
        return null;
    }

    /**
     * Creates a new instance of Simulation given an algorithm factory, a problem factory and a
     * measurement suite. {@see net.sourceforge.cilib.XMLObjectFactory}
     * @param algorithmFactory The algorithm factory.
     * @param problemFactory The problem factory.
     * @param measurementSuite The measurement suite.
     */
    public Simulation(AlgorithmFactory algorithmFactory, ProblemFactory problemFactory, MeasurementSuite measurementSuite) {

        measurementSuite.initialise();
        this.measurementSuite = measurementSuite;
        progressListeners = new Vector<ProgressListener>();
        progress = new Hashtable<Algorithm, Double>();

        algorithms = new Algorithm[measurementSuite.getSamples()];
        threads = new Thread[measurementSuite.getSamples()];
        for (int i = 0; i < measurementSuite.getSamples(); ++i) {
            algorithms[i] = algorithmFactory.newAlgorithm();
            threads[i] = new Thread(algorithms[i]);
            algorithms[i].addAlgorithmListener(this);
            Problem problem = problemFactory.newProblem();
            try {
                Class<? extends Object> current = problem.getClass();
                // System.out.println(current.getName());
                while (!current.getSuperclass().equals(Object.class)) {
                    current = current.getSuperclass();
                    // System.out.println(current.getName());
                }
                String type = current.getInterfaces()[0].getName();
                // System.out.println("type: " + type);
                Class<?> [] parameters = new Class[1];
                parameters[0] = Class.forName(type);
                // System.out.println("parameters: " + parameters[0].getName());
                String setMethodName = "set" + type.substring(type.lastIndexOf(".") + 1);
                // System.out.println("setMethodName: " + setMethodName);
                Method setProblemMethod = algorithms[i].getClass().getMethod(setMethodName, parameters);
                // System.out.println("setProblemMethod: " + setProblemMethod.getName());
                setProblemMethod.invoke(algorithms[i], new Object[] {problem});
            }
            catch (Exception ex) {
                throw new InitialisationException(algorithms[i].getClass().getName() + " does not support problems of type " + problem.getClass().getName());
            }
            progress.put(algorithms[i], new Double(0));
        }
    }

    public void initialise() {
        for (Algorithm algorithm : algorithms)
            algorithm.initialise();
    }

    /**
     * Executes all the experiments for this simulation.
     */
    public void run() {
        for (int i = 0; i < measurementSuite.getSamples(); ++i) {
            threads[i].start();
        }
        for (int i = 0; i < measurementSuite.getSamples(); ++i) {
            try {
                threads[i].join();
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        measurementSuite.getOutputBuffer().close();
        measurementSuite = null;
        algorithms = null;
        progress = null;
        progressListeners = null;
        threads = null;
    }

    /**
     * Terminates all the experiments.
     */
    public void terminate() {
        for (int i = 0; i < measurementSuite.getSamples(); ++i) {
            algorithms[i].terminate();
        }
    }

    /**
     * Adds a listener for progress events. A progress is fired periodically based on the resolution
     * of the measurements. {@see ProgressEvent} {@see ProgressListener}
     * @param The event listener
     */
    public void addProgressListener(ProgressListener listener) {
        progressListeners.add(listener);
    }

    /**
     * Removes a listener for progress events.
     * @param The event listener
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

    public void algorithmStarted(AlgorithmEvent e) {
    }

    public void algorithmFinished(AlgorithmEvent e) {
        measurementSuite.measure(e.getSource());
        progress.put(e.getSource(), new Double(e.getSource().getPercentageComplete()));
        notifyProgress();
    }

    public void algorithmTerminated(AlgorithmEvent e) {
    }

    public void iterationCompleted(AlgorithmEvent e) {
        if (e.getSource().getIterations() % measurementSuite.getResolution() == 0) {
            measurementSuite.measure(e.getSource());
            progress.put(e.getSource(), new Double(e.getSource().getPercentageComplete()));
            notifyProgress();
        }
    }
}
