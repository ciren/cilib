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

import java.util.HashMap;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import junit.framework.Assert;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Kristina
 */
public class StandardVelocityProviderTest {
    
    public StandardVelocityProviderTest() {
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
     * Test of get method, of class StandardVelocityProvider.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^5");
        problem.setFunction(new Spherical());
        
        Particle particle = new ParameterizedParticle();
        particle.initialise(problem);
        particle.setCandidateSolution(Vector.of(1.0,1.0,1.0));
        particle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(2.0,2.0,2.0));
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, Vector.of(2.0,2.0,2.0));
        particle.setNeighbourhoodBest(particle);
        
        StandardVelocityProvider instance = new StandardVelocityProvider();
        Vector result = instance.get(particle);
        
        Assert.assertNotSame(result, Vector.of(2.0,2.0,2.0));
    }

    /**
     * Test of getInertiaWeight method, of class StandardVelocityProvider.
     */
    @Test
    public void testGetInertiaWeight() {
        System.out.println("getInertiaWeight");
        StandardVelocityProvider instance = new StandardVelocityProvider();
        ControlParameter expResult = ConstantControlParameter.of(0.9);
        instance.setInertiaWeight(expResult);
        ControlParameter result = instance.getInertiaWeight();
        Assert.assertTrue(expResult.getParameter() == result.getParameter());
    }

    /**
     * Test of setInertiaWeight method, of class StandardVelocityProvider.
     */
    @Test
    public void testSetInertiaWeight() {
        System.out.println("setInertiaWeight");
        StandardVelocityProvider instance = new StandardVelocityProvider();
        ControlParameter expResult = ConstantControlParameter.of(0.9);
        instance.setInertiaWeight(expResult);
        ControlParameter result = instance.getInertiaWeight();
        Assert.assertTrue(expResult.getParameter() == result.getParameter());
    }

    /**
     * Test of getCognitiveAcceleration method, of class StandardVelocityProvider.
     */
    @Test
    public void testGetCognitiveAcceleration() {
        System.out.println("getCognitiveAcceleration");
        StandardVelocityProvider instance = new StandardVelocityProvider();
        ControlParameter expResult = ConstantControlParameter.of(0.9);
        instance.setCognitiveAcceleration(expResult);
        ControlParameter result = instance.getCognitiveAcceleration();
        Assert.assertTrue(expResult.getParameter() == result.getParameter());
    }

    /**
     * Test of setCognitiveAcceleration method, of class StandardVelocityProvider.
     */
    @Test
    public void testSetCognitiveAcceleration() {
        System.out.println("setCognitiveAcceleration");
        StandardVelocityProvider instance = new StandardVelocityProvider();
        ControlParameter expResult = ConstantControlParameter.of(0.9);
        instance.setCognitiveAcceleration(expResult);
        ControlParameter result = instance.getCognitiveAcceleration();
        Assert.assertTrue(expResult.getParameter() == result.getParameter());
    }

    /**
     * Test of getSocialAcceleration method, of class StandardVelocityProvider.
     */
    @Test
    public void testGetSocialAcceleration() {
        System.out.println("getSocialAcceleration");
        StandardVelocityProvider instance = new StandardVelocityProvider();
        ControlParameter expResult = ConstantControlParameter.of(0.9);
        instance.setSocialAcceleration(expResult);
        ControlParameter result = instance.getSocialAcceleration();
        Assert.assertTrue(expResult.getParameter() == result.getParameter());
    }

    /**
     * Test of setSocialAcceleration method, of class StandardVelocityProvider.
     */
    @Test
    public void testSetSocialAcceleration() {
        System.out.println("setSocialAcceleration");
        StandardVelocityProvider instance = new StandardVelocityProvider();
        ControlParameter expResult = ConstantControlParameter.of(0.9);
        instance.setSocialAcceleration(expResult);
        ControlParameter result = instance.getSocialAcceleration();
        Assert.assertTrue(expResult.getParameter() == result.getParameter());
    }

    /**
     * Test of getR1 method, of class StandardVelocityProvider.
     */
    @Test
    public void testGetR1() {
        System.out.println("getR1");
        StandardVelocityProvider instance = new StandardVelocityProvider();
        RandomProvider expResult = new MersenneTwister();
        instance.setR1(expResult);
        RandomProvider result = instance.getR1();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of setR1 method, of class StandardVelocityProvider.
     */
    @Test
    public void testSetR1() {
        System.out.println("setR1");
        StandardVelocityProvider instance = new StandardVelocityProvider();
        RandomProvider expResult = new MersenneTwister();
        instance.setR1(expResult);
        RandomProvider result = instance.getR1();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of getR2 method, of class StandardVelocityProvider.
     */
    @Test
    public void testGetR2() {
        System.out.println("getR2");
        StandardVelocityProvider instance = new StandardVelocityProvider();
        RandomProvider expResult = new MersenneTwister();
        instance.setR2(expResult);
        RandomProvider result = instance.getR2();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of setR2 method, of class StandardVelocityProvider.
     */
    @Test
    public void testSetR2() {
        System.out.println("setR2");
        StandardVelocityProvider instance = new StandardVelocityProvider();
        RandomProvider expResult = new MersenneTwister();
        instance.setR2(expResult);
        RandomProvider result = instance.getR2();
        Assert.assertEquals(expResult, result);
    }

    /**
     * Test of setControlParameters method, of class StandardVelocityProvider.
     */
    @Test
    public void testSetControlParameters() {
        System.out.println("setControlParameters");
        ParameterizedParticle particle = new ParameterizedParticle();
        particle.setInertia(ConstantControlParameter.of(0.55));
        particle.setSocialAcceleration(ConstantControlParameter.of(0.55));
        particle.setCognitiveAcceleration(ConstantControlParameter.of(0.55));
        
        StandardVelocityProvider instance = new StandardVelocityProvider();
        instance.setControlParameters(particle);
        
        Assert.assertTrue(instance.getInertiaWeight().getParameter() == 0.55);
        Assert.assertTrue(instance.getCognitiveAcceleration().getParameter() == 0.55);
        Assert.assertTrue(instance.getSocialAcceleration().getParameter() == 0.55);
    }

    /**
     * Test of getControlParameterVelocity method, of class StandardVelocityProvider.
     */
    @Test
    public void testGetControlParameterVelocity() {
        System.out.println("getControlParameterVelocity");
        FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
        problem.setDomain("R(-5.12, 5.12)^5");
        problem.setFunction(new Spherical());
        
        ParameterizedParticle particle = new ParameterizedParticle();
        /*particle.initialise(problem);
        particle.setCandidateSolution(Vector.of(1.0,1.0,1.0));
        particle.getProperties().put(EntityType.Particle.VELOCITY, Vector.of(2.0,2.0,2.0));
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, Vector.of(2.0,2.0,2.0));*/
        particle.setInertia(ConstantControlParameter.of(0.55));
        particle.setSocialAcceleration(ConstantControlParameter.of(0.55));
        particle.setCognitiveAcceleration(ConstantControlParameter.of(0.55));
        
        particle.setBestInertia(ConstantControlParameter.of(0.55));
        particle.setBestSocialAcceleration(ConstantControlParameter.of(0.55));
        particle.setBestCognitiveAcceleration(ConstantControlParameter.of(0.55));
        
        particle.getInertia().setVelocity(0.2);
        particle.getSocialAcceleration().setVelocity(0.2);
        particle.getCognitiveAcceleration().setVelocity(0.2);
        
        particle.setNeighbourhoodBest(particle);
        
        StandardVelocityProvider instance = new StandardVelocityProvider();
        HashMap<String, Double>  result = instance.getControlParameterVelocity(particle);
        
        Assert.assertNotSame(result.get("InertiaVelocity"), 0.55);
        Assert.assertNotSame(result.get("SocialAccelerationVelocity"), 0.55);
        Assert.assertNotSame(result.get("CognitiveAccelerationVelocity"), 0.55);
    }
}
