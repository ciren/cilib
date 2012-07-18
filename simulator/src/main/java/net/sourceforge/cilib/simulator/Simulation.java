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
package net.sourceforge.cilib.simulator;

import java.io.IOException;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.AlgorithmEvent;
import net.sourceforge.cilib.algorithm.AlgorithmListener;
import net.sourceforge.cilib.problem.OptimisationProblem;
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
     * Prepre for execution. The simulation is prepared for execution by
     * setting the provided {@code problem} on the current {@code algorithm},
     * followed by the required initialization for the {@code algorithm} itself.
     */
    public void init() {
        AbstractAlgorithm alg = (AbstractAlgorithm) algorithm;
        alg.addAlgorithmListener(this);
        alg.setOptimisationProblem((OptimisationProblem) problem);
        alg.performInitialisation();
    }

    /**
     * Execute the simulation.
     */
    @Override
    public void run() {
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
