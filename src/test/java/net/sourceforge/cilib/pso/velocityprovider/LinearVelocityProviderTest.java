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
import junit.framework.Assert;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.pso.guideprovider.PBestGuideProvider;
import net.sourceforge.cilib.pso.particle.ParameterizedParticle;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kristina
 */
public class LinearVelocityProviderTest {
    
    public LinearVelocityProviderTest() {
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
     * Test of get method, of class LinearVelocityProvider.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        Particle particle = new StandardParticle();
        Vector position = Vector.of(1.0,1.0,1.0);
        particle.setCandidateSolution(position);
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, position);
        particle.setNeighbourhoodBest(particle);
        particle.getProperties().put(EntityType.Particle.VELOCITY, position);
        
        ParticleBehavior behaviour = new ParticleBehavior();
        PBestGuideProvider guide = new PBestGuideProvider();
        behaviour.setLocalGuideProvider(guide);
        behaviour.setGlobalGuideProvider(guide);
        
        LinearVelocityProvider instance = new LinearVelocityProvider();
        Vector result = instance.get(particle);
        
        Assert.assertNotSame(result, position);
    }

    /**
     * Test of getCongnitiveRandomGenerator method, of class LinearVelocityProvider.
     */
    @Test
    public void testGetCongnitiveRandomGenerator() {
        System.out.println("getCongnitiveRandomGenerator");
        RandomProvider randomGenerator = new MersenneTwister();
        LinearVelocityProvider provider = new LinearVelocityProvider();
        
        provider.setCongnitiveRandomGenerator(randomGenerator);
        RandomProvider result = provider.getCongnitiveRandomGenerator();
        
        Assert.assertEquals(result, randomGenerator);
    }

    /**
     * Test of setCongnitiveRandomGenerator method, of class LinearVelocityProvider.
     */
    @Test
    public void testSetCongnitiveRandomGenerator() {
        System.out.println("setCongnitiveRandomGenerator");
        RandomProvider randomGenerator = new MersenneTwister();
        LinearVelocityProvider provider = new LinearVelocityProvider();
        
        provider.setCongnitiveRandomGenerator(randomGenerator);
        RandomProvider result = provider.getCongnitiveRandomGenerator();
        
        Assert.assertEquals(result, randomGenerator);
    }

    /**
     * Test of getSocialRandomGenerator method, of class LinearVelocityProvider.
     */
    @Test
    public void testGetSocialRandomGenerator() {
        System.out.println("getSocialRandomGenerator");
        RandomProvider randomGenerator = new MersenneTwister();
        LinearVelocityProvider provider = new LinearVelocityProvider();
        
        provider.setSocialRandomGenerator(randomGenerator);
        RandomProvider result = provider.getSocialRandomGenerator();
        
        Assert.assertEquals(result, randomGenerator);
    }

    /**
     * Test of setSocialRandomGenerator method, of class LinearVelocityProvider.
     */
    @Test
    public void testSetSocialRandomGenerator() {
        System.out.println("setSocialRandomGenerator");
        RandomProvider randomGenerator = new MersenneTwister();
        LinearVelocityProvider provider = new LinearVelocityProvider();
        
        provider.setSocialRandomGenerator(randomGenerator);
        RandomProvider result = provider.getSocialRandomGenerator();
        
        Assert.assertEquals(result, randomGenerator);
    }

    /**
     * Test of setControlParameters method, of class LinearVelocityProvider.
     */
    @Test
    public void testSetControlParameters() {
        System.out.println("setControlParameters");
        ParameterizedParticle particle = new ParameterizedParticle();
        particle.setInertia(ConstantControlParameter.of(0.0));
        particle.setSocialAcceleration(ConstantControlParameter.of(0.0));
        particle.setCognitiveAcceleration(ConstantControlParameter.of(0.0));
        
        particle.setBestInertia(ConstantControlParameter.of(0.0));
        particle.setBestSocialAcceleration(ConstantControlParameter.of(0.0));
        particle.setBestCognitiveAcceleration(ConstantControlParameter.of(0.0));
        
        particle.setNeighbourhoodBest(particle);
        
        LinearVelocityProvider instance = new LinearVelocityProvider();
        HashMap<String, Double> result = instance.getControlParameterVelocity(particle);
        
        Assert.assertTrue(result.get("InertiaVelocity") == 0.0);
        Assert.assertTrue(result.get("SocialAccelerationVelocity") == 0.0);
        Assert.assertTrue(result.get("CognitiveAccelerationVelocity") == 0.0);
        
    }

    /**
     * Test of getControlParameterVelocity method, of class LinearVelocityProvider.
     */
    @Test
    public void testGetControlParameterVelocity() {
        System.out.println("getControlParameterVelocity");
        ParameterizedParticle particle = new ParameterizedParticle();
        particle.setInertia(ConstantControlParameter.of(0.0));
        particle.setSocialAcceleration(ConstantControlParameter.of(0.0));
        particle.setCognitiveAcceleration(ConstantControlParameter.of(0.0));
        
        particle.setBestInertia(ConstantControlParameter.of(0.0));
        particle.setBestSocialAcceleration(ConstantControlParameter.of(0.0));
        particle.setBestCognitiveAcceleration(ConstantControlParameter.of(0.0));
        
        particle.setNeighbourhoodBest(particle);
        
        LinearVelocityProvider instance = new LinearVelocityProvider();
        HashMap<String, Double> result = instance.getControlParameterVelocity(particle);
        
        Assert.assertTrue(result.get("InertiaVelocity") == 0.0);
        Assert.assertTrue(result.get("SocialAccelerationVelocity") == 0.0);
        Assert.assertTrue(result.get("CognitiveAccelerationVelocity") == 0.0);
        
        particle.setInertia(ConstantControlParameter.of(2.0));
        particle.setSocialAcceleration(ConstantControlParameter.of(2.0));
        particle.setCognitiveAcceleration(ConstantControlParameter.of(2.0));
        
        particle.setBestInertia(ConstantControlParameter.of(2.0));
        particle.setBestSocialAcceleration(ConstantControlParameter.of(2.0));
        particle.setBestCognitiveAcceleration(ConstantControlParameter.of(2.0));
        
        particle.setNeighbourhoodBest(particle);
        
        instance = new LinearVelocityProvider();
        result = instance.getControlParameterVelocity(particle);
        
        Assert.assertTrue(result.get("InertiaVelocity") == 0.0);
        Assert.assertTrue(result.get("SocialAccelerationVelocity") == 0.0);
        Assert.assertTrue(result.get("CognitiveAccelerationVelocity") == 0.0);
    }
}
