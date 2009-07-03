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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.math.random.generator.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.Seeder;
import net.sourceforge.cilib.math.random.generator.ZeroSeederStrategy;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;
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
public class ConstrictionVelocityUpdateTest {
    private Mockery mockery = new JUnit4Mockery();

    /**
     * Test cloning and implicetly the copy constructor.
     */
    @Test
    public void getClone() {
        ConstrictionVelocityUpdate original = new ConstrictionVelocityUpdate();
        ConstrictionVelocityUpdate copy = original.getClone();

        Assert.assertEquals(original.getKappa().getParameter(), copy.getKappa().getParameter(), Maths.EPSILON);
        Assert.assertEquals(original.getVMax().getParameter(), copy.getVMax().getParameter(), Maths.EPSILON);
        Assert.assertEquals(((RandomizingControlParameter)original.getCognitiveAcceleration()).getControlParameter().getParameter(),
                ((RandomizingControlParameter)copy.getCognitiveAcceleration()).getControlParameter().getParameter(), Maths.EPSILON);
        Assert.assertEquals(((RandomizingControlParameter)original.getSocialAcceleration()).getControlParameter().getParameter(),
                ((RandomizingControlParameter)copy.getSocialAcceleration()).getControlParameter().getParameter(), Maths.EPSILON);

        copy.setKappa(new ConstantControlParameter(0.7));
        copy.setVMax(new ConstantControlParameter(0.7));
        RandomizingControlParameter randomizingControlParameter = new RandomizingControlParameter();
        randomizingControlParameter.setParameter(4.0);
        copy.setSocialAcceleration(randomizingControlParameter.getClone());
        copy.setCognitiveAcceleration(randomizingControlParameter.getClone());

        Assert.assertFalse(Double.compare(original.getKappa().getParameter(), copy.getKappa().getParameter()) == 0);
        Assert.assertFalse(Double.compare(original.getVMax().getParameter(), copy.getVMax().getParameter()) == 0);
        Assert.assertFalse(Double.compare(((RandomizingControlParameter)original.getCognitiveAcceleration()).getControlParameter().getParameter(),
                ((RandomizingControlParameter)copy.getCognitiveAcceleration()).getControlParameter().getParameter()) == 0);
        Assert.assertFalse(Double.compare(((RandomizingControlParameter)original.getSocialAcceleration()).getControlParameter().getParameter(),
                ((RandomizingControlParameter)copy.getSocialAcceleration()).getControlParameter().getParameter()) == 0);
    }

    /**
     * Test the velocity update as well as the constraint assertion. This
     * sadly needs to use an annoying try..finally to ensure that the type
     * of random numbers is expected to reproduce values always.
     */
    @Test
    public void velocityUpdate() {
        SeedSelectionStrategy strategy = Seeder.getSeederStrategy();
        Seeder.setSeederStrategy(new ZeroSeederStrategy());

        try {
            Particle particle = createParticle(Vectors.create(0.0));
            Particle nBest = createParticle(Vectors.create(1.0));
            particle.setNeighbourhoodBest(nBest);
            nBest.setNeighbourhoodBest(nBest);

            ConstrictionVelocityUpdate velocityUpdate = new ConstrictionVelocityUpdate();
            velocityUpdate.updateVelocity(particle);

            Vector velocity = (Vector) particle.getVelocity();
            Assert.assertEquals(0.09831319691873514, velocity.getReal(0), Maths.EPSILON);
        }
        finally {
            Seeder.setSeederStrategy(strategy);
        }
    }

    private Particle createParticle(Vector vector) {
        Particle particle = new StandardParticle();
        particle.getProperties().put(EntityType.CANDIDATE_SOLUTION, vector);
        particle.getProperties().put(EntityType.Particle.VELOCITY, Vectors.create(0.0));
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, vector.getClone());

        return particle;
    }

    @Test(expected=UnsupportedOperationException.class)
    public void illegalVelocityUpdate() {
        final Particle particle = mockery.mock(Particle.class);

        RandomizingControlParameter randomizingControlParameter = new RandomizingControlParameter();
        ConstrictionVelocityUpdate velocityUpdate = new ConstrictionVelocityUpdate();
        velocityUpdate.setCognitiveAcceleration(randomizingControlParameter);
        velocityUpdate.setSocialAcceleration(randomizingControlParameter);

        mockery.checking(new Expectations() {{
            ignoring(particle);
        }});

        velocityUpdate.updateVelocity(particle);
    }

    /**
     * Test velocity clamping.
     */
    @Test
    public void testClamping() {
        Particle particle = createParticle(Vectors.create(0.0));
        Particle nBest = createParticle(Vectors.create(1.0));
        particle.setNeighbourhoodBest(nBest);
        nBest.setNeighbourhoodBest(nBest);

        ConstrictionVelocityUpdate constrictionVelocityUpdate = new ConstrictionVelocityUpdate();
        constrictionVelocityUpdate.setVMax(new ConstantControlParameter(0.5));
        constrictionVelocityUpdate.updateVelocity(particle);
        Vector velocity = (Vector) particle.getVelocity();

        for (Numeric number : velocity) {
            Assert.assertTrue(Double.compare(number.getReal(), 0.5) <= 0);
            Assert.assertTrue(Double.compare(number.getReal(), -0.5) >= 0);
        }

    }

}
