/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;

/**
 * All algorithms in CIlib should be subclasses of {@link Algorithm}. This class
 * handles stopping criteria, events, threading and measurements.
 * <p>
 * Subclasses of {@link Algorithm} must provide an implementation for
 * {@link Algorithm#performIteration()}. If a subclass overrides
 * {@link #algorithmInitialisation()} then it must call {@code super.initialise()}.
 */
public abstract class AbstractAlgorithm implements Algorithm, Stoppable {

    private static final long serialVersionUID = 7197544770653732632L;
    private final List<StoppingCondition<Algorithm>> stoppingConditions;
    private final List<AlgorithmListener> algorithmListeners;
    private Predicate<Algorithm> stoppingCondition = Predicates.alwaysFalse();
    private int iteration;
    private volatile boolean running;
    private boolean initialised;
    protected Problem optimisationProblem;
    /**
     * This {@linkplain ThreadLocal} variable maintains the stack of the currently
     * executing algorithm. It is defined as a static member and as a result is not
     * required to be marked as transient as static members are not allowed to be
     * serialisable according to the Java Specification.
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
        stoppingConditions = new ArrayList<StoppingCondition<Algorithm>>();
        algorithmListeners = new ArrayList<AlgorithmListener>();

        running = false;
        initialised = false;
    }

    @Override
    public abstract AbstractAlgorithm getClone();

    /**
     * Copy constructor. Create a deep copy of the provided instance and return it.
     * @param copy The instance to copy.
     */
    protected AbstractAlgorithm(AbstractAlgorithm copy) {
        stoppingConditions = Lists.newArrayList();
        algorithmListeners = Lists.newArrayList();

        for (AlgorithmListener listen : copy.algorithmListeners) {
            algorithmListeners.add(listen.getClone());
        }

        if (copy.optimisationProblem != null) {
            optimisationProblem = copy.optimisationProblem;
        }

        for (StoppingCondition sc : copy.stoppingConditions) {
            addStoppingCondition(sc);
        }

        running = false;
        initialised = false;
        iteration = copy.iteration;
    }

    /**
     * Initialises the algorithm. Must be called before {@link #run()} is called.
     */
    @Override
    public final void performInitialisation() {
        iteration = 0;
        running = true;
        initialised = true;

        currentAlgorithmStack.get().push(this);
        algorithmInitialisation();
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
     * Initialise the algorithm.
     */
    public void algorithmInitialisation() {
        // subclasses can override the behaviour for this method
    }

    /**
     * Executes the algorithm without cleaning up afterwards.
     * Useful for running algorithms within algorithms.
     */
    public void runAlgorithm() {
        Preconditions.checkState(!stoppingConditions.isEmpty(), "No stopping conditions specified");
        Preconditions.checkState(initialised, "Algorithm not initialised");

        while (running && (!isFinished())) {
            performIteration();
            fireIterationCompleted();
        }
    }

    /**
     * Executes the algorithm.
     * @exception InitialisationException algorithm was not properly initialised.
     */
    @Override
    public void run() {
        if (!initialised) {
            performInitialisation();
        }

        currentAlgorithmStack.get().push(this);
        fireAlgorithmStarted();

        runAlgorithm();

        fireAlgorithmFinished();
        currentAlgorithmStack.get().pop();

        cleanUp();
    }

    public void cleanUp() {
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
    public void setOptimisationProblem(Problem problem) {
        this.optimisationProblem = problem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Problem getOptimisationProblem() {
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
