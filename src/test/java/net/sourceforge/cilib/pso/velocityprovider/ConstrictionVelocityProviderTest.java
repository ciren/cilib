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

import net.sourceforge.cilib.controlparameter.ParameterAdaptingPSOControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.math.random.generator.SeedSelectionStrategy;
import net.sourceforge.cilib.math.random.generator.Seeder;
import net.sourceforge.cilib.math.random.generator.ZeroSeederStrategy;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.mock;

/**
 * Unit test for the constriction velocity update.
 */
public class ConstrictionVelocityProviderTest {

    /**
     * Test cloning and implicitly the copy constructor.
     */
    @Test
    public void getClone() {
        ConstrictionVelocityProvider original = new ConstrictionVelocityProvider();
        ConstrictionVelocityProvider copy = original.getClone();

        Assert.assertEquals(original.getKappa().getParameter(), copy.getKappa().getParameter(), Maths.EPSILON);
        //Assert.assertEquals(original.getVMax().getParameter(), copy.getVMax().getParameter(), Maths.EPSILON);
        Assert.assertEquals(original.getCognitiveAcceleration().getParameter(), copy.getCognitiveAcceleration().getParameter(), Maths.EPSILON);
        Assert.assertEquals(original.getSocialAcceleration().getParameter(), copy.getSocialAcceleration().getParameter(), Maths.EPSILON);

        copy.setKappa(ConstantControlParameter.of(0.7));
        //copy.setVMax(ConstantControlParameter.of(0.7));
        ControlParameter controlParameter = ConstantControlParameter.of(4.0);
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

    /**
     * Test of getKappa method, of class ConstrictionVelocityProvider.
     */
    @Test
    public void testGetKappa() {
        System.out.println("getKappa");
        ConstrictionVelocityProvider instance = new ConstrictionVelocityProvider();
        ControlParameter expectedResult = ConstantControlParameter.of(0.55);
        instance.setKappa(expectedResult);
        ControlParameter result = instance.getKappa();
        
        Assert.assertTrue(expectedResult.getParameter() - result.getParameter() == 0);
    }

    /**
     * Test of setKappa method, of class ConstrictionVelocityProvider.
     */
    @Test
    public void testSetKappa() {
        System.out.println("setKappa");
        ConstrictionVelocityProvider instance = new ConstrictionVelocityProvider();
        ControlParameter expectedResult = ConstantControlParameter.of(0.55);
        instance.setKappa(expectedResult);
        ControlParameter result = instance.getKappa();
        
        Assert.assertTrue(expectedResult.getParameter() - result.getParameter() == 0);
    }

    /**
     * Test of getCognitiveAcceleration method, of class ConstrictionVelocityProvider.
     */
    @Test
    public void testGetCognitiveAcceleration() {
        System.out.println("getCognitiveAcceleration");
        ConstrictionVelocityProvider instance = new ConstrictionVelocityProvider();
        ControlParameter expectedResult = ConstantControlParameter.of(0.55);
        instance.setCognitiveAcceleration(expectedResult);
        ControlParameter result = instance.getCognitiveAcceleration();
        
        Assert.assertTrue(expectedResult.getParameter() - result.getParameter() == 0);
    }

    /**
     * Test of setCognitiveAcceleration method, of class ConstrictionVelocityProvider.
     */
    @Test
    public void testSetCognitiveAcceleration() {
        System.out.println("setCognitiveAcceleration");
        ConstrictionVelocityProvider instance = new ConstrictionVelocityProvider();
        ControlParameter expectedResult = ConstantControlParameter.of(0.55);
        instance.setCognitiveAcceleration(expectedResult);
        ControlParameter result = instance.getCognitiveAcceleration();
        
        Assert.assertTrue(expectedResult.getParameter() - result.getParameter() == 0);
    }

    /**
     * Test of getSocialAcceleration method, of class ConstrictionVelocityProvider.
     */
    @Test
    public void testGetSocialAcceleration() {
        System.out.println("getSocialAcceleration");
        ConstrictionVelocityProvider instance = new ConstrictionVelocityProvider();
        ControlParameter expectedResult = ConstantControlParameter.of(0.55);
        instance.setSocialAcceleration(expectedResult);
        ControlParameter result = instance.getSocialAcceleration();
        
        Assert.assertTrue(expectedResult.getParameter() - result.getParameter() == 0);
    }

    /**
     * Test of setSocialAcceleration method, of class ConstrictionVelocityProvider.
     */
    @Test
    public void testSetSocialAcceleration() {
        System.out.println("setSocialAcceleration");
        ConstrictionVelocityProvider instance = new ConstrictionVelocityProvider();
        ControlParameter expectedResult = ConstantControlParameter.of(0.55);
        instance.setSocialAcceleration(expectedResult);
        ControlParameter result = instance.getSocialAcceleration();
        
        Assert.assertTrue(expectedResult.getParameter() - result.getParameter() == 0);
    }

    /**
     * Test of getConstrictionCoefficient method, of class ConstrictionVelocityProvider.
     */
    @Test
    public void testGetConstrictionCoefficient() {
        System.out.println("getConstrictionCoefficient");
        ConstrictionVelocityProvider instance = new ConstrictionVelocityProvider();
        ControlParameter expectedResult = ConstantControlParameter.of(0.55);
        instance.setConstrictionCoefficient(expectedResult);
        ControlParameter result = instance.getConstrictionCoefficient();
        
        Assert.assertTrue(expectedResult.getParameter() - result.getParameter() == 0);
    }

    /**
     * Test of setConstrictionCoefficient method, of class ConstrictionVelocityProvider.
     */
    @Test
    public void testSetConstrictionCoefficient() {
        System.out.println("setConstrictionCoefficient");
        ConstrictionVelocityProvider instance = new ConstrictionVelocityProvider();
        ControlParameter expectedResult = ConstantControlParameter.of(0.55);
        instance.setConstrictionCoefficient(expectedResult);
        ControlParameter result = instance.getConstrictionCoefficient();
        
        Assert.assertTrue(expectedResult.getParameter() - result.getParameter() == 0);
    }

    /**
     * Test of setControlParameters method, of class ConstrictionVelocityProvider.
     */
    @Test
    public void testSetControlParameters() {
        System.out.println("setControlParameters");
        ParameterizedParticle particle = new ParameterizedParticle();
        ParameterAdaptingPSOControlParameter socialParameter = ConstantControlParameter.of(0.55);
        particle.setSocialAcceleration(socialParameter);
        ParameterAdaptingPSOControlParameter cognitiveParameter = ConstantControlParameter.of(0.55);
        particle.setCognitiveAcceleration(cognitiveParameter);
        ConstrictionVelocityProvider instance = new ConstrictionVelocityProvider();
        instance.setControlParameters(particle);
        
        Assert.assertTrue(socialParameter.getParameter() - instance.getSocialAcceleration().getParameter() == 0);
        Assert.assertTrue(cognitiveParameter.getParameter() - instance.getCognitiveAcceleration().getParameter() == 0);
    }

    /**
     * Test of getControlParameterVelocity method, of class ConstrictionVelocityProvider.
     */
    @Test
    public void testGetControlParameterVelocity() {
        System.out.println("getControlParameterVelocity");
        ParameterizedParticle particle = new ParameterizedParticle();
        ParameterAdaptingPSOControlParameter socialParameter = ConstantControlParameter.of(0.55);
        particle.setSocialAcceleration(socialParameter);
        ParameterAdaptingPSOControlParameter cognitiveParameter = ConstantControlParameter.of(0.55);
        particle.setCognitiveAcceleration(cognitiveParameter);
        ConstrictionVelocityProvider instance = new ConstrictionVelocityProvider();
        instance.setControlParameters(particle);
        
        Assert.assertTrue(socialParameter.getParameter() - instance.getSocialAcceleration().getParameter() == 0);
        Assert.assertTrue(cognitiveParameter.getParameter() - instance.getCognitiveAcceleration().getParameter() == 0);
    }
    
}
