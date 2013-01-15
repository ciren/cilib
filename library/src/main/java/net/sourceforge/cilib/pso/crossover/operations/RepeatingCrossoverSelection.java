/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.operations;

import fj.P3;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * Performs a standard iteration then selects three random parents and performs
 * crossover with them (default crossover strategy is PCX). If the offspring is
 * better than the worst parent then the worst parent is replaced by the offspring.
 * If not, the process is repeated a number of times (default 10). This class is
 * also used with the CrossoverGuideProvider using the nBest instead of worst parent.
 */
public class RepeatingCrossoverSelection extends CrossoverSelection {

    private ControlParameter retries;

    public RepeatingCrossoverSelection() {
        super();
        this.retries = ConstantControlParameter.of(10);
    }

    public RepeatingCrossoverSelection(RepeatingCrossoverSelection copy) {
        super(copy);
        this.retries = copy.retries;
    }

    @Override
    public P3<Boolean, Particle, Particle> doAction(PSO algorithm, Enum solutionType, Enum fitnessType) {
        int counter = 0;
        boolean isBetter;
        P3<Boolean, Particle, Particle> result;

        do {
            result = select(algorithm, solutionType, fitnessType);
            isBetter = result._1();
        } while(++counter < retries.getParameter() && !isBetter);

        return result;
    }

    public void setRetries(ControlParameter retries) {
        this.retries = retries;
    }

    @Override
    public RepeatingCrossoverSelection getClone() {
        return new RepeatingCrossoverSelection(this);
    }
}
