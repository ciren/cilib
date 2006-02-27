/*
 * Simulation.java
 *
 * Created on February 5, 2003, 10:13 AM
 *
 * 
 * Copyright (C) 2003, 2004 - CIRG@UP 
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

package net.sourceforge.cilib.Simulator;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import net.sourceforge.cilib.Algorithm.Algorithm;
import net.sourceforge.cilib.Algorithm.AlgorithmEvent;
import net.sourceforge.cilib.Algorithm.AlgorithmFactory;
import net.sourceforge.cilib.Algorithm.CompleteEventListener;
import net.sourceforge.cilib.Algorithm.InitialisationException;
import net.sourceforge.cilib.Algorithm.IterationEventListener;
import net.sourceforge.cilib.Problem.Problem;
import net.sourceforge.cilib.Problem.ProblemFactory;

/**
 * <p>
 * This class represents a single simulation experiment. The experiment is repeated based
 * on the number of samples that the measurement suite requires. In this implementation 
 * each experiment is run in its own thread. Thus, each experiment is run in parallel for
 * a given simulation.
 * </p>
 * <p>
 * The simulation executes a given algorithm on the given problem. Factories are utilised so
 * that the simulation can create as many alogirthms and problems as it needs to run many experiments.
 * </p>
 * <p>
 * The primary purpose of running simulations is to measure the performance of the given algorithm on 
 * a given problem. For that reason, a simulation accepts a measurement suite which it uses to
 * record the performace.
 * </p>
 *
 * @author  espeer
 */
public class Simulation extends Thread implements Serializable, IterationEventListener, CompleteEventListener {
    
    /** 
     * Creates a new instance of Simulation given an algorithm factory, a problem factory and
     * a measurement suite.
     *
     * {@see net.sourceforge.cilib.XMLObjectFactory}
     *
     * @param algorithmFactory The algorithm factory.
     * @param problemFactory The problem factory.
     * @param measurementSuite The measurement suite.
     */
    public Simulation(AlgorithmFactory algorithmFactory, 
                      ProblemFactory problemFactory,
                      MeasurementSuite measurementSuite) {
        
        measurementSuite.initialise();
        this.measurementSuite = measurementSuite;
        progressListeners = new Vector();
        progress = new HashMap();

        algorithms = new Algorithm[measurementSuite.getSamples()];
        threads = new Thread[measurementSuite.getSamples()];
        for (int i = 0; i < measurementSuite.getSamples(); ++i) {
            algorithms[i] = algorithmFactory.newAlgorithm();
            threads[i] = new Thread(algorithms[i]);
            algorithms[i].addIterationEventListener(this);
            algorithms[i].addCompleteEventListener(this);
            Problem[] problems = new Problem[1];
            problems[0] = problemFactory.newProblem();
            try {
                String type = problems[0].getClass().getInterfaces()[0].getName();
                Class parameters[] = new Class[1];
                parameters[0] = Class.forName(type);
                String setMethodName = "set" + type.substring(type.lastIndexOf(".") + 1);
                Method setProblemMethod = algorithms[i].getClass().getMethod(setMethodName, parameters);
                setProblemMethod.invoke(algorithms[i], problems);
            }
            catch (Exception ex) {
                throw new InitialisationException(algorithms[i].getClass().getName() + " does not support problems of type " + problems[0].getClass().getName());
            }
            algorithms[i].initialise();
            progress.put(algorithms[i], new Double(0));
        }
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
            catch (InterruptedException ex) { }
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
     
    public void handleCompleteEvent(AlgorithmEvent e) {
        measurementSuite.measure(e.getSource());
        progress.put(e.getSource(), new Double(e.getSource().getPercentageComplete()));
        notifyProgress();
    }
    
    public void handleIterationEvent(AlgorithmEvent e) {
        if (e.getSource().getIterations() % measurementSuite.getResolution() == 0) {
            measurementSuite.measure(e.getSource());
            progress.put(e.getSource(), new Double(e.getSource().getPercentageComplete()));
            notifyProgress();
        }
    }
    
    private int getThreadId(Algorithm algorithm) {
        for (int i = 0; i < measurementSuite.getSamples(); ++i) {
            if (algorithm == algorithms[i]) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Adds a listener for progress events. A progress is fired periodically based
     * on the resolution of the measurements.
     *
     * {@see ProgressEvent}
     * {@see ProgressListener}
     *
     * @param The event listener
     */
    public void addProgressListener(ProgressListener listener) {
        progressListeners.add(listener);
    }
    
    /**
     * Removes a listener for progress events.
     *
     * @param The event listener
     */
    public void removeProgressListener(ProgressListener listener) {
        progressListeners.remove(listener);
    }
    
    private void notifyProgress() {
        double ave = 0;
        for (Iterator i = progress.values().iterator(); i.hasNext(); ) {
            ave += ((Double) i.next()).doubleValue();
        }   
        
        ave /= progress.size();
        
        for (Iterator i = progressListeners.iterator(); i.hasNext(); ) {
            ProgressListener listener = (ProgressListener) i.next();
            listener.handleProgressEvent(new ProgressEvent(ave));
        }
    }
    
    private MeasurementSuite measurementSuite;
    private Algorithm[] algorithms;
    private Thread[] threads;
    private int simulation;
    private Vector progressListeners;
    private HashMap progress;
}
