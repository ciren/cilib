/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.pbestupdate;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 *
 */
public class BoundedPersonalBestUpdateStrategyTest {

    @Test
    public void updatePersonalBest() {
        Real real = Real.valueOf(0.0, new Bounds(-5.0, 5.0));
        Particle particle = new StandardParticle();

        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(300.0));
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(real));

        BoundedPersonalBestUpdateStrategy strategy = new BoundedPersonalBestUpdateStrategy();
        strategy.updatePersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(particle.getFitness()));
        Assert.assertThat(particle.getBestPosition(), is(particle.getPosition()));
    }

    @Test
    public void updatePersonalBestFails() {
        Real real = Real.valueOf(-10.0, new Bounds(-5.0, 5.0));
        Particle particle = new StandardParticle();

        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(200.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(300.0));
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.of(real));

        Type previousBestPosition = particle.getBestPosition();

        BoundedPersonalBestUpdateStrategy strategy = new BoundedPersonalBestUpdateStrategy();
        strategy.updatePersonalBest(particle);

        Assert.assertThat(particle.getBestFitness(), is(not(particle.getFitness())));
        Assert.assertThat(particle.getBestPosition(), is(previousBestPosition));
        Assert.assertThat(particle.getFitness(), is(InferiorFitness.instance()));
    }
}
