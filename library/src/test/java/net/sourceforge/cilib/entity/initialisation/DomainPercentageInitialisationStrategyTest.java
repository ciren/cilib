/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import net.sourceforge.cilib.entity.initialisation.ConstantInitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.DomainPercentageInitialisationStrategy;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

/**
 *
 */
public class DomainPercentageInitialisationStrategyTest {

    @Test
    public void initialise() {
        final Bounds bounds = new Bounds(-8.0, 8.0);
        Vector vector = Vector.newBuilder()
                .addWithin(0.0, bounds)
                .addWithin(0.0, bounds)
                .addWithin(0.0, bounds)
                .build();

        Particle particle = new StandardParticle();
        particle.getProperties().put(EntityType.Particle.VELOCITY, vector);

        DomainPercentageInitialisationStrategy<Particle> strategy = new DomainPercentageInitialisationStrategy<Particle>();
        strategy.setVelocityInitialisationStrategy(new ConstantInitialisationStrategy(1.0));
        strategy.initialise(EntityType.Particle.VELOCITY, particle);

        Vector velocity = (Vector) particle.getVelocity();
        for (int i = 0; i < velocity.size(); i++) {
            Assert.assertThat(velocity.doubleValueOf(i), is(lessThanOrEqualTo(0.1)));
        }
    }

    @Test
    public void defaultPercentage() {
        DomainPercentageInitialisationStrategy strategy = new DomainPercentageInitialisationStrategy();

        Assert.assertThat(strategy.getPercentage(), is(equalTo(0.1)));
    }

}
