/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;

/**
 * @inproceedings{ 2002.Hu.may00, title = "Adaptive Particle Swarm Optimization: Detection
 *                 and Response to Dynamic Systems", author = "Xiaohui Hu and Russell. C.
 *                 Eberhart", year = "2002", volume = "2", pages = "1666--1670", booktitle =
 *                 "Proceedings of the 2002 Congress on Evolutionary Computation", location =
 *                 "Honolulu, HI, USA", isbn = "0-7803-7282-4", month = may }
 * @param <E> some {@link PopulationBasedAlgorithm population based algorithm}
 */
public class TopologyBestSentryDetectionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeDetectionStrategy<E> {
    private static final long serialVersionUID = 7060690546029355964L;

    @Override
    public EnvironmentChangeDetectionStrategy<E> getClone() {
        return this;
    }

    @Override
    public boolean detect(PopulationBasedAlgorithm algorithm) {
        if (algorithm.getIterations() % interval == 0) {
            Entity sentry = Topologies.getBestEntity(algorithm.getTopology());
            double previousFitness = sentry.getFitness().getValue();
            sentry.calculateFitness();
            double currentFitness = sentry.getFitness().getValue();

            if (Math.abs(previousFitness - currentFitness) >=  epsilon) {
                return true;
            }
        }
        return false;
    }
}
