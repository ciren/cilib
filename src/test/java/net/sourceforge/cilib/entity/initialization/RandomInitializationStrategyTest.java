/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.entity.initialization;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 *
 * @author gpampara
 */
@RunWith(JMock.class)
public class RandomInitializationStrategyTest {
    private Mockery mockery = new JUnit4Mockery();

    @Test
    public void testInitialize() {
        Vector expected = Vectors.create(1.0, 1.0, 1.0);
        Particle particle = new StandardParticle();
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, expected.getClone());

        RandomInitializationStrategy<Particle> strategy = new RandomInitializationStrategy<Particle>();
        strategy.initialize(EntityType.CANDIDATE_SOLUTION, particle);

        Vector position = (Vector) particle.getPosition();

        for (int i = 0; i < particle.getDimension(); i++) {
            Assert.assertThat(expected.getReal(i), is(not(equalTo(position.getReal(i)))));
        }
    }

    @Test
    public void randomized() {
        final Particle particle = mockery.mock(Particle.class);
        final StructuredType randomizable = mockery.mock(StructuredType.class);
        final Blackboard<Enum<?>, Type> blackboard = new Blackboard<Enum<?>, Type>();
        blackboard.put(EntityType.CANDIDATE_SOLUTION, randomizable);

        RandomInitializationStrategy<Particle> strategy = new RandomInitializationStrategy<Particle>();

        mockery.checking(new Expectations() {{
            oneOf(particle).getProperties(); will(returnValue(blackboard));
            oneOf(randomizable).randomize(with(any(Random.class)));
        }});

        strategy.initialize(EntityType.CANDIDATE_SOLUTION, particle);
    }

}