/*
 * Algorithm.java
 *
 * Created on January 17, 2003, 4:54 PM
 *
 * 
 * Copyright (C) 2003 - 2006 
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
package net.sourceforge.cilib.algorithm;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import net.sourceforge.cilib.algorithm.proxy.AlgorithmProxy;
import net.sourceforge.cilib.algorithm.proxy.DistributedAlgorithmProxy;
import net.sourceforge.cilib.algorithm.proxy.LocalAlgorithmProxy;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;

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
 * @author  Edwin Peer
 */
public abstract class Algorithm implements Runnable, Serializable {
	
	private Vector<StoppingCondition> stoppingConditions;
    private Vector<AlgorithmListener> algorithmListeners;
    private int iterations;
    private volatile boolean running;
    private boolean initialised;
    private boolean distributed;
    public static byte _ciclops_exclude_algorithmListener = 1; // TODO: Replace these with annotations
    
    private OptimisationProblem problem;
    private static AlgorithmProxy proxy; // TODO: Is this over engineered? Thin about the flow and determine whether or not this strategy couldn't work like the original??? 
    
    
    protected Algorithm() {
//    	LoggingSingleton.initialise();
    	    	
        stoppingConditions = new Vector<StoppingCondition>();
        algorithmListeners = new Vector<AlgorithmListener>();
        running = false;
        initialised = false;
        distributed = false;
    }
    
    public abstract Algorithm clone();
    
    /**
     * Intialises the algorithm. Must be called before {@link #run()} is called.
     */
    public final void initialise() {
        iterations = 0;
        running = true;
        initialised = true;
        
        if (stoppingConditions.isEmpty()) {
        	throw new InitialisationException("No stopping conditions specified");
        }
        
        if (distributed) {
        	proxy = new DistributedAlgorithmProxy(); 
        }
        else {
        	proxy = new LocalAlgorithmProxy();
        }
        
        performInitialisation();
    }
    
    public abstract void performIteration();
    public void performInitialisation() {
    	// subclasses can override the behaviour for this method
    }
    public void performUninitialisation() {
    	// subclasses can override the behaviour for this method
    }

    /** 
     * Executes the algorithm.
     * <p />
     * @exception InitialisationException algorithm was not properly initialised.  
     */
    public void run() {
    	if (! initialised) {
            throw new InitialisationException("Algorithm not initialised");
        }

    	if (proxy.get() == null) {
    		proxy.set(this);
    	}
    
        fireAlgorithmStarted();
        
        while (running && (! isFinished())) {
            performIteration();
            ++iterations;
    
            fireIterationCompleted();
        }
        
        if (running) {
        	fireAlgorithmFinished();
        }
        else {
        	fireAlgorithmTerminated();
        }
        
        performUninitialisation();
      
        // TODO: Figure this stuff out
        // initialised = false; // This breaks MultiStartOptimisationAlgorithm - does it make sense to set it false here?
        // localInstance.set(null); // By not setting to null we allow algorithm to be accessed after compeltion - should be fine
    }
    
    /**
     * Adds a stopping condition.
     *
     * @param stoppingCondition A {@link net.sourceforge.cilib.stoppingcondition.StoppingCondition} to be added.
     */
    public final void addStoppingCondition(StoppingCondition stoppingCondition) {
        stoppingCondition.setAlgorithm(this);
        stoppingConditions.add(stoppingCondition);
    }
    
    /**
     * Removes a stopping condition.
     *
     * @param stoppingCondition The {@link net.sourceforge.cilib.stoppingcondition.StoppingCondition} to be removed.
     */
    public final void removeStoppingCondition(StoppingCondition stoppingCondition) {
        stoppingConditions.remove(stoppingCondition);
    }
    
    /**
     * Adds an algorithm event listener. Event listeners are notified at various stages 
     * during the execution of an algorithm.
     * 
     * @param listener An {@link AlgorithmListener} to be added.
     */
    public final void addAlgorithmListener(AlgorithmListener listener) {
        algorithmListeners.add(listener);
    }
    
    /**
     * Removes an alogorithm event listener
     *
     * @param listener The {@link AlgorithmListener} to be removed.
     */
    public final void removeAlgorithmListener(AlgorithmListener listener) {
        algorithmListeners.remove(listener);
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
     * percentage complete is calculated based on the stopping condition that is
     * closest to finished.
     *
     * @return The percentage complete as a fraction.
     */
    public final double getPercentageComplete() {
    	double percentageComplete = 0;
        for (StoppingCondition condition : stoppingConditions) {
            if (condition.getPercentageCompleted() > percentageComplete) {
                percentageComplete = condition.getPercentageCompleted();
            }
        }
        return percentageComplete;
    }
    
    /**
     * Returns true if the algorithm has finished executing.
     * 
     * @return true if the algorithm is finished
     */
    public final boolean isFinished() {
        for (StoppingCondition condition : stoppingConditions) {
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
    
    /**
     * Accessor for the top-level algorithm running in the current thread
     * 
     * @return the instance of the algorithm that is running in the current thread,.
     */
    public static Algorithm get() {
        return proxy.get();
    }
    
    public static AlgorithmProxy getProxy() {
    	return proxy;
    }
    
    
    public Vector<StoppingCondition> getStoppingConditions() {
    	return this.stoppingConditions;
    }
    
    private void fireAlgorithmStarted() {
    	for (AlgorithmListener listener : algorithmListeners) {
    		listener.algorithmStarted(new AlgorithmEvent(this));
    	}
    }

    private void fireAlgorithmFinished() {
    	for (AlgorithmListener listener : algorithmListeners) {
    		listener.algorithmFinished(new AlgorithmEvent(this));
    	}
    }

    private void fireAlgorithmTerminated() {
    	for (AlgorithmListener listener : algorithmListeners ) {
    		listener.algorithmTerminated(new AlgorithmEvent(this));
    	}
    }
    
    private void fireIterationCompleted() {
    	for (AlgorithmListener listener : algorithmListeners) {
    		listener.iterationCompleted(new AlgorithmEvent(this));
    	}
    }
    
	public boolean isDistributed() {
		return distributed;
	}

	public void setDistributed(boolean distributed) {
		this.distributed = distributed;
	}

	/**
     * Set the optimisation problem to be solved. By default, the problem is
     * <code>null</code>. That is, it is necessary to set the optimisation problem
     * before calling {@link #initialise()}.
     *
     * @param problem An implementation of the {@link net.sourceforge.cilib.problem.OptimisationProblemAdapter} interface.
     *
     */
    public void setOptimisationProblem(OptimisationProblem problem) {
    	this.problem = problem;
    }

    
    /**
     * Get the specified <code>OptimisationProblem</code>
     * @return The specified <code>OptimisationProblem</code>
     */
    public OptimisationProblem getOptimisationProblem() {
    	return this.problem;
    }
    
    
    /**
     * Get the best current solution. This best solution is determined from the personal bests of the particles.
     * @return The <code>OptimisationSolution</code> representing the best solution.
     */
    public abstract OptimisationSolution getBestSolution();

    
    /**
     * Get the collection of best solutions. This result does not actually make sense in the normal PSO
     * algorithm, but rather in a MultiObjective optimisation.
     * @return  The <code>Collection&lt;OptimisationSolution&gt;</code> containing the solutions. 
     */
    public abstract List<OptimisationSolution> getSolutions();
	
}
