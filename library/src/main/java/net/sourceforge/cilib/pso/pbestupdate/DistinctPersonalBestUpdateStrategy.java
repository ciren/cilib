/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.pbestupdate;

import java.util.Arrays;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.pso.crossover.velocityprovider.IdentityOffspringVelocityProvider;
import net.sourceforge.cilib.pso.crossover.velocityprovider.OffspringVelocityProvider;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;

public class DistinctPersonalBestUpdateStrategy implements PersonalBestUpdateStrategy {

    private DistinctPositionProvider positionProvider;
    private OffspringVelocityProvider velocityProvider;

    public DistinctPersonalBestUpdateStrategy() {
        this.positionProvider = new MutatedDistinctPositionProvider();
        this.velocityProvider = new IdentityOffspringVelocityProvider();
    }

    public DistinctPersonalBestUpdateStrategy(DistinctPersonalBestUpdateStrategy copy) {
        this.positionProvider = copy.positionProvider.getClone();
        this.velocityProvider = copy.velocityProvider;
    }

    @Override
    public DistinctPersonalBestUpdateStrategy getClone() {
        return new DistinctPersonalBestUpdateStrategy(this);
    }

    @Override
    public void updatePersonalBest(Particle particle) {
        if (particle.getFitness().compareTo(particle.getBestFitness()) > 0) {
            particle.getParticleBehavior().incrementSuccessCounter();
            particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));

            Particle temp = particle.getClone();
            temp.setCandidateSolution(positionProvider.f(particle));

            Fitness tempFitness = particle.getFitnessCalculator().getFitness(temp);

            if (tempFitness.compareTo(particle.getFitness()) > 0) {
                particle.getProperties().put(EntityType.Particle.BEST_FITNESS, tempFitness);
                particle.getProperties().put(EntityType.Particle.BEST_POSITION, temp.getCandidateSolution());
            } else {
                particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());
                particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getCandidateSolution());

                particle.getProperties().put(EntityType.FITNESS, tempFitness);
                particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, temp.getCandidateSolution());

                particle.getProperties().put(EntityType.Particle.VELOCITY, (Vector) velocityProvider.f(Arrays.asList(temp), particle));
            }

            return;
        }

        //PBest didn't change. Increment stagnation counter.
        int count = ((Int)particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue();
        particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER,  Int.valueOf(++count));
    }

    public DistinctPositionProvider getPositionProvider() {
        return positionProvider;
    }

    public void setPositionProvider(DistinctPositionProvider positionProvider) {
        this.positionProvider = positionProvider;
    }

    public void setVelocityProvider(OffspringVelocityProvider velocityProvider) {
        this.velocityProvider = velocityProvider;
    }

    public OffspringVelocityProvider getVelocityProvider() {
        return velocityProvider;
    }
}
