package net.cilib.entity;

import org.junit.Assert;
import fj.F;
import fj.Unit;
import net.cilib.collection.immutable.Velocity;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class EntitiesTest {

    @Test
    public void particleGen() {
        RandomProvider random = new MersenneTwister();
        F<Unit, Particle> generator = Entities.particleGen(30, random);

        Particle p1 = generator.f(Unit.unit());
        Particle p2 = generator.f(Unit.unit());

        Assert.assertNotSame(p1, p2);

        Assert.assertEquals(30, p1.size());
        Assert.assertEquals(p1.solution(), p1.memory());
        Assert.assertArrayEquals(p1.velocity().toArray(), Velocity.replicate(30, 0.0).toArray(), 0.001);
        Assert.assertTrue(p1.fitness().isNone());
    }
}
