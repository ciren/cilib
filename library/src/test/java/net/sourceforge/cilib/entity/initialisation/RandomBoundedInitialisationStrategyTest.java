/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import net.sourceforge.cilib.entity.initialisation.RandomBoundedInitialisationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import org.junit.Assert;
import org.junit.Test;

public class RandomBoundedInitialisationStrategyTest {

    @Test
    public void initialise() {
        Vector vector = Vector.of(Real.valueOf(5.0),
                Real.valueOf(10.0),
                Int.valueOf(7));

        Particle particle = new StandardParticle();
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, vector);

        RandomBoundedInitialisationStrategy<Particle> strategy = new RandomBoundedInitialisationStrategy<Particle>();
        strategy.lowerBound = (ConstantControlParameter.of(-5.0));
        strategy.upperBound = (ConstantControlParameter.of(5.0));
        strategy.initialise(EntityType.CANDIDATE_SOLUTION, particle);

        for (int i = 0; i < vector.size(); i++) {
            Numeric numeric = vector.get(i);
            Assert.assertThat(numeric.doubleValue(), is(greaterThanOrEqualTo(-5.0)));
            Assert.assertThat(numeric.doubleValue(), is(lessThanOrEqualTo(5.0)));
        }
    }

}
