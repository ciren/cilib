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
package net.sourceforge.cilib.algorithm;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;

/**
 * <p>
 * All algorithms in CIlib should be subclasses of <code>Algorithm</code>. This class handles
 * stopping criteria, events, threading and measurements. Subclasses of <code>Algorithm</code>
 * must provide an implementation for <code>protected abstract void performIteration()</code>. If
 * a subclass overrides {@link #initialise()} then it must call <code>super.initialise()</code>.
 * Failure to do so will cause an {@linkplain InitialisationException} to be thrown when {@link #run()}
 * is called.
 * </p>
 * @author Edwin Peer
 */
public abstract class AbstractAlgorithm implements Algorithm, Stoppable, Runnable {
    private static final long serialVersionUID = 7197544770653732632L;
    private List<StoppingCondition> stoppingConditions;
    private List<AlgorithmListener> algorithmListeners;
    private int iterations;
    private volatile boolean running;
    private boolean initialised;

    protected OptimisationProblem optimisationProblem;

    /**
     * This {@linkplain ThreadLocal} variable maintains the stack of the currently
     * executing algorithm. It is defined as a static member and as a result is not
     * required to be marked as transient as static members are not allowed to be
     * serializable according to the Java Specification.
     *
     * @TODO: This static variable needs to be removed.
     */
    private static ThreadLocal<AlgorithmStack> currentAlgorithmStack = new ThreadLocal<AlgorithmStack>() {
        @Override
        protected AlgorithmStack initialValue() {
            return new AlgorithmStack();
        }
    };

    /**
     * Default constructor for {@linkplain Algorithm} classes. Sets up the correct state
     * for the instance and initialises the needed containers needed for the different
     * {@linkplain AlgorithmEvent}s that are generated.
     */
    protected AbstractAlgorithm() {
        stoppingConditions = new ArrayList<StoppingCondition>();
        algorithmListeners = new ArrayList<AlgorithmListener>();

        running = false;
        initialised = false;
    }

    /**
     * Copy constructor. Create a deep copy of the provided instance and return it.
     * @param copy The instance to copy.
     */
    protected AbstractAlgorithm(AbstractAlgorithm copy) {
        stoppingConditions = new ArrayList<StoppingCondition>();
        for (StoppingCondition stoppingCondition : copy.stoppingConditions) {
            StoppingCondition clone = stoppingCondition.getClone();
            clone.setAlgorithm(this);
            stoppingConditions.add(clone);
        }

        algorithmListeners = new ArrayList<AlgorithmListener>();
        for (AlgorithmListener listen : copy.algorithmListeners) {
            algorithmListeners.add(listen.getClone());
        }

        running = false;
        initialised = false;

        if (copy.optimisationProblem != null)
            optimisationProblem = copy.optimisationProblem.getClone();
    }

