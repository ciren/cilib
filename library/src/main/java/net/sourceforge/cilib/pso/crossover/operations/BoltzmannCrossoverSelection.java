/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.operations;

import fj.P;
import fj.P3;
import java.util.Comparator;
import net.sourceforge.cilib.entity.comparator.BoltzmannComparator;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * A CrossoverSelection strategy that performs Boltzmann selection on the worst
 * parent and the offspring if the offspring is worse than the worst parent.
 * This class is also used in the CrossoverGuideProvider using the nBest instead
 * of the worst parent.
 */
public class BoltzmannCrossoverSelection extends CrossoverSelection {

    private Comparator comparator;

    public BoltzmannCrossoverSelection() {
        this.comparator = new BoltzmannComparator();
    }

    public BoltzmannCrossoverSelection(BoltzmannCrossoverSelection copy) {
        this.comparator = copy.comparator;
    }

    @Override
    public P3<Boolean, Particle, Particle> doAction(PSO algorithm, Enum solutionType, Enum fitnessType) {
        P3<Boolean, Particle, Particle> result = select(algorithm, solutionType, fitnessType);
        Particle selectedParticle = result._2();
        Particle offspring = result._3();

        if (!result._1()) {
            if (comparator.compare(selectedParticle, offspring) < 0) {
                return P.p(true, result._2(), result._3());
            }
        }

        return result;
    }

    @Override
    public BoltzmannCrossoverSelection getClone() {
        return new BoltzmannCrossoverSelection(this);
    }

    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

    public Comparator getComparator() {
        return comparator;
    }
}
