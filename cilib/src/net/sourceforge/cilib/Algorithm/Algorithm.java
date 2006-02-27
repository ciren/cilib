/*
 * Algorithm.java
 *
 * Created on January 17, 2003, 4:54 PM
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

package net.sourceforge.cilib.Algorithm;

import java.util.Iterator;
import java.util.Vector;

import net.sourceforge.cilib.StoppingCondition.StoppingCondition;

/**
 * <p>
 * All algorithms in CILib should be subclasses of <code>Algorithm</code>. This class
 * handles stopping criteria, events, threading and measurements. Subclasses of 
 * <code>Algorithm</code> must provide an implementation for 
 * <code>protected abstract void performIteration()</code>.
 * If a subclass overrides {@link #initialise()} then it must call 
 * <code>super.initialise()</code>. Failure to do so will cause an 
 * {@link InitialisationException} to be thrown when {@link #run()} is called.
 * </p>
 *
 * @author  espeer
 */
public abstract class Algorithm implements Runnable {
    
    protected Algorithm() {
        stoppingConditions = new Vector();
        iterationEventListeners = new Vector();
        completeEventListeners = new Vector();
        running = false;
        initialised = false;
    }
    
    /**
     * Intialises the algorithm. Must be called before {@link #run()} is called.
     */
    public void initialise() {
        iterations = 0;
        running = true;
        initialised = true;
    }
    
    protected abstract void performIteration();

    /** 
     * Executes the algorithm.  
     */
    public final void run() {
        if (! initialised) {
            throw new InitialisationException("Algorithm not initialised");
        }
    
        running = true;
        
        while (running && (! isFinished())) {
            performIteration();
            ++iterations;
            
            for (Iterator i = iterationEventListeners.iterator(); i.hasNext(); ) {
                IterationEventListener listener = (IterationEventListener) i.next();
                listener.handleIterationEvent(new AlgorithmEvent(this));
            }            
        }
        
        if (running) {
            for (Iterator i = completeEventListeners.iterator(); i.hasNext(); ) {
                CompleteEventListener listener = (CompleteEventListener) i.next();
                listener.handleCompleteEvent(new AlgorithmEvent(this));
            }
        }
    }
    
    /**
     * Adds a stopping condition.
     *
     * @param stoppingCondition A {@link StoppingCondition} to be added.
     */
    public final void addStoppingCondition(StoppingCondition stoppingCondition) {
        stoppingCondition.setAlgorithm(this);
        stoppingConditions.add(stoppingCondition);
    }
    
    /**
     * Removes a progress indicator.
     *
     * @param progressIndicator The {@link ProgressIndicator} to be removed.
     */
    public final void removeStoppingCondition(StoppingCondition stoppingCondition) {
        stoppingConditions.remove(stoppingCondition);
    }
    
    /**
     * Adds an iteration event listener. Iteration event listeners are
     * notified at the end of each iteration of the algorithm.
     * 
     * @param listener An {@link IterationEventListener} to be added.
     */
    public final void addIterationEventListener(IterationEventListener listener) {
        iterationEventListeners.add(listener);
    }
    public static byte _ciclops_exclude_iterationEventListener = 1;
    
    /**
     * Removes an iteration event listener
     *
     * @param listener The {@link IterationEventListener} to be removed.
     */
    public final void removeIterationEventListener(IterationEventListener listener) {
        iterationEventListeners.remove(listener);
    }
    
    /**
     * Adds a completion event listener. Completion event listeners are notified
     * when the algorithm has finished executing.
     *
     * @param listener A {@link CompleteEventListener} to be added.
     */
    public final void addCompleteEventListener(CompleteEventListener listener) {
        completeEventListeners.add(listener);
    }
    public static byte _ciclops_exclude_completeEventListener = 1;
    
    /**
     * Removes a completion event listener.
     *
     * @param listener The {@link CompleteEventListener} to be removed.
     */
    public final void removeCompleteEventListener(CompleteEventListener listener) {
        completeEventListeners.remove(listener);
    }
    
    /**
     * Returns the number of iterations that have been performed by the algorihtm.
     *
     * @return The number of iterations.
     */
    public final int getIterations() {
        return iterations;
    }
    
    /**
     * Returns the percentage the algorithm is from completed (as a fraction). The
     * percentage complete is calculated based on the progress indicator that is
     * closest to finished.
     *
     * @return The percentage complete as a fraction.
     */
    public final double getPercentageComplete() {
        double percentageComplete = 0;
        for (Iterator i = stoppingConditions.iterator(); i.hasNext(); ) {
            StoppingCondition condition = (StoppingCondition) i.next();
            if (condition.getPercentageCompleted() > percentageComplete) {
                percentageComplete = condition.getPercentageCompleted();
            }
        }
        return percentageComplete;
    }
    
    public final boolean isFinished() {
        for (Iterator i = stoppingConditions.iterator(); i.hasNext(); ) {
            StoppingCondition condition = (StoppingCondition) i.next();
            if (condition.isCompleted()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Terminates the algorithm.
     */
    public final void terminate() {
        running = false;
    }
    
    private Vector stoppingConditions;
    private Vector iterationEventListeners;
    private Vector completeEventListeners;
    private int iterations;
    private volatile boolean running;
    private boolean initialised;
}
