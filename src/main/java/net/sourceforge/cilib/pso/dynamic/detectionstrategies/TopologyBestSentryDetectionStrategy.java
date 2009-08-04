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
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;

/**
 * @inproceedings{ 2002.Hu.may00, title = "Adaptive Particle Swarm Optimization: Detection
 *                 and Response to Dynamic Systems", author = "Xiaohui Hu and Russell. C.
 *                 Eberhart", year = "2002", volume = "2", pages = "1666--1670", booktitle =
 *                 "Proceedings of the 2002 Congress on Evolutionary Computation", location =
 *                 "Honolulu, HI, USA", isbn = "0-7803-7282-4", month = may }
 * @author Theuns Cloete
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
            Entity sentry = algorithm.getTopology().getBestEntity();
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