    /**
     * Initialises the algorithm. Must be called before {@link #run()} is called.
     */
    public final void initialise() {
        iterations = 0;
        running = true;
        initialised = true;

        if (stoppingConditions.isEmpty())
            throw new InitialisationException("No stopping conditions specified");

        for (StoppingCondition stoppingCondition : stoppingConditions)
            stoppingCondition.setAlgorithm(this);

        currentAlgorithmStack.get().push(this);
        performInitialisation();
        currentAlgorithmStack.get().pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void performIteration() {
        currentAlgorithmStack.get().push(this);
        algorithmIteration();
        iterations++;
        currentAlgorithmStack.get().pop();
    }

    /**
     * The actual operations that the current {@linkplain Algorithm} performs within a single
     * iteration.
     */
    protected abstract void algorithmIteration();

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInitialisation() {
        // subclasses can override the behaviour for this method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performUninitialisation() {
        // subclasses can override the behaviour for this method
    }

    /**
     * Executes the algorithm.
     * @exception InitialisationException algorithm was not properly initialised.
     */
    @Override
    public void run() {
        if (!initialised) {
            throw new InitialisationException("Algorithm not initialised");
        }

        fireAlgorithmStarted();

        currentAlgorithmStack.get().push(this);

        while (running && (!isFinished())) {
            performIteration();
            fireIterationCompleted();
        }

        if (running) {
            fireAlgorithmFinished();
        }
        else {
            fireAlgorithmTerminated();
        }

        performUninitialisation();

        currentAlgorithmStack.remove();
    }

    /**
     * Adds a stopping condition.
     * @param stoppingCondition A {@link net.sourceforge.cilib.stoppingcondition.StoppingCondition}
     *        to be added.
     */
    @Override
    public final void addStoppingCondition(StoppingCondition stoppingCondition) {
        stoppingConditions.add(stoppingCondition);
    }

    /**
     * Removes a stopping condition.
     * @param stoppingCondition The {@link net.sourceforge.cilib.stoppingcondition.StoppingCondition}
     *        to be removed.
     */
    @Override
    public final void removeStoppingCondition(StoppingCondition stoppingCondition) {
        stoppingConditions.remove(stoppingCondition);
    }

    /**
     * Adds an algorithm event listener. Event listeners are notified at various stages during the
     * execution of an algorithm.
     * @param listener An {@link AlgorithmListener} to be added.
     */
    public final void addAlgorithmListener(AlgorithmListener listener) {
        algorithmListeners.add(listener);
    }

    /**
     * Removes an algorithm event listener.
     * @param listener The {@link AlgorithmListener} to be removed.
     */
    public final void removeAlgorithmListener(AlgorithmListener listener) {
        algorithmListeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getIterations() {
        return iterations;
    }

    /**
     * Returns the percentage the algorithm is from completed (as a fraction). The percentage
     * complete is calculated based on the stopping condition that is closest to finished.
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
     * Accessor for the top-level currently executing algorithm running in the current thread.
     * @return the instance of the algorithm that is running in the current thread.
     */
    public static AbstractAlgorithm get() {
        return (AbstractAlgorithm) currentAlgorithmStack.get().peek();
    }

    /**
     * Static accessor to allow the current level of algorithm composition to be returned.
     * @see AlgorithmStack#asList()
     * @return An unmodifiable list of algorithms.
     */
    public static List<Algorithm> getAlgorithmList() {
        return currentAlgorithmStack.get().asList();
    }

    /**
     * Get the current list of {@linkplain StoppingCondition} instances that are
     * associated with the current {@linkplain Algorithm}.
     * @return The list of {@linkplain StoppingCondition} instances associated with
     *         the current {@linkplain Algorithm}.
     */
    public List<StoppingCondition> getStoppingConditions() {
        return this.stoppingConditions;
    }

    /**
     * Fire the {@linkplain AlgorithmEvent} to indicate that the {@linkplain Algorithm}
     * has started execution.
     */
    private void fireAlgorithmStarted() {
        for (AlgorithmListener listener : algorithmListeners) {
            listener.algorithmStarted(new AlgorithmEvent(this));
        }
    }

    /**
     * Fire the {@linkplain AlgorithmEvent} to indicate that the {@linkplain Algorithm}
     * has finished execution.
     */
    private void fireAlgorithmFinished() {
        for (AlgorithmListener listener : algorithmListeners) {
            listener.algorithmFinished(new AlgorithmEvent(this));
        }
    }

    /**
     * Fire the {@linkplain AlgorithmEvent} to indicate that the {@linkplain Algorithm}
     * has been terminated.
     */
    private void fireAlgorithmTerminated() {
        for (AlgorithmListener listener : algorithmListeners) {
            listener.algorithmTerminated(new AlgorithmEvent(this));
        }
    }

    /**
     * Fire the {@linkplain AlgorithmEvent} to indicate that the {@linkplain Algorithm}
     * has completed an iteration.
     */
    private void fireIterationCompleted() {
        for (AlgorithmListener listener : algorithmListeners) {
            listener.iterationCompleted(new AlgorithmEvent(this));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOptimisationProblem(OptimisationProblem problem) {
        this.optimisationProblem = problem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationProblem getOptimisationProblem() {
        return this.optimisationProblem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract OptimisationSolution getBestSolution();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Iterable<OptimisationSolution> getSolutions();

}
