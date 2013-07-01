/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.pso.pbestupdate;

import java.util.Arrays;
import net.cilib.entity.EntityType;
import net.cilib.entity.operators.crossover.CrossoverStrategy;
import net.cilib.entity.operators.crossover.OnePointCrossoverStrategy;
import net.cilib.problem.solution.Fitness;
import net.cilib.pso.particle.Particle;
import net.cilib.type.types.Int;

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

        p1.setCandidateSolution(particle.getCandidateSolution());
        p2.setCandidateSolution(particle.getBestPosition());

        Particle tmp = crossoverStrategy.crossover(Arrays.asList(p1, p2)).get(0);
        Fitness tmpFitness = particle.getFitnessCalculator().getFitness(tmp);

        if (tmpFitness.compareTo(particle.getBestFitness()) > 0) {
            particle.getParticleBehavior().incrementSuccessCounter();
            particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
            particle.getProperties().put(EntityType.Particle.BEST_FITNESS, tmpFitness);
            particle.getProperties().put(EntityType.Particle.BEST_POSITION, tmp.getCandidateSolution());
            return;
        }

        //PBest didn't change. Increment stagnation counter.
        int count = ((Int)particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue();
        particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER,  Int.valueOf(++count));
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

}
