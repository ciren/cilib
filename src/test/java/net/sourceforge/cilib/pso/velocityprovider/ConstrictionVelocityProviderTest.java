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
package net.sourceforge.cilib.pso.velocityprovider;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.math.random.generator.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.Seeder;
import net.sourceforge.cilib.math.random.generator.ZeroSeederStrategy;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit test for the constriction velocity update.
 * @author andrich
 */
@RunWith(JMock.class)
public class ConstrictionVelocityProviderTest {

    private Mockery mockery = new JUnit4Mockery();

    /**
     * Test cloning and implicetly the copy constructor.
     */
    @Test
    public void getClone() {
        ConstrictionVelocityProvider original = new ConstrictionVelocityProvider();
        ConstrictionVelocityProvider copy = original.getClone();

        Assert.assertEquals(original.getKappa().getParameter(), copy.getKappa().getParameter(), Maths.EPSILON);
        //Assert.assertEquals(original.getVMax().getParameter(), copy.getVMax().getParameter(), Maths.EPSILON);
        Assert.assertEquals(original.getCognitiveAcceleration().getParameter(), copy.getCognitiveAcceleration().getParameter(), Maths.EPSILON);
        Assert.assertEquals(original.getSocialAcceleration().getParameter(), copy.getSocialAcceleration().getParameter(), Maths.EPSILON);

        copy.setKappa(new ConstantControlParameter(0.7));
        //copy.setVMax(new ConstantControlParameter(0.7));
        ConstantControlParameter controlParameter = new ConstantControlParameter();
        controlParameter.setParameter(4.0);
        copy.setSocialAcceleration(controlParameter.getClone());
        copy.setCognitiveAcceleration(controlParameter.getClone());

        Assert.assertFalse(Double.compare(original.getKappa().getParameter(), copy.getKappa().getParameter()) == 0);
        //Assert.assertFalse(Double.compare(original.getVMax().getParameter(), copy.getVMax().getParameter()) == 0);
        Assert.assertFalse(Double.compare(original.getCognitiveAcceleration().getParameter(),
                (copy.getCognitiveAcceleration()).getParameter()) == 0);
        Assert.assertFalse(Double.compare(original.getSocialAcceleration().getParameter(),
                (copy.getSocialAcceleration()).getParameter()) == 0);
    }

    /**
     * Test the velocity provider as well as the constraint assertion. This
     * sadly needs to use an annoying try..finally to ensure that the type
     * of random numbers is expected to reproduce values always.
     */
    @Test
    public void velocityProvision() {
        SeedSelectionStrategy strategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
            Particle particle = createParticle(Vector.of(0.0));
            Particle nBest = createParticle(Vector.of(1.0));
            particle.setNeighbourhoodBest(nBest);
            nBest.setNeighbourhoodBest(nBest);

            ConstrictionVelocityProvider velocityProvider = new ConstrictionVelocityProvider();
            Vector velocity = velocityProvider.get(particle);

            Assert.assertEquals(1.2189730956981684, velocity.doubleValueOf(0), Maths.EPSILON);
        } finally {
            Seeder.setSeederStrategy(strategy);
        }
    }

    @Test
    public void testConstrictionCalculation() {
        SeedSelectionStrategy strategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
            ConstrictionVelocityProvider velocityProvider = new ConstrictionVelocityProvider();
            Particle particle = createParticle(Vector.of(0.0));
            particle.setVelocityProvider(velocityProvider);
            Particle nBest = createParticle(Vector.of(1.0));
            particle.setNeighbourhoodBest(nBest);
            nBest.setNeighbourhoodBest(nBest);
            Particle clone = particle.getClone();

            particle.getVelocityProvider().get(particle);
            clone.getVelocityProvider().get(particle);

            double kappa = 1.0;
            double c1 = 2.05;
            double c2 = 2.05;
            double phi = c1 + c2;
            double chi = (2 * kappa) / Math.abs(2 - phi - Math.sqrt(phi * phi - 4.0 * phi)); //this was not copied from the implementation.

            //verify implementation maths is correct.
            Assert.assertEquals(chi, velocityProvider.getConstrictionCoefficient().getParameter(), Maths.EPSILON);
            //verify it is the same for two particles.

            Assert.assertEquals(((ConstrictionVelocityProvider) particle.getVelocityProvider()).getConstrictionCoefficient().getParameter(),
                    ((ConstrictionVelocityProvider) clone.getVelocityProvider()).getConstrictionCoefficient().getParameter(), Maths.EPSILON);


        } finally {
            Seeder.setSeederStrategy(strategy);
        }
    }

    private Particle createParticle(Vector vector) {
        Particle particle = new StandardParticle();
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, vector);
        particle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(0.0));
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, vector.getClone());

        return particle;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void illegalVelocityProvision() {
        final Particle particle = mockery.mock(Particle.class);

        ConstantControlParameter controlParameter = new ConstantControlParameter();
        ConstrictionVelocityProvider velocityProvider = new ConstrictionVelocityProvider();
        velocityProvider.setCognitiveAcceleration(controlParameter);
        velocityProvider.setSocialAcceleration(controlParameter);

        mockery.checking(new Expectations() {
            {
                ignoring(particle);
            }
        });

        velocityProvider.get(particle);
    }
}
