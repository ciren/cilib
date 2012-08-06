/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.entity.initialization;

import java.util.ArrayList;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.Matchers.any;

/**
 *
 */
public class RandomInitializationStrategyTest {

    @Test
    public void testInitialize() {
        Vector expected = Vector.of(1.0, 1.0, 1.0);
        Particle particle = new StandardParticle();
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.copyOf(expected));

        RandomInitializationStrategy<Particle> strategy = new RandomInitializationStrategy<Particle>();
        strategy.initialize(EntityType.CANDIDATE_SOLUTION, particle);

        Vector position = (Vector) particle.getPosition();

        for (int i = 0; i < particle.getDimension(); i++) {
            Assert.assertThat(expected.doubleValueOf(i), is(not(equalTo(position.doubleValueOf(i)))));
        }
    }

    @Test
    public void randomized() {
        final Particle particle = mock(Particle.class);
        final StructuredType<?> randomizable = mock(StructuredType.class);
        
        final Blackboard<Enum<?>, Type> blackboard = new Blackboard<Enum<?>, Type>();
        blackboard.put(EntityType.CANDIDATE_SOLUTION, randomizable);

        when(particle.getProperties()).thenReturn(blackboard);
        RandomInitializationStrategy<Particle> strategy = new RandomInitializationStrategy<Particle>();

        strategy.initialize(EntityType.CANDIDATE_SOLUTION, particle);
        
        verify(randomizable).randomize(any(RandomProvider.class));
    }
    
    @Test
    public void testSetBoundsPerDimension() {
        RandomBoundedInitializationStrategy instance = new RandomBoundedInitializationStrategy();
        
        ArrayList<ControlParameter[]> bounds = new ArrayList<ControlParameter[]>();
        ControlParameter[] bound1 =  {ConstantControlParameter.of(1.0), ConstantControlParameter.of(3.0)};
        ControlParameter[] bound2 =  {ConstantControlParameter.of(1.2), ConstantControlParameter.of(5.1)};
        bounds.add(bound1);
        bounds.add(bound2);
        
        instance.setBoundsPerDimension(bounds);
        
        Assert.assertEquals(bounds, instance.boundsPerDimension);
        
        StandardParticle particle = new StandardParticle();
        particle.setCandidateSolution(Vector.of(0,0));
        instance.initialize(EntityType.CANDIDATE_SOLUTION, particle);
        
        Assert.assertTrue((((Vector) particle.getCandidateSolution()).get(0).doubleValue() > 1.0) 
                || (((Vector) particle.getCandidateSolution()).get(0).doubleValue() < 3.0));
        
        Assert.assertTrue((((Vector) particle.getCandidateSolution()).get(1).doubleValue() > 1.2) 
                || (((Vector) particle.getCandidateSolution()).get(1).doubleValue() < 5.1));
    }
}
