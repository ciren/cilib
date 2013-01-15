/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.simulator;

import java.io.IOException;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.AlgorithmEvent;
import net.sourceforge.cilib.algorithm.AlgorithmListener;
import net.sourceforge.cilib.math.random.generator.Rand;
import net.sourceforge.cilib.problem.Problem;

/**
 * A Simulation is a complete simulation that runs as a separate thread.
 */
public class Simulation implements AlgorithmListener, Runnable {

    private static final long serialVersionUID = -3733724215662398762L;
    private final Simulator simulator;
    private final Algorithm algorithm;
    private final Problem problem;
    private final MeasurementSuite measurementSuite;

    /**
     * Create a Simulation with the required dependencies.
     * @param simulator The controlling {@code Simulator}.
     * @param algorithmFactory The factory that creates {@code Algorithm} instances.
     * @param problemFactory The factory that creates {@code Problem} instances.
     */
    public Simulation(Simulator simulator, Algorithm algorithm, Problem problem, MeasurementSuite measurementSuite) {
        this.simulator = simulator;
        this.algorithm = algorithm;
        this.problem = problem;
        this.measurementSuite = measurementSuite;
    }

    /**
     * Prepare for execution. The simulation is prepared for execution by
     * setting the provided {@code problem} on the current {@code algorithm},
     * followed by the required initialisation for the {@code algorithm} itself.
     */
    public void init() {
        AbstractAlgorithm alg = (AbstractAlgorithm) algorithm;
        alg.addAlgorithmListener(this);
        alg.setOptimisationProblem(problem);
        alg.performInitialisation();
    }

    /**
     * Execute the simulation.
     */
    @Override
    public void run() {
        Rand.reset();
        init();
        algorithm.run();
    }

    /**
     * Terminate the current simulation.
     */
    public void terminate() {
        ((AbstractAlgorithm) algorithm).terminate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmStarted(AlgorithmEvent event) {
        measurementSuite.initialise(); // Initialise the temporary data store
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmFinished(AlgorithmEvent event) {
        measurementSuite.measure(event.getSource());
        simulator.updateProgress(this, ((AbstractAlgorithm) event.getSource()).getPercentageComplete());

        try {
            measurementSuite.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void iterationCompleted(AlgorithmEvent event) {
        Algorithm alg = event.getSource();
        if (alg.getIterations() % measurementSuite.getResolution() == 0) {
            measurementSuite.measure(alg);
            simulator.updateProgress(this, ((AbstractAlgorithm) alg).getPercentageComplete());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AlgorithmListener getClone() {
        return this;
    }

    public MeasurementSuite getMeasurementSuite() {
        return measurementSuite;
    }

    public Problem getProblem() {
        return problem;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
