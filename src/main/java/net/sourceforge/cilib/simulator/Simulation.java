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
class Simulation implements AlgorithmListener, Runnable {

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
    Simulation(Simulator simulator, Algorithm algorithm, Problem problem, MeasurementSuite measurementSuite) {
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
    void init() {
        AbstractAlgorithm alg = (AbstractAlgorithm) algorithm;
        alg.addAlgorithmListener(this);
        alg.setOptimisationProblem((OptimisationProblem) problem);
        alg.initialise();
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
    void terminate() {
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
    public void algorithmTerminated(AlgorithmEvent event) {
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

    MeasurementSuite getMeasurementSuite() {
        return measurementSuite;
    }
}
