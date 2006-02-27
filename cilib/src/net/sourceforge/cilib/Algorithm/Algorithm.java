/*
 * Algorithm.java
 *
 * Created on January 17, 2003, 4:54 PM
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

package net.sourceforge.cilib.Algorithm;

import java.io.*;
import java.util.*;
import java.lang.*;

import net.sourceforge.cilib.Functions.*;
import net.sourceforge.cilib.PSO.*;

/**
 *
 * @author  espeer
 */
public abstract class Algorithm extends Thread implements Serializable {
    
    public Algorithm() {
        progressIndicators = new Vector();
        iterationEventListeners = new Vector();
        completeEventListeners = new Vector();
        running = false;
        initialised = false;
    }
    
    public void initialise() {
        iterations = 0;
        running = true;
        initialised = true;
    }
    
    public abstract void performIteration();

    public void run() {
        if (! initialised) {
            throw new InitialisationException("Algorithm not initialised");
        }
        
        while (running && getPercentageComplete() < 1) {
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
    
    public void addProgressIndicator(ProgressIndicator progressIndicator) {
        progressIndicator.setAlgorithm(this);
        progressIndicators.add(progressIndicator);
    }
    
    public void removeProgressIndicator(ProgressIndicator progressIndicator) {
        progressIndicators.remove(progressIndicator);
    }
    
    public void addIterationEventListener(IterationEventListener listener) {
        iterationEventListeners.add(listener);
    }
    
    public void removeIterationEventListener(IterationEventListener listener) {
        iterationEventListeners.remove(listener);
    }
    
    public void addCompleteEventListener(CompleteEventListener listener) {
        completeEventListeners.add(listener);
    }
    
    public void removeCompleteEventListener(CompleteEventListener listener) {
        completeEventListeners.remove(listener);
    }
    
    public int getIterations() {
        return iterations;
    }
    
    public double getPercentageComplete() {
        double percentageComplete = 0;
        for (Iterator i = progressIndicators.iterator(); i.hasNext(); ) {
            ProgressIndicator indicator = (ProgressIndicator) i.next();
            if (indicator.getPercentageCompleted() > percentageComplete) {
                percentageComplete = indicator.getPercentageCompleted();
            }
        }
        return percentageComplete;
    }
    
    public void start() {
        running = true;
        super.start();
    }
    
    public void terminate() {
        running = false;
    }
    
    private Vector progressIndicators;
    private Vector iterationEventListeners;
    private Vector completeEventListeners;
    private int iterations;
    private volatile boolean running;
    private boolean initialised;
}
