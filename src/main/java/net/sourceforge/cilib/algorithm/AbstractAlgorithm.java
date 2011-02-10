/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.algorithm;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.entity.EntityIdFactory;
import net.sourceforge.cilib.moo.archive.Archive;
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
public abstract class AbstractAlgorithm implements Algorithm, Stoppable {

    private static final long serialVersionUID = 7197544770653732632L;
    private final List<StoppingCondition<Algorithm>> stoppingConditions;
    private final List<AlgorithmListener> algorithmListeners;
    private Predicate<Algorithm> stoppingCondition = Predicates.alwaysFalse();
    private int iteration;
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
     * for the instance and initializes the needed containers needed for the different
     * {@linkplain AlgorithmEvent}s that are generated.
     */
    protected AbstractAlgorithm() {
        stoppingConditions = new ArrayList<StoppingCondition<Algorithm>>();
        algorithmListeners = new ArrayList<AlgorithmListener>();

        running = false;
        initialised = false;
    }

    /**
     * Copy constructor. Create a deep copy of the provided instance and return it.
     * @param copy The instance to copy.
     */
    protected AbstractAlgorithm(AbstractAlgorithm copy) {
        stoppingConditions = Lists.newArrayList(copy.stoppingConditions);
        algorithmListeners = Lists.newArrayList();
        for (AlgorithmListener listen : copy.algorithmListeners) {
            algorithmListeners.add(listen.getClone());
        }

        if (copy.optimisationProblem != null) {
            optimisationProblem = copy.optimisationProblem.getClone();
        }

        running = false;
        initialised = false;
    }

    /**
     * Initializes the algorithm. Must be called before {@link #run()} is called.
     */
    public final void initialise() {
        Preconditions.checkState(!stoppingConditions.isEmpty(), "No stopping conditions specified");
        iteration = 0;
        running = true;
        initialised = true;

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
        iteration++;
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
     * @exception InitialisationException algorithm was not properly initialized.
     */
    @Override
    public void run() {
        Preconditions.checkState(initialised, "Algorithm not initialised");

        fireAlgorithmStarted();

        currentAlgorithmStack.get().push(this);

        while (running && (!isFinished())) {
            performIteration();
            fireIterationCompleted();
        }

        if (running) {
            fireAlgorithmFinished();
        } else {
            fireAlgorithmTerminated();
        }

        performUninitialisation();

        currentAlgorithmStack.get().pop();

        // Cleanup thread-local variables -- very ugly hack!!!
        currentAlgorithmStack.remove();
        EntityIdFactory.remove();
        Archive.Provider.remove();
    }

    /**
     * Adds a stopping condition.
     * @param stoppingCondition A {@link net.sourceforge.cilib.stoppingcondition.StoppingCondition}
     *        to be added.
     */
    @Override
    public final void addStoppingCondition(StoppingCondition<Algorithm> stoppingCondition) {
        stoppingConditions.add(stoppingCondition);
        this.stoppingCondition = Predicates.or(this.stoppingCondition, stoppingCondition);
    }

    /**
     * Removes a stopping condition.
     * @param stoppingCondition The {@link net.sourceforge.cilib.stoppingcondition.StoppingCondition}
     *        to be removed.
     */
    @Override
    public final void removeStoppingCondition(StoppingCondition<Algorithm> stoppingCondition) {
        stoppingConditions.remove(stoppingCondition);

        this.stoppingCondition = Predicates.alwaysFalse();
        for (StoppingCondition<Algorithm> condition : stoppingConditions) {
            this.stoppingCondition = Predicates.or(this.stoppingCondition, condition);
        }
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
        return iteration;
    }

    /**
     * Returns the percentage the algorithm is from completed (as a fraction). The percentage
     * complete is calculated based on the stopping condition that is closest to finished.
     * @return The percentage complete as a fraction.
     */
    public final double getPercentageComplete() {
        double percentageComplete = 0;
        for (StoppingCondition condition : stoppingConditions) {
            double percentage = condition.getPercentageCompleted(this);
            if (percentage > percentageComplete) {
                percentageComplete = percentage;
            }
        }
        return percentageComplete;
    }

    /**
     * Returns true if the algorithm has finished executing.
     * @return true if the algorithm is finished
     */
    @Override
    public final boolean isFinished() {
        return stoppingCondition.apply(this);
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
    public List<StoppingCondition<Algorithm>> getStoppingConditions() {
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
