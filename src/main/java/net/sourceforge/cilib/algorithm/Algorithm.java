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
package net.sourceforge.cilib.algorithm;

import java.util.List;
import java.util.Vector;

import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.util.Cloneable;

/**
 * <p>
 * All algorithms in CILib should be subclasses of <code>Algorithm</code>. This class handles
 * stopping criteria, events, threading and measurements. Subclasses of <code>Algorithm</code>
 * must provide an implementation for <code>protected abstract void performIteration()</code>. If
 * a subclass overrides {@link #initialise()} then it must call <code>super.initialise()</code>.
 * Failure to do so will cause an {@linkplain InitialisationException} to be thrown when {@link #run()}
 * is called.
 * </p>
 * @author Edwin Peer
 */
public abstract class Algorithm implements Cloneable, Runnable {
	private static final long serialVersionUID = 7197544770653732632L;
	private Vector<StoppingCondition> stoppingConditions;
	private Vector<AlgorithmListener> algorithmListeners;
	private int iterations;
	private volatile boolean running;
	private boolean initialised;

	protected OptimisationProblem optimisationProblem;

	/**
	 * This {@linkplain ThreadLocal} variable maintains the stack of the currently
	 * executing algorithm. It is defined as a static member and as a result is not
	 * required to be marked as transient as static members are not allowed to be
	 * serializable according to the Java Specification.
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
	protected Algorithm() {
		// LoggingSingleton.initialise();
		stoppingConditions = new Vector<StoppingCondition>();
		algorithmListeners = new Vector<AlgorithmListener>();

		running = false;
		initialised = false;
	}

	/**
	 * Copy constructor. Create a deep copy of the provided instance and return it.
	 * @param copy The instance to copy.
	 */
	public Algorithm(Algorithm copy) {
		stoppingConditions = new Vector<StoppingCondition>();
		for (StoppingCondition stoppingCondition : copy.stoppingConditions) {
			StoppingCondition clone = stoppingCondition.getClone();
			clone.setAlgorithm(this);
			stoppingConditions.add(clone);
		}

		algorithmListeners = new Vector<AlgorithmListener>();
		for (AlgorithmListener listen : copy.algorithmListeners) {
			algorithmListeners.add(listen.getClone());
		}

		running = false;
		initialised = false;

		if (copy.optimisationProblem != null)
			optimisationProblem = copy.optimisationProblem.getClone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Algorithm getClone();


	/**
	 * Reset the {@linkplain Algorithm} internals if needed.
	 */
	public void reset() {
		 throw new UnsupportedOperationException("'reset()' method not implemented for '" + this.getClass().getName() + "'");
	}

	/**
	 * Initialises the algorithm. Must be called before {@link #run()} is called.
	 */
	public final void initialise() {
		iterations = 0;
		running = true;
		initialised = true;

		if (stoppingConditions.isEmpty()) {
			throw new InitialisationException("No stopping conditions specified");
		}

		performInitialisation();
	}

	/**
	 * Perform the actions of the current {@linkplain Algorithm} for a single iteration. This
	 * method calls {@linkplain Algorithm#algorithmIteration()} after it performs some
	 * internal tasks by maintaining the stack of the currently executing algorithm instances.
	 */
	public final void performIteration() {
		currentAlgorithmStack.get().push(this);
		algorithmIteration();
		currentAlgorithmStack.get().pop();
	}

	/**
	 * The actual operations that the current {@linkplain Algorithm} performs within a single
	 * iteration.
	 */
	protected abstract void algorithmIteration();

	/**
	 * Perform the needed initialisation required before the execution of the algorithm
	 * starts.
	 */
	public void performInitialisation() {
		// subclasses can override the behaviour for this method
	}

	/**
	 * Perform the needed unintialisation steps after the algorithm completes it's
	 * execution.
	 */
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

		currentAlgorithmStack.remove();
	}

	/**
	 * Adds a stopping condition.
	 * @param stoppingCondition A {@link net.sourceforge.cilib.stoppingcondition.StoppingCondition}
	 *        to be added.
	 */
	public final void addStoppingCondition(StoppingCondition stoppingCondition) {
		stoppingCondition.setAlgorithm(this);
		stoppingConditions.add(stoppingCondition);
	}

	/**
	 * Removes a stopping condition.
	 * @param stoppingCondition The {@link net.sourceforge.cilib.stoppingcondition.StoppingCondition}
	 *        to be removed.
	 */
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
	 * Returns the number of iterations that have been performed by the algorihtm.
	 * @return The number of iterations.
	 */
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
	public static Algorithm get() {
		return currentAlgorithmStack.get().peek();
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
	public Vector<StoppingCondition> getStoppingConditions() {
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
	 * Set the optimisation problem to be solved. By default, the problem is <code>null</code>.
	 * That is, it is necessary to set the optimisation problem before calling {@link #initialise()}.
	 * @param problem An implementation of the
	 *        {@link net.sourceforge.cilib.problem.OptimisationProblemAdapter} interface.
	 */
	public void setOptimisationProblem(OptimisationProblem problem) {
		this.optimisationProblem = problem;
	}

	/**
	 * Get the specified {@linkplain OptimisationProblem}.
	 * @return The specified {@linkplain OptimisationProblem}.
	 */
	public OptimisationProblem getOptimisationProblem() {
		return this.optimisationProblem;
	}

	/**
	 * Get the best current solution. This best solution is determined from the personal bests of the
	 * particles.
	 * @return The <code>OptimisationSolution</code> representing the best solution.
	 */
	public abstract OptimisationSolution getBestSolution();

	/**
	 * Get the collection of best solutions. This result does not actually make sense in the normal
	 * PSO algorithm, but rather in a MultiObjective optimization.
	 * @return The <code>Collection&lt;OptimisationSolution&gt;</code> containing the solutions.
	 */
	public abstract List<OptimisationSolution> getSolutions();

	/**
	 * General method to accept a visitor to perform a calculation on the current algorithm. The
	 * operation is generally deferred down to the underlying topology associated with the
	 * algorithm, as the algorithm does not contain information, but rather only behaviour to alter
	 * the candidate solutions that are managed by the <tt>Topology</tt>.
	 * @param visitor The <tt>Visitor</tt> to be applied to the algorithm
	 * @return The result of the visitor operation.
	 */
	public abstract double accept(TopologyVisitor visitor);
}
