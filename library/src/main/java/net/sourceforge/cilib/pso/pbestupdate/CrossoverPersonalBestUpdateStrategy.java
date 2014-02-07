/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.pbestupdate;

import java.util.Arrays;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.discrete.OnePointCrossoverStrategy;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Int;

/**
 * A crossover strategy which uses an entity, created using crossover on the particle's position and its pbest,
 * instead of the particle's position to update the pbest.
 */
public class CrossoverPersonalBestUpdateStrategy implements PersonalBestUpdateStrategy {

    private CrossoverStrategy crossoverStrategy;

    public CrossoverPersonalBestUpdateStrategy() {
        this.crossoverStrategy = new OnePointCrossoverStrategy();
    }

    private CrossoverPersonalBestUpdateStrategy(CrossoverPersonalBestUpdateStrategy copy) {
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
    }

    public PersonalBestUpdateStrategy getClone() {
        return new CrossoverPersonalBestUpdateStrategy(this);
    }

    public void updatePersonalBest(Particle particle) {
        Particle p1 = particle.getClone();
        Particle p2 = particle.getClone();

        p1.setPosition(particle.getPosition());
        p2.setPosition(particle.getBestPosition());

        Particle tmp = crossoverStrategy.crossover(Arrays.asList(p1, p2)).get(0);
        Fitness tmpFitness = particle.getBehaviour().getFitnessCalculator().getFitness(tmp);

        if (tmpFitness.compareTo(particle.getBestFitness()) > 0) {
            particle.getBehaviour().incrementSuccessCounter();
            particle.put(Property.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
            particle.put(Property.BEST_FITNESS, tmpFitness);
            particle.put(Property.BEST_POSITION, tmp.getPosition());
            return;
        }

        //PBest didn't change. Increment stagnation counter.
        int count = ((Int)particle.get(Property.PBEST_STAGNATION_COUNTER)).intValue();
        particle.put(Property.PBEST_STAGNATION_COUNTER,  Int.valueOf(++count));
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

}
