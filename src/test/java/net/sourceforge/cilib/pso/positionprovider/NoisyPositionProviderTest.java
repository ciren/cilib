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
package net.sourceforge.cilib.pso.positionprovider;

import junit.framework.Assert;
import net.sourceforge.cilib.controlparameter.BoundedModifiableControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ParameterAdaptingPSOControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.ExponentialDistribution;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kristina
 */
public class NoisyPositionProviderTest {
    
    public NoisyPositionProviderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of get method, of class NoisyPositionProvider.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        NoisyPositionProvider noisyProvider = new NoisyPositionProvider();
        Particle particle = new StandardParticle();
        Vector position = Vector.of(1.0,1.0,1.0,1.0);
        particle.setCandidateSolution(position);
        particle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(2.0,2.0,2.0,2.0));
        Vector intermediatePosition = Vectors.sumOf((Vector) particle.getCandidateSolution(), (Vector) particle.getVelocity());
        Vector result = noisyProvider.get(particle);
        
        Assert.assertNotSame(intermediatePosition, result);
        Assert.assertNotSame(result, position);
    }

    /**
     * Test of getDelegate method, of class NoisyPositionProvider.
     */
    @Test
    public void testGetDelegate() {
        System.out.println("getDelegate");
        NoisyPositionProvider instance = new NoisyPositionProvider();
        PositionProvider expResult = new GaussianPositionProvider();
        instance.setDelegate(expResult);
        PositionProvider result = instance.getDelegate();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of setDelegate method, of class NoisyPositionProvider.
     */
    @Test
    public void testSetDelegate() {
        System.out.println("setDelegate");
        NoisyPositionProvider instance = new NoisyPositionProvider();
        PositionProvider expResult = new GaussianPositionProvider();
        instance.setDelegate(expResult);
        PositionProvider result = instance.getDelegate();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getDistribution method, of class NoisyPositionProvider.
     */
    @Test
    public void testGetDistribution() {
        System.out.println("getDistribution");
        NoisyPositionProvider instance = new NoisyPositionProvider();
        ProbabilityDistributionFuction expResult = new ExponentialDistribution();
        instance.setDistribution(expResult);
        ProbabilityDistributionFuction result = instance.getDistribution();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of setDistribution method, of class NoisyPositionProvider.
     */
    @Test
    public void testSetDistribution() {
        System.out.println("setDistribution");
        NoisyPositionProvider instance = new NoisyPositionProvider();
        ProbabilityDistributionFuction expResult = new ExponentialDistribution();
        instance.setDistribution(expResult);
        ProbabilityDistributionFuction result = instance.getDistribution();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getInertia method, of class NoisyPositionProvider.
     */
    @Test
    public void testGetInertia() {
        System.out.println("getInertia");
        NoisyPositionProvider noisyProvider = new NoisyPositionProvider();
        ParameterizedParticle particle = new ParameterizedParticle();
        ParameterAdaptingPSOControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(0.5);
        particle.setInertia(parameter);
        particle.getInertia().setVelocity(0.1);
        double intermediatePosition = particle.getInertia().getParameter() + particle.getInertia().getVelocity();
        double result = noisyProvider.getInertia(particle);
        
        Assert.assertNotSame(intermediatePosition, result);
        Assert.assertNotSame(result, parameter.getParameter());
        
    }

    /**
     * Test of getSocial method, of class NoisyPositionProvider.
     */
    @Test
    public void testGetSocial() {
        System.out.println("getSocial");
        NoisyPositionProvider noisyProvider = new NoisyPositionProvider();
        ParameterizedParticle particle = new ParameterizedParticle();
        ParameterAdaptingPSOControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(0.5);
        particle.setSocialAcceleration(parameter);
        particle.getSocialAcceleration().setVelocity(0.1);
        double intermediatePosition = particle.getSocialAcceleration().getParameter() + particle.getSocialAcceleration().getVelocity();
        double result = noisyProvider.getSocialAcceleration(particle);
        
        Assert.assertNotSame(intermediatePosition, result);
        Assert.assertNotSame(result, parameter.getParameter());
    }

    /**
     * Test of getPersonal method, of class NoisyPositionProvider.
     */
    @Test
    public void testGetPersonal() {
        System.out.println("getPersonal");
        NoisyPositionProvider noisyProvider = new NoisyPositionProvider();
        ParameterizedParticle particle = new ParameterizedParticle();
        ParameterAdaptingPSOControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(0.5);
        particle.setCognitiveAcceleration(parameter);
        particle.getCognitiveAcceleration().setVelocity(0.1);
        double intermediatePosition = particle.getCognitiveAcceleration().getParameter() + particle.getCognitiveAcceleration().getVelocity();
        double result = noisyProvider.getCognitiveAcceleration(particle);
        
        Assert.assertNotSame(intermediatePosition, result);
        Assert.assertNotSame(result, parameter.getParameter());
    }

    /**
     * Test of getVmax method, of class NoisyPositionProvider.
     */
    @Test
    public void testGetVmax() {
        System.out.println("getVmax");
        NoisyPositionProvider noisyProvider = new NoisyPositionProvider();
        ParameterizedParticle particle = new ParameterizedParticle();
        ParameterAdaptingPSOControlParameter parameter = new BoundedModifiableControlParameter();
        parameter.setParameter(0.5);
        particle.setVmax(parameter);
        particle.getVmax().setVelocity(0.1);
        double intermediatePosition = particle.getVmax().getParameter() + particle.getVmax().getVelocity();
        double result = noisyProvider.getVmax(particle);
        
        Assert.assertNotSame(intermediatePosition, result);
        Assert.assertNotSame(result, parameter.getParameter());
    }
    
    /*
     * Test isWithinBounds, of class NoisyPositionProvider
     */
    public void testIsWithinBounds() {
       BoundedModifiableControlParameter parameter = new BoundedModifiableControlParameter();
       parameter.setLowerBound(0.3);
       parameter.setUpperBound(0.6);
       parameter.setParameter(0.44);
       
       NoisyPositionProvider instance = new NoisyPositionProvider();
       
       Assert.assertTrue(instance.isWithinBounds(parameter.getParameter(), parameter));
       
       parameter = new BoundedModifiableControlParameter();
       parameter.setLowerBound(0.3);
       parameter.setUpperBound(0.6);
       parameter.setParameter(0.9);
       
       Assert.assertFalse(instance.isWithinBounds(parameter.getParameter(), parameter));
    }
}
