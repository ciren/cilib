/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.pbestupdate;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 *
 */
public class StandardPersonalBestUpdateStrategyTest {

    /**
     * If a particle's current fitness is "more fit" than the current best
     * fitness, then the best fitness should equal the current fitness, the
     * best position should be updated to the current position, and the pbest
     * stagnation counter should <b>not be</b> incremented.
     */
    @Test
    public void updatePersonalBest() {
        Particle particle = new StandardParticle();

        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(300.0));
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.0));
        particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));

        StandardPersonalBestUpdateStrategy strategy = new StandardPersonalBestUpdateStrategy();
        strategy.updatePersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(particle.getFitness()));
        Assert.assertThat(particle.getBestPosition(), is(particle.getPosition()));
        Assert.assertEquals(((Int)particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue(), 0);
    }

    /**
     * If a particle's current fitness is "less fit" than the current best
     * fitness, then the best fitness should <b>not be</b> equal the current fitness,
     * the best position should <b>not be</b> updated to the current position,
     * and the pbest stagnation counter should be incremented.
     */
    @Test
    public void updatePersonalBestFails() {
        Particle particle = new StandardParticle();

        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(100.0));
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(0.0));
        particle.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));

        StandardPersonalBestUpdateStrategy strategy = new StandardPersonalBestUpdateStrategy();
        strategy.updatePersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(not(particle.getFitness())));
        Assert.assertThat(particle.getBestPosition(), is(not(particle.getPosition())));
        Assert.assertEquals(((Int)particle.getProperties().get(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER)).intValue(), 1);
    }
}
