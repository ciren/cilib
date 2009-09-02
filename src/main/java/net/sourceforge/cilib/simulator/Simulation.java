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

import java.lang.reflect.Method;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.AlgorithmEvent;
import net.sourceforge.cilib.algorithm.AlgorithmListener;
import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.problem.Problem;

/**
 * A Simulation is a complete simulation that runs as a separate thread.
 */
class Simulation extends Thread implements AlgorithmListener {
    private static final long serialVersionUID = -3733724215662398762L;

    private final Simulator simulator;
    private final Algorithm algorithm;
    private final Problem problem;

    /**
     * Create a Simulation with the required dependencies.
     * @param simulator The controlling {@code Simulator}.
     * @param algorithmFactory The factory that creates {@code Algorithm} instances.
     * @param problemFactory The factory that creates {@code Problem} instances.
     */
    public Simulation(Simulator simulator, Algorithm algorithm, Problem problem) {
        this.simulator = simulator;
        this.algorithm = algorithm;
        this.problem = problem;
    }

    /**
     * Execute the simulation.
     */
    @Override
    public void run() {
        AbstractAlgorithm alg = (AbstractAlgorithm) algorithm;
        alg.addAlgorithmListener(this);

        try {
            Class<? extends Object> current = problem.getClass();

            while (!current.getSuperclass().equals(Object.class)) {
                current = current.getSuperclass();
            }

            String type = current.getInterfaces()[0].getName();
            Class<?> [] parameters = new Class[1];
            parameters[0] = Class.forName(type);
            String setMethodName = "set" + type.substring(type.lastIndexOf(".") + 1);
            Method setProblemMethod = algorithm.getClass().getMethod(setMethodName, parameters);
            setProblemMethod.invoke(algorithm, new Object[] {problem});
        }
        catch (Exception ex) {
            throw new InitialisationException(algorithm.getClass().getName() + " does not support problems of type " + problem.getClass().getName());
        }

        alg.initialise();
        alg.run();
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
    public void algorithmStarted(AlgorithmEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmFinished(AlgorithmEvent e) {
        this.simulator.simulationFinished(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmTerminated(AlgorithmEvent e) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void iterationCompleted(AlgorithmEvent e) {
        this.simulator.simulationIterationCompleted(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AlgorithmListener getClone() {
        return this;
    }

    /**
     * Obtain the {@code Algorithm} of the current {@code Simulation}.
     * @return The current {@code Algorithm}.
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }

}
