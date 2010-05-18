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
package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.measurement.single.diversity.Diversity;

/**
 * A stopping condition that is based on the {@link Diversity} of the population. The
 * {@link Algorithm} will stop as soon as the population's diversity drops below a (user-specified)
 * threshold for a (user-specified) number of consecutive iterations.
 * @author Theuns Cloete
 */
public class MinimumDiversity implements StoppingCondition<PopulationBasedAlgorithm> {
    private static final long serialVersionUID = 8678755417913002799L;

    // minimum diversity value to satisfy the StoppingCondition
    private ControlParameter minimumDiversity;
    // number of consecutive iterations for which the diversity should be below minimumDiversity before stopping
    private ControlParameter consecutiveIterations;
    // Diversity object used to calculate diversity
    private Diversity diversity;
    // used to determine a rough completion percentage
    private double maximumDiversity = 0.0;
    // stores the last calculated diversity value
    private double calculatedDiversity = 0.0;
    // number of consecutive iterations for which the diversity has been below minimumDiversity
    private int iterations = 0;

    public MinimumDiversity() {
        minimumDiversity = new ConstantControlParameter(1.0);
        consecutiveIterations = new ConstantControlParameter(10);
        diversity = new Diversity();
    }

    @Override
    public double getPercentageCompleted(PopulationBasedAlgorithm algorithm) {
        return 1.0 - ((calculatedDiversity - minimumDiversity.getParameter()) / (maximumDiversity - minimumDiversity.getParameter()));
    }

    @Override
    public boolean apply(PopulationBasedAlgorithm input) {
        updateControlParameters();
        calculatedDiversity = (diversity.getValue(input)).doubleValue();
        maximumDiversity = Math.max(maximumDiversity, calculatedDiversity);

        iterations = calculatedDiversity < minimumDiversity.getParameter() ? iterations + 1 : 0;

        return iterations >= consecutiveIterations.getParameter();
    }

    private void updateControlParameters() {
        minimumDiversity.updateParameter();
        consecutiveIterations.updateParameter();
    }

    /**
     * Any {@link ControlParameter} can be used to control the {@link #minimumDiversity} value.
     * @param md a {@link ControlParameter} to control the {@link #minimumDiversity} value
     */
    public void setMinimumDiversity(ControlParameter md) {
        minimumDiversity = md;
    }

    /**
     * Any {@link ControlParameter} can be used to control the {@link #consecutiveIterations} value.
     * @param ci a {@link ControlParameter} to control the {@link #consecutiveIterations} value
     */
    public void setConsecutiveIterations(ControlParameter ci) {
        consecutiveIterations = ci;
    }

    /**
     * The manner in which the diversity should be calculated can be constructed using the
     * {@link Diversity} hierarchy and its strategies.
     * @param d a {@link Diversity} object that will be used to calculate the diversity of the
     *        population
     */
    public void setDiversity(Diversity d) {
        diversity = d;
    }

}
